package com.example.project1;

import android.bluetooth.le.ScanSettings;
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

import java.util.ArrayList;

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
        String url ="https://api.darksky.net/forecast/"+secretKey+"/"+latitude+","+longitude;
        System.out.println("url : "+url);
//        String url = "https://api.darksky.net/forecast/b76283c6897558702b987cbb96599c59/35.696,51.401";
//        System.out.println(url);
//        System.out.println(url2);


        RequestQueue queue = Volley.newRequestQueue(context);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String text ="";
                            System.out.println(response.toString(4));

                            JSONObject jsonObject = response.getJSONObject("daily");
//                            System.out.println(jsonObject.toString(4));

                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            String[] icon = new String[7];
                            String[] summery = new String[7];
                            String[] precipType = new String[7];
                            String[] precipProbability = new String[7];
                            String[] discription = new String[7];
                            for (int i = 0; i < 7 ; i++) {
                                JSONObject c = jsonArray.getJSONObject(i);
                                summery[i] = c.getString("summary");
                                icon[i] = c.getString("icon");
                                precipType[i] = c.getString("precipType");
                                precipProbability[i] = c.getString("precipProbability");
                            }
                            Integer[] imgid=new Integer[7];
                            String[] icons = {
                                    "clear-day",
                                    "clear-night",
                                    "partly-cloudy-day",
                                    "partly-cloudy-night",
                                    "cloudy",
                                    "rain",
                                    "sleet",
                                    "snow",
                                    "wind",
                                    "fog"
                            };
                            for(int i = 0 ; i <7 ; i ++){
                                switch (icon[i]){
                                    case ("clear-day"):
                                        imgid[i] = R.drawable.clear_day;
                                        break;
                                    case ("clear-night"):
                                        imgid[i] = R.drawable.clear_night;
                                        break;
                                    case ("partly-cloudy-day"):
                                        imgid[i] = R.drawable.partly_cloudy_day;
                                        break;
                                    case ("partly-cloudy-night"):
                                        imgid[i] = R.drawable.partly_cloudy_night;
                                        break;
                                    case ("cloudy"):
                                        imgid[i] = R.drawable.cloudy;
                                        break;
                                    case ("rain"):
                                        imgid[i] = R.drawable.rain;
                                        break;
                                    case ("sleet"):
                                        imgid[i] = R.drawable.sleet;
                                        break;
                                    case ("snow"):
                                        imgid[i] = R.drawable.snow;
                                        break;
                                    case ("wind"):
                                        imgid[i] = R.drawable.wind;
                                        break;
                                    case ("fog"):
                                        imgid[i] = R.drawable.fog;
                                        break;


                                    default:
                                        System.out.println("oooooooops icon not cosidered :" + icons[i]);
                                        imgid[i] = R.drawable.ic_launcher_background;
                                }
                                discription[i] = "probability of "+ precipType[i] + " is "+precipProbability[i];
                            }



                            ArrayList msg = new ArrayList();
                            msg.add(imgid);
                            msg.add(summery);
                            msg.add(discription);

                            Message message = new Message();
                            message.obj = msg;
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

