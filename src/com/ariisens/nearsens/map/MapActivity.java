package com.ariisens.nearsens.map;


import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.ariisens.nearsens.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MapActivity extends Activity implements OnMapReadyCallback {

	public static int raggio = 5;
	private GoogleMap myMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		MapFragment vMapFragment = (MapFragment) getFragmentManager()
				.findFragmentById(R.id.fragment1);

		vMapFragment.getMapAsync(this);
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

				myMap.addMarker(new MarkerOptions().title(name).position(city));

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}
}
