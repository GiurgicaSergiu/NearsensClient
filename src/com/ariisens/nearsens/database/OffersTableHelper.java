package com.ariisens.nearsens.database;

import android.provider.BaseColumns;

public class OffersTableHelper implements BaseColumns {

	public static final String TABLE_NAME = "offers";
	public static final String STARTDATE = "startDate";
	public static final String EXPIRATIONDATE = "expirationDate";
	public static final String DESCRIPTION = "description";
	public static final String LINK = "link";
	public static final String PRICE = "price";
	public static final String DISCOUNT = "discount";
	public static final String TITLE = "title";
	public static final String MAINPHOTO = "miainPhoto";
	public static final String IDPLACE = "idPlace";
	public static final String PLACEADDRESS = "placeAddress";
	public static final String PLACENAME = "placeName";

	public static String CREATE_QUERY = "CREATE TABLE " + TABLE_NAME + " ("
			+ _ID + " INTEGER, " 
			+ STARTDATE + " TEXT , " 
			+ EXPIRATIONDATE + " TEXT , "
			+ DESCRIPTION + " TEXT ,"
			+ LINK + " TEXT ,"
			+ PRICE + " REAL ,"
			+ DISCOUNT + " REAL ,"
			+ TITLE + " TEXT ,"
			+ MAINPHOTO + " TEXT ,"
			+ IDPLACE + " INTEGER ,"
			+ PLACEADDRESS + " TEXT ,"
			+ PLACENAME + " TEXT"
			+ ");";
}