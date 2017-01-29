package com.example.zed.newnews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Zed on 1/26/2017.
 */

public class ListAdapter extends ArrayAdapter<Headline>{

    public ListAdapter(Context context, ArrayList<Headline> headlines) {super (context,0, headlines);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        View listView = convertView;


        if (listView == null){
            listView = LayoutInflater.from(getContext()).inflate(R.layout.news_entry, parent, false);
        }

        Headline headline = getItem(position);

        TextView sectionView = (TextView) convertView.findViewById(R.id.section);
        sectionView.setText(headline.getSection());

        TextView titleTextView = (TextView) convertView.findViewById(R.id.title);
        titleTextView.setText(headline.getTitle());

        return listView;

    }

}
