package com.example.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ListView weekView = (ListView) findViewById(R.id.listView2);
        if (getIntent().hasExtra("weekWeather")) {
            String[] weekWeather = getIntent().getExtras().getStringArray("weekWeather");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Main2Activity.this, android.R.layout.simple_list_item_1, weekWeather);
            weekView.setAdapter(adapter);

        }
    }

}
