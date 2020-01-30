package com.example.reminder.view;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;

import com.example.reminder.controller.DatabaseHelper;
import com.example.reminder.R;
import com.example.reminder.controller.RecyclerAdapter;
import com.example.reminder.models.PlaceInfo;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // variables
    DatabaseHelper helper ;
    Toolbar tb;
    FloatingActionButton fab;
    RecyclerView rc;
    RecyclerView.LayoutManager layoutManager;
    public RecyclerAdapter adapter;
    public ArrayList<String> titlee = new ArrayList<String>();
    public ArrayList<String> detaill = new ArrayList<String>();
    public ArrayList<Double> latitude = new ArrayList<Double>();
    public ArrayList<Double> longitude = new ArrayList<Double>();
    ImageView trash;
    Location location;
    PlaceInfo place_info;
    LocationManager lm;
    LocationListener locationListener;
    String name, des;
    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.template, null, false);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        helper=new DatabaseHelper(this);
// variables initialization
        trash = (ImageView) view.findViewById(R.id.trash);
        tb = (Toolbar) findViewById(R.id.toolbar);
        rc = (RecyclerView) findViewById(R.id.recycler);
        setSupportActionBar(tb);
        place_info = getIntent().getParcelableExtra("place");
// data get from db
        titlee = helper.getName();
        detaill = helper.getDetail();
        latitude=helper.getLat();
        longitude=helper.getLng();
        if(!(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)))
        {
            buildAlertMessage();
        }

        if (titlee.isEmpty() && detaill.isEmpty()) {
            TextView t = (TextView) findViewById(R.id.textView);
            t.setText("You dont have any Task Click + \nButton to Add new Task!");
        }
// adapter onclick listner
        adapter = new RecyclerAdapter(titlee, detaill, new RecyclerAdapter.onItemClickListner() {
            @Override
            public void onItemClickListner(int position) {
                Intent intent = new Intent(getApplicationContext(), taskDetail.class);
                intent.putExtra("position", position);
                intent.putExtra("title", titlee.get(position));
                intent.putExtra("detail", detaill.get(position));
                startActivity(intent);

            }

            @Override
            public void onDeleteClick(final int position) {
                if (place_info != null) {
                    PlaceInfo info=new PlaceInfo();
                    info.setLatLng(new LatLng(latitude.get(position),longitude.get(position)));
                    helper.delete(titlee.get(position), detaill.get(position));
                    titlee.remove(position);
                    detaill.remove(position);
                    latitude.remove(position);
                    longitude.remove(position);
                    adapter.notifyItemRemoved(position);
                    if (titlee.isEmpty() && detaill.isEmpty()) {
                        TextView t = (TextView) findViewById(R.id.textView);
                        t.setText("You dont have any Task Click + \nButton to Add new Task!");
                    }
                }

            }
        });

// setting recycler view
        layoutManager = new LinearLayoutManager(this);
        rc.setLayoutManager(layoutManager);
        rc.setAdapter(adapter);
        adapter.notifyDataSetChanged();

// onclick listners
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, add_task.class);
                startActivity(intent);
                finish();
            }
        });

//        AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
//
//        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,1,notify,PendingIntent.FLAG_UPDATE_CURRENT);
//        alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+30000,pendingIntent);
    }
    private void buildAlertMessage()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        finish();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
