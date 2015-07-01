package com.ariisens.nearsens.augmentedreality;

import com.ariisens.nearsens.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;

public class AugmentedRealityActivity extends Activity {

	private OverlayView arContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        setContentView(R.layout.augmented_reality);
        
        FrameLayout arViewPane = (FrameLayout) findViewById(R.id.ar_view_pane);
        
        ArDisplayView arDisplay = new ArDisplayView(getApplicationContext(), this);
        arViewPane.addView(arDisplay);

        arContent = new OverlayView(getApplicationContext());
        arViewPane.addView(arContent);
   }

	@Override
	protected void onPause() {
		arContent.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		arContent.onResume();
	}
   
}
