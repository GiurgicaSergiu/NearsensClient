package com.ariisens.nearsens.map;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.ariisens.nearsens.MyMainApplication;
import com.ariisens.nearsens.R;
import com.ariisens.nearsens.interfaces.IOptionMap;
import com.loopj.android.http.JsonHttpResponseHandler;

public class DialogSelectCategory extends DialogFragment {

	public static final String DIALOG_TAG = "dialog_cat";
	private TextView txtTitle;
	private ListView listView;
	private ArrayList<ItemsCategory> itemsCategories;

	public static DialogSelectCategory getInstance() {
		DialogSelectCategory dialog = new DialogSelectCategory();
		return dialog;
	}

	IOptionMap iOptionMap;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		iOptionMap = (IOptionMap) activity;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder vBuilder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View view = inflater.inflate(R.layout.dialog_select_category, null);
		
		txtTitle = (TextView) view.findViewById(R.id.txtTitleCategory);
		listView = (ListView) view.findViewById(R.id.lvCategories);
		
		MyLoopJ.getInstance().get(MyLoopJ.getSubCategories(), new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {

				super.onSuccess(statusCode, headers, response);
				
				new AsyncTask<JSONArray, Void, Void>() {

					@Override
					protected Void doInBackground(JSONArray... params) {
						itemsCategories = ItemsCategory.createArray(params[0]);
						return null;
					}

					protected void onPostExecute(Void result) {
						listView.setAdapter(new MyAdapterCategory(getActivity(), itemsCategories));

					};

				}.execute(response);
				

			}
		});
		
		txtTitle.setTypeface(MyMainApplication.getInstance(null).getTypeFace());
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(position == 0){
					iOptionMap.updateCategory("", itemsCategories.get(position).name);
					
				}else{
					iOptionMap.updateCategory("&Subcategory=" + itemsCategories.get(position).name, itemsCategories.get(position).name);
				}
				dismiss();
			}
		});

		vBuilder.setView(view);

		return vBuilder.create();
	}

}