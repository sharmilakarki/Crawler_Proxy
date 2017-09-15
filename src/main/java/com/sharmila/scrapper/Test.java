package com.sharmila.scrapper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

public class Test {
	 public static void main(String[] args) {
	        
	        try{
	            
	            //set HTTP proxy host to 127.0.0.1 (localhost)
	            System.setProperty("http.proxyHost", "195.208.128.117");
	            
	            //set HTTP proxy port to 3128
	            System.setProperty("http.proxyPort", "53281");
	            
	          Response response=
	                    Jsoup
	                    .connect("https://geo.craigslist.org/iso/us")
	                    .userAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1) Opera 7.10 [en]")
	                    .referrer("www.google.com")
	                    .timeout(10 * 1000).followRedirects(true).execute();
	                    
	            
	            System.out.println(response.parse());
	        
	      
	       System.out.println( response);
	            
	        }catch(IOException ioe){
	            System.out.println("Exception: " + ioe);
	        }
	 
	    }
}
