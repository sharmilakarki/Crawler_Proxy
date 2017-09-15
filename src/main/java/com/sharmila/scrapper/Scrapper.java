package com.sharmila.scrapper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scrapper {

	public static void main(String[] args)  throws IOException, ParseException  {
		
		//URL url=new URL("https://geo.craigslist.org/iso/us");
		
		//Proxy proxy=new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 8888));
		
		System.setProperty("http.proxyHost", "195.208.128.117");
	    System.setProperty("http.proxyPort", "53281");
		
	    Document doc = null;
		try {
			Response response = Jsoup
					.connect("https://geo.craigslist.org/iso/us")
					.ignoreContentType(true)
					.userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
					.referrer("http://www.google.com")
					.timeout(12000)
					.followRedirects(true).execute();

			doc = response.parse();
			
				Thread.sleep(1100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
		}
		
	    
	    
		 Elements elementList= doc.select(".geo-site-list-container ul.height6.geo-site-list" ).select("a[href]");
		 System.out.println(elementList);
		 //Set<Element> siteList=new LinkedHashSet<>();
		 List<String> siteList=new ArrayList<>();
		 for(Element e:elementList){
		
		 // System.out.println(e.attr("href"));
		 //siteList.add(e);
		 siteList.add(e.attr("href"));
		
		 }
		 //can guess the class and loop over document---- but not done that way
	
		 Document document=Jsoup.connect(siteList.get(36)).get();
		 Elements secondSite=document.select(".jobs #jjj .cats").select("a[href]");
		 List<String> e2List=new ArrayList<>();
		 for(Element e2:secondSite){
			
		 e2List.add(e2.attr("href"));
		 }

		 Document  document1=Jsoup.connect(siteList.get(36)+e2List.get(18)).get();
		//Document document1 = Jsoup.connect("https://abilene.craigslist.org/search/acc").get();=
		 
		Elements thirdSite = document1.select("#sortable-results ul.rows li");
	//	System.out.println(":::::::::::"+secondSite);
		//System.out.println(" third site "+thirdSite);
  
		// adding result row in the map and doing operation on them
		
		
		Map<String, String> jobMap = new HashMap<>();

		// getting 7 days earlier date
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -7);
		System.out.println("date before a week " + cal.getTime());

		for (Element els : thirdSite) {
		//	System.out.println("****"+els.getElementsByTag("a").attr("href"));

			jobMap.put(els.getElementsByTag("a").attr("href"), els.getElementsByTag("time").attr("datetime"));
		}

		for (Map.Entry<String, String> e : jobMap.entrySet()) {

			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
			Date parsedDate = format.parse(e.getValue());
			
			
			if (parsedDate.after(cal.getTime()) |parsedDate.equals(cal.getTime())) {
					System.out.println(e.getKey()  +" ---- with date ---" +e.getValue());
					Document withWeekDoc=null;
					if(!(e.getKey().startsWith("https://") |(e.getKey().startsWith("www")))){
						System.out.println(" starts without prefix");
						 withWeekDoc=Jsoup.connect((siteList.get(36)+e2List.get(18))+e.getKey()).get();
					}else{
					
						System.out.println(" starts with prefix");
					
					 withWeekDoc=Jsoup.connect(e.getKey()).get();
					}
				
					Elements elements=withWeekDoc.select("#postingbody");
					System.out.println("++"+elements);
					for(Element el:elements){
						System.out.println("///////"+el.ownText());
						
						Pattern pattern=Pattern.compile("(locations.*\\.)(.)");
						Pattern pattern2=Pattern.compile("(call.*\\.)(.)");
						Matcher matcher=pattern.matcher(el.ownText().toLowerCase());
						Matcher matcher2=pattern2.matcher(el.ownText().toLowerCase());
						if(matcher.find()){
							
							System.out.println("Address "+matcher.group());
							
						}
						if(matcher2.find()){
							System.out.println("phone number "+matcher2.group());
						}
					}
					System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			
			}
		}



	}
}
