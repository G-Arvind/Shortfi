package com.example.arvind.shortfi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ListAdapter;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;


public class CustomAdapter extends ArrayAdapter<String>{
    public CustomAdapter(Context context, String[] t) {
        super(context,R.layout.activity_customrow ,t);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater myinf=LayoutInflater.from(getContext());
        View CustomView=myinf.inflate(R.layout.activity_customrow,parent,false);

        String tr=getItem(position);
        TextView text=(TextView)CustomView.findViewById(R.id.text);
        ImageView image=(ImageView)CustomView.findViewById(R.id.image);

        text.setText(tr);
        return CustomView;

    }
}
