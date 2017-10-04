package com.sharmila.scrapper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.sharmila.scrapper.domain.GlassDoor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;

public class GlassdoorCrawler extends Thread {

	private Map<String, String> companyLocationMap = new HashMap<>();

	public String htmlToText(String html) {
		return Jsoup.parse(html).text();
	}

	public void run() {
		System.setProperty("http.proxyHost", "205.189.37.86");
		System.setProperty("http.proxyPort", "53281");
		Map<String, String> jobMap = new HashMap<>();
		//String location = "Illinois";
		//String keyword = "Bankers Life";
		GlassDoor jobSummary = new GlassDoor();
		System.out.println("here");
		for (Map.Entry<String, String> m : companyLocationMap.entrySet()) {
			System.out.println("----");
		
		try {
			Response response = Jsoup.connect("https://www.glassdoor.com/Job/jobs.htm")

					.userAgent("Mozilla/5.0").timeout(10 * 1000).method(Method.POST).data("suggestCount", "0")
					.data("suggestChosen", "false").data("clickSource", "searchBtn").data("typedKeyword", "")
					.data("sc.keyword", m.getKey()).data("LocationSearch", m.getValue()).data("locT", "").data("locId", "")
					.data("jobType", "")

					.followRedirects(true).execute();

			Document document = response.parse();
			Elements elements = document.select("#JobResults ul.jlGrid li.jl");

			for (Element e : elements) {
				jobMap.put(e.getElementsByTag("a").attr("href"), e.getElementsByClass("minor").text());

			}

			for (Map.Entry<String, String> map : jobMap.entrySet()) {
				System.out.println("https://www.glassdoor.com" + map.getKey());
				Response response1 = Jsoup.connect("https://www.glassdoor.com" + map.getKey()).userAgent("Mozilla/5.0")
						.timeout(10 * 1000).execute();
				Document document2 = response1.parse();

				// System.out.println(response1.url());

				Elements elements2 = document2.select("article#MainCol");

				for (Element element : elements2) {

					Elements sc = element.getElementsByTag("script");
					String jsonBeforeParse = sc.html();

					// parse the html to text
					String jsonAfterParse = htmlToText(jsonBeforeParse);
					// System.out.println(jsonAfterParse);

					try {
						System.out.println("after parsing : " + jsonAfterParse);
						JSONObject jsonObject = new JSONObject(jsonAfterParse);
						// System.out.println(jsonObject.getString("title"));
						// System.out.println(jsonObject.getString("datePosted"));
						// System.out.println(jsonObject.getString("employmentType"));

						JSONObject jobLocation = (JSONObject) jsonObject.get("jobLocation");
						JSONObject addressParse = (JSONObject) jobLocation.get("address");
						JSONObject hiringOrganization = (JSONObject) jsonObject.get("hiringOrganization");

						// System.out.println("location type
						// :"+jobLocation.getString("@type"));
						//
						// System.out.println("address type
						// "+addressParse.getString("@type"));
						// System.out.println("locality
						// "+addressParse.getString("addressLocality"));
						// System.out.println("region
						// "+addressParse.getString("addressRegion"));
						//
						// System.out.println("hiring organization
						// "+hiringOrganization.getString("name"));

						jobSummary.setCompanyName(hiringOrganization.getString("name"));
						jobSummary.setJobPostDate(jsonObject.getString("datePosted"));

						// jobSummary.setTitle(title);(jsonObject.getString("title"));

						System.out.println("Here is the job summary " + jobSummary.toString());
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
				try {
					Thread.sleep(11000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	}

	public HashMap<String, String> readCompanyLocationFile() {
		try {

			FileReader fileReader = new FileReader("CompanyLocationList.txt");
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String line = "";
			
			while ((line = bufferedReader.readLine()) != null) {

				String[] list = line.split("##");
				System.out.println(list);
				companyLocationMap.put(list[0], list[1]);
			}
			for (Map.Entry<String, String> m : companyLocationMap.entrySet()) {
				System.out.println(m.getKey() + " --------------- " + m.getValue());
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (HashMap<String, String>) companyLocationMap;

	}

	@Override
	public String toString() {
		return "GlassdoorCrawler []";
	}

	public static void main(String[] arg) {
		GlassdoorCrawler glassdoorCrawler = new GlassdoorCrawler();
		 glassdoorCrawler.readCompanyLocationFile();
		glassdoorCrawler.start();
		

	}
}
