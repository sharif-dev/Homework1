package com.example.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    String[] maintitle;

    String[] subtitle ;

    Integer[] imgid;

    boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onResume();
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if ( !isConnected ){
            Toast.makeText(this,"Not connected to Internet",Toast.LENGTH_LONG).show();
        }
        setContentView(R.layout.activity_main2);
        ListView weekView = (ListView) findViewById(R.id.listView2);
        if (getIntent().hasExtra("weekWeather")) {
            ArrayList arrayList = getIntent().getExtras().getParcelableArrayList("weekWeather");
            imgid = (Integer[]) arrayList.get(0);
            maintitle = (String[] ) arrayList.get(1);
            subtitle = (String[ ] )  arrayList.get(2);

//            String[] weekWeather = getIntent().getExtras().getStringArray("weekWeather");
            MyListAdapter adapter=new MyListAdapter(this, maintitle, subtitle,imgid);
            weekView.setAdapter(adapter);

        }
    }

}