package com.ariisens.nearsens.map;

public class MyMarker {
	private String mLabel;
	private String mIcon;
	private String mPrice;
	private String mSconto;
	private String mOffersName;
	private Double mLatitude;
	private Double mLongitude;
	private String mId;

	public MyMarker(String label, String icon, String price, String sconto,
			String offertName, String id) {
		mLabel = label;
		mIcon = icon;
		mId = id;
		mPrice = price;
		mSconto = sconto;
		mOffersName = offertName;
	}

	public String getmLabel() {
		return mLabel;
	}

	public void setmLabel(String mLabel) {
		this.mLabel = mLabel;
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

	public String getmPrice() {
		return mPrice;
	}

	public void setmPrice(String mPrice) {
		this.mPrice = mPrice;
	}

	public String getmSconto() {
		return mSconto;
	}

	public void setmSconto(String mSconto) {
		this.mSconto = mSconto;
	}

	public String getmOffersName() {
		return mOffersName;
	}

	public void setmOffersName(String mOffersName) {
		this.mOffersName = mOffersName;
	}
}