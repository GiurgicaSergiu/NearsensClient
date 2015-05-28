package com.ariisens.nearsens.offers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ariisens.nearsens.R;
import com.ariisens.nearsens.map.ItemsCategory;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ItemsOffers {
	
	public int id;
    public String title;
    public float price;
    public float previousPrice;
    public String icon;
    public String placeName;
    public double placeLat;
    public double placeLng;

	public ItemsOffers(int id, String title,float price,float previousPrice,String icon,String placeName,double placeLat,double placeLng) {
		this.id = id;
		this.title = title;
		this.price = price;
		this.previousPrice = previousPrice;
		this.icon = icon;
		this.placeName = placeName;
		this.placeLat = placeLat;
		this.placeLng = placeLng;
	}
	
	public static ArrayList<ItemsOffers> createArray(JSONArray response){	
		ArrayList<ItemsOffers> data = new ArrayList<ItemsOffers>();
		JSONObject json_data = null;
		for (int i = 0; i < response.length(); i++) {
			try {
				json_data = response.getJSONObject(i);
			} catch (JSONException e) {

				e.printStackTrace();
			}
			try {
				double placeLat = json_data.getDouble("PlaceLat");
				double placeLng = json_data.getDouble("PlaceLng");
				String title = json_data.getString("Title");
				int id = json_data.getInt("Id");
				float price = json_data.getLong("Price");
				float previousPrice = json_data.getLong("PreviousPrice");
				String placeName = json_data.getString("PlaceName");
				String icon = json_data.getString("Icon");
				
				data.add(new ItemsOffers(id, title, price, previousPrice, icon, placeName, placeLat, placeLng));
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return data;
	}
}
