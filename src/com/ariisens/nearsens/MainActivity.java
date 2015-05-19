package com.ariisens.nearsens;

import com.ariisens.nearsens.map.MapActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
