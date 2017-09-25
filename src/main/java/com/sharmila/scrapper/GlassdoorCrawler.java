package com.sharmila.scrapper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.sharmila.scrapper.domain.JobSummary;

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

	public String htmlToText(String html) {
		return Jsoup.parse(html).text();
	}

	public void run() {
		System.setProperty("http.proxyHost", "205.189.37.86");
		System.setProperty("http.proxyPort", "53281");
		Map<String, String> jobMap = new HashMap<>();
		String location="newyork";
		String keyword="software developer";
		JobSummary jobSummary=new JobSummary();
		try {
			Response response = Jsoup.connect("https://www.glassdoor.com/Job/jobs.htm")

					.userAgent("Mozilla/5.0").timeout(10 * 1000).method(Method.POST).data("suggestCount", "0")
					.data("suggestChosen", "false").data("clickSource", "searchBtn").data("typedKeyword", "")
					.data("sc.keyword", keyword).data("LocationSearch", location).data("locT", "")
					.data("locId", "").data("jobType", "")

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
					
					
					//parse the html to text
					String jsonAfterParse = htmlToText(jsonBeforeParse);
					// System.out.println(jsonAfterParse);

					try {
						System.out.println("after parsing : "+jsonAfterParse);
						JSONObject jsonObject = new JSONObject(jsonAfterParse);
//						System.out.println(jsonObject.getString("title"));
//						System.out.println(jsonObject.getString("datePosted"));
//						System.out.println(jsonObject.getString("employmentType"));
						
						JSONObject jobLocation=(JSONObject) jsonObject.get("jobLocation");
						JSONObject addressParse=(JSONObject) jobLocation.get("address");
						JSONObject hiringOrganization=(JSONObject) jsonObject.get("hiringOrganization");

						
//						System.out.println("location type :"+jobLocation.getString("@type"));
//						
//						System.out.println("address type  "+addressParse.getString("@type"));
//						System.out.println("locality  "+addressParse.getString("addressLocality"));
//						System.out.println("region  "+addressParse.getString("addressRegion"));
//						
//						System.out.println("hiring organization "+hiringOrganization.getString("name"));
						
						
						jobSummary.setCompanyName(hiringOrganization.getString("name"));
						jobSummary.setJobPostDate(jsonObject.getString("datePosted"));
						jobSummary.setTitle(jsonObject.getString("title"));
						jobSummary.setJobType(jsonObject.getString("employmentType"));
						jobSummary.setLocations(addressParse.getString("addressLocality"));
						jobSummary.setLink("https://www.glassdoor.com" + map.getKey());
						
						
						System.out.println("Here is the job summary "+jobSummary.toString());
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

	@Override
	public String toString() {
		return "GlassdoorCrawler []";
	}

	public static void main(String[] arg) {
		GlassdoorCrawler glassdoorCrawler = new GlassdoorCrawler();
		glassdoorCrawler.start();
	}
}
