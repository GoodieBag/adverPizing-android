package com.jp.band.com.smartkube.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jp.band.com.smartkube.R;
import com.jp.band.com.smartkube.activity.MainVolleyActivity;
import com.jp.band.com.smartkube.networks.CustomJSONObjectRequest;
import com.jp.band.com.smartkube.networks.CustomVolleyRequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kai on 6/4/16.
 */
public class NotificationService extends Service implements Response.Listener, Response.ErrorListener  {
    public static final String REQUEST_TAG = "NotificationTag";
    public static final String SERVICE_TAG = "Notificati0nService";
    private RequestQueue mQueue;
    String companyGlobal  =null;
    MediaPlayer mp;


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(SERVICE_TAG, "Destroyed");
        Toast.makeText(getApplicationContext() , "Destroyed" , Toast.LENGTH_LONG).show();
        if (mQueue != null) {
            mQueue.cancelAll(REQUEST_TAG);
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
       Log.d(SERVICE_TAG,"Kappa");
       mp = MediaPlayer.create(this, R.raw.tweeters);
        setUpRequest();
        Toast.makeText(getApplicationContext() , "creaated" , Toast.LENGTH_LONG).show();

    }

    private void setUpRequest() {
        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();
        String url = getString(R.string.pi_ip)+"/TEST/getdata.php";
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method
                .GET, url,
                new JSONObject(), this, this);
        jsonRequest.setTag(REQUEST_TAG);
        mQueue.add(jsonRequest);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onResponse(Object response) {


        //Json Parsing
        JSONObject job = null;
        try {

            job = new JSONObject(response.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String dateTime = null,message = null, company = null;
        try {

            company = job.getString("college");
            companyGlobal = company;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("response Json" , dateTime + "LOL" + message + " " + company);

        //Show Notification
        popUpNotification();


    }



    private void popUpNotification() {
        mp.start();
        Notification mNotification;
        Intent resultIntent = new Intent(this, MainVolleyActivity.class);

        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.notify)
                .setAutoCancel(true)
                .setContentTitle(companyGlobal)
                .setContentText("Alert! Important announcements from college!")
                .setContentIntent(resultPendingIntent);

        //Get current notification
        mNotification = builder.getNotification();

        //Show the notification
        mNotificationManager.notify(1, mNotification);

    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }
}