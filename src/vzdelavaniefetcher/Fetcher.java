/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vzdelavaniefetcher;

import com.unlink.common.HttpClient.BasicNameValuePair;
import com.unlink.common.HttpClient.HttpClient;
import com.unlink.common.HttpClient.IHttpClient;
import com.unlink.common.bugTrackerLogger.BugTrackingLoger;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import vzdelavaniefetcher.tools.SimpleSerializedMappedSet;

/**
 *
 * @author Unlink
 */
public class Fetcher {

	private static Fetcher aInstance;
	private static IHttpClient aDefaultClient;
	private IHttpClient aClient;

	private String aMeno;
	private String aHeslo;

	private SimpleSerializedMappedSet<String, Integer> aZaznamyTerminov;

	/**
	 * Vráti inštanciu Fetchera
	 *
	 * @return
	 */
	public static synchronized Fetcher dajInstanciu() {
		if (aInstance == null) {
			aInstance = new Fetcher();
		}
		return aInstance;
	}
	
	public static void setDefaultClient(IHttpClient paClient) {
		if (aInstance != null) {
			throw new IllegalStateException("Fetcher inštancia je už vytvorená, teraz už nieje možné nastaviť http clienta");
		}
		aDefaultClient = paClient;
	}

	private Fetcher() {
		aClient = (aDefaultClient != null) ? aDefaultClient : new HttpClient();
		aHeslo = null;
		aMeno = null;

		aZaznamyTerminov = new SimpleSerializedMappedSet<>(new File("terminy.dat"));
	}

	/**
	 * Overí či je uživateľ prihlásebý
	 *
	 * @return
	 */
	public boolean isLogged() {
		try {
			return isLogged(aClient.get("https://vzdelavanie.uniza.sk/vzdelavanie/", "CP1250"));
		}
		catch (IOException ex) {
			return false;
		}
	}

	/**
	 * Overí či je uživateľ prihlásený
	 *
	 * @param page html kód stránky
	 *
	 * @return
	 */
	private boolean isLogged(String page) {
		return isLogged(Jsoup.parse(page));
	}

