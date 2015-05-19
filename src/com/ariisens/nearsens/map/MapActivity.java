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

import com.ariisens.nearsens.R;
import com.ariisens.nearsens.interfaces.ICheckGPS;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MapActivity extends Activity implements OnMapReadyCallback,ICheckGPS {

	public static int raggio = 5;
	private GoogleMap myMap;
	private ArrayList<MarkerOptions> myMarkers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
		MapFragment vMapFragment = (MapFragment) getFragmentManager()
				.findFragmentById(R.id.fragment1);

		myMarkers = new ArrayList<MarkerOptions>();
		vMapFragment.getMapAsync(this);
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
			
			return true;
		} else {
			
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {

		myMap = googleMap;

		GPSTracker gpsTracker = new GPSTracker(this);
		double lat = gpsTracker.getLatitude();
		double lng = gpsTracker.getLongitude();

		LatLng center = new LatLng(lat,
				lng);
		
		Log.d("KKK", "Lat : " + lat + "\n Long : " + lng);
		MyLoopJ.getInstance().get(
				"http://nearsens.somee.com/api/Places?lat="
						+ lat + "&lng="
						+ lng + "&distanceLimit="
						+ raggio + "", new JsonHttpResponseHandler() {

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
		googleMap.addCircle(new CircleOptions()
	     .center(center)
	     .radius(raggio*1000)
	     .strokeColor(Color.parseColor("#76c2af"))
	     .strokeWidth(2)
	     .fillColor(Color.parseColor("#2276c2af")));
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 12));

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
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
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
}
