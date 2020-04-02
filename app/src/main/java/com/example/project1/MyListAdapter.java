package com.example.project1;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class MyListAdapter extends ArrayAdapter<String> {
    Context context;
    String[] maintitle;
    String[] subtitle;
    Integer[] imgid;

    public MyListAdapter(Context context, String[] maintitle,String[] subtitle, Integer[] imgid) {
        super(context, R.layout.custom_list_item_1 , maintitle);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.maintitle=maintitle;
        this.subtitle=subtitle;
        this.imgid=imgid;

    }

    public MyListAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View rowView=inflater.inflate(R.layout.custom_list_item_1, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.summerytxt);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.Itemimage);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.discriptiontxt);

        titleText.setText(maintitle[position]);
        imageView.setImageResource(imgid[position]);
        subtitleText.setText(subtitle[position]);

        return rowView;

    };
}
