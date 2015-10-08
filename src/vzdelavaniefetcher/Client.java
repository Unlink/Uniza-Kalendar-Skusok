/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vzdelavaniefetcher;

import com.unlink.common.HttpClient.BasicNameValuePair;
import com.unlink.common.HttpClient.HttpClient;
import com.unlink.common.bugTrackerLogger.BugTrackingLoger;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Named;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import vzdelavaniefetcher.tools.SimpleSerializedMappedSet;

/**
 *
 * @author Unlink
 */
public class Client {

	private HttpClient aClient;
	private Parser aParser;

	private String aMeno;
	private String aHeslo;

	@Inject
	public Client(HttpClient client, Parser parser) {
		aClient = client;
		aParser = parser;
		aHeslo = null;
		aMeno = null;
	}

	/**
	 * Overí či je uživateľ prihlásebý
	 *
	 * @return
	 */
	public boolean isLogged() {
		try {
			return aParser.isLogged(aClient.get("https://vzdelavanie.uniza.sk/vzdelavanie/", "CP1250"));
		}
		catch (IOException ex) {
			return false;
		}
	}

	/**
	 * Prihlási použivateľa so zadaným menom a heslom
	 *
	 * @param paMeno
	 * @param paHeslo
	 *
	 * @return uspesnost prihlasenia
	 */
	public boolean login(String paMeno, String paHeslo) {
		aMeno = paMeno;
		aHeslo = paHeslo;
		return login();
	}

	/**
	 * Odosle poziadavku na prihlasenie na server
	 *
	 * @return uspesnost prihlasenia
	 */
	private boolean login() {
		if (aMeno == null || aHeslo == null) {
			return false;
		}
		try {
			final List<BasicNameValuePair> nvps = new ArrayList<>();
			nvps.add(new BasicNameValuePair("meno", aMeno));
			nvps.add(new BasicNameValuePair("heslo", aHeslo));

			String resp = aClient.post("https://vzdelavanie.uniza.sk/vzdelavanie/login.php", nvps, "CP1250");
			if (resp.contains("\"logged\": true")) {
				return true;
			}
			else {
				return false;
			}
		}
		catch (IOException ex) {
			BugTrackingLoger.throwException(ex);
			return false;
		}
	}

	public String getUserName() {
		try {
			String response = loadWithLogin("");
			return aParser.parseUserName(response);
		}
		catch (IOException | FetchException ex) {
			return null;
		}
	}

	/**
	 * Vráti zoznam zapísaných predmetov
	 *
	 * @return
	 * @throws vzdelavaniefetcher.FetchException
	 */
	public List<Predmet> getPredmety() throws FetchException {

		try {
			//Featch stranky zapísaných predmetov
			String response = loadWithLogin("predmety_s.php");

			List<Predmet> predmety = aParser.parsePredmety(response);

			/*int delta = 80 / predmety.size();
			int progress = 20;*/
			//Fetchnutie terminov predmetov
			for (Predmet predmet : predmety) {
				response = aClient.get("https://vzdelavanie.uniza.sk/vzdelavanie/" + predmet.getTerminyHref(), "CP1250");
				aParser.parseTerminy(predmet, response);
				/*if (fe != null) {
					fe.updateState("Nahrávam " + p.getNazov(), progress);
				}*/
				//progress += delta;
			}
			/*if (fe != null) {
				fe.updateState("Hotovo", 100);
			}*/
			return predmety;
		}
		catch (IOException | FetchException ex) {
			throw new FetchException("Nepodarilo sa nahrať predmety\n" + ex.getMessage());
		}
	}

	/**
	 * Získanie inforácii o konkrétnom termíne
	 *
	 * @param termin
	 * @throws vzdelavaniefetcher.FetchException
	 */
	public void loadTerminDetail(Termin termin) throws FetchException {
		try {
			String response = loadWithLogin(termin.getHref());
			aParser.parseTerminInfo(termin, response);
		}
		catch (IOException ex) {
			throw new FetchException("Nepodarilo sa načítať informácie o termíne\n" + ex.getMessage());
		}
	}

	/**
	 * Vykonanie akcie na daný termín (ak som priklásený tak odhlásenie, inak
	 * prihklásenie)
	 *
	 * @param termin
	 *
	 * @return
	 * @throws vzdelavaniefetcher.FetchException
	 */
	public String terminAkcia(Termin termin) throws FetchException {
		try {
			
			String response = loadWithLogin("https://vzdelavanie.uniza.sk/vzdelavanie/" + termin.getHrefZapis());
			
			String message = aParser.parseActionInfo(response);
			response = aClient.get("https://vzdelavanie.uniza.sk/vzdelavanie/" + termin.getPredmet().getTerminyHref(), "CP1250");
			aParser.parseTerminy(termin.getPredmet(), response);
			return message;
		}
		catch (IOException ex) {
			throw new FetchException("Nepodarilo sa vykonať operáciu\n" + ex.getMessage());
		}
	}

	private String loadWithLogin(String url) throws IOException, FetchException {
		String response = aClient.get("https://vzdelavanie.uniza.sk/vzdelavanie/" + url, "CP1250");
		if (!aParser.isLogged(response)) {
			if (!login()) {
				throw new FetchException("Nepodarilo sa autorizovať účet");
			}
			response = aClient.get("https://vzdelavanie.uniza.sk/vzdelavanie/" + url, "CP1250");
		}
		return response;
	}

	/**
	 * Načítanie študijnych výsledkov
	 *
	 * @param paPaVysledky
	 *
	 * @throws FetchException
	 */
	public void featch(StudijneVysledky paPaVysledky, FetcherListner fe) throws FetchException {

		if (fe != null) {
			fe.updateState("Načítavam študujne výsledky", 0);
		}

		try {
			Document doc = Jsoup.parse(aClient.get("https://vzdelavanie.uniza.sk/vzdelavanie/svysledky.php", "CP1250"));

			int i = 0;
			String akademickyRok = "";
			String semester = "";
			for (Element e : doc.select(".main table")) {
				if (!e.hasClass("data")) {
					akademickyRok = e.select("tr td:first-child b").text().replace("Akademický rok ", "");
				}
				else {

					for (Element es : e.select("tr")) {
						if (es.hasClass("hdr")) {
							continue;
						}
						else if (es.hasClass("odd") || es.hasClass("evn")) {
							Elements row = es.select("td");
							String[] predmet = row.get(0).text().split(" ", 2);
							Date datumZapoctu = (row.get(4).text().isEmpty()) ? null : new SimpleDateFormat("dd.MM.yyyy").parse(row.get(4).text());
							Date datumSkusky = (row.get(6).text().isEmpty()) ? null : new SimpleDateFormat("dd.MM.yyyy").parse(row.get(6).text());
							StudijnyVysledok sv = new StudijnyVysledok(akademickyRok, semester, predmet[0], predmet[1], row.get(1).text(), row.get(3).text(), datumZapoctu, row.get(5).text(), datumSkusky, row.get(7).text(), Integer.parseInt(row.get(8).text()));
							paPaVysledky.vloz(sv);
						}
						else {
							semester = es.text();
						}
					}
				}
			}
		}
		catch (IOException | NumberFormatException | ParseException ex) {
			throw new FetchException(ex.getMessage());
		}
	}
}
