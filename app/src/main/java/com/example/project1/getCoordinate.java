package com.example.project1;

import android.content.Context;
//import android.net.Network;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

//import androidx.constraintlayout.solver.Cache;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class getCoordinate implements Runnable {
    String cityName = "";
    Context context ;
    Handler handler;
    getCoordinate(String cityName,Context context,Handler handler){
        this.cityName = cityName;
        this.context = context;
        this.handler = handler;

    }
    @Override
    public void run() {

        String accessToken = "pk.eyJ1IjoibmphdmlkIiwiYSI6ImNrOGNxcmxoMjA1MW8zbm1sejlyNGQxMnYifQ.QkYvVvl4rDBuZqkNscXX6A";
        String url = "https://api.mapbox.com/geocoding/v5/mapbox.places/"+cityName+".json?access_token="+accessToken;

        // Setup 1 MB disk-based cache for Volley
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);

        // Use HttpURLConnection as the HTTP client
        Network network = new BasicNetwork(new HurlStack());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String jsonStr = response;
                        if (jsonStr != null) {
                            try {
                                System.out.println("hiii");
                                Log.i("1","gui");
                                JSONObject jsonObj = new JSONObject(jsonStr);
                                // Getting JSON Array node
                                JSONArray contacts = jsonObj.getJSONArray("features");
                                // looping through All Contacts
                                String[]  cityNames = new String[contacts.length()];
                                HashMap<String, double[]> contact = new HashMap<>();
                                System.out.println(jsonObj);
                                for (int i = 0; i < contacts.length(); i++) {
                                    JSONObject c = contacts.getJSONObject(i);
                                    cityNames[i] = c.getString("place_name");
                                    String placeName = c.getString("place_name");
                                    double latitude = c.getJSONArray("center").getDouble(0);
                                    double longitude = c.getJSONArray("center").getDouble(1);
                                    System.out.println(latitude);
                                    System.out.println(longitude);
                                    double [] doubleAreay = {latitude,longitude};
                                    contact.put(placeName, doubleAreay);


                                    Message message = new Message();
                                    message.obj = cityNames;
                                    handler.sendMessage(message);

                                }
                            } catch (final JSONException e) {Log.i("2",e.toString());
                                e.printStackTrace();
                            }

                        } else {
                            System.out.println("Couldn't get json from server.");

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        // Instantiate the RequestQueue with the cache and network, start the request
        // and add it to the queue
        RequestQueue queue = new RequestQueue(cache, network);
        queue.start();
        queue.add(stringRequest);


    }
}
