package com.goodiebag.adverPizing.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.Log;
import com.goodiebag.adverPizing.R;
import com.goodiebag.adverPizing.activity.MainVolleyActivity;
import com.goodiebag.adverPizing.networks.rest.AdverPizingRetroServer;
import com.goodiebag.adverPizing.networks.rest.AdverPizingService;
import com.goodiebag.adverPizing.networks.rest.models.NoticeBoardRespnose;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * Created by kai on 6/4/16.
 */
public class NotificationService extends Service {
    public static final String REQUEST_TAG = "NotificationTag";
    public static final String SERVICE_TAG = "Notificati0nService";
    MediaPlayer mp;


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(SERVICE_TAG, "Destroyed");
        //Toast.makeText(getApplicationContext(), "Destroyed", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(SERVICE_TAG, "Kappa");
        mp = MediaPlayer.create(this, R.raw.tweeters);
        setUpRequest();
        //Toast.makeText(getApplicationContext(), "creaated", Toast.LENGTH_LONG).show();

    }

    private void setUpRequest() {
        SharedPreferences prefs = getSharedPreferences("PREF", MODE_PRIVATE);
        String ip = prefs.getString("ip", null);
        Retrofit retroServer = AdverPizingRetroServer.getRetroServer(ip);
        AdverPizingService service = retroServer.create(AdverPizingService.class);

        Call<List<NoticeBoardRespnose>> responses = service.listNotices();
        responses.enqueue(new Callback<List<NoticeBoardRespnose>>() {
            @Override
            public void onResponse(Call<List<NoticeBoardRespnose>> call, retrofit2.Response<List<NoticeBoardRespnose>> response) {
                Log.d("onResponse : ", response.toString());
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 500 milliseconds
                v.vibrate(500);
                popUpNotification();
            }

            @Override
            public void onFailure(Call<List<NoticeBoardRespnose>> call, Throwable t) {
                Log.d("onFailure : ", "Failure");
            }
        });
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

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.notification_bell : R.mipmap.school_bell;
    }
}
