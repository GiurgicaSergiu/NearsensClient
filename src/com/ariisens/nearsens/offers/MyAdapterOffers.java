package com.ariisens.nearsens.offers;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ariisens.nearsens.R;

public class MyAdapterOffers extends BaseAdapter {

	Context context;
	ArrayList<ItemsOffers> items;
	
	private static int[] images = {R.drawable.ms,R.drawable.pesce,R.drawable.carne,R.drawable.usb};
	
	public MyAdapterOffers(Context context,ArrayList<ItemsOffers> data) {
		this.context = context;
		this.items = data;
	}
	@Override
	public int getCount() {
		
		return items.size();
	}

	@Override
	public ItemsOffers getItem(int index) {
		return items.get(index);
	}

	@Override
	public long getItemId(int index) {
		ItemsOffers items = getItem(index);
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
			view = inflater.inflate(R.layout.cella_offerte,null);
			
			TextView txtPlaceName = (TextView) view.findViewById(R.id.txtPlaceName);
			TextView txtTitleOf = (TextView) view.findViewById(R.id.txtTileOf);
			TextView txtPrevPrice = (TextView) view.findViewById(R.id.txtPreviusPrice);
			TextView txtPrice = (TextView) view.findViewById(R.id.txtPrice);
			ImageView imgOf = (ImageView) view.findViewById(R.id.imgOffers);
			
			ViewHolder myHolder = new ViewHolder();
			myHolder.title = txtTitleOf;
			myHolder.placeName = txtPlaceName;
			myHolder.prevousPrice = txtPrevPrice;
			myHolder.price = txtPrice;
			myHolder.imgOffers = imgOf;

			view.setTag(myHolder);
			
		}
		
		
		ItemsOffers myItems = items.get(position);
		
		ViewHolder holder = (ViewHolder) view.getTag();
		
		
		holder.title.setText("" + myItems.title);
		holder.placeName.setText("" + myItems.placeName);
		holder.prevousPrice.setText("" + myItems.previousPrice + " €");
		holder.price.setText("" + myItems.price + " €");
		
	
		holder.imgOffers.setImageResource(images[position%4]);
	
		//holder.name.setTypeface(MyMainApplication.getInstance(null).getTypeFace());
		
		return view;
	}

	private class ViewHolder{
		TextView title;
		TextView placeName;
		TextView prevousPrice;
		TextView price;
		ImageView imgOffers;
	}
	
}
