package com.ariisens.nearsens;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

public class MyMainApplication extends Application {
	
	private Context context;
	private static MyMainApplication mainApplication;
	
	private MyMainApplication() {
	}
	
	private MyMainApplication(Context ct){
		context = ct;
	}
	public static MyMainApplication getInstance(Context context){
		
		if(mainApplication == null){
			mainApplication = new MyMainApplication(context);
		}
		return mainApplication;
	}

	public Typeface getTypeFace(){
		return Typeface.createFromAsset(context.getAssets(), "roboto.ttf");
	}

	public Context getMyContext() {
		return context;
	}

	public void setMyContext(Context context) {
		this.context = context;
	}
	
	
}
