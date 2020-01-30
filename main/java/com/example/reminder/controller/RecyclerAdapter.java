package com.example.reminder.controller;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.reminder.R;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyHolder>
{

    public interface onItemClickListner
    {
        void onItemClickListner(int position);
        void onDeleteClick(int position);
    }
    public ArrayList<String> name;
    public ArrayList<String> description;
    public onItemClickListner listner;
    //public String [] description;

    public RecyclerAdapter(ArrayList<String> name,ArrayList<String>description, onItemClickListner listner)
    {
        this.description=description;
        this.name=name;
        this.listner=listner;

    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater=LayoutInflater.from(viewGroup.getContext());
        View view=layoutInflater.inflate(R.layout.template,viewGroup,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder viewHolder, int i)
    {
        String heading=name.get(i);
        String body=description.get(i);

        viewHolder.title.setText(heading);
        viewHolder.detail.setText(body);
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

   public class MyHolder extends RecyclerView.ViewHolder
    {

        TextView title;
        ImageView icon;
        TextView detail;
        ImageView trash;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.name);
            detail=(TextView)itemView.findViewById(R.id.des);
            icon=(ImageView)itemView.findViewById(R.id.icon1);
            trash=(ImageView)itemView.findViewById(R.id.trash);


//action listners
            trash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listner!=null)
                    {
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            listner.onDeleteClick(position);
                        }
                    }

                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listner!=null)
                    {
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            listner.onItemClickListner(position);
                        }
                    }
                }
            });

        }
    }

}
