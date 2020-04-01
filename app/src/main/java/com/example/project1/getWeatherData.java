package com.example.project1;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class getWeatherData  implements Runnable{
    double latitude;
    double longitude;
    Handler handler;
    Context context ;




    getWeatherData(double latitude,double longitude,Context context,Handler handler){
        this.latitude = latitude;
        this.longitude = longitude;
        this.context = context;
        this.handler = handler;
    }
    @Override
    public void run() {
        String secretKey = "b76283c6897558702b987cbb96599c59";
        // String url ="https://api.darksky.net/forecast/"+secretKey+"/"+latitude+"/"+longitude;
        String url = "https://api.darksky.net/forecast/b76283c6897558702b987cbb96599c59/37.8267,-122.4233";


        RequestQueue queue = Volley.newRequestQueue(context);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String text ="";

                            JSONObject jsonObject = response.getJSONObject("minutely");

                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            String[] weeklyWeather = new String[7];
                            for (int i = 0; i < 7 ; i++) {
                                weeklyWeather[i] = jsonArray.getJSONObject(i).toString();
                            }
                            Message message = new Message();
                            message.obj = weeklyWeather;
                            handler.sendMessage(message);

                        }
                        catch (JSONException E){
                            E.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("No response");
            }
        });
        queue.add(jsonObjectRequest);

    }

}

