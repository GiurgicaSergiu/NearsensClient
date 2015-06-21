package com.ariisens.nearsens.map;

public class InfoWindowMarker {
	private String mTitle;
	private String mDescription;
	private String mIcon;
	private int mPrice;
	private int mSconto;
	private String mOffersName;
	private Double mLatitude;
	private Double mLongitude;
	private String mId;

	public InfoWindowMarker(String description, String icon, int price, int sconto,
			String offertName, String id,String title) {
		mDescription = description;
		mIcon = icon;
		mId = id;
		mPrice = price;
		mSconto = sconto;
		mOffersName = offertName;
		mTitle = title;
	}

	public String getmLabel() {
		return mDescription;
	}

	public void setmLabel(String mLabel) {
		this.mDescription = mLabel;
	}

	public String getmIcon() {
		return mIcon;
	}

	public void setmIcon(String icon) {
		this.mIcon = icon;
	}

	public Double getmLatitude() {
		return mLatitude;
	}

	public void setmLatitude(Double mLatitude) {
		this.mLatitude = mLatitude;
	}

	public Double getmLongitude() {
		return mLongitude;
	}

	public void setmLongitude(Double mLongitude) {
		this.mLongitude = mLongitude;
	}

	public String getmId() {
		return mId;
	}

	public void setmId(String mId) {
		this.mId = mId;
	}

	public int getmPrice() {
		return mPrice;
	}

	public void setmPrice(int mPrice) {
		this.mPrice = mPrice;
	}

	public int getmSconto() {
		return mSconto;
	}

	public void setmSconto(int mSconto) {
		this.mSconto = mSconto;
	}

	public String getmOffersName() {
		return mOffersName;
	}

	public void setmOffersName(String mOffersName) {
		this.mOffersName = mOffersName;
	}

	public String getmTitle() {
		return mTitle;
	}

	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public String getmDescription() {
		return mDescription;
	}

	public void setmDescription(String mDescription) {
		this.mDescription = mDescription;
	}
}