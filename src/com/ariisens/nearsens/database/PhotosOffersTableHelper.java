package com.ariisens.nearsens.database;

import android.provider.BaseColumns;

public class PhotosOffersTableHelper implements BaseColumns {

	public static final String TABLE_NAME = "table_photos";	
	public static final String ID_OFFER = "id_offer";
	public static final String PHOTOS = "photos";

	public static String CREATE_QUERY = "CREATE TABLE " + TABLE_NAME + " ("
			+ _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ ID_OFFER + " TEXT NOT NULL,"
			+ PHOTOS + " TEXT NOT NULL"
			+ ");";
}