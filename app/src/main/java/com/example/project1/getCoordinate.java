package com.example.project1;

import android.content.Context;
//import android.net.Network;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.HashMap;

public class getCoordinate implements Runnable {
    String cityName = "";
    Context context ;
    Handler handler;
    Handler handler2;
    Handler handler3;
    ArrayList<double[]> contact;

    getCoordinate(String cityName,Context context,ArrayList<double[]> contact,Handler handler,Handler coordinateHandler,Handler handler3){
        this.handler3 = handler3;
        this.cityName = cityName;
        this.context = context;
        this.handler = handler;
        this.handler2 = coordinateHandler;
        this.contact = contact;

    }
    @Override
    public void run() {

        String accessToken = "pk.eyJ1IjoibmphdmlkIiwiYSI6ImNrOGNxcmxoMjA1MW8zbm1sejlyNGQxMnYifQ.QkYvVvl4rDBuZqkNscXX6A";
        String url = "https://api.mapbox.com/geocoding/v5/mapbox.places/"+cityName+".json?access_token="+accessToken;

        // Setup 1 MB disk-based cache for Volley
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);

        // Use HttpURLConnection as the HTTP client
        Network network = new BasicNetwork(new HurlStack());

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String jsonStr = response;
                        if (jsonStr != null) {
                            try {
                                JSONObject jsonObj = new JSONObject(jsonStr);
                                // Getting JSON Array node
                                JSONArray contacts = jsonObj.getJSONArray("features");
                                // looping through All Contacts
                                String[]  cityNames = new String[contacts.length()];
                                // ArrayList<double[]> contact = new ArrayList<>();

                                //  HashMap<String, double[]> contact = new HashMap<>();

                                System.out.println(response.toString());


                                if (contacts.length() == 0){
                                    Toast.makeText(context,"query not found !",Toast.LENGTH_LONG).show();
                                    Message message = new Message();
                                    handler3.sendMessage(message);
                                }
                                for (int i = 0; i < contacts.length(); i++) {
                                    JSONObject c = contacts.getJSONObject(i);
                                    cityNames[i] = c.getString("place_name");
                                    String placeName = c.getString("place_name");
                                    double latitude = c.getJSONArray("center").getDouble(0);
                                    double longitude = c.getJSONArray("center").getDouble(1);
                                    double [] doubleArray = {latitude,longitude};
                                    // contact.put(placeName, doubleArray);
                                    contact.add(doubleArray);


                                    Message message = new Message();
                                    message.obj = cityNames;
                                    handler.sendMessage(message);

                                    Message message2 = new Message();
                                    message2.obj = contact;
                                    handler2.sendMessage(message2);

                                }
                            } catch (final JSONException e) {Log.i("2",e.toString());
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(context,"Couldn't get json from server." ,Toast.LENGTH_LONG).show();
                            Message message = new Message();
                            handler3.sendMessage(message);

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "cannot find information !\ncheck your connection:)",Toast.LENGTH_LONG).show();
                Message message = new Message();
                handler3.sendMessage(message);
            }
        });

        // Instantiate the RequestQueue with the cache and network, start the request
        // and add it to the queue
        RequestQueue queue = new RequestQueue(cache, network);
        queue.start();
        queue.add(stringRequest);


    }
}