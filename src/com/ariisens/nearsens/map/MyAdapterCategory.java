package com.ariisens.nearsens.map;

import java.util.ArrayList;

import com.ariisens.nearsens.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapterCategory extends BaseAdapter {

	Context context;
	ArrayList<ItemsCategory> items;
	
	public MyAdapterCategory(Context context,ArrayList<ItemsCategory> data) {
		this.context = context;
		this.items = data;
	}
	@Override
	public int getCount() {
		
		return items.size();
	}

	@Override
	public ItemsCategory getItem(int index) {
		return items.get(index);
	}

	@Override
	public long getItemId(int index) {
		ItemsCategory items = getItem(index);
		return items.mioId;
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
			view = inflater.inflate(R.layout.cella_category,null);
			TextView textViewId = (TextView) view.findViewById(R.id.txtItem);
		
			ViewHolder myHolder = new ViewHolder();
			myHolder.name = textViewId;

			view.setTag(myHolder);
			
		}
		
		ItemsCategory myItems = items.get(position);
		
		ViewHolder holder = (ViewHolder) view.getTag();
			
		holder.name.setText("" + myItems.name);
		
		return view;
	}

	private class ViewHolder{
		TextView name;
	}
}
