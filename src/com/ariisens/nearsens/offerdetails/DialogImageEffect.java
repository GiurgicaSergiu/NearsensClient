package com.ariisens.nearsens.offerdetails;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;




import android.widget.LinearLayout;

import com.ariisens.nearsens.R;
import com.bumptech.glide.Glide;

public class DialogImageEffect extends DialogFragment {

	public static final String DIALOG_TAG = "dialog";

	private static final String URL = "img_url";
	
	private ImageView img;
	private LinearLayout ll_effect;

	public static DialogImageEffect getInstance(String url) {
		DialogImageEffect dialog = new DialogImageEffect();
		Bundle bundle = new Bundle();
		bundle.putString(URL, url);
		dialog.setArguments(bundle);
		return dialog;
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder vBuilder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View view = inflater.inflate(R.layout.dialog_effect, null);

		img = (ImageView) view.findViewById(R.id.img_effect);
		ll_effect = (LinearLayout) view.findViewById(R.id.ll_effect);
		
		view.animate().translationY(500).setDuration(300);

		Bundle bundle = getArguments();

		//Glide.with(this).load("http://nearsens.somee.com/"+bundle.getString(URL)).centerCrop().into(img);

		vBuilder.setView(view);

		return vBuilder.create();
	}

}