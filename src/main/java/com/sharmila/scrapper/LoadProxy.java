package com.sharmila.scrapper;

import java.util.HashMap;
import java.util.Map;

public class LoadProxy {

	Map<String,String> freeProxyMap=new HashMap<>();
	
	public Map<String, String> loadMap(){
		
		freeProxyMap.put("61.91.235.226", "8080");
		freeProxyMap.put("185.119.57.121", "53281");
		freeProxyMap.put("103.41.122.14", "65103");
		freeProxyMap.put("195.208.128.117", "53281");
		freeProxyMap.put("14.139.248.17", "80");
		freeProxyMap.put("205.189.37.86", "53281");
		return freeProxyMap;
	}
}
