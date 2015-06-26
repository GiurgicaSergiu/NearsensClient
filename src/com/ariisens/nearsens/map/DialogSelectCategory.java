package com.ariisens.nearsens.map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.ariisens.nearsens.R;
import com.ariisens.nearsens.database.MyContentProvider;
import com.ariisens.nearsens.interfaces.IOption;

public class DialogSelectCategory extends DialogFragment implements LoaderCallbacks<Cursor> {

	public static final String DIALOG_TAG = "dialog_cat";
	private ListView listView;
	private static final int SUBCATEGORIES_LOADER_ID = 0;
	private MyAdapterCategory mAdapterCategory;

	public static DialogSelectCategory getInstance() {
		DialogSelectCategory dialog = new DialogSelectCategory();
		return dialog;
	}

	IOption iOptionMap;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof IOption)
			iOptionMap = (IOption) activity;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder vBuilder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View view = inflater.inflate(R.layout.dialog_select_category, null);
		
	
		listView = (ListView) view.findViewById(R.id.lvCategories);
		mAdapterCategory = new MyAdapterCategory(getActivity(), null);
		
		listView.setAdapter(mAdapterCategory);
		
		getLoaderManager().initLoader(SUBCATEGORIES_LOADER_ID, null, this);

		
	
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TextView txtItem = (TextView) view.findViewById(R.id.txtItem);
				String subcategory = (String) txtItem.getText();
				if(position == 0){
					if (iOptionMap != null)
						iOptionMap.updateCategory("", subcategory);
					
				}else{
					if (iOptionMap != null)
						iOptionMap.updateCategory("&Subcategory=" + subcategory, subcategory);
				}
				dismiss();
			}
		});

		vBuilder.setView(view);

		return vBuilder.create();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		if (id == SUBCATEGORIES_LOADER_ID) {
			CursorLoader loader = new CursorLoader(getActivity(), MyContentProvider.SUBCATEGORIES_URI
	                , null 
	                , null 
	                , null
	                , null);
	        return loader;
		}
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		mAdapterCategory.changeCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapterCategory.changeCursor(null);
	}

}