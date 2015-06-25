package com.ariisens.nearsens;

import com.facebook.stetho.Stetho;

import android.app.Application;

public class MyMainApplication extends Application {

	public void onCreate() {
	    super.onCreate();
	    Stetho.initialize(
	      Stetho.newInitializerBuilder(this)
	        .enableDumpapp(
	            Stetho.defaultDumperPluginsProvider(this))
	        .enableWebKitInspector(
	            Stetho.defaultInspectorModulesProvider(this))
	        .build());
	  }
}
