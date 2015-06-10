package com.ariisens.nearsens.offerdetails;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemOffersGridView implements Parcelable {
	public long id;
	public String description;

	public ItemOffersGridView(Parcel parcel) {
		this.id = parcel.readLong();
		this.description = parcel.readString();

	}

	public ItemOffersGridView(long id, String description) {
		this.id = id;
		this.description = description;

	}

	public static ArrayList<ItemOffersGridView> createArray(String[] s) {
		ArrayList<ItemOffersGridView> ifArrayList = new ArrayList<ItemOffersGridView>();
		for (int i = 0; i < s.length; i++) {
			ItemOffersGridView gb = new ItemOffersGridView(i, s[i]);
			ifArrayList.add(gb);
		}
		return ifArrayList;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {

		parcel.writeString(description);

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
