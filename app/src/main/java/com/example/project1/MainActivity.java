package com.example.project1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project1.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public Handler handler;
    public Handler coordinateHandler;
    public Handler getWeather;

    ArrayList<double[]> coordinates = new ArrayList<>();

    ListView listView;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

 //       Intent intent = new Intent(this, Main2Activity.class);
//        EditText editText = (EditText) findViewById(R.id.editText);
//        String message = editText.getText().toString();
        ArrayList massage = new ArrayList();
//        intent.putExtra("past_data", message);
//        startActivity(intent);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = (Button) findViewById(R.id.searchBtn);
//        Toast.makeText(this,"hello ", Toast.LENGTH_LONG).show();
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);


        final EditText cityName = (EditText) findViewById(R.id.cityNameTxt);
        listView = (ListView) findViewById(R.id.listview);

        cityName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                findViewById(R.id.listview).setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityNameStr = cityName.getText().toString();
                new Thread(new getCoordinate(cityNameStr, MainActivity.this, coordinates, handler, coordinateHandler)).start();
                progressBar.setVisibility(View.VISIBLE);
            }

        });
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                progressBar.setVisibility(View.GONE);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, (String[]) msg.obj);
                findViewById(R.id.listview).setVisibility(View.VISIBLE);
                listView.setAdapter(adapter);

            }
        };


        coordinateHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        new Thread(new getWeatherData(coordinates.get(position)[0], coordinates.get(position)[1], MainActivity.this, getWeather)).start();
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });
            }
        };

        getWeather = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                progressBar.setVisibility(View.GONE);
                System.out.println("here ");
                Intent startIntent = new Intent(getApplicationContext(), Main2Activity.class);
                startIntent.putExtra("weekWeather",(ArrayList) msg.obj);
                startActivity(startIntent);

            }
        };
    }
}
