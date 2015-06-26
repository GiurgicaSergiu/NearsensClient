package com.ariisens.nearsens.map;

import java.util.ArrayList;

import com.ariisens.nearsens.R;
import com.ariisens.nearsens.database.SubcategoriesTableHelper;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class MyAdapterCategory extends CursorAdapter {
	
	public MyAdapterCategory(Context context, Cursor data) {
		super(context, data, 0);
	}
	
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.cella_category,null);
		TextView textViewId = (TextView) view.findViewById(R.id.txtItem);
	
		ViewHolder myHolder = new ViewHolder();
		myHolder.name = textViewId;

		view.setTag(myHolder);
		return view;
	}
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		int subcategorColumnIndex = cursor.getColumnIndex(SubcategoriesTableHelper.NAME);
		
		ViewHolder holder = (ViewHolder) view.getTag();
			
		holder.name.setText("" + cursor.getString(subcategorColumnIndex));
	}
	
	private class ViewHolder{
		TextView name;
	}
}
