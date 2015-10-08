/*
 *  Copyright (c) 2015 Michal Ďuračík
 */
package vzdelavaniefetcher;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Unlink
 */
public class Parser {

	@Inject
	public Parser() {
	}
	
	/**
	 * Overí či je uživateľ prihlásený
	 * @param page html kód stránky
	 *
	 * @return
	 */
	public boolean isLogged(String page) {
		return isLogged(Jsoup.parse(page));
	}

	/**
	 * Overí či je uživateľ prihlásený
	 * @param doc jsoup dokument html stranky
	 *
	 * @return
	 */
	private boolean isLogged(Document doc) {
		return !doc.select(".SRH-inp table.tform tr td:eq(1)").text().equals("Neprihlásený");
	}
	
	public String parseUserName(String response) {
		Document doc = Jsoup.parse(response);
		//Parsnutie mena
		Elements els = doc.select("table.SRH-inp table.tform tr td:nth-child(1)");
		return els.text().replaceAll("Zmena hesla", "").trim();
	}
	
	public List<Predmet> parsePredmety(String response) {
		List<Predmet> predmety = new ArrayList<>();
		Document doc = Jsoup.parse(response);
		Elements res = doc.select(".main table.SRH-inp tr td:lt(1) a");
		for (Element e : res) {
			if (!e.text().isEmpty()) {
				String href = e.parent().nextElementSibling().nextElementSibling().nextElementSibling().select("a").attr("href");
				predmety.add(new Predmet(e.text(), href));
			}
		}
		return predmety;
	}
	
	/**
	 * Načíta dáta o konkrétnom predmete
	 *
	 * @param predmet
	 * @param response
	 * @throws vzdelavaniefetcher.FetchException
	 */
	public void parseTerminy(Predmet predmet, String response) throws FetchException {
		int prihlasenyNa = 0;
		int zostavajucichTerminov = 0;
		ArrayList<Termin> terminy = new ArrayList<>();
		try {
			Document doc = Jsoup.parse(response);
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
				terminy.add(t);

			}
			predmet.setPrihlasenyNa(prihlasenyNa);
			predmet.setZostavajucichTerminov(zostavajucichTerminov);
			predmet.setTerminy(terminy);
		}
		catch (ParseException | NumberFormatException ex) {
			throw new FetchException(ex.getMessage());
		}
	}
	
	/**
	 * Získanie inforácii o konkrétnom termíne
	 *
	 * @param termin
	 * @param response
	 */
	public void parseTerminInfo(Termin termin, String response) {
		Document doc = Jsoup.parse(response);
		Elements prihlaseny = doc.select(".main table.SRH-inp2:last-child tr[class!=hdr] td:first-child");
		StringBuilder sb = new StringBuilder();
		for (Element e : prihlaseny) {
			sb.append(e.text()).append("\n");
		}
		termin.setPrihlasenyStudenti(sb.toString());
	}
	
	public String parseActionInfo(String response) {
		Document doc = Jsoup.parse(response);
		Elements els = doc.select(".main");
		els.select("div").remove();
		return els.text();
	}
	
}
