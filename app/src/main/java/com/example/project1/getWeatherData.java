package com.example.project1;

import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class getWeatherData  implements Runnable{
    double latitude;
    double longitude;
    Handler handler;
    Handler handler3;
    Context context ;




    getWeatherData(double latitude,double longitude,Context context,Handler handler,Handler handler3){
        this.handler3 = handler3;
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
                            try {
                                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
                                outputStreamWriter.write(response.toString());
                                outputStreamWriter.close();
                            }
                            catch (IOException e) {
                                Log.e("Exception", "File write failed: " + e.toString());
                            }

                            JSONObject jsonObject = response.getJSONObject("daily");
//                            System.out.println(jsonObject.toString(4));
//                            jsonObject.getString("error");
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            String[] icon = new String[7];
                            String[] summery = new String[7];
                            String[] precipType = new String[7];
                            String[] precipProbability = new String[7];
                            String[] discription = new String[7];
                            if (jsonArray.length() == 0){
                                Toast.makeText(context,"query not found !",Toast.LENGTH_LONG).show();
                                Message message = new Message();
                                handler3.sendMessage(message);
                            }
                            for (int i = 0; i < 7 ; i++) {
                                JSONObject c = jsonArray.getJSONObject(i);
                                summery[i] = c.getString("summary");
                                icon[i] = c.getString("icon");
                                precipProbability[i] = c.getString("precipProbability");
                                if(c.has("precipType")){
                                    precipType[i] = c.getString("precipType");
                                }
                                else{
                                    precipType[i] = "precipitation";
                                }


                            }
                            Integer[] imgid=new Integer[7];

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
                                        System.out.println("oooooooops icon not cosidered :" + icon[i]);
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
                            Toast.makeText(context,"Couldn't get json from server." ,Toast.LENGTH_LONG).show();
                            Message message = new Message();
                            handler3.sendMessage(message);
                            E.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "cannot find information !\ncheck your connection:)",Toast.LENGTH_LONG).show();
                Message message = new Message();
                handler3.sendMessage(message);
//                String statusCode = new String(error.networkResponse.data);
//                System.out.println("Codigo " + statusCode);
            }
        });
        queue.add(jsonObjectRequest);

    }

}

