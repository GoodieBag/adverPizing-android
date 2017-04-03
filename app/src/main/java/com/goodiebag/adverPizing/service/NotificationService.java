package com.goodiebag.adverPizing.service;

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
import com.goodiebag.adverPizing.R;
import com.goodiebag.adverPizing.activity.MainVolleyActivity;
import com.goodiebag.adverPizing.networks.CustomJSONObjectRequest;
import com.goodiebag.adverPizing.networks.CustomVolleyRequestQueue;
import com.goodiebag.adverPizing.utils.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by kai on 6/4/16.
 */
public class NotificationService extends Service implements Response.Listener<JSONArray>, Response.ErrorListener  {
    public static final String REQUEST_TAG = "NotificationTag";
    public static final String SERVICE_TAG = "Notificati0nService";
    private RequestQueue mQueue;
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
        String url = Constants.IP+ Constants.noticeboards + Constants.firstTenNotices;
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
    public void onResponse(JSONArray response) {
        //Json Parsing
        Log.d("response Json" , response.toString());
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
                .setSmallIcon(getNotificationIcon())
                .setAutoCancel(true)
                .setContentTitle("Dept.of Ise")
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

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.notification_bell : R.mipmap.school_bell;
    }
}
