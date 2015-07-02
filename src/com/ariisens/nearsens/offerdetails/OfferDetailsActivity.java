package com.ariisens.nearsens.offerdetails;

import android.R.anim;
import android.R.integer;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.ariisens.nearsens.R;

public class OfferDetailsActivity extends AppCompatActivity {

	public static final String ID_OFFER = "idOffer";
	public static final String LAT = "lat";
	public static final String LON = "lon";
	public static final String PLACE = "place";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offer_details);

		FragmentOfferDetail.getOrCreateFragment(getFragmentManager(), "detail",
				getIntent().getExtras().getInt(ID_OFFER), getIntent()
						.getExtras().getFloat(LAT), getIntent().getExtras()
						.getFloat(LON), getIntent().getExtras()
						.getString(PLACE));

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home)
			onBackPressed();
		return true;
	}
	
	@Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
