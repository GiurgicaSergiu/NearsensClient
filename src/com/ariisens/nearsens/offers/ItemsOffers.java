 package com.ariisens.nearsens.offers;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ariisens.nearsens.MyLoopJ;
import com.ariisens.nearsens.MyMainApplication;
import com.ariisens.nearsens.database.MyContentProvider;
import com.ariisens.nearsens.database.OffersTableHelper;
import com.ariisens.nearsens.offerdetails.DownloaderFragmentOfferDetail;
import com.ariisens.nearsens.offerdetails.ItemOfferDetails;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class ItemsOffers implements Parcelable {
	
	public long id;
    public String title;
    public float price;
    public float previousPrice;
    public String icon;
    public String placeName;
    public double placeLat;
    public double placeLng;
    
    public ItemsOffers(Parcel parcel) {
    	this.id = parcel.readLong();
		this.title = parcel.readString();
		this.price = parcel.readFloat();
		this.previousPrice = parcel.readFloat();
		this.icon = parcel.readString();
		this.placeName = parcel.readString();
		this.placeLat = parcel.readDouble();
		this.placeLng = parcel.readDouble();
	}

	public ItemsOffers(long id, String title,float price,float previousPrice,String icon,String placeName,double placeLat,double placeLng) {
		this.id = id;
		this.title = title;
		this.price = price;
		this.previousPrice = previousPrice;
		this.icon = icon;
		this.placeName = placeName;
		this.placeLat = placeLat;
		this.placeLng = placeLng;
	}
	
	public static ArrayList<ItemsOffers> createArray(JSONArray response,final Context context){	
		ArrayList<ItemsOffers> data = new ArrayList<ItemsOffers>();
		JSONObject json_data = null;
		context.getContentResolver().delete(MyContentProvider.OFFER_URI, null, null);
		
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
				float previousPrice = json_data.getLong("Discount");
				String placeName = json_data.getString("PlaceName");
				String icon = json_data.getString("MainPhoto");
				
				ContentValues contentValues = new ContentValues();
				contentValues.put(OffersTableHelper._ID, id);
				contentValues.put(OffersTableHelper.PLACENAME, placeName);
				context.getContentResolver().insert(MyContentProvider.OFFER_URI, contentValues);
				
				
				try {
					Thread.sleep(300);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
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
							
							context.getContentResolver().update(Uri.parse(MyContentProvider.OFFER_URI + "/" + response.getString("Id") ), contentValues, null, null);
			
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
				
				data.add(new ItemsOffers(id, title, price, previousPrice, icon, placeName, placeLat, placeLng));
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return data;
	}

	@Override
	public int describeContents() {
		
		return 0;
	}

	public void writeToParcel(Parcel parcel, int flags) {

		    parcel.writeLong(id);
			parcel.writeString(title);
			parcel.writeFloat(price);;
			parcel.writeFloat(previousPrice);
			parcel.writeString(icon);;
			parcel.writeString(placeName);
			parcel.writeDouble(placeLat);
			parcel.writeDouble(placeLng);
		}

		public static final Parcelable.Creator<ItemsOffers> CREATOR = new Parcelable.Creator<ItemsOffers>() {
		         public ItemsOffers createFromParcel(Parcel in) 
		         {
		             return new ItemsOffers(in);
		         }

		         public ItemsOffers[] newArray (int size) 
		         {
		             return new ItemsOffers[size];
		         }
		    };
}
