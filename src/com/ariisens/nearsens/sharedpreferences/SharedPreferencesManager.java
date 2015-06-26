package com.ariisens.nearsens.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesManager {
	
	private final static String PREFS = "mysharedpreferences";
	private final static String SUBCATEGORIES = "subcategories";
	
	private static SharedPreferencesManager mSingleton;
	
	private static Context mContext;
	private boolean mAreSubcategoriesDownloaded;
	private SharedPreferences mPrefs;
	
	
	private SharedPreferencesManager() {
		mPrefs = mContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
		mAreSubcategoriesDownloaded = mPrefs.getBoolean(SUBCATEGORIES, false);
	}
	
	public static SharedPreferencesManager instance() {
		if (mSingleton == null) mSingleton = new SharedPreferencesManager();
		return mSingleton;
	}
	
	public static SharedPreferencesManager instance(Context context) {
		setContext(context);
		return instance();
	}
	
	public static void setContext(Context context) {
		SharedPreferencesManager.mContext = context;
	}
	
	public boolean areSubcategoriesDownloaded() {
		return mAreSubcategoriesDownloaded;
	}
	
	public void setSubcategoriesDownloaded(boolean areSubcategoriesDownloaded) {
		this.mAreSubcategoriesDownloaded = areSubcategoriesDownloaded;
		Editor editablePrefs = mPrefs.edit();
		editablePrefs.putBoolean(SUBCATEGORIES, areSubcategoriesDownloaded);
		editablePrefs.apply();
	}
}
