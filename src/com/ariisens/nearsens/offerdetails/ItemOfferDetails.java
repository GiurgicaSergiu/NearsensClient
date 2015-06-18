package com.ariisens.nearsens.offerdetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
public class ItemOfferDetails implements Parcelable {

	public String title;
	public int discount;
	public int price;
	public String mainPhoto;
	public String adress;
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
		this.title = parcel.readString();
		this.discount = parcel.readInt();
		this.price = parcel.readInt();
		this.mainPhoto = parcel.readString();
		this.adress = parcel.readString();
	}

	public ItemOfferDetails(String description, String link, String startDate,
			String expirationDate, String[] images,String title,int discount,int price,String mainPhoto,String adress) {

		this.description = description;
		this.link = link;
		this.startDate = startDate;
		this.expirationDate = expirationDate;
		this.images = images;
		this.title = title;
		this.discount = discount;
		this.price = price;
		this.mainPhoto = mainPhoto;
		this.adress = adress;
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
			String link = json_data.getString("Link");
			if(link.compareTo("null") == 0)
				link = "Non disponibile";
			itemOfferDetails = new ItemOfferDetails(
					json_data.getString("Description"),
					link,
					json_data.getString("StartDate"),
					json_data.getString("ExpirationDate"), 
					urlImage,
					json_data.getString("Title"),
					json_data.getInt("Discount"),
					json_data.getInt("Price"),
					json_data.getString("MainPhoto"),
					json_data.getString("PlaceAddress")
					);

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
		parcel.writeString(title);
		parcel.writeInt(discount);
		parcel.writeInt(price);
		parcel.writeString(mainPhoto);
		parcel.writeString(adress);

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
