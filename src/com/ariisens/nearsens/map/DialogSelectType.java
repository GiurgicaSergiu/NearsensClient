package com.ariisens.nearsens.map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ariisens.nearsens.R;
import com.ariisens.nearsens.interfaces.IOption;

public class DialogSelectType extends DialogFragment {

	public static final String DIALOG_TAG = "dialogType";
	
	private TextView txtAc;
	private TextView txtPOI;
	private TextView txtAll;


	public static DialogSelectType getInstance() {
		DialogSelectType dialog = new DialogSelectType();
		return dialog;
	}

	IOption iOptionMap;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		iOptionMap = (IOption) activity;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder vBuilder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View view = inflater.inflate(R.layout.dialog_select_type, null);

		txtAc = (TextView) view.findViewById(R.id.txtAC);
		txtPOI = (TextView) view.findViewById(R.id.txtPOI);
		txtAll = (TextView) view.findViewById(R.id.txtAll);
		
		txtAc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendOption("&category=AC","Attivitò commerciali");
			}
		});
		
		txtPOI.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendOption("&category=POI","Punti di interesse");
			}
		});
		
		txtAll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendOption("","Tutti");
			}
		});

		vBuilder.setView(view);

		return vBuilder.create();
	}

	protected void sendOption(String tipo,String value) {
		iOptionMap.updateTipo(tipo,value);
		dismiss();
	}

}