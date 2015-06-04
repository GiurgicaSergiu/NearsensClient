package com.ariisens.nearsens.map;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ariisens.nearsens.R;
import com.ariisens.nearsens.interfaces.ICheckGPS;
import com.ariisens.nearsens.interfaces.IOptionMap;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MapActivity extends Activity implements OnMapReadyCallback,
		ICheckGPS, IOptionMap {

	private static final String URL_API = "url_api";
	private static final String TIPO_VALUE = "url_val";
	private static final String RAGGIO_VALUE = "raggio_area";
	private static final String URL_API_CAT = "urls_cat";
	private static final String CAT_VALUE = "cat_val";
	private int raggioArea;
	private static int zoom = 12;
	private GoogleMap myMap;
	private ArrayList<MarkerOptions> myMarkers;
	private Circle circle;
	private String urlApiTipo;
	private String urlApiCat;
    private TextView txtTipo;
    private TextView txtCat;
    private TextView txtKm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_map);
		editActionBar();
		
		txtTipo = (TextView) findViewById(R.id.txtTipo);
		txtCat = (TextView) findViewById(R.id.txtCat);
		txtKm = (TextView) findViewById(R.id.txtKm);
		
		Typeface face = Typeface.createFromAsset(getAssets(),
	            "pl.ttf");
		
		txtCat.setTypeface(face);
		txtKm.setTypeface(face);
		txtTipo.setTypeface(face);
		
		
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
		
		urlApiTipo = "";
		urlApiCat = "";
		raggioArea = 5;
		
		if(savedInstanceState!=null){
			urlApiTipo = savedInstanceState.getString(URL_API);
			urlApiCat = savedInstanceState.getString(URL_API_CAT);
			txtCat.setText(savedInstanceState.getString(CAT_VALUE));
			txtTipo.setText(savedInstanceState.getString(TIPO_VALUE));
			raggioArea = savedInstanceState.getInt(RAGGIO_VALUE);
			txtKm.setText(raggioArea + " Km");
		}

		

	}

	private void editActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
	}
	
	@Override
	protected void onResume() {

		super.onResume();

		MapFragment vMapFragment = (MapFragment) getFragmentManager()
				.findFragmentById(R.id.fragment1);

		myMarkers = new ArrayList<MarkerOptions>();
		vMapFragment.getMapAsync(this);
		Log.d("KKK", "On RESUME");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		//getMenuInflater().inflate(R.menu.option_map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();

		if (id == android.R.id.home) {
			onBackPressed();
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {

		myMap = googleMap;

		loadMyMap(googleMap);
	}

	private void loadMyMap(GoogleMap googleMap) {
		
		myMarkers = new ArrayList<MarkerOptions>();
		myMap.clear();
		
		GPSTracker gpsTracker = new GPSTracker(this);
		double lat = gpsTracker.getLatitude();
		double lng = gpsTracker.getLongitude();

		LatLng center = new LatLng(lat, lng);

		MyLoopJ.getInstance().get(MyLoopJ.getPlaces(lat, lng, raggioArea) + urlApiTipo + urlApiCat, new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONArray response) {

						super.onSuccess(statusCode, headers, response);

						new AsyncTask<JSONArray, Void, Void>() {

							@Override
							protected Void doInBackground(JSONArray... params) {
								prepareListData(params[0]);
								return null;
							}

							protected void onPostExecute(Void result) {
								for (MarkerOptions marker : myMarkers) {
									myMap.addMarker(marker);
								}

							};

						}.execute(response);

					}
				});

		googleMap.setMyLocationEnabled(true);
		circle = googleMap.addCircle(new CircleOptions().center(center)
				.radius(raggioArea * 1000)
				.strokeColor(Color.parseColor("#76c2af")).strokeWidth(2)
				.fillColor(Color.parseColor("#5576c2af")));
		zoom = (int) (16 - Math.log(circle.getRadius() / 500) / Math.log(2));
		googleMap
				.animateCamera(CameraUpdateFactory.newLatLngZoom(center, zoom));
	}

	private void prepareListData(JSONArray response) {

		JSONObject json_data = null;

		for (int i = 0; i < response.length(); i++) {
			try {
				json_data = response.getJSONObject(i);
			} catch (JSONException e) {

				e.printStackTrace();
			}
			try {
				double lat = json_data.getDouble("Lat");
				double lng = json_data.getDouble("Lng");
				String name = json_data.getString("Name");

				LatLng city = new LatLng(lat, lng);
				
				BitmapDescriptor bitmapDescriptor;
				
				if(json_data.getString("MainCategory").compareTo("AC") == 0){
					bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.mac);
				}else{
					bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.mpoi);						
				}

				myMarkers.add(new MarkerOptions().title(name).position(city).icon(bitmapDescriptor));

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	public void enableGPS() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Enable GPS?")
				.setTitle("GPS is disabled!")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent intent = new Intent(
										Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								startActivity(intent);
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	public void updateRaggio(int raggio) {

		raggioArea = raggio;
		txtKm.setText(raggioArea + " Km");

	}

	@Override
	public void onMyBackPressed() {
		
		loadMyMap(myMap);
	}

	@Override
	public void updateTipo(String tipo,String value) {
		urlApiTipo = tipo;
		txtTipo.setText(value);
		loadMyMap(myMap);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(URL_API, urlApiTipo);
		outState.putString(TIPO_VALUE, txtTipo.getText().toString());
		outState.putString(URL_API_CAT, urlApiCat);
		outState.putString(CAT_VALUE, txtCat.getText().toString());
		outState.putInt(RAGGIO_VALUE, raggioArea);
	}

	@Override
	public void updateCategory(String cat,String value) {
		urlApiCat = cat;
		txtCat.setText(value);
		loadMyMap(myMap);
	}
}
