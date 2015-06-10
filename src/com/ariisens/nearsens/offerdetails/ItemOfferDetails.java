package com.ariisens.nearsens.offerdetails;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.ariisens.nearsens.offers.ItemsOffers;

public class ItemOfferDetails implements Parcelable {

	public String description;
	public String link;
	public String startDate, expirationDate;
	public String[] images;

	public ItemOfferDetails(Parcel parcel) {

		this.description = parcel.readString();
		this.link = parcel.readString();
		this.startDate = parcel.readString();
		this.expirationDate = parcel.readString();
		this.images = new String[parcel.readInt()];
		parcel.readStringArray(this.images);
	}

	public ItemOfferDetails(String description, String link, String startDate,
			String expirationDate, String[] images) {

		this.description = description;
		this.link = link;
		this.startDate = startDate;
		this.expirationDate = expirationDate;
		this.images = images;
	}

	public static ItemOfferDetails createDetail(JSONObject response) {

		JSONObject json_data = response;
		ItemOfferDetails itemOfferDetails = null;
		try {

			JSONArray img = json_data.getJSONArray("Photos");
			String[] urlImage = new String[img.length()];
			for (int i = 0; i < img.length(); i++) {
				try {
					urlImage[i] = img.getString(i);

				} catch (JSONException e) {

					e.printStackTrace();
				}
			}
			itemOfferDetails = new ItemOfferDetails(
					json_data.getString("Description"),
					json_data.getString("Link"),
					json_data.getString("StartDate"),
					json_data.getString("ExpirationDate"), urlImage);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return itemOfferDetails;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {

		parcel.writeString(description);
		parcel.writeString(link);
		parcel.writeString(startDate);
		parcel.writeString(expirationDate);
		parcel.writeInt(images.length);
		parcel.writeStringArray(images);

	}

	public static final Parcelable.Creator<ItemOfferDetails> CREATOR = new Parcelable.Creator<ItemOfferDetails>() {
		public ItemOfferDetails createFromParcel(Parcel in) {
			return new ItemOfferDetails(in);
		}

		public ItemOfferDetails[] newArray(int size) {
			return new ItemOfferDetails[size];
		}
	};

}
