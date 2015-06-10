package com.ariisens.nearsens.map;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.SyncHttpClient;

public class MyLoopJ {
	
	private static AsyncHttpClient client;
	
	private static SyncHttpClient clientSync;

	private MyLoopJ() {

	}

	public static AsyncHttpClient getInstance() {
		if (client == null)
			client = new AsyncHttpClient();
		return client;
	}
	
	public static AsyncHttpClient getInstanceSync() {
		if (clientSync == null)
			clientSync = new SyncHttpClient();
		return clientSync;
	}
	
	public static String getPlaces(double lat,double lng,int raggioArea){
		return "http://nearsens.somee.com/api/Places?lat=" + lat + "&lng="+ lng + "&distanceLimit=" + raggioArea;
	}
	
	public static String getSubCategories(){
		return "http://nearsens.somee.com/api/subcategories";
	}
	
	public static String getOffers(double lat,double lng,int raggioArea){
		return "http://nearsens.somee.com/api/Offers?lat=" + lat + "&lng="+ lng + "&distanceLimit=" + raggioArea+"&page=1&pageSize=20";
	}
	
	public static String getOffertByPlaceId(String id){
		return "http://nearsens.somee.com/api/offers?placeId=" + id;
	}
	
	public static String getOfferDetails(long mId){
		return "http://nearsens.somee.com/api/offers?id=" + mId;
	}
}
