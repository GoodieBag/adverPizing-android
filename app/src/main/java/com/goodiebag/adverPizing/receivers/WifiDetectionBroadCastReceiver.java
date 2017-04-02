package com.goodiebag.adverPizing.receivers;

/**
 * Created by kai on 5/4/16.
 */

        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.net.NetworkInfo;
        import android.net.wifi.WifiInfo;
        import android.net.wifi.WifiManager;
        import android.util.Log;

        import com.android.volley.RequestQueue;
        import com.goodiebag.adverPizing.service.NotificationService;

public class WifiDetectionBroadCastReceiver  extends BroadcastReceiver {
    private RequestQueue mQueue;

    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if (info != null && info.isConnected()) {
            // Do your work.
            Log.d("hihi", "yea");
            String ssidJss = "JSSDigiBoard";
            Intent i = new Intent(context,NotificationService.class);
            context.startService(i);
            // e.g. To check the Network Name or other info:
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String ssid = wifiInfo.getSSID();

        } else {
            Log.d("hihi", "no");
            Intent i = new Intent(context,NotificationService.class);
            context.stopService(i);
        }

    }

}
