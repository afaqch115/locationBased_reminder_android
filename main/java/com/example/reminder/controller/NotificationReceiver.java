package com.example.reminder.controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.example.reminder.models.PlaceInfo;
import com.example.reminder.view.MainActivity;
import com.example.reminder.view.taskDetail;

public class NotificationReceiver extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

        LocationManager lm;
        LocationListener listen;
        double lati, longi;
        static final float Radius=1000;
        public static final String chanel_id="007";
        public String chanel_name="myChannel";
        boolean check=true;
        DatabaseHelper databaseHelper;



    @SuppressLint("WrongConstant")
        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            super.onStartCommand(intent, flags, startId);

            final PlaceInfo info=intent.getParcelableExtra("place");
            final String namme=intent.getStringExtra("name");
            final String des=intent.getStringExtra("des");
            databaseHelper=new DatabaseHelper(this);

            final float[] results=new float[10];

            listen = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    lati = location.getLatitude();
                    longi = location.getLongitude();
                    Location.distanceBetween(lati,longi,info.getLatLng().latitude,info.getLatLng().longitude,results);

                    if(results[0]<Radius && check)
                    {
                        check=false;
                        Toast.makeText(getApplicationContext(),"in range",Toast.LENGTH_LONG).show();

                        Uri alert= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        MediaPlayer m=MediaPlayer.create(NotificationReceiver.this,alert);
                        m.start();
                        Intent mainIntent=new Intent(NotificationReceiver.this,taskDetail.class);
                        mainIntent.putExtra("title",namme);
                        mainIntent.putExtra("detail",des);
                        PendingIntent content=PendingIntent.getActivity(NotificationReceiver.this,1,mainIntent,0);
                        NotificationManager nm=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                        int importance = NotificationManager.IMPORTANCE_LOW;
                       // NotificationChannel notificationChannel = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                        {

                            NotificationChannel notificationChannel = new NotificationChannel(chanel_id, chanel_name, importance);
                            notificationChannel.enableLights(true);
                            notificationChannel.setLightColor(Color.RED);
                            notificationChannel.enableVibration(true);
                            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                            nm.createNotificationChannel(notificationChannel);
                            Notification.Builder notification=new Notification.Builder(getApplicationContext(),chanel_id);
                            notification.setSmallIcon(android.R.drawable.ic_dialog_info)
                                    .setContentTitle(namme)
                                    .setContentText(des)
                                    .setWhen(System.currentTimeMillis())
                                    .setAutoCancel(true)
                                    .setContentIntent(content)
                                    .setWhen(System.currentTimeMillis())
                                    .setShowWhen(true)
                                    .setPriority(Notification.PRIORITY_MAX)
                                    .setDefaults(Notification.DEFAULT_ALL);

                            nm.notify(1,notification.build());
                        }
                        else
                        {
                            Notification.Builder notification=new Notification.Builder(getApplicationContext());
                            notification.setSmallIcon(android.R.drawable.ic_dialog_info)
                                    .setContentTitle(namme)
                                    .setContentText(des)
                                    .setWhen(System.currentTimeMillis())
                                    .setAutoCancel(true)
                                    .setShowWhen(true)
                                    .setContentIntent(content)
                                    .setPriority(Notification.PRIORITY_MAX)
                                    .setDefaults(Notification.DEFAULT_ALL);

                            nm.notify(1,notification.build());

                        }
                        databaseHelper.delete(namme,des);
                    }



                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return 0;
            }
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 100, listen);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 100, listen);


        return START_STICKY;
        }
}
