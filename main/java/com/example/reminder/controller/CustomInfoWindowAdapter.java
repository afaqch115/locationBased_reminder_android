package com.example.reminder.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.reminder.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter

{
    private View mwindow;
    private Context context;

    public CustomInfoWindowAdapter(Context context) {
        this.context = context;
        mwindow= LayoutInflater.from(context).inflate(R.layout.info_window_layout,null,false);
    }
    private void renderText(Marker marker ,View view)
    {
        String title=marker.getTitle();
        TextView t1=(TextView)view.findViewById(R.id.title_info);

        if(!title.equals(""))
        {
            t1.setText(title);
        }

        String snip=marker.getSnippet();
        TextView t2=(TextView)view.findViewById(R.id.snippet);
        if(!snip.equals(""))
        {
            t2.setText(snip);
        }

    }

    @Override
    public View getInfoWindow(Marker marker)
    {
        renderText(marker,mwindow);
        return mwindow;
    }

    @Override
    public View getInfoContents(Marker marker)
    {
        renderText(marker,mwindow);
        return mwindow;
    }
}
