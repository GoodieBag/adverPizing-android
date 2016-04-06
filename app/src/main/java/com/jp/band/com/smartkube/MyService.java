package com.jp.band.com.smartkube;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kai on 6/4/16.
 */
public class MyService extends Service implements Response.Listener, Response.ErrorListener  {
    public static final String REQUEST_TAG = "MyService";
    private RequestQueue mQueue;
    String companyGlobal  =null;


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Service", "Destroyed");
        Toast.makeText(getApplicationContext() , "Destroyed" , Toast.LENGTH_LONG).show();
        if (mQueue != null) {
            mQueue.cancelAll(REQUEST_TAG);
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
       Log.d("Service","Kappa");
        Toast.makeText(getApplicationContext() , "creaated" , Toast.LENGTH_LONG).show();
        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();
        String url = "http://192.168.1.126/TEST/getdata.php";
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method
                .GET, url,
                new JSONObject(), this, this);
        jsonRequest.setTag(REQUEST_TAG);
        mQueue.add(jsonRequest);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onResponse(Object response) {

        JSONObject job = null;
        try {

            job = new JSONObject(response.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String dateTime = null,message = null, company = null;
        try {
            dateTime = job.getString("dateTime");
            message = job.getString("message");
            company = job.getString("company");
            companyGlobal = company;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("response Json" , dateTime + "LOL" + message + " " + company);


        popUpNotification();


    }

    private void popUpNotification() {
        Notification mNotification;
        Intent resultIntent = new Intent(this, MainActivity.class);

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
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentTitle(companyGlobal)
                .setContentText("AMAZING SUPER MEGA OFFERS HURRY !!")
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
