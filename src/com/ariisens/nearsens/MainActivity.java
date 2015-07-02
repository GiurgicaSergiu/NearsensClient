package com.ariisens.nearsens;

//import com.crashlytics.android.Crashlytics;

//import io.fabric.sdk.android.Fabric;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ariisens.nearsens.augmentedreality.AugmentedRealityActivity;
import com.ariisens.nearsens.customview.CustomProgressBar;
import com.ariisens.nearsens.database.DbHelper;
import com.ariisens.nearsens.database.MyContentProvider;
import com.ariisens.nearsens.database.OffersTableHelper;
import com.ariisens.nearsens.interfaces.ILoadOffers;
import com.ariisens.nearsens.interfaces.IOption;
import com.ariisens.nearsens.map.DialogOption;
import com.ariisens.nearsens.map.DialogSelectCategory;
import com.ariisens.nearsens.map.DialogSelectType;
import com.ariisens.nearsens.map.GPSTracker;
import com.ariisens.nearsens.map.MapActivity;
import com.ariisens.nearsens.offerdetails.OfferDetailsActivity;
import com.ariisens.nearsens.offers.InsertDataInDb;
import com.ariisens.nearsens.offers.ItemsOffers;
import com.ariisens.nearsens.offers.MyCursorAdapterOffers;
import com.ariisens.nearsens.sharedpreferences.SharedPreferencesManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,IOption,ILoadOffers,SwipeRefreshLayout.OnRefreshListener {
	
	private static final String URL_API = "url_api";
	private static final String TIPO_VALUE = "url_val";
	private static final String RAGGIO_VALUE = "raggio_area";
	private static final String URL_API_CAT = "urls_cat";
	private static final String CAT_VALUE = "cat_val";

	private static final String PARSABLE = "parcable";
	private ListView listView;
	private int raggioArea;
	private ArrayList<ItemsOffers> itemsOffers;
	
	private TextView txtTipo;
	private TextView txtCat;
	private TextView txtKm;
	private LinearLayout llLoadingOffers;
	
	private String urlApiTipo;
	private String urlApiCat;
	
	private static final int ITEMS_LOADER_ID = 203;

	private MyCursorAdapterOffers adapter;
	private SwipeRefreshLayout swipeLayout;
	private Toolbar toolbar;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Fabric.with(this, new Crashlytics());

		setContentView(R.layout.activity_main);
		initInstances();

		listView = (ListView) findViewById(R.id.lvOffers);
		llLoadingOffers = (LinearLayout) findViewById(R.id.ll_Loading_offers);
		
		swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
	    swipeLayout.setOnRefreshListener(this);
	    swipeLayout.setColorScheme(android.R.color.holo_blue_bright, 
	            android.R.color.holo_green_light, 
	            android.R.color.holo_orange_light, 
	            android.R.color.holo_red_light);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				goToDetail(id);
			
			}
		});
		
		createMenu();
		
		adapter = new MyCursorAdapterOffers(this, null);
		
		if (!SharedPreferencesManager.instance(this).areSubcategoriesDownloaded()) {
			loadSubcategories();
		}
		
		if (savedInstanceState == null) {
			llLoadingOffers.bringToFront();
			loadOffers();
			
		} else {
			llLoadingOffers.removeAllViews();
			urlApiTipo = savedInstanceState.getString(URL_API);
			urlApiCat = savedInstanceState.getString(URL_API_CAT);
			txtCat.setText(savedInstanceState.getString(CAT_VALUE));
			txtTipo.setText(savedInstanceState.getString(TIPO_VALUE));
			raggioArea = savedInstanceState.getInt(RAGGIO_VALUE);
			txtKm.setText(raggioArea + " Km");
			listView.setAdapter(adapter);
		}
		
		getLoaderManager().initLoader(ITEMS_LOADER_ID, null, this);
	}

	private void initInstances() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.inflateMenu(R.menu.main);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
	}
	
	@Override 
	public void onRefresh() {
	    loadOffers();
	}

	protected void goToDetail(long id) {
		DbHelper dbHelper = new DbHelper(this);
		String query = "SELECT  * FROM " + OffersTableHelper.TABLE_NAME + " WHERE " + OffersTableHelper._ID + " = " + id;

		SQLiteDatabase db = dbHelper.getWritableDatabase();

		Cursor cursor = db.rawQuery(query, null);

		int placeName = cursor.getColumnIndex(OffersTableHelper.PLACENAME);
		int placeLat = cursor.getColumnIndex(OffersTableHelper.PLACELAT);
		int plageLng = cursor.getColumnIndex(OffersTableHelper.PLACELNG);
		int _id = cursor.getColumnIndex(OffersTableHelper._ID);

		cursor.moveToFirst();
		Intent intent = new Intent (MainActivity.this, OfferDetailsActivity.class);
		intent.putExtra(OfferDetailsActivity.PLACE, cursor.getString(placeName));
		intent.putExtra(OfferDetailsActivity.LAT, cursor.getFloat(placeLat));
		intent.putExtra(OfferDetailsActivity.LON, cursor.getFloat(plageLng));
		intent.putExtra(OfferDetailsActivity.ID_OFFER, cursor.getInt(_id));
		cursor.close();
			            
		startActivity(intent);
		
	}

	private void createMenu() {
		txtTipo = (TextView) findViewById(R.id.txtTipo);
		txtCat = (TextView) findViewById(R.id.txtCat);
		txtKm = (TextView) findViewById(R.id.txtKm);
		
		urlApiTipo = "";
		urlApiCat = "";
		raggioArea = 100;
		
		txtTipo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DialogSelectType.getInstance().show(getFragmentManager(), DialogSelectType.DIALOG_TAG);
			}
		});
		txtCat.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DialogSelectCategory.getInstance().show(getFragmentManager(), DialogSelectCategory.DIALOG_TAG);
			}
		});
		txtKm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DialogOption.getInstance(raggioArea).show(getFragmentManager(),
						DialogOption.DIALOG_TAG);
			}
		});
		
	}
	
	private void loadSubcategories() {
		MyLoopJ.getInstance().get(MyLoopJ.getSubCategories(),
				new JsonHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers,
				JSONArray response) {
				ArrayList<String> subcategories = new ArrayList<String>();
				for (int i = 0; i < response.length(); i++) {
					String object;
					try {
						object = (String) response.get(i);
						subcategories.add(object);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				InsertDataInDb.insertSubcategories(subcategories, MainActivity.this);
				SharedPreferencesManager.instance(MainActivity.this).setSubcategoriesDownloaded(true);
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONArray errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	private void loadOffers() {

		GPSTracker gpsTracker = new GPSTracker(this);
		double lat = gpsTracker.getLatitude();
		double lng = gpsTracker.getLongitude();

		MyLoopJ.getInstance().get(MyLoopJ.getOffers(lat, lng, raggioArea)+ urlApiTipo + urlApiCat,
				new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONArray response) {

						super.onSuccess(statusCode, headers, response);
						ContentLoadingProgressBar progressBar = new ContentLoadingProgressBar(MainActivity.this);
						llLoadingOffers.setVisibility(View.VISIBLE);
						llLoadingOffers.addView(progressBar);
						llLoadingOffers.bringToFront();
						
						InsertDataInDb.insertOffers(response, getApplicationContext(), MainActivity.this);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONArray errorResponse) {
						super.onFailure(statusCode, headers, throwable, errorResponse);
						finishOperation();
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						super.onFailure(statusCode, headers, responseString, throwable);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {			
						super.onFailure(statusCode, headers, throwable, errorResponse);
						finishOperation();
					}
					
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_map:

			goToMap();
			return true;

		case R.id.action_camera:
			goToCamera();
			break;
		default:
			break;
		}

	
		return super.onOptionsItemSelected(item);
	}

	private void goToMap() {
		Intent intent = new Intent(this, MapActivity.class);
		startActivity(intent);
	}
	
	private void goToCamera() {
		Intent intent = new Intent(this, AugmentedRealityActivity.class);
		startActivity(intent);
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putParcelableArrayList(PARSABLE, itemsOffers);
		outState.putString(URL_API, urlApiTipo);
		outState.putString(TIPO_VALUE, txtTipo.getText().toString());
		outState.putString(URL_API_CAT, urlApiCat);
		outState.putString(CAT_VALUE, txtCat.getText().toString());
		outState.putInt(RAGGIO_VALUE, raggioArea);
	}

	@Override
	public void updateRaggio(int raggio) {
		raggioArea = raggio;
		txtKm.setText(raggioArea + " Km");

	}

	@Override
	public void onConfirmArea() {
		loadOffers();
	}

	@Override
	public void updateTipo(String tipo, String value) {
		urlApiTipo = tipo;
		txtTipo.setText(value);
		loadOffers();
	}

	@Override
	public void updateCategory(String cat, String value) {
		urlApiCat = cat;
		txtCat.setText(value);
		loadOffers();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		CursorLoader loader = null;
		switch (id) {
		case ITEMS_LOADER_ID:
			loader = new CursorLoader(this, MyContentProvider.OFFER_URI, null,
					null, null, null);
			break;
		default:
			break;
		}

		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		adapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		adapter.swapCursor(null);
	}

	@Override
	public void finishOperation() {
		llLoadingOffers.setVisibility(View.GONE);
		llLoadingOffers.removeAllViews();
		listView.setAdapter(adapter);
		swipeLayout.setRefreshing(false);
	}

}
