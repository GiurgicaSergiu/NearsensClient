package com.ariisens.nearsens.database;

import android.provider.BaseColumns;

public class SubcategoriesTableHelper implements BaseColumns {
	
	public static final String TABLE_NAME = "subcategories";	
	public static final String NAME = "name";

	public static String CREATE_QUERY = "CREATE TABLE " + TABLE_NAME + " ("
			+ _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ NAME + " TEXT NOT NULL"
			+ ");";

}
