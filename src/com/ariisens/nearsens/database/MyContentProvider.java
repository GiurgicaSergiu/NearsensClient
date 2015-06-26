package com.ariisens.nearsens.database;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class MyContentProvider extends ContentProvider {

	public static final String AUTHORITY = "com.ariisens.nearsens.database";

	public static final String OFFERS_PATH = "offers";
	public static final String PHOTOS_PATH = "photos";
	public static final String SUBCATEGORIES_PATH = "subcategories";

	public static final Uri OFFER_URI = Uri
			.parse(ContentResolver.SCHEME_CONTENT + "://" + AUTHORITY + "/"
					+ OFFERS_PATH);
	public static final Uri PHOTO_URI = Uri
			.parse(ContentResolver.SCHEME_CONTENT + "://" + AUTHORITY + "/"
					+ PHOTOS_PATH);
	public static final Uri SUBCATEGORIES_URI = Uri
			.parse(ContentResolver.SCHEME_CONTENT + "://" + AUTHORITY + "/"
					+ SUBCATEGORIES_PATH);
	
	private static final int FULL_OFFERS_TABLE = 1;
	private static final int SINGLE_OFFER = 101;
	
	private static final int FULL_PHOTOS_TABLE = 2;
	private static final int SINGLE_PHOTO = 102;
	
	private static final int FULL_SUBCATEGORIES_TABLE = 3;
	private static final int SINGLE_SUBCATEGORY = 103;

	private static final UriMatcher URI_MATCHER = new UriMatcher(
			UriMatcher.NO_MATCH);

	static {
		URI_MATCHER.addURI(AUTHORITY, OFFERS_PATH, FULL_OFFERS_TABLE);
		URI_MATCHER.addURI(AUTHORITY, OFFERS_PATH + "/#", SINGLE_OFFER);
		URI_MATCHER.addURI(AUTHORITY, PHOTOS_PATH, FULL_PHOTOS_TABLE);
		URI_MATCHER.addURI(AUTHORITY, PHOTOS_PATH + "/#", SINGLE_PHOTO);
		URI_MATCHER.addURI(AUTHORITY, SUBCATEGORIES_PATH, FULL_SUBCATEGORIES_TABLE);
		URI_MATCHER.addURI(AUTHORITY, SUBCATEGORIES_PATH + "/#", SINGLE_SUBCATEGORY);
	}

	private DbHelper helper;

	@Override
	public boolean onCreate() {

		helper = new DbHelper(getContext());
		return true;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {

		SQLiteDatabase database = helper.getWritableDatabase();
		int result = 0;
		String tmp;
		switch (URI_MATCHER.match(uri)) {

		case FULL_OFFERS_TABLE:
			result = database.delete(OffersTableHelper.TABLE_NAME, selection,
					selectionArgs);
			break;
		case SINGLE_OFFER:
			tmp = OffersTableHelper._ID + " = " + uri.getLastPathSegment();
			result = database.delete(OffersTableHelper.TABLE_NAME, selection
					+ "AND" + tmp, selectionArgs);
			break;
		case FULL_PHOTOS_TABLE:
			result = database.delete(PhotosOffersTableHelper.TABLE_NAME, selection,
					selectionArgs);
			break;
		case SINGLE_PHOTO:
			tmp = PhotosOffersTableHelper._ID + " = " + uri.getLastPathSegment();
			result = database.delete(PhotosOffersTableHelper.TABLE_NAME, selection
					+ "AND" + tmp, selectionArgs);
			break;

		default:
			break;
		}

		if (result != 0)
			getContext().getContentResolver().notifyChange(uri, null);

		return result;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase database;
		long result;
		switch (URI_MATCHER.match(uri)) {
		case FULL_OFFERS_TABLE:
			database = helper.getWritableDatabase();
			result = database
					.insert(OffersTableHelper.TABLE_NAME, null, values);

			getContext().getContentResolver().notifyChange(uri, null);

			return Uri.parse(OFFER_URI + "/" + result);
		case FULL_PHOTOS_TABLE:
			database = helper.getWritableDatabase();
			result = database
					.insert(PhotosOffersTableHelper.TABLE_NAME, null, values);

			getContext().getContentResolver().notifyChange(uri, null);

			return Uri.parse(OFFER_URI + "/" + result);
		case FULL_SUBCATEGORIES_TABLE:
			database = helper.getWritableDatabase();
			result = database
					.insert(SubcategoriesTableHelper.TABLE_NAME, null, values);

			getContext().getContentResolver().notifyChange(uri, null);

			return Uri.parse(SUBCATEGORIES_URI + "/" + result);

		default:
			return null;
		}

	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		switch (URI_MATCHER.match(uri)) {

		case FULL_OFFERS_TABLE:
			queryBuilder.setTables(OffersTableHelper.TABLE_NAME);
			break;
		case SINGLE_OFFER:
			queryBuilder.setTables(OffersTableHelper.TABLE_NAME);
			queryBuilder.appendWhere(OffersTableHelper._ID + "="
					+ uri.getLastPathSegment());
			break;
		case FULL_PHOTOS_TABLE:
			queryBuilder.setTables(PhotosOffersTableHelper.TABLE_NAME);
			break;
		case SINGLE_PHOTO:
			queryBuilder.setTables(PhotosOffersTableHelper.TABLE_NAME);
			queryBuilder.appendWhere(PhotosOffersTableHelper._ID + "="
					+ uri.getLastPathSegment());
			break;
		case FULL_SUBCATEGORIES_TABLE:
			queryBuilder.setTables(SubcategoriesTableHelper.TABLE_NAME);
			break;
		case SINGLE_SUBCATEGORY:
			queryBuilder.setTables(SubcategoriesTableHelper.TABLE_NAME);
			queryBuilder.appendWhere(SubcategoriesTableHelper._ID + "="
					+ uri.getLastPathSegment());
			break;

		default:
			break;
		}
		SQLiteDatabase vdDatabase = helper.getReadableDatabase();

		Cursor cursor = queryBuilder.query(vdDatabase, projection, selection,
				selectionArgs, null, null, "_ID DESC");

		cursor.setNotificationUri(getContext().getContentResolver(), OFFER_URI);

		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {

		SQLiteDatabase database = helper.getWritableDatabase();
		int result = 0;
		String tmp;

		switch (URI_MATCHER.match(uri)) {

		case FULL_OFFERS_TABLE:
			result = database.update(OffersTableHelper.TABLE_NAME, values,
					selection, selectionArgs);

			break;
		case SINGLE_OFFER:
			tmp = OffersTableHelper._ID + " = " + uri.getLastPathSegment();
			if(selection == null){
				result = database.update(OffersTableHelper.TABLE_NAME, values,
						tmp, selectionArgs);
			}else{
				result = database.update(OffersTableHelper.TABLE_NAME, values,
						selection + " AND " + tmp, selectionArgs);
			}
			break;
			
		case FULL_PHOTOS_TABLE:
			result = database.update(PhotosOffersTableHelper.TABLE_NAME, values,
					selection, selectionArgs);

			break;
		case SINGLE_PHOTO:
			tmp = PhotosOffersTableHelper._ID + " = " + uri.getLastPathSegment();
			result = database.update(PhotosOffersTableHelper.TABLE_NAME, values,
					selection + "AND" + tmp, selectionArgs);
			break;

		default:
			break;
		}

		if (result != 0)
			getContext().getContentResolver().notifyChange(uri, null);

		return result;
	}

	@Override
	public String getType(Uri uri) {

		return null;
	}

}
