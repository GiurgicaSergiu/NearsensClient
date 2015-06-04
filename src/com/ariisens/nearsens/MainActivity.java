package com.ariisens.nearsens;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ariisens.nearsens.map.GPSTracker;
import com.ariisens.nearsens.map.MapActivity;
import com.ariisens.nearsens.map.MyLoopJ;
import com.ariisens.nearsens.notification.NotificationWithImage;
import com.ariisens.nearsens.offers.ItemsOffers;
import com.ariisens.nearsens.offers.MyAdapterOffers;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MainActivity extends Activity {

	private ListView listView;
	private int raggioArea;
	private ArrayList<ItemsOffers> itemsOffers;
	
	private static int[] images = {R.drawable.ms,R.drawable.usb,R.drawable.pesce,R.drawable.carne,};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
	
		MyMainApplication.getInstance(this);
		
		listView = (ListView) findViewById(R.id.lvOffers);
		
		GPSTracker gpsTracker = new GPSTracker(this);
		double lat = gpsTracker.getLatitude();
		double lng = gpsTracker.getLongitude();
		
		raggioArea = 100;
		
		MyLoopJ.getInstance().get(MyLoopJ.getOffers(lat, lng, raggioArea), new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {

				super.onSuccess(statusCode, headers, response);
				
				new AsyncTask<JSONArray, Void, Void>() {

					@Override
					protected Void doInBackground(JSONArray... params) {
						itemsOffers = ItemsOffers.createArray(params[0]);
						return null;
					}

					protected void onPostExecute(Void result) {
						listView.setAdapter(new MyAdapterOffers(getApplicationContext(), itemsOffers));

					};

				}.execute(response);
				

			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				NotificationWithImage.putNotification(getApplicationContext(),itemsOffers.get(position).title,itemsOffers.get(position).placeName,images[position]);
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
			if (id == R.id.action_filter) {
				return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}

	private void goToMap() {
		Intent intent = new Intent(this, MapActivity.class);
		startActivity(intent);
	}

}
