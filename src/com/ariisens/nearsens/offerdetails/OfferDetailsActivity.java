package com.ariisens.nearsens.offerdetails;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.ariisens.nearsens.R;

public class OfferDetailsActivity extends Activity {

	public static final String ID_OFFER = "idOffer";
	public static final String LAT = "lat";
	public static final String LON = "lon";
	public static final String PLACE = "place";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offer_details);

		FragmentOfferDetail.getOrCreateFragment(getFragmentManager(), "detail",
				getIntent().getExtras().getLong(ID_OFFER), getIntent()
						.getExtras().getDouble(LAT), getIntent().getExtras()
						.getDouble(LON),
				getIntent().getExtras().getString(PLACE));

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
