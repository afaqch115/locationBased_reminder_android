package com.example.reminder.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reminder.controller.CustomInfoWindowAdapter;
import com.example.reminder.controller.PlaceAutocompleteAdapter;
import com.example.reminder.R;
import com.example.reminder.models.PlaceInfo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Map_view extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private static final String Fine_location = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String Coarse_location = Manifest.permission.ACCESS_COARSE_LOCATION;
    private Boolean loc_permission = false;
    GoogleMap gmap;
    private FusedLocationProviderClient locationProviderClient;
    private static float Default_zoom = 16f;
    private AutoCompleteTextView search_bar;
    private ImageView gps;
    private PlaceAutocompleteAdapter autocompleteAdapter;
    private GoogleApiClient googleApiClient;
    private static LatLngBounds lat_lng_bound=new LatLngBounds(new LatLng(-40,-168),new LatLng(71,136));
    private PlaceInfo mplace;
    MarkerOptions options;
    ImageView info_window;
    ImageView place_picker;
    Marker marker;
    int place_picker_request=1;
    Button loc_select;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        getPermissions();
        search_bar=(AutoCompleteTextView)findViewById(R.id.search_input);
        gps=(ImageView)findViewById(R.id.gps);
        options=new MarkerOptions();
        info_window=(ImageView)findViewById(R.id.info_window);
        place_picker=(ImageView)findViewById(R.id.place_picker);
        loc_select=(Button)findViewById(R.id.loc_select);

    }
    // Map initializer
    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                gmap = googleMap;

                if (loc_permission) {
                    getLocation();
                    if (ActivityCompat.checkSelfPermission(Map_view.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(Map_view.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    {
                        return;
                    }
                    gmap.setMyLocationEnabled(true);
                    gmap.getUiSettings().setMyLocationButtonEnabled(false);
                    gmap.getUiSettings();
                    initSearch();

                }

            }
        });



    }

