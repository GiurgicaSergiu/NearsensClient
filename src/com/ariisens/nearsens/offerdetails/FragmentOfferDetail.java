package com.ariisens.nearsens.offerdetails;

import android.app.Fragment;
import android.app.FragmentManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ariisens.nearsens.MyLoopJ;
import com.ariisens.nearsens.R;
import com.ariisens.nearsens.database.DbHelper;
import com.ariisens.nearsens.database.OffersTableHelper;
import com.ariisens.nearsens.database.PhotosOffersTableHelper;
import com.ariisens.nearsens.offerdetails.DownloaderFragmentOfferDetail.DownloadListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FragmentOfferDetail extends Fragment{
	
	private static final String ID_OFFER = "idOffer";

	private static final String LAT_OFFER = "latOffer";

	private static final String LON_OFFER = "lonOffer";

	private static final String PLACE_NAME = "place";
	
    private TextView txtTitle, txtPlace, txtDiscount, txtPreviousPrice, txtPrice, txtDescription, txtScadenza, txtAdress, txtLink;

    private ImageView imgHeader;
    
    private GoogleMap map;
	
    private String mPlace;
    
    private View view;

    private double mLat, mLng;
    private BitmapDescriptor bitmapDescriptor;
    
    private DownloaderFragmentOfferDetail fragment;
    
    private Drawable mActionBarBackgroundDrawable;
    
    private ItemOfferDetails itemOfferDetails;
    
    private LinearLayout llLoading;
    
    private String[] images;
    
    private int idOffer;
    
    private DownloadListener listener = new DownloadListener() {

		@Override
		public void onLoad(ItemOfferDetails result) {
			populateView(result);
		}

		@Override
		public void onError(int statusCode) {
			
		}

		
	};

	public static FragmentOfferDetail getOrCreateFragment(FragmentManager fragmentManager, String tag,int id,double lat,double lon,String place) {
		FragmentOfferDetail fragment = (FragmentOfferDetail) fragmentManager.findFragmentByTag(tag);
		if (fragment == null) {
			fragment = new FragmentOfferDetail();
			Bundle bundle = new Bundle();
			bundle.putInt(ID_OFFER, id);
			bundle.putDouble(LAT_OFFER, lat);
			bundle.putDouble(LON_OFFER, lon);
			bundle.putString(PLACE_NAME, place);
			fragment.setArguments(bundle);
			fragmentManager.beginTransaction().replace(R.id.frame_offer_detail,fragment, tag).commit();
		}
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view  = inflater.inflate(R.layout.fragment_offer_details, container,false);
		
		inizializeView(view);
		
        initilizeMap();
        
        getMyArguments();
        
        //controllMyLoadData(savedInstanceState);
        
        DbHelper dbHelper = new DbHelper(getActivity());
		String query = "SELECT  * FROM " + OffersTableHelper.TABLE_NAME + " WHERE " + OffersTableHelper._ID + " = " + idOffer;

		SQLiteDatabase db = dbHelper.getWritableDatabase();

		Cursor cursor = db.rawQuery(query, null);
		
		cursor.moveToFirst();
        
        populateView(cursor);
        
        editActionBar(view);
        
        setMap();
		
		return view;
	}

	/*private void controllMyLoadData(Bundle primoAvvio) {
		fragment = DownloaderFragmentOfferDetail.getOrCreateFragment(getFragmentManager(), "stationLoader");

		if (fragment.isTaskRunning()) {
			fragment.attach(listener);
		} else {
			ItemOfferDetails item = null;
			if (primoAvvio != null) {
				item = primoAvvio.getParcelable("stations");
			}

			if (item != null) {
				populateView(item);
			} else {
				loadData();
			}
		}
		
	}*/
	
	private void loadData() {
		llLoading.setVisibility(View.VISIBLE);
		fragment.attachOrLoadData(listener,getArguments().getLong(ID_OFFER));
	}
	

	private void inizializeView(View v) {
		
		txtTitle = (TextView) v.findViewById(R.id.txtTitle);
		txtPlace = (TextView) v.findViewById(R.id.txtPlace);
		txtDiscount = (TextView) v.findViewById(R.id.txtDiscount);
		txtPreviousPrice = (TextView) v.findViewById(R.id.txtPreviusPrice);
		txtPreviousPrice.setPaintFlags(txtPreviousPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		txtPrice = (TextView) v.findViewById(R.id.txtPrice);
		txtDescription = (TextView) v.findViewById(R.id.txtDesc);
		txtScadenza = (TextView) v.findViewById(R.id.txtScadenza);
		imgHeader = (ImageView) v.findViewById(R.id.imgHeader);
		txtAdress = (TextView) v.findViewById(R.id.txtAddress);
		txtLink = (TextView) v.findViewById(R.id.txtLink);
		llLoading = (LinearLayout) v.findViewById(R.id.ll_load_detail_offer);
		
		inizializeEvent();
	}
	

	private void inizializeEvent() {
		imgHeader.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentImageFullScreen.getOrCreateFragment(getActivity().getFragmentManager(), "fragmentfullScreen",imgHeader.getDrawable(),images);			
			}
		});
	}

	protected void populateView(ItemOfferDetails result) {
		llLoading.setVisibility(View.GONE);
		itemOfferDetails = result;
		txtTitle.setText(result.title);
		txtPlace.setText(mPlace);
		txtPrice.setText((result.price - ((result.price*result.discount)/100)) + " €");
		txtDiscount.setText(result.discount + "%");
		txtPreviousPrice.setText(result.price + " €");
		txtDescription.setText(result.description);
		txtScadenza.setText("L'offerta scade il " + result.expirationDate.split("T")[0]);
		txtAdress.setText(result.adress);
		txtLink.setText(result.link);
		Glide.with(getActivity().getApplicationContext()).load(MyLoopJ.BASE_URL + "/"+result.mainPhoto).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(imgHeader);
		
		images = result.images;
	}
	protected void populateView(Cursor result) {
		llLoading.setVisibility(View.GONE);
		txtTitle.setText(result.getString(result.getColumnIndex(OffersTableHelper.TITLE)));
		txtPlace.setText(result.getString(result.getColumnIndex(OffersTableHelper.PLACENAME)));
		txtPrice.setText((result.getFloat(result.getColumnIndex(OffersTableHelper.PRICE))) - ((result.getFloat(result.getColumnIndex(OffersTableHelper.PRICE))*result.getFloat(result.getColumnIndex(OffersTableHelper.DISCOUNT))/100)) + " €");
		txtDiscount.setText(result.getFloat(result.getColumnIndex(OffersTableHelper.DISCOUNT)) + "%");
		txtPreviousPrice.setText(result.getFloat(result.getColumnIndex(OffersTableHelper.PRICE)) + " €");
		txtDescription.setText(result.getString(result.getColumnIndex(OffersTableHelper.DESCRIPTION)));
		txtScadenza.setText("L'offerta scade il " + result.getString(result.getColumnIndex(OffersTableHelper.EXPIRATIONDATE)).split("T")[0]);
		txtAdress.setText(result.getString(result.getColumnIndex(OffersTableHelper.PLACEADDRESS)));
		txtLink.setText(result.getString(result.getColumnIndex(OffersTableHelper.LINK)));
		Glide.with(getActivity().getApplicationContext()).load(MyLoopJ.BASE_URL + "/"+result.getString(result.getColumnIndex(OffersTableHelper.MAINPHOTO))).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(imgHeader);
		
		DbHelper dbHelper = new DbHelper(getActivity());
		String query = "SELECT  * FROM " + PhotosOffersTableHelper.TABLE_NAME + " WHERE " + PhotosOffersTableHelper.ID_OFFER + " = " + result.getString(result.getColumnIndex(OffersTableHelper._ID));

		SQLiteDatabase db = dbHelper.getWritableDatabase();

		Cursor cursor = db.rawQuery(query, null);

		String[] photos = new String[cursor.getColumnCount()];
		if (cursor.moveToFirst()) {
			int i = 0;
			do {
				photos[i] = cursor.getString(cursor.getColumnIndex(PhotosOffersTableHelper.PHOTOS));
				i++;
			} while (cursor.moveToNext());
		}
		
		images = photos;
		result.close();
	}

	private void getMyArguments() {
		Bundle bundle = getArguments();
		mLat = bundle.getDouble(LAT_OFFER);
		mLng = bundle.getDouble(LON_OFFER);
		mPlace = bundle.getString(PLACE_NAME);
		idOffer = bundle.getInt(ID_OFFER);
	}

	private void initilizeMap() {
        if (map == null) {
            map = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            if (map == null) {
                Toast.makeText(getActivity().getApplicationContext(),
                        "Impossibile caricare la mappa", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
	
	private void setMap(){
		bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.mac);
		LatLng coordPlace = new LatLng(mLat, mLng);
		map.setMyLocationEnabled(true);
		MarkerOptions marker = new MarkerOptions().position(coordPlace).title(mPlace).icon(bitmapDescriptor);
		map.addMarker(marker);
		
		CameraPosition cameraPosition = new CameraPosition.Builder().target(coordPlace).zoom(13).build();
		map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		map.getUiSettings().setCompassEnabled(false);
		
	}
	
	private void editActionBar(View v) {
		mActionBarBackgroundDrawable = getResources().getDrawable(R.drawable.bg_actionbar);
		mActionBarBackgroundDrawable.setAlpha(0);
		getActivity().getActionBar().setBackgroundDrawable(mActionBarBackgroundDrawable);
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		getActivity().getActionBar().setHomeButtonEnabled(true);
		
		view = v;
		
		 ((NotifyingScrollView) v.findViewById(R.id.scroll_view)).setOnScrollChangedListener(mOnScrollChangedListener);

		 if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
			    mActionBarBackgroundDrawable.setCallback(mDrawableCallback);
		}
	}
	
	private NotifyingScrollView.OnScrollChangedListener mOnScrollChangedListener = new NotifyingScrollView.OnScrollChangedListener() {
        public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
            final int headerHeight = view.findViewById(R.id.imgHeader).getHeight() - getActivity().getActionBar().getHeight();
            final float ratio = (float) Math.min(Math.max(t, 0), headerHeight) / headerHeight;
            final int newAlpha = (int) (ratio * 255);
            mActionBarBackgroundDrawable.setAlpha(newAlpha);
        }
    };

    private Drawable.Callback mDrawableCallback = new Drawable.Callback() {
        @Override
        public void invalidateDrawable(Drawable who) {
        	getActivity().getActionBar().setBackgroundDrawable(who);
        }

        @Override
        public void scheduleDrawable(Drawable who, Runnable what, long when) {
        }

        @Override
        public void unscheduleDrawable(Drawable who, Runnable what) {
        }
    };
    
    @Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//outState.putParcelable("stations", itemOfferDetails);
	}
	
	
}