	/**
	 * Overí či je uživateľ prihlásený
	 *
	 * @param doc jsoup dokument html stranky
	 *
	 * @return
	 */
	private boolean isLogged(Document doc) {
		return !doc.select(".SRH-inp table.tform tr td:eq(1)").text().equals("Neprihlásený");
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

	/**
	 * Načíta uživateľové detaily zo stránky
	 *
	 * @return
	 */
	public String featchUserName() {
		try {
			Document doc = Jsoup.parse(aClient.get("https://vzdelavanie.uniza.sk/vzdelavanie/", "CP1250"));
			if (!isLogged(doc)) {
				if (!login()) {
					return null;
				}
				doc = Jsoup.parse(aClient.get("https://vzdelavanie.uniza.sk/vzdelavanie/", "CP1250"));
			}
			//Parsnutie mena
			Elements els = doc.select("table.SRH-inp table.tform tr td:nth-child(1)");
			return els.text().replaceAll("Zmena hesla", "").trim();
		}
		catch (IOException ex) {
			return null;
		}
	}

	/**
	 * Načíta zoznam zapísaných predmetov
	 *
	 * @param fe Listnerer, pomocou ktorého je možné oznamovať progres
	 * načitavania
	 *
	 * @return
	 * @throws FatchException
	 */
	public List<Predmet> featchPredmety(FetcherListner fe) throws FetchException {
		List<Predmet> predmety = new ArrayList<>();

		if (fe != null) {
			fe.updateState("Načítavam zoznam predmetov", 5);
		}

		try {
			//Featch stranky zapísaných predmetov
			Document doc = Jsoup.parse(aClient.get("https://vzdelavanie.uniza.sk/vzdelavanie/predmety_s.php", "CP1250"));
			//Overenie či je uživatel stále prihládený (ak by uplynula platnosť session...)
			if (!isLogged(doc)) {
				if (!login()) {
					throw new FetchException("Nepodarilo sa autorizovať účet");
				}
				doc = Jsoup.parse(aClient.get("https://vzdelavanie.uniza.sk/vzdelavanie/predmety_s.php", "CP1250"));
			}

			Elements res = doc.select(".main table.SRH-inp tr td:lt(1) a");
			for (Element e : res) {
				if (!e.text().isEmpty()) {
					String href = e.parent().nextElementSibling().nextElementSibling().nextElementSibling().select("a").attr("href");
					predmety.add(new Predmet(e.text(), href));
				}
			}

			int delta = 80 / predmety.size();
			int progress = 20;
			//Fetchnutie terminov predmetov
			for (Predmet p : predmety) {
				if (fe != null) {
					fe.updateState("Nahrávam " + p.getNazov(), progress);
				}
				featch(p);
				progress += delta;
			}
			if (fe != null) {
				fe.updateState("Hotovo", 100);
			}
		}
		catch (IOException | FetchException ex) {
			throw new FetchException("Nepodarilo sa nahrať predmety\n" + ex.getMessage());
		}
		return predmety;
	}

	/**
	 * Načíta dáta o konkrétnom predmete
	 *
	 * @param predmet
	 *
	 * @throws FatchException
	 */
	public void featch(Predmet predmet) throws FetchException {
		int prihlasenyNa = 0;
		int zostavajucichTerminov = 0;
		ArrayList<Termin> terminy = new ArrayList<>();
		try {
			Document doc = Jsoup.parse(aClient.get("https://vzdelavanie.uniza.sk/vzdelavanie/" + predmet.getTerminyHref(), "CP1250"));
			Elements res = doc.select(".main table.SRH-inp2 tr[class!=hdr6]");
			for (Element e : res) {
				//Preskocime zrušene terminy
				if (e.attr("style").contains("background-color: #FFD0D0;")) {
					continue;
				}
				Date datum = new SimpleDateFormat("dd.MM.yyyy / HH:mm").parse(e.child(0).text());
				boolean prihlaseny = e.attr("style").contains("background-color: #D0FFD0;");
				if (prihlaseny) {
					prihlasenyNa++;
				}
				if (!prihlaseny && datum.compareTo(new Date()) == 1) {
					zostavajucichTerminov++;
				}

				String prodhls = null;
				String link = e.child(7).child(0).attr("href");
				if (!link.startsWith("javascript: alert(")) {
					prodhls = link;
					if (prodhls.startsWith("javascript:odhlasenie('")) {
						prodhls = "terminy_uid.php?pid=" + prodhls.substring(23, prodhls.length() - 3);
					}
				}

				Termin t = new Termin(predmet, datum, e.child(1).text(), e.child(2).text(), Integer.parseInt(e.child(3).text()), Integer.parseInt(e.child(4).text()), e.child(5).text(), e.child(6).text(), prihlaseny, e.child(7).child(1).attr("href"), prodhls);
				if (!aZaznamyTerminov.contains(aMeno, t.getTerminId())) {
					t.setNovy(true);
					aZaznamyTerminov.add(aMeno, t.getTerminId());
				}
				terminy.add(t);

			}
			predmet.setPrihlasenyNa(prihlasenyNa);
			predmet.setZostavajucichTerminov(zostavajucichTerminov);
			predmet.setTerminy(terminy);
		}
		catch (IOException | ParseException | NumberFormatException ex) {
			throw new FetchException(ex.getMessage());
		}
	}

	/**
	 * Získanie inforácii o konkrétnom termíne
	 *
	 * @param termin
	 * @param fe
	 *
	 * @throws FatchException
	 */
	public void featch(Termin termin, FetcherListner fe) throws FetchException {
		if (fe != null) {
			fe.updateState("Nahrávam " + termin.getPredmet().getNazov() + " (" + termin.getDatum() + ")", 0);
		}

		try {
			Document doc = Jsoup.parse(aClient.get("https://vzdelavanie.uniza.sk/vzdelavanie/" + termin.getHref(), "CP1250"));
			if (!isLogged(doc)) {
				if (!login()) {
					throw new FetchException("Nepodarilo sa autorizovať účet");
				}
				doc = Jsoup.parse(aClient.get("https://vzdelavanie.uniza.sk/vzdelavanie/" + termin.getHref(), "CP1250"));
			}
			Elements prihlaseny = doc.select(".main table.SRH-inp2:last-child tr[class!=hdr] td:first-child");
			StringBuilder sb = new StringBuilder();
			for (Element e : prihlaseny) {
				sb.append(e.text()).append("\n");
			}
			termin.setPrihlasenyStudenti(sb.toString());
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
	 * @throws FatchException
	 */
	public String terminAkcia(Termin termin) throws FetchException {
		try {
			Document doc = Jsoup.parse(aClient.get("https://vzdelavanie.uniza.sk/vzdelavanie/" + termin.getHrefZapis(), "CP1250"));
			if (!isLogged(doc)) {
				if (!login()) {
					throw new FetchException("Nepodarilo sa autorizovať účet");
				}
				doc = Jsoup.parse(aClient.get("https://vzdelavanie.uniza.sk/vzdelavanie/" + termin.getHrefZapis(), "CP1250"));
			}
			Elements els = doc.select(".main");
			els.select("div").remove();
			//Znovunačitanie celého predmetu
			featch(termin.getPredmet());
			return els.text();
		}
		catch (IOException ex) {
			throw new FetchException("Nepodarilo sa vykonať operáciu\n" + ex.getMessage());
		}
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
