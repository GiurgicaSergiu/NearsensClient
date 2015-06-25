package com.ariisens.nearsens;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ariisens.nearsens.interfaces.IOptionMap;
import com.ariisens.nearsens.map.DialogOption;
import com.ariisens.nearsens.map.DialogSelectCategory;
import com.ariisens.nearsens.map.DialogSelectType;
import com.ariisens.nearsens.map.GPSTracker;
import com.ariisens.nearsens.map.MapActivity;
import com.ariisens.nearsens.offerdetails.OfferDetailsActivity;
import com.ariisens.nearsens.offers.ItemsOffers;
import com.ariisens.nearsens.offers.MyAdapterOffers;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MainActivity extends Activity implements IOptionMap{
	
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

	
	private String urlApiTipo;
	private String urlApiCat;
	
	Drawable imgHeader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics());

		setContentView(R.layout.activity_main);

		listView = (ListView) findViewById(R.id.lvOffers);


		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent (MainActivity.this, OfferDetailsActivity.class);
				intent.putExtra(OfferDetailsActivity.PLACE, itemsOffers.get(position).placeName);
				intent.putExtra(OfferDetailsActivity.LAT, itemsOffers.get(position).placeLat);
				intent.putExtra(OfferDetailsActivity.LON, itemsOffers.get(position).placeLng);
				intent.putExtra(OfferDetailsActivity.ID_OFFER, itemsOffers.get(position).id);
					            
				startActivity(intent);
			}
		});
		
		createMenu();
		

		if (savedInstanceState == null) {
			loadOffers();
		} else {
			itemsOffers = savedInstanceState.getParcelableArrayList(PARSABLE);
			urlApiTipo = savedInstanceState.getString(URL_API);
			urlApiCat = savedInstanceState.getString(URL_API_CAT);
			txtCat.setText(savedInstanceState.getString(CAT_VALUE));
			txtTipo.setText(savedInstanceState.getString(TIPO_VALUE));
			raggioArea = savedInstanceState.getInt(RAGGIO_VALUE);
			txtKm.setText(raggioArea + " Km");
			listView.setAdapter(new MyAdapterOffers(
					getApplicationContext(), itemsOffers));
		}
	}

	private void createMenu() {
		txtTipo = (TextView) findViewById(R.id.txtTipo);
		txtCat = (TextView) findViewById(R.id.txtCat);
		txtKm = (TextView) findViewById(R.id.txtKm);
		
		urlApiTipo = "";
		urlApiCat = "";
		raggioArea = 10;
		
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

				
								itemsOffers = ItemsOffers
										.createArray(response,getApplicationContext());
								

								listView.setAdapter(new MyAdapterOffers(
										getApplicationContext(), itemsOffers));

						

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

		int id = item.getItemId();
		if (id == R.id.action_map) {
			goToMap();
			return true;
		} else {
			
		}
		return super.onOptionsItemSelected(item);
	}

	private void goToMap() {
		Intent intent = new Intent(this, MapActivity.class);
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

}
