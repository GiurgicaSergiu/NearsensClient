package com.ariisens.nearsens.map;

import com.loopj.android.http.AsyncHttpClient;

public class MyLoopJ {
	
	private static AsyncHttpClient client;

	private MyLoopJ() {

	}

	public static AsyncHttpClient getInstance() {
		if (client == null)
			client = new AsyncHttpClient();
		return client;
	}
	
	public static String getPlaces(double lat,double lng,int raggioArea){
		return "http://nearsens.somee.com/api/Places?lat=" + lat + "&lng="+ lng + "&distanceLimit=" + raggioArea;
	}
}
