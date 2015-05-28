package com.ariisens.nearsens.map;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

public class ItemsCategory {

	public int mioId;
	public String name;

	public ItemsCategory(int id, String name) {
		this.mioId = id;
		this.name = name;
	}
	
	public static ArrayList<ItemsCategory> createArray(JSONArray response){	
		ArrayList<ItemsCategory> data = new ArrayList<ItemsCategory>();
		ItemsCategory itemsCategory = new ItemsCategory(0, "Tutte le categorie");
		data.add(itemsCategory);
		
		for(int i = 1; i<response.length() + 1;i++){
			ItemsCategory items = null;
			try {
				items = new ItemsCategory(i, response.getString(i-1));
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
			data.add(items);
		}
		return data;
	}
}
