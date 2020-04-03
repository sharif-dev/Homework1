package com.example.project1;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ReadAndDisplayLastData implements Runnable {
    Handler handler;
    Context context ;
    ReadAndDisplayLastData (Context context , Handler handler){
        this.context = context;
        this.handler = handler;
    }
    @Override
    public void run() {
        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        try {

            JSONObject response = new JSONObject(ret);
            JSONObject jsonObject = response.getJSONObject("daily");
            JSONArray jsonArray = jsonObject.getJSONArray("data");

            String[] icon = new String[7];
            String[] summery = new String[7];
            String[] precipType = new String[7];
            String[] precipProbability = new String[7];
            String[] discription = new String[7];
            if (jsonArray.length() == 0) {
                Toast.makeText(context, "query not found !", Toast.LENGTH_LONG).show();
            }
            for (int i = 0; i < 7; i++) {
                JSONObject c = jsonArray.getJSONObject(i);
                summery[i] = c.getString("summary");
                icon[i] = c.getString("icon");
                precipProbability[i] = c.getString("precipProbability");
                if (c.has("precipType")) {
                    precipType[i] = c.getString("precipType");
                } else {
                    precipType[i] = "precipitation";
                }


            }
            Integer[] imgid = new Integer[7];

            for (int i = 0; i < 7; i++) {
                switch (icon[i]) {
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
                discription[i] = "probability of " + precipType[i] + " is " + precipProbability[i];

            }
            ArrayList msg = new ArrayList();
            msg.add(imgid);
            msg.add(summery);
            msg.add(discription);

            Message message = new Message();
            message.obj = msg;
            handler.sendMessage(message);
        }catch (JSONException e){
            System.out.println(e.getStackTrace().toString());
        }





    }
}
