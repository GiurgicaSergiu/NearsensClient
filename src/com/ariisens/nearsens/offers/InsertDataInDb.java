package com.ariisens.nearsens.offers;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;

import com.ariisens.nearsens.MyLoopJ;
import com.ariisens.nearsens.database.MyContentProvider;
import com.ariisens.nearsens.database.OffersTableHelper;
import com.ariisens.nearsens.database.PhotosOffersTableHelper;
import com.ariisens.nearsens.database.SubcategoriesTableHelper;
import com.ariisens.nearsens.interfaces.ILoadOffers;
import com.loopj.android.http.JsonHttpResponseHandler;

public class InsertDataInDb {
	
	private static int total;
	
	public static void insertSubcategories(ArrayList<String> subcategories, Context context) {
		context.getContentResolver().delete(MyContentProvider.SUBCATEGORIES_URI, null, null);
		ContentValues values;
		for (String subcategory : subcategories) {
			values = new ContentValues();
			values.put(SubcategoriesTableHelper.NAME, subcategory);
			context.getContentResolver().insert(MyContentProvider.SUBCATEGORIES_URI, values);
		}
		values = new ContentValues();
		values.put(SubcategoriesTableHelper.NAME, "Tutte");
		context.getContentResolver().insert(MyContentProvider.SUBCATEGORIES_URI, values);
	}
	
	public static void insertOffers(final JSONArray offers,final Context context,final ILoadOffers loadOffers){	
		
		JSONObject json_data = null;
		total = 0;
		
		context.getContentResolver().delete(MyContentProvider.OFFER_URI, null, null);
		context.getContentResolver().delete(MyContentProvider.PHOTO_URI, null, null);
		for (int i = 0; i < offers.length(); i++) {
			try {
				json_data = offers.getJSONObject(i);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			try {
				final double placeLat = json_data.getDouble("PlaceLat");
				final double placeLng = json_data.getDouble("PlaceLng");
				final int id = json_data.getInt("Id");

				final String placeName = json_data.getString("PlaceName");

				MyLoopJ.getInstance().get(MyLoopJ.getOfferDetails(id), new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {

						super.onSuccess(statusCode, headers, response);
						
						ContentValues contentValues = new ContentValues();
						try {
							contentValues.put(OffersTableHelper.STARTDATE,response.getString("StartDate"));
							contentValues.put(OffersTableHelper.EXPIRATIONDATE,response.getString("ExpirationDate"));
							contentValues.put(OffersTableHelper.DESCRIPTION,response.getString("Description"));
							contentValues.put(OffersTableHelper.LINK,response.getString("Link"));
							contentValues.put(OffersTableHelper.PRICE,response.getString("Price"));
							contentValues.put(OffersTableHelper.DISCOUNT,response.getString("Discount"));
							contentValues.put(OffersTableHelper.MAINPHOTO,response.getString("MainPhoto"));
							contentValues.put(OffersTableHelper.IDPLACE,response.getString("IdPlace"));
							contentValues.put(OffersTableHelper.PLACEADDRESS,response.getString("PlaceAddress"));
							contentValues.put(OffersTableHelper._ID, id);
							contentValues.put(OffersTableHelper.TITLE, response.getString("Title"));
							contentValues.put(OffersTableHelper.PLACENAME, placeName);
							contentValues.put(OffersTableHelper.PLACELAT, placeLat);
							contentValues.put(OffersTableHelper.PLACELNG, placeLng);
							context.getContentResolver().insert(MyContentProvider.OFFER_URI, contentValues);
							//context.getContentResolver().update(Uri.parse(MyContentProvider.OFFER_URI + "/" + response.getString("Id") ), contentValues, null, null);
							
							JSONArray img = response.getJSONArray("Photos");
							for (int i = 0; i < img.length(); i++) {
								ContentValues cv = new ContentValues();
								cv.put(PhotosOffersTableHelper.PHOTOS, img.getString(i));
								cv.put(PhotosOffersTableHelper.ID_OFFER, id);
								context.getContentResolver().insert(MyContentProvider.PHOTO_URI, cv);
							}
							total++;
							if(total>=offers.length()){
								loadOffers.finishOperation();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
										
					}
					
					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
					
						super.onFailure(statusCode, headers, throwable, errorResponse);
						
					}
				});
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
	}
}
