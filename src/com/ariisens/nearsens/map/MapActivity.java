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
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;

import com.ariisens.nearsens.R;
import com.ariisens.nearsens.interfaces.ICheckGPS;
import com.ariisens.nearsens.interfaces.IOptionMap;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MapActivity extends Activity implements OnMapReadyCallback,
		ICheckGPS, IOptionMap {

	private static int raggioArea = 5;
	private static int zoom = 12;
	private GoogleMap myMap;
	private ArrayList<MarkerOptions> myMarkers;
	private Button btnArea;
	private Circle circle;
	private String urlApi;
	private static final String URL_BASE = "&distanceLimit=" ;
	private Spinner spCat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		urlApi = "";
		spCat = (Spinner) findViewById(R.id.spCat);

		spCat.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

				switch (position) {
				case 0:
					urlApi = "";
					break;
				case 1:
					urlApi =  "&category=AC";
					loadMyMap(myMap);
					break;
				case 2:
					urlApi =  "&category=POI";
					loadMyMap(myMap);
					break;

				default:
					break;
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		btnArea = (Button) findViewById(R.id.btnArea);

		btnArea.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DialogOption.getInstance(raggioArea).show(getFragmentManager(),
						DialogOption.DIALOG_TAG);
			}
		});

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

		getMenuInflater().inflate(R.menu.option_map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();

		if (id == R.id.listMap) {
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

		MyLoopJ.getInstance().get(
				"http://nearsens.somee.com/api/Places?lat=" + lat + "&lng="
						+ lng + URL_BASE + raggioArea + urlApi, new JsonHttpResponseHandler() {

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

		/*
		 * for (MarkerOptions markerOption : CreateMarker.getMyMarkerOptions())
		 * { Marker marker = googleMap.addMarker(markerOption);
		 * hashMap.put(marker.getId(), marker); }
		 */

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

				myMarkers.add(new MarkerOptions().title(name).position(city));

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

	}

	@Override
	public void onMyBackPressed() {
		
		loadMyMap(myMap);
	}
}
