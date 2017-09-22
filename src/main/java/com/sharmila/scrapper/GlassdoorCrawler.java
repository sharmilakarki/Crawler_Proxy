package com.sharmila.scrapper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;

public class GlassdoorCrawler extends Thread {

	public void run() {
		System.setProperty("http.proxyHost", "205.189.37.86");
		System.setProperty("http.proxyPort", "53281");
		Map<String,String> jobMap=new HashMap<>();
		try {
			Response response = Jsoup.connect("https://www.glassdoor.com/Job/jobs.htm")

					.userAgent("Mozilla/5.0").timeout(10 * 1000).method(Method.POST).data("suggestCount", "0")
					.data("suggestChosen", "false").data("clickSource", "searchBtn").data("typedKeyword", "")
					.data("sc.keyword", "developer").data("LocationSearch", "newyork").data("locT", "")
					.data("locId", "").data("jobType", "")

					.followRedirects(true).execute();

//			System.out.println(response.statusMessage());
//
//			System.out.println(response.url());
			;
			Document document = response.parse();
			Elements elements = document.select("#JobResults ul.jlGrid li.jl");

			for (Element e : elements) {
				jobMap.put(e.getElementsByTag("a").attr("href"), e.getElementsByClass("minor").text());
//				System.out.println(e.getElementsByTag("a").attr("href") + "-------- time ------"
//						+ e.getElementsByClass("minor").text());
			}
			
			
			for(Map.Entry<String, String> map:jobMap.entrySet()){
				System.out.println("https://www.glassdoor.com"+map.getKey());
				Response response1=Jsoup.connect("https://www.glassdoor.com"+map.getKey()).userAgent("Mozilla/5.0").timeout(10 * 1000).execute();
				Document document2=response1.parse();
				
				System.out.println(response1.url());
				
				Elements elements2=document2.select("article#MainCol");
			
				for (Element element :elements2 ){ 
				
					Elements sc = element.getElementsByTag("script");
					String json=sc.html();
					System.out.println("the text "+json);
				//	System.out.println(json.replace("<script type='application/ld+json'>", "").replace("</script>", ""));
					String newJson=json.replace("<script type='application/ld+json'>", "").replace("</script>", "");
					System.out.println("------"+newJson);
//				try {
//					JSONObject jsonObject=new JSONObject(newJson);
//				} catch (JSONException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//					
//					System.out.println(jsonObject.getString("title"));
//					System.out.println(jsonObject.getString("datePosted"));
//					System.out.println(jsonObject.getString("employmentType"));
//					
					
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
