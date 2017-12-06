package com.pyramitec.museumcatalog.Views.Museums;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.pyramitec.museumcatalog.Models.Museum;
import com.pyramitec.museumcatalog.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MuseumAdapter extends ArrayAdapter<Museum> {

public MuseumAdapter(Context context, List<Museum> data){
        super(context, 0, data);
        }

public View getView(int position, View convertView, ViewGroup parent) {

        Museum museum = getItem(position);

        if (convertView == null) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_museum_list, parent, false);
        }

        TextView id = (TextView) convertView.findViewById(R.id.id);
        id.setText("0");

        TextView tv = (TextView) convertView.findViewById(R.id.content);
        tv.setText(museum.getName());

        return convertView;
        }
}
