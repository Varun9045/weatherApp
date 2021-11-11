package com.varunkumar123.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.varunkumar123.weatherapp.model.Weather;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherActivity extends AppCompatActivity {
    TextView[] dots;
    LinearLayout dots_layout;
    String key = "124ea8729f7958dcf237de03bbac3e3f";
    String location = "delhi";
    TextView weatherText,city_Name,temprature;
    ImageView clear_Weather,not_Clear_Weather;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        dots_layout = findViewById(R.id.dots);
        weatherText = findViewById(R.id.weather_text);
        clear_Weather = findViewById(R.id.clear_weather);
        not_Clear_Weather = findViewById(R.id.not_clear_weather);
        temprature = findViewById(R.id.temp);
        addDots(2);
        extractWeather();
        extractTemp();
        city_Name = findViewById(R.id.city_Name);
        city_Name.setText(location+", IN");
    }

    private void extractTemp() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.openweathermap.org/data/2.5/weather?q="+location+"&appid="+key, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("weather");
                    for(int i = 0 ;i<array.length();i++){
                        JSONObject o = array.getJSONObject(i);
                        Weather item = new Weather(
                                o.getString("main"),
                                o.getString("description")
                        );
                        weatherText.setText(item.getMain());

                        if(item.getMain().equalsIgnoreCase("Clear")){
                            clear_Weather.setVisibility(View.VISIBLE);
                            not_Clear_Weather.setVisibility(View.GONE);
                        }else{
                            clear_Weather.setVisibility(View.GONE);
                            not_Clear_Weather.setVisibility(View.VISIBLE);
                        }
                    }
                }
                catch (Exception e){
                    Toast.makeText(WeatherActivity.this, "Error"+e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(WeatherActivity.this, "VolleyError"+error, Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void extractWeather() {
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://api.openweathermap.org/data/2.5/weather?q="+location+"&appid="+key, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONObject object = response.getJSONObject("main");
                    String Temprature = object.getString("temp");
                    Double temp = Double.parseDouble(Temprature)-273.15;
                    temprature.setText(temp.toString().substring(0,4)+"\u2103");

                }catch (Exception e){
                    Toast.makeText(WeatherActivity.this, "error"+e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(WeatherActivity.this, "Volley Error"+error, Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void addDots(int position) {
        dots = new TextView[5];
        dots_layout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(40);

            dots_layout.addView(dots[i]);
        }
        if (dots.length > 0) {
            dots[position].setTextColor(getResources().getColor(R.color.white));
        }
    }
}
