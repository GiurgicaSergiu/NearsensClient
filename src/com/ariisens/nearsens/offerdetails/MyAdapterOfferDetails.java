package com.ariisens.nearsens.offerdetails;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ariisens.nearsens.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class MyAdapterOfferDetails extends BaseAdapter {

	Context context;
	ArrayList<ItemOffersGridView> items;
	
	
	public MyAdapterOfferDetails(Context context,ArrayList<ItemOffersGridView> data) {
		this.context = context;
		this.items = data;
	}
	@Override
	public int getCount() {
		
		return items.size();
	}

	@Override
	public ItemOffersGridView getItem(int index) {
		return items.get(index);
	}

	@Override
	public long getItemId(int index) {
		ItemOffersGridView items = getItem(index);
		return items.id;
	}
	int ia = 1;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	
		View view;
		if(convertView!= null){
			view = convertView;
		}
		else {
			LayoutInflater inflater = LayoutInflater.from(context);
			view = inflater.inflate(R.layout.cella_images,null);
			
			ImageView imgOf = (ImageView) view.findViewById(R.id.imgPhoto);
			
			ViewHolder myHolder = new ViewHolder();
			
			myHolder.imgOffers = imgOf;

			view.setTag(myHolder);
			
		}
		
		
		ItemOffersGridView myItems = items.get(position);
		
		ViewHolder holder = (ViewHolder) view.getTag();

		Glide.with(context).load("http://nearsens.somee.com/"+myItems.description).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imgOffers);
	
		
		return view;
	}

	private class ViewHolder{
		ImageView imgOffers;
	}
	
}

