package com.ariisens.nearsens.offerdetails;

import com.ariisens.nearsens.R;
import com.ariisens.nearsens.R.id;
import com.ariisens.nearsens.R.layout;
import com.ariisens.nearsens.R.menu;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class OfferDetailsActivity extends Activity {
	
	ProgressDialog pDialog;
	
	TextView txtTitle, txtPlace, txtDiscount, txtPreviousPrice, txtPrice, txtDescription, txtScadenza;
	ImageView imgHeader;
	GoogleMap map;
	
	String mTitle, mPlace;
	float mPrev_price, mPrice, mDiscount;
	double mLat, mLng;

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
		
		
		try {
            // Loading map
            initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }
		
		new MyAsync().execute();
		
	}
	
	
	class MyAsync extends AsyncTask<String, String, String> {
		
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
			mLat = intent.getDoubleExtra("lat", 0);
			mLng= intent.getDoubleExtra("lng", 0);
			mPrev_price = (mPrice-((mPrice*mDiscount)/100));
			
			
			
			
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			txtTitle.setText(mTitle);
			txtPlace.setText(mPlace);
			txtDiscount.setText(""+mDiscount);
			txtPrice.setText(""+mPrice);
			txtPreviousPrice.setText(""+ mPrev_price);
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

	
}
