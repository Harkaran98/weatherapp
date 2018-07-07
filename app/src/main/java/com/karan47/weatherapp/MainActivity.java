package com.karan47.weatherapp;

import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Constraints;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uber.sdk.android.core.UberSdk;
import com.uber.sdk.android.rides.RideParameters;
import com.uber.sdk.android.rides.RideRequestButton;
import com.uber.sdk.android.rides.RideRequestButtonCallback;
import com.uber.sdk.core.auth.Scope;
import com.uber.sdk.rides.client.ServerTokenSession;
import com.uber.sdk.rides.client.SessionConfiguration;
import com.uber.sdk.rides.client.error.ApiError;


import org.apache.log4j.chainsaw.Main;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    StringBuffer res = new StringBuffer();
    weatherclass b;
    fetch f;
    Button btn;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        f = new fetch();
        f.execute();
        btn = findViewById(R.id.btnid);
        tv = findViewById(R.id.tvid);
        SessionConfiguration config = new SessionConfiguration.Builder()
                .setClientId("9sx28J8l9nPA5mmaN2GXcoFFMa7nBBO2")
                .setServerToken("tfbGT2vFS1gMAPJwKrhz-tJaxZp1AxlE-Ms3tKYJ")
               // .setRedirectUri()
                .setScopes(Arrays.asList(Scope.RIDE_WIDGETS))
                .setEnvironment(SessionConfiguration.Environment.PRODUCTION).build();
        UberSdk.initialize(config);
        RideRequestButton requestButton=new RideRequestButton(MainActivity.this);
        ConstraintLayout layout =(ConstraintLayout)findViewById(R.id.mainact);
        layout.addView(requestButton);


        RideParameters rideParams = new RideParameters.Builder()
                // Optional product_id from /v1/products endpoint (e.g. UberX). If not provided, most cost-efficient product will be used
             //   .setProductId("a1111c8c-c720-46c3-8534-2fcdd730040d")
                // Required for price estimates; lat (Double), lng (Double), nickname (String), formatted address (String) of dropoff location
                .setDropoffLocation(
                        30.902866, 75.8201693, "Destination", "")
                // Required for pickup estimates; lat (Double), lng (Double), nickname (String), formatted address (String) of pickup location
                .setPickupLocation(30.9141552, 75.8106216, "Pickup", "")
                .build();
// set parameters for the RideRequestButton instance
        requestButton.setRideParameters(rideParams);

        ServerTokenSession session = new ServerTokenSession(config);
        requestButton.setSession(session);
        requestButton.loadRideInformation();


    }

    class fetch extends AsyncTask implements View.OnClickListener{

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                URL u = new URL("http://api.openweathermap.org/data/2.5/weather?lat=30.9141552&lon=75.8106216&APPID=1a026c69a68436344c5e8e04e3a19347");

                URLConnection uc = u.openConnection();

                InputStream stm = uc.getInputStream();

                InputStreamReader r = new InputStreamReader(stm);
                BufferedReader b = new BufferedReader(r);

                String line = "";
                while ((line = b.readLine()) != null) {
                    res.append(line + "\n");

                }


            } catch (Exception e) {


                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
           // Toast.makeText(MainActivity.this, "Response" + res.toString(), Toast.LENGTH_SHORT).show();
            btn.setOnClickListener(this);
            try {
                JSONObject jo = new JSONObject(res.toString());
                JSONArray ja = jo.getJSONArray("weather");
                for (int i = 0; i < ja.length(); i++) {

                    JSONObject jobj = ja.getJSONObject(i);

                    b = new weatherclass();
                    b.description = jobj.getString("description");
                    b.main = jobj.getString("main");
                }
                Toast.makeText(MainActivity.this, b.toString(), Toast.LENGTH_SHORT).show();

            } catch (Exception e) {

                e.printStackTrace();
            }


        }

        @Override
        public void onClick(View view) {
            if(view.getId()==R.id.btnid)
            {

             //  b.main="Rain";
              // b.description="Heavy Rain";
                tv.setText(b.toString());
                if((b.main).equals("Rain") || (b.main).equals("Drizzle")) {
                    tv.append("\nExtra charges are applied because of Rain.\nSorry for inconvenience ");
                }

            }
        }
    }
}




