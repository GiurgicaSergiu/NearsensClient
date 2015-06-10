package com.ariisens.nearsens.offerdetails;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ariisens.nearsens.R;
import com.ariisens.nearsens.map.MyLoopJ;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

public class OfferDetailsActivity extends Activity {
	
	ProgressDialog pDialog;
	
	TextView txtTitle, txtPlace, txtDiscount, txtPreviousPrice, txtPrice, txtDescription, txtScadenza;
	ImageView imgHeader;
	GoogleMap map;
	
	String mTitle, mPlace, mUrl, jsonUrl, mDescription;
	float mPrev_price, mPrice, mDiscount;
	double mLat, mLng;
	long mId;
	
    boolean isImageFitToScreen;
    
    GridView gridView;
	
	BitmapDescriptor bitmapDescriptor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offer_details);
		
		editActionBar();
		
		txtTitle = (TextView)findViewById(R.id.txtTitle);
		txtPlace = (TextView)findViewById(R.id.txtPlace);
		txtDiscount = (TextView)findViewById(R.id.txtDiscount);
		txtPreviousPrice = (TextView)findViewById(R.id.txtPreviusPrice);
		txtPrice = (TextView)findViewById(R.id.txtPrice);
		txtDescription = (TextView)findViewById(R.id.txtDesc);
		txtScadenza = (TextView)findViewById(R.id.txtScadenza);
		imgHeader = (ImageView)findViewById(R.id.imgHeader);
		gridView = (GridView)findViewById(R.id.gridView);
		
		
		try {
            // Loading map
            initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }
		
		Intent intent = getIntent();
		mId = intent.getLongExtra("id", 0);
		mLat = intent.getDoubleExtra("lat", 0);
		mLng= intent.getDoubleExtra("lng", 0);
		
	    jsonUrl = "http://nearsens.somee.com/api/offers/json?id=" + mId;

		
		new PassParameterAsync().execute();
		new GetParamAsync().execute();
		
		//loadDetails();
		
		setMap();
		
		imgHeader.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
            }
		});
		
		loadOffersDetail();
		
	}
	
	
	private void loadOffersDetail() {
		MyLoopJ.getInstance().get(MyLoopJ.getOfferDetails(mId), new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {

				super.onSuccess(statusCode, headers, response);

				new AsyncTask<JSONObject, Void, ItemOfferDetails>() {

					@Override
					protected ItemOfferDetails doInBackground(JSONObject... params) {
						ItemOfferDetails items = ItemOfferDetails.createDetail(params[0]);
						return items;
					}

				

					protected void onPostExecute(ItemOfferDetails items) {

						txtDescription.setText(items.description);
						txtScadenza.setText("Scade il: "+items.expirationDate);
						gridView.setAdapter(new MyAdapterOfferDetails(getApplicationContext(), ItemOffersGridView.createArray(items.images)));
					};

				}.execute(response);

			}
		});
		
	}


	class PassParameterAsync extends AsyncTask<String, String, String> {
		
		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
			pDialog = new ProgressDialog(OfferDetailsActivity.this);
            pDialog.setMessage("Caricamento informazioni...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			Intent intent = getIntent();
			
			mTitle = intent.getStringExtra("title");
			mPlace = intent.getStringExtra("place");
			mPrice = intent.getFloatExtra("price", 0);
			mDiscount = intent.getFloatExtra("discount", 0);
			mUrl = intent.getStringExtra("urlImgHeader");
			
			Log.d("imgHeader",mUrl);
			
			mPrev_price = (mPrice-((mPrice*mDiscount)/100));
			
			getActionBar().setTitle(mTitle);	
			
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			
			//set
			txtTitle.setText(mTitle);
			txtPlace.setText(mPlace);
			txtDiscount.setText(""+mDiscount);
			txtPrice.setText(""+mPrice);
			txtPreviousPrice.setText(""+ mPrev_price);
			
			Glide.with(getApplicationContext()).load("http://nearsens.somee.com//"+mUrl).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(imgHeader);
		
		}
		
	}
	
	class GetParamAsync extends AsyncTask<String, String, String> {
		
		InputStream inputStream = null;
	    String result = ""; 

	    JSONArray user = null;
	    
		@Override
		protected void onPreExecute() {
			
			
			super.onPreExecute();     
            
		}

		@Override
		protected String doInBackground(String... params) {		
			
			
				
			
			
			/*JSONObject json = null;
	        String str = "";
	        HttpResponse response;
	        HttpClient myClient = new DefaultHttpClient();
	        HttpPost myConnection = new HttpPost(jsonUrl);
	         
	        try {
	            response = myClient.execute(myConnection);
	            str = EntityUtils.toString(response.getEntity(), "UTF-8");
	             
	        } catch (ClientProtocolException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	         
	         
	        try{
	            JSONArray jArray = new JSONArray(str);
	            json = jArray.getJSONObject(0);
	             
	             
	            mDescription=json.getString("Description");
	            
	             
	             
	        } catch ( JSONException e) {
	            e.printStackTrace();                
	        }*/
	    
			return null;
	    }
		
		@Override
		protected void onPostExecute(String result) {
			txtDescription.setText(mDescription);
		}	
	}
	
	
		
	
	
	

	
	private void initilizeMap() {
        if (map == null) {
            map = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            // check if map is created successfully or not
            if (map == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
	
	private void setMap(){
		bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.mac);
		LatLng coordPlace = new LatLng(mLat, mLng);
		map.setMyLocationEnabled(true);
		MarkerOptions marker = new MarkerOptions().position(coordPlace).title(mPlace).icon(bitmapDescriptor);
		map.addMarker(marker);
		
		CameraPosition cameraPosition = new CameraPosition.Builder().target(coordPlace).zoom(13).build();
		map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		map.getUiSettings().setCompassEnabled(false);
		
	}
	
	
	private void editActionBar() {
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
	}

	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MapFragment vMapFragment = (MapFragment) getFragmentManager()
				.findFragmentById(R.id.map);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == android.R.id.home) {
			onBackPressed();
		}

		return super.onOptionsItemSelected(item);
	}
	
}