// Permissions checking

    private void getPermissions()
    {
        String [] permissions={android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Fine_location)== PackageManager.PERMISSION_GRANTED)
        {
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),Coarse_location)== PackageManager.PERMISSION_GRANTED)
            {
                loc_permission=true;
                initMap();
            }
        }
        else
        {
            ActivityCompat.requestPermissions(this,permissions,1);
        }
    }

    // Location getting
    private void getLocation()
    {
        locationProviderClient= LocationServices.getFusedLocationProviderClient(this);

        try {
            if(loc_permission)
            {
                Task location=locationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task)
                    {
                        if(task.isSuccessful() && task!=null)
                        {

                            Location currentLocation=(Location)task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),Default_zoom,"My Location");

                        }
                        else
                        {
                            Toast.makeText(Map_view.this,"Unable to get Current Location",Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }

        }catch (SecurityException e)
        {
            e.printStackTrace();
        }

    }

    public LatLng retunLocation(Context context)
    {
        final LatLng[] latLng = new LatLng[1];
        locationProviderClient= LocationServices.getFusedLocationProviderClient(context);
        try {
            Task<Location> location=locationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task task)
                {
                    if(task.isSuccessful())
                    {
                        Location currentLocation=(Location)task.getResult();
                        latLng[0] =new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());

                    }
                    else
                    {
                        Toast.makeText(Map_view.this,"Unable to get Current Location",Toast.LENGTH_LONG).show();
                    }
                }
            });

        }catch (SecurityException e)
        {
            e.printStackTrace();
        }
        return latLng[0];

    }

    // moving camera to new location
    private void moveCamera(LatLng latLng,float zoom,String title)
    {
        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
        if(!title.equalsIgnoreCase("My Location"))
        {
            options.position(latLng)
                    .title(title);
            gmap.addMarker(options);
        }

    }

    // moving camera to new location
    private void moveCamera(LatLng latLng,float zoom,PlaceInfo place)
    {
        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
        gmap.clear();
        gmap.setInfoWindowAdapter(new CustomInfoWindowAdapter(Map_view.this));
        if(place!=null)
        {
            try
            {
                String info="Address: "+place.getAddress()+"\n"+
                        "Phone Number: "+place.getPhoneNumber()+"\n"+
                        "LatLng: "+place.getLatLng().latitude+", "+place.getLatLng().longitude+"\n"+
                        "Website: "+place.getWebsiteUri()+"\n"+
                        "Price Rating: "+place.getRatting()+"\n";

                options.position(latLng)
                        .title(place.getName())
                        .snippet(info);

                marker=gmap.addMarker(options);

            }catch (NullPointerException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            gmap.addMarker(new MarkerOptions().position(latLng));
        }


    }

// search functionality

    private void initSearch()
    {
        googleApiClient=new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this,  this)
                .build();


        search_bar.setOnItemClickListener(autocompleteClickListner);
        autocompleteAdapter=new PlaceAutocompleteAdapter(Map_view.this,googleApiClient,lat_lng_bound,null);
        search_bar.setAdapter(autocompleteAdapter);
        search_bar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEARCH ||
                        actionId==EditorInfo.IME_ACTION_DONE ||
                        event.getAction()==KeyEvent.ACTION_DOWN||
                        event.getAction()==KeyEvent.KEYCODE_ENTER )
                {
                    geoLocate();

                }
                return false;
            }
        });
        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });

        info_window.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    if(marker.isInfoWindowShown())
                    {
                        marker.hideInfoWindow();
                    }
                    else
                    {
                        marker.showInfoWindow();
                    }


                }catch (NullPointerException w)
                {
                    w.printStackTrace();
                }
            }
        });
        place_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                PlacePicker.IntentBuilder builder=new PlacePicker.IntentBuilder();
                try
                {
                    startActivityForResult(builder.build(Map_view.this),place_picker_request);
                } catch (GooglePlayServicesRepairableException e)
                {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

            }
        });

        loc_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent( Map_view.this,add_task.class);
                i.putExtra("latlng", (Parcelable) mplace);
                startActivity(i);
            }
        });
        gmap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng)
            {
                Geocoder geocoder=new Geocoder(Map_view.this);
                List<Address> list=new ArrayList<>();

                try
                {
                    list=geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);

                }catch (IOException e)
                {
                    e.printStackTrace();
                }
                if(list.size()>0)
                {
                    Address address=list.get(0);
                    //   moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),Default_zoom,address.getAddressLine(0));
                    mplace=new PlaceInfo();

                    mplace.setName(address.getFeatureName());
                    mplace.setLatLng(latLng);
                    mplace.setAddress(address.getAddressLine(0));
                    mplace.setPhoneNumber(address.getPhone());
                    moveCamera(latLng,Default_zoom,mplace);
                }
            }
        });

    }

    // finding required location
    private void geoLocate()
    {
        String searchString = search_bar.getText().toString();

        Geocoder geocoder=new Geocoder(Map_view.this);
        List<Address> list=new ArrayList<>();

        try
        {
            list=geocoder.getFromLocationName(searchString,1);

        }catch (IOException e)
        {
            e.printStackTrace();
        }
        if(list.size()>0)
        {
            Address address=list.get(0);
            LatLng latLng=new LatLng(address.getLatitude(),address.getLongitude());
            mplace=new PlaceInfo();

            mplace.setName(address.getFeatureName());
            mplace.setLatLng(latLng);
            mplace.setAddress(address.getAddressLine(0));
            mplace.setPhoneNumber(address.getPhone());
            moveCamera(latLng,Default_zoom,mplace);
            // moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),Default_zoom,address.getAddressLine(0))   ;

        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if(requestCode==place_picker_request)
        {
            if(resultCode==RESULT_OK)
            {
                Place place=PlacePicker.getPlace(Map_view.this,data);
                PendingResult<PlaceBuffer> placeResult= Places.GeoDataApi
                        .getPlaceById(googleApiClient,place.getId());
                placeResult.setResultCallback(placeDetailCallback);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        loc_permission=false;
        switch (requestCode)
        {
            case 1:
            {
                if(grantResults.length>0 )
                {
                    for(int i=0;i<grantResults.length;i++)
                    {
                        if( grantResults[0]!= PackageManager.PERMISSION_GRANTED)
                        {
                            loc_permission=false;
                            return;
                        }
                    }
                    loc_permission=true;
                    initMap();
                }
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    private void hideKeyboard()
    {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }







    /*
    -------------------- google places Api auto complete suggestion list handling-------------------------------
    */


    private AdapterView.OnItemClickListener autocompleteClickListner= new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            hideKeyboard();
            final AutocompletePrediction item=autocompleteAdapter.getItem(position);
            final String placeId=item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult= Places.GeoDataApi
                    .getPlaceById(googleApiClient,placeId);
            placeResult.setResultCallback(placeDetailCallback);

        }
    };
    private ResultCallback<PlaceBuffer> placeDetailCallback=new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if(!places.getStatus().isSuccess())
            {
                places.release();
                return;
            }
            final Place place=places.get(0);
            try
            {
                mplace=new PlaceInfo();
                mplace.setAddress(place.getAddress().toString());
                mplace.setId(place.getId().toString());
//                mplace.setAttributatiosn(place.getAttributions().toString());
                mplace.setLatLng(place.getLatLng());
                mplace.setName(place.getName().toString());
                mplace.setPhoneNumber(place.getPhoneNumber().toString());
                mplace.setWebsiteUri(place.getWebsiteUri());
                mplace.setRatting(place.getRating());

            }catch (NullPointerException e)
            {
                e.printStackTrace();
            }
            moveCamera(new LatLng(place.getViewport().getCenter().latitude
                    ,place.getViewport().getCenter().longitude),Default_zoom,mplace);
            places.release();

        }
    };


}
