package com.ariisens.nearsens.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

	private final static String DB_NAME = "ariisens.db";
	private final static int DB_VERSION = 6;

	public DbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(OffersTableHelper.CREATE_QUERY);
		db.execSQL(PhotosOffersTableHelper.CREATE_QUERY);
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE " + OffersTableHelper.TABLE_NAME);
		db.execSQL(OffersTableHelper.CREATE_QUERY);
		db.execSQL("DROP TABLE " + PhotosOffersTableHelper.TABLE_NAME);
		db.execSQL(PhotosOffersTableHelper.CREATE_QUERY);
	}

}

