package com.jp.band.com.smartkube.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


import com.jp.band.com.smartkube.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;



public class MainActivity extends AppCompatActivity {
    String link1 = "http://puppygifs.tumblr.com/api/read/json";
   //puniths String link = "http://192.168.0.101/test.php";

    //My Ip i guess
   String link = "http://192.168.1.126/TEST/getdata.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new SigninActivity().execute();
    }


    public class SigninActivity extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
           // TextView o = (TextView)findViewById(R.id.textViewResponse);
           // o.setText(s);
        }

        @Override
        protected String doInBackground(String... strings) {

            URL url = null;
            try {
                url = new URL(link);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                conn.setRequestMethod("GET");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }

// read the response
            try {
                System.out.println("Response Code: " + conn.getResponseCode());
            } catch (IOException e) {
                e.printStackTrace();
            }
            InputStream in = null;
            try {
                in = new BufferedInputStream(conn.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            String response = null;

            try {
                response = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.d("response Json" , response);


            JSONObject job = null;
            try {

                job = new JSONObject(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }String completeInfoWithDelimters = null;
            String dateTime = null,message = null, company = null;
            try {
                dateTime = job.getString("dateTime");
                message = job.getString("message");
                company = job.getString("company");
                completeInfoWithDelimters = dateTime+ ":" + message + ":" + company;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("response Json" , dateTime + "LOL" + message + " " + company);


            return completeInfoWithDelimters;
        }
    }

}