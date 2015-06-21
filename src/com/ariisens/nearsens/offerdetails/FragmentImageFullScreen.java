package com.ariisens.nearsens.offerdetails;

import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.StaticLayout;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ariisens.nearsens.R;
import com.ariisens.nearsens.customview.TouchImageView;
import com.bumptech.glide.Glide;

public class FragmentImageFullScreen extends Fragment {

	private ImageView img;
	private Drawable drawable;
	private LinearLayout llEffect;
	private LinearLayout llGallery;
	private String[] images;
	private LinearLayout llImages;

	public static FragmentImageFullScreen getOrCreateFragment(
			FragmentManager fragmentManager, String tag, Drawable d,String[] images) {
		FragmentImageFullScreen fragment = (FragmentImageFullScreen) fragmentManager
				.findFragmentByTag(tag);
		if (fragment == null) {
			fragment = new FragmentImageFullScreen();
			fragment.drawable = d;
			fragment.images = images;
			fragmentManager.beginTransaction()
					.add(R.id.frame_offer_detail, fragment, tag)
					.addToBackStack(tag).commit();
		}
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_image_full, container,
				false);

		img = (ImageView) view.findViewById(R.id.imgFull);
		llEffect = (LinearLayout) view.findViewById(R.id.ll_effect);
		llGallery = (LinearLayout) view.findViewById(R.id.ll_gallery);
		llImages = (LinearLayout) view.findViewById(R.id.ll_images);

		img.setImageDrawable(drawable);

		animateImage();

		animateLL();
		
		loadAllImages();

		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getView().requestFocus();
	}

	private void loadAllImages() {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int width = displaymetrics.widthPixels;
		for(int i = 0; i<images.length;i++){
		    TouchImageView imgGenereted = new TouchImageView(getActivity());
			imgGenereted.setLayoutParams(new LinearLayout.LayoutParams(width,(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 170, getActivity().getResources().getDisplayMetrics())));
			llImages.addView(imgGenereted);
			Glide.with(getActivity().getApplicationContext()).load("http://nearsens.somee.com//"+images[i]).centerCrop().into(imgGenereted);
		}
	}

	private void animateLL() {
		new AsyncTask<Void, Integer, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				int op = 0;
				for (int i = 0; i < 40; i++) {
					try {
						Thread.sleep(15);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					op += 5;
					publishProgress(op);
				}
				return null;
			}

			@Override
			protected void onProgressUpdate(Integer... values) {

				super.onProgressUpdate(values);
				llEffect.setBackgroundColor(Color.argb(values[0], 0, 0, 0));
			}

		}.execute();
	}

	private void animateImage() {
		llGallery.clearAnimation();
		ObjectAnimator.ofFloat(llGallery, "translationY", 0, getDisplayHeight() / 2)
				.setDuration(600).start();
		ObjectAnimator.ofFloat(llGallery, "scaleX", 1.0f, 1.2f).setDuration(600)
				.start();
		ObjectAnimator.ofFloat(llGallery, "scaleY", 1.0f, 1.4f).setDuration(600)
				.start();

	}
	
	private void animateImageBack() {
		llGallery.clearAnimation();
		ObjectAnimator.ofFloat(llGallery, "translationY", getDisplayHeight() / 2,0)
				.setDuration(600).start();
		ObjectAnimator.ofFloat(llGallery, "scaleX", 1.2f, 1.0f).setDuration(600)
				.start();
		ObjectAnimator.ofFloat(llGallery, "scaleY", 1.4f, 1.0f).setDuration(600)
				.start();

	}

	private int getDisplayHeight() {
		DisplayMetrics metrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay()
				.getMetrics(metrics);
		if (getScreenOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
				|| getScreenOrientation() == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE)
			return metrics.heightPixels;
		return metrics.widthPixels;
	}

	private int getScreenOrientation() {
		int rotation = getActivity().getWindowManager().getDefaultDisplay()
				.getRotation();
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		int height = dm.heightPixels;
		int orientation;
		// if the device's natural orientation is portrait:
		if ((rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180)
				&& height > width
				|| (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270)
				&& width > height) {
			switch (rotation) {
			case Surface.ROTATION_0:
				orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
				break;
			case Surface.ROTATION_90:
				orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
				break;
			case Surface.ROTATION_180:
				orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
				break;
			case Surface.ROTATION_270:
				orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
				break;
			default:

				orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
				break;
			}
		}
		// if the device's natural orientation is landscape or if the device
		// is square:
		else {
			switch (rotation) {
			case Surface.ROTATION_0:
				orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
				break;
			case Surface.ROTATION_90:
				orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
				break;
			case Surface.ROTATION_180:
				orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
				break;
			case Surface.ROTATION_270:
				orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
				break;
			default:

				orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
				break;
			}
		}

		return orientation;
	}
}
