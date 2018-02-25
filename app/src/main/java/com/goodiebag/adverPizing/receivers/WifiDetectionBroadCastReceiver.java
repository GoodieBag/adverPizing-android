package com.goodiebag.adverPizing.receivers;

/**
 * Created by kai on 5/4/16.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.goodiebag.adverPizing.networks.UdpAsyncTask;
import com.goodiebag.adverPizing.service.NotificationService;

public class WifiDetectionBroadCastReceiver extends BroadcastReceiver {
    private static final String TAG = WifiDetectionBroadCastReceiver.class.getSimpleName();
    @Override
    public void onReceive(final Context context, Intent intent) {
        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if (info != null && info.isConnected()) {
            // Do your work.
            Log.d(TAG, "Wifi Toggle : Connected");
            //Throw an ip search async
            UdpAsyncTask udpAsyncTask = new UdpAsyncTask();
            udpAsyncTask.setListener(new UdpAsyncTask.UdpAsyncTaskInteraction() {
                @Override
                public void onIpObtained(String ip) {
                    Log.d(TAG, "Ip is : " + ip);
                    Intent i = new Intent(context, NotificationService.class);
                    context.startService(i);
                }

                @Override
                public void onIpNull() {
                    Log.d(TAG, "Ip is null");
                }
            });
            udpAsyncTask.execute("");

            // e.g. To check the Network Name or other info:
            /*WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String ssid = wifiInfo.getSSID();*/

        } else {
            Log.d(TAG, "Wifi Toggle : Disconnected");
            Intent i = new Intent(context, NotificationService.class);
            context.stopService(i);
        }

    }

}
