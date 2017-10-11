package com.sharmila.scrapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import com.sharmila.scrapper.domain.Glassdoor;

public class Monster extends Thread {

	public String htmlToText(String html) {
		return Jsoup.parse(html).text();
	}

	public void run() {
		System.setProperty("http.proxyHost", "205.189.37.86");
		System.setProperty("http.proxyPort", "53281");
		Map<String, String> jobMap = new HashMap<>();
		String location="newyork";
		String keyword="software developer";
		Glassdoor jobSummary=new Glassdoor ();
		try {
			Response response = Jsoup.connect("https://www.monster.com/geo/siteselection")

					.userAgent("Mozilla/5.0").timeout(10 * 1000).method(Method.GET).data("q", keyword)
					.data("where", location).data("locT", "")
					.data("action", "https://www.monster.com/jobs/search/")
					.data("doQuickSearch","1")

					.followRedirects(true).execute();

		
			Document document = response.parse();
			Elements elements = document.select("*");
			System.out.println(elements);

			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	@Override
	public String toString() {
		return "GlassdoorCrawler []";
	}

	public static void main(String[] arg) {
		Monster m = new Monster();
		m.start();
	}
}
