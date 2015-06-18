package com.ariisens.nearsens.offerdetails;

import org.apache.http.Header;
import org.json.JSONObject;

import com.ariisens.nearsens.map.MyLoopJ;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

public class DownloaderFragmentOfferDetail extends Fragment{
	
	private DownloadListener listener;

	private boolean isRunning;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	public interface DownloadListener {
		void onLoad(ItemOfferDetails result);

		void onError(int statusCode);
	}
	
	public static DownloaderFragmentOfferDetail getOrCreateFragment(FragmentManager fragmentManager, String tag) {
		DownloaderFragmentOfferDetail fragment = (DownloaderFragmentOfferDetail) fragmentManager.findFragmentByTag(tag);
		if (fragment == null) {
			fragment = new DownloaderFragmentOfferDetail();
			fragmentManager.beginTransaction().add(fragment, tag).commit();
		}
		return fragment;
	}
	
	public void attachOrLoadData(DownloadListener listener,long id) {
		this.listener = listener;

		isRunning = true;
		MyLoopJ.getInstance().get(MyLoopJ.getOfferDetails(id), new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {

				super.onSuccess(statusCode, headers, response);
				
				DownloaderFragmentOfferDetail.this.listener.onLoad(ItemOfferDetails.createDetail(response));
				isRunning = false;

			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
			
				super.onFailure(statusCode, headers, throwable, errorResponse);
				
				DownloaderFragmentOfferDetail.this.listener.onError(statusCode);
				isRunning = false;
			}
		});
	}

	public boolean isTaskRunning() {
		return isRunning;
	}

	public void attach(DownloadListener listener) {
		this.listener = listener;
	}

	public void stopTask() {
		MyLoopJ.getInstance().cancelAllRequests(true);
	}

}
