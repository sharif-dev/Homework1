package com.example.project1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {
    public Handler handler;
    public Handler coordinateHandler;
    public Handler getWeather;
    public Handler showLastData;
    public Handler queryDone ;

    ArrayList<double[]> coordinates = new ArrayList<>();

    ListView listView;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = (Button) findViewById(R.id.searchBtn);
//        Toast.makeText(this,"hello ", Toast.LENGTH_LONG).show();
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        final GifImageView gifImageView = findViewById(R.id.gif);
        gifImageView.setVisibility(View.GONE);
        findViewById(R.id.rectimage).setVisibility(View.GONE);


        final EditText cityName = (EditText) findViewById(R.id.cityNameTxt);
        listView = (ListView) findViewById(R.id.listview);

        cityName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                findViewById(R.id.listview).setVisibility(View.INVISIBLE);
                gifImageView.setVisibility(View.GONE);
                findViewById(R.id.rectimage).setVisibility(View.GONE);
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityNameStr = cityName.getText().toString();
                new Thread(new getCoordinate(cityNameStr, MainActivity.this, coordinates, handler, coordinateHandler,queryDone)).start();
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
                        new Thread(new getWeatherData(coordinates.get(position)[0], coordinates.get(position)[1], MainActivity.this, getWeather,queryDone)).start();
                        gifImageView.setVisibility(View.VISIBLE);
                        findViewById(R.id.rectimage).setVisibility(View.VISIBLE);
                    }
                });
            }
        };

        getWeather = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                progressBar.setVisibility(View.GONE);
                gifImageView.setVisibility(View.GONE);
                findViewById(R.id.rectimage).setVisibility(View.GONE);
                System.out.println("here ");
                Intent startIntent = new Intent(getApplicationContext(), Main2Activity.class);
                startIntent.putExtra("weekWeather",(ArrayList) msg.obj);
                startActivity(startIntent);

            }
        };
        showLastData = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                gifImageView.setVisibility(View.GONE);
                findViewById(R.id.rectimage).setVisibility(View.GONE);
                Intent startIntent = new Intent(getApplicationContext(), Main2Activity.class);
                startIntent.putExtra("weekWeather",(ArrayList) msg.obj);
                startActivity(startIntent);

            }
        };
        queryDone = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                gifImageView.setVisibility(View.GONE);
                findViewById(R.id.rectimage).setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);

            }
        };
    }
    @Override
    protected void onResume() {
        super.onResume();
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if ( !isConnected ){
            Toast.makeText(this,"Not connected to Internet",Toast.LENGTH_LONG).show();
            new Thread(new ReadAndDisplayLastData(this,showLastData)).start();
        }
        System.out.println("net :" + isConnected);

    }
}
