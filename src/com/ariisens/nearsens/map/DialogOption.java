package com.ariisens.nearsens.map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.ariisens.nearsens.R;
import com.ariisens.nearsens.interfaces.IOptionMap;

public class DialogOption extends DialogFragment {

	public static final String DIALOG_TAG = "dialog";
	private static final String RAGGIO = "raggio";
	private SeekBar areaBar;
	private TextView txtKmArea;
	private TextView txtOk;

	public static DialogOption getInstance(int raggio) {
		DialogOption dialog = new DialogOption();
		Bundle bundle = new Bundle();
		bundle.putInt(RAGGIO, raggio);
		dialog.setArguments(bundle);
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
		View view = inflater.inflate(R.layout.dialog_option_map, null);

		areaBar = (SeekBar) view.findViewById(R.id.seekBarArea);
		txtKmArea = (TextView) view.findViewById(R.id.txtKm);
		txtOk = (TextView) view.findViewById(R.id.txtOk);
	
		txtOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				iOptionMap.onConfirmArea();
				dismiss();
			}
		});
		
		Bundle bundle = getArguments();
		
		areaBar.setProgress(bundle.getInt(RAGGIO));
		txtKmArea.setText(bundle.getInt(RAGGIO) + " Km");

		areaBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				int valueProgress = progress + 1;
				txtKmArea.setText(valueProgress + " km");
				if(iOptionMap!=null){
					
					iOptionMap.updateRaggio(valueProgress);
				}
					
			}
		});

		vBuilder.setView(view);

		return vBuilder.create();
	}

}