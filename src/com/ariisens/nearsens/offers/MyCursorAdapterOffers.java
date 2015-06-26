package com.ariisens.nearsens.offers;

import com.ariisens.nearsens.MyLoopJ;
import com.ariisens.nearsens.R;
import com.ariisens.nearsens.database.OffersTableHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyCursorAdapterOffers extends CursorAdapter {

	public MyCursorAdapterOffers(Context context, Cursor c) {
		super(context, c,0);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		
		int title = cursor.getColumnIndex(OffersTableHelper.TITLE);
		int placeName = cursor.getColumnIndex(OffersTableHelper.PLACENAME);
		int discount = cursor.getColumnIndex(OffersTableHelper.DISCOUNT);
		int price = cursor.getColumnIndex(OffersTableHelper.PRICE);
		int img = cursor.getColumnIndex(OffersTableHelper.MAINPHOTO);
		

		ViewHolder holder = (ViewHolder) view.getTag();
		
		
		holder.title.setText("" + cursor.getString(title));
		holder.placeName.setText("" + cursor.getString(placeName));
		holder.prevousPrice.setText("" + cursor.getFloat(price) + " �");
		holder.price.setText("" + (cursor.getFloat(price) - (cursor.getFloat(price)*cursor.getFloat(discount))/100) + " �");
		//ObjectAnimator.ofFloat(holder.ll, "translationX", -200, 0).setDuration(600).start();
		ObjectAnimator.ofFloat(holder.ll, "scaleX", 0.2f, 1.0f).setDuration(600)
		.start();
		ObjectAnimator.ofFloat(holder.ll, "scaleY", 0.2f, 1.0f).setDuration(600)
		.start();
		
		try {
			Glide.with(context).load(MyLoopJ.BASE_URL + "/"+cursor.getString(img)).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imgOffers);
		} catch (Exception e) {
			
		}

	}

	private class ViewHolder{
		TextView title;
		TextView placeName;
		TextView prevousPrice;
		TextView price;
		ImageView imgOffers;
		LinearLayout ll;
	}
	
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.cella_offerte, null);
		

		TextView txtPlaceName = (TextView) view.findViewById(R.id.txtPlaceName);
		TextView txtTitleOf = (TextView) view.findViewById(R.id.txtTileOf);
		TextView txtPrevPrice = (TextView) view.findViewById(R.id.txtPreviusPrice);
		TextView txtPrice = (TextView) view.findViewById(R.id.txtPrice);
		ImageView imgOf = (ImageView) view.findViewById(R.id.imgOffers);
		LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll_single_offer);
		
		ViewHolder myHolder = new ViewHolder();
		myHolder.title = txtTitleOf;
		myHolder.placeName = txtPlaceName;
		myHolder.prevousPrice = txtPrevPrice;
		myHolder.price = txtPrice;
		myHolder.imgOffers = imgOf;
		myHolder.ll = ll;

		view.setTag(myHolder);
		
		return view;
	}

}