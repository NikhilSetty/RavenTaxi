package com.diemen.olaoff;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.diemen.olaoff.utilities.CabItem;
import com.diemen.olaoff.utilities.CabItemAdapter;
import com.diemen.olaoff.utilities.LocationUtility;
import com.diemen.olaoff.utilities.RecyclerOnItemClickListener;
import com.diemen.olaoff.utilities.SmsSender;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class BookingActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private static String TAG = "BookingActivity";
    public static final String EXTRA_INTERNET = "is_internet";

    GoogleApiClient mGoogleApiClient;

    public GoogleMap googleMap;
    public Marker myMarker;
    private Location myLocation;
    private boolean isAnimated = false;
    private static final int MIN_DISTANCE = 500;
    private static final float DEFAULT_ZOOM_LEVEL = 15.0f;
    BitmapDescriptor mMarkerIcon;

    private Button mRideNowButton;
    private boolean isConnected;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<CabItem> mCabList;
    private CabItemAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        buildGoogleApiClient();
        mMarkerIcon = BitmapDescriptorFactory.fromResource(R.drawable.marker);
//        mMarkerIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
        isConnected = getIntent().getBooleanExtra(EXTRA_INTERNET, false);
        mRideNowButton = (Button) findViewById(R.id.ride_now_btn);

        SupportMapFragment mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(
                R.id.map_view));
        if(mapFragment != null) {
            googleMap =mapFragment.getMap();
            if (googleMap != null) {
                initMap();
            }
        }

        if(!isConnected){
            showOfflineDialog();
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mCabList = CabItem.getCabList();
        mAdapter = new CabItemAdapter(mCabList, mOnItemClickCallback);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void showOfflineDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MaterialDialog);
        Resources res = getApplicationContext().getResources();
        builder.setTitle("Offline !!!");
        builder.setMessage("You are not connected to internet. Want to book cab Offline??");
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.dialog_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(myLocation == null){
                    Toast.makeText(getApplicationContext(), "Hang on... Waiting for location to fetch...",Toast.LENGTH_SHORT).show();
                }else {
                    dialog.dismiss();
                    offlineBook();
                }
            }
        });
        builder.setNegativeButton(getString(R.string.dialog_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.show();
    }

    private void offlineBook(){
        SmsSender.sendSms("", "", myLocation,"mini");
    }

    //-------------------------------------------------------------------------
    // map
    //-------------------------------------------------------------------------
    public void initMap() {
        // check if map is created successfully or not
        if (googleMap == null) {
            Toast.makeText(getApplicationContext(),
                    "Unable to load map", Toast.LENGTH_SHORT)
                    .show();
        } else {
            // Showing / hiding your current location
            googleMap.setMyLocationEnabled(true);
            // Enable / Disable my location button
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.getUiSettings().setMapToolbarEnabled(false);
            googleMap.setOnMyLocationChangeListener(myLocationChangeListener);
        }
    }

    private com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            if (myLocation == null || myLocation.distanceTo(location) > MIN_DISTANCE) {
                myLocation = location;
                Log.i(TAG, "onMyLocationChange Location:"+myLocation.getLatitude()+" : "+myLocation.getLongitude());
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                if (googleMap != null) {
                    if (myMarker != null) {
                        myMarker.remove();
                    }
                    MarkerOptions marker = new MarkerOptions()
                            .position(loc)
                            .icon(mMarkerIcon);
                    myMarker = googleMap.addMarker(marker);
                    if (!isAnimated) {
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, DEFAULT_ZOOM_LEVEL));
                        isAnimated = true;
                    }
                }
            }
        }
    };

    private void showPlayLocation(Location location) {
        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
        Log.v("Booking", "Real Time : lat:" + location.getLatitude() + " longitude:" + location.getLongitude());
        if (googleMap != null) {
            if (myMarker != null) {
                myMarker.remove();
            }
            MarkerOptions marker = new MarkerOptions()
                    .position(loc)
                    .icon(mMarkerIcon);
            myMarker = googleMap.addMarker(marker);
            myMarker.showInfoWindow();

            // set zoom level to show marker
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, DEFAULT_ZOOM_LEVEL));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocationUtility locationUtility = LocationUtility.getInstance(getApplicationContext());
        if (locationUtility.isGPSEnabled()) {
            Location location = locationUtility.getLastKnownLocation();
            if(location!= null){
                myLocation = location;
                Log.i(TAG, "onStart Location:"+myLocation.getLatitude()+" : "+myLocation.getLongitude());
            }
        } else {
            locationUtility.showSettingsAlert(BookingActivity.this);
        }
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        myLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(myLocation != null){
            showPlayLocation(myLocation);
            Log.i(TAG, "onConnected Location:" + myLocation.getLatitude() + " : " + myLocation.getLongitude());
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private RecyclerOnItemClickListener.OnItemClickCallback mOnItemClickCallback = new RecyclerOnItemClickListener.OnItemClickCallback() {
        @Override
        public void onItemClicked(View view, int position) {
            switch (view.getId()) {
                default:
                    Toast.makeText(getApplicationContext(), mCabList.get(position).getCabItem()+" selected", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
