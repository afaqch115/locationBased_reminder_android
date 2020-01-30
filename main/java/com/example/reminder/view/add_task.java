package com.example.reminder.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reminder.controller.DatabaseHelper;
import com.example.reminder.R;
import com.example.reminder.controller.NotificationReceiver;
import com.example.reminder.models.PlaceInfo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class add_task extends AppCompatActivity {

    DatabaseHelper db_helper = new DatabaseHelper(this);
    EditText tittle, detaill;
    Toolbar tb;
    TextView place_detail;
    ImageView show_map;
    PlaceInfo info;
    String name, des;
    Location location;
    Map_view map_view;
    double lati, longi;
    LocationManager lm;
    static final float Radius = 1000;
    LocationListener listen;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        tittle = (EditText) findViewById(R.id.title_name);
        detaill = (EditText) findViewById(R.id.detail);
        tb = (Toolbar) findViewById(R.id.toolbar);
        place_detail = (TextView) findViewById(R.id.location_text);
        tb.setTitle("Add Task");
        setSupportActionBar(tb);
        info = new PlaceInfo();
        Button save = (Button) findViewById(R.id.save);
        map_view = new Map_view();

        info = getIntent().getParcelableExtra("latlng");

        if (savedInstanceState != null) {
            name = savedInstanceState.getString("title");
            des = savedInstanceState.getString("detail");
            tittle.setText(name);
            detaill.setText(des);
        }

        if (info != null) {
            String text = info.getName() + "," + info.getAddress();
            place_detail.setText(text);

        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (savedInstanceState != null) {
                    name = savedInstanceState.getString("title");
                    des = savedInstanceState.getString("detail");
                } else {
                    name = tittle.getText().toString();
                    des = detaill.getText().toString();
                }

                if (name.equalsIgnoreCase("") || des.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please Fill All Fields", Toast.LENGTH_LONG).show();
                } else {
                    if (info != null) {
                        createDialog();
//                        db_helper.insert(name, des, info);
//                        Intent intent = new Intent(add_task.this, MainActivity.class);
//                        intent.putExtra("place", (Parcelable) info);
//                        intent.putExtra("name", name);
//                        intent.putExtra("des", des);
////                        float[] results = new float[5];
////                        Location.distanceBetween(lati, longi, info.getLatLng().latitude, info.getLatLng().longitude, results);
////
////                        if(results[0]<Radius)
////                        {
////                            Toast.makeText(getApplicationContext(),"in range",Toast.LENGTH_LONG).show();
////                        }
////                        else
////                        {
////                            Toast.makeText(getApplicationContext(),"not in range",Toast.LENGTH_LONG).show();
////                        }
//                        // service start
//
//                        Intent notify=new Intent(getApplicationContext(), NotificationReceiver.class);
//                        notify.putExtra("place", (Parcelable) info);
//                        notify.putExtra("name",name);
//                        notify.putExtra("des",des);
//                        startService(notify);
//                        //service
//                        startActivity(intent);
//                        finish();
                    }
                }
            }
        });

        show_map = (ImageView) findViewById(R.id.map_view);

        show_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkServices()) {
                    Intent intent = new Intent(add_task.this, Map_view.class);
                    startActivity(intent);
                }
            }
        });

        if (info != null) {
            place_detail.setText(info.getName());
        }
//        listen = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                lati = location.getLatitude();
//                longi = location.getLongitude();
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//
//            }
//        };
//        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listen);

    }

    public  boolean checkServices()
    {
        int available= GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(add_task.this);
        if(available== ConnectionResult.SUCCESS)
        {
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available))
        {
            Dialog dialog=GoogleApiAvailability.getInstance().getErrorDialog(add_task.this,available,9001);
            dialog.show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"You can't make Map Request!!",Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState!=null)
        {
            name=savedInstanceState.getString("title");
            des=savedInstanceState.getString("detail");
            tittle.setText(name);
            detaill.setText(des);
        };
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("title",name);
        outState.putString("detail",des);

    }
    private void createDialog()
    {
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setMessage("Do you want to save the data.");
        alert.setCancelable(false);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                db_helper.insert(name, des, info);
                Intent intent = new Intent(add_task.this, MainActivity.class);
                intent.putExtra("place", (Parcelable) info);
                intent.putExtra("name", name);
                intent.putExtra("des", des);
                Intent notify=new Intent(getApplicationContext(), NotificationReceiver.class);
                notify.putExtra("place", (Parcelable) info);
                notify.putExtra("name",name);
                notify.putExtra("des",des);
                startService(notify);
                //service
                startActivity(intent);
                finish();

            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {


            }
        });
        alert.create().show();
    }

}
