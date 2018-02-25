package com.goodiebag.adverPizing.receivers;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.goodiebag.adverPizing.AppController;
import com.goodiebag.adverPizing.utils.Constants;
import com.goodiebag.adverPizing.utils.NetworkUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by basavarajsarwad on 17/04/17.
 */

public class ReceiveService extends Service {

    private final String TAG = getClass().getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
//        new ReceiveTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new Thread(new Runnable() {
            @Override
            public void run() {
                receive();
            }
        }).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        return START_STICKY; // Tells the OS to recreate the service after it has enough memory and call onStartCommand() again with a null intent
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    /**
     * Socket connections cannot be managed on UI thread so this class is used
     * to do the task in a background thread
     */
//    private static class ReceiveTask extends AsyncTask<Void, Void, Void> {
//        private final String TAG = getClass().getSimpleName();
//        private WeakReference<ReceiveService> mWeakReference;
//
//        ReceiveTask(ReceiveService receiveService) {
//            Log.i(TAG, "ReceiveTask");
//            mWeakReference = new WeakReference<>(receiveService);
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            ReceiveService receiveService = mWeakReference.get();
//            if (receiveService != null) {
//                receiveService.receive();
//            }
//            return null;
//        }
//    }
    private void receive() {
        Log.i(TAG, "receive");
        WifiManager wifi = (WifiManager) AppController.getAppContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //WifiInfo info = wifi.getConnectionInfo();

        if (wifi != null) {

            WifiManager.MulticastLock lock = wifi.createMulticastLock("The Lock");
            lock.acquire();
            Log.i(TAG, "locked");
            MulticastSocket socket = null;

            InetAddress group = null;
            try {
                int port = 1111;
                socket = new MulticastSocket(port);
                group = InetAddress.getByName("239.255.255.250");
                NetworkInterface ni = NetworkUtils.findWifiNetworkInterface();
                if (ni != null) {
                    //Log.d(TAG,"WIFI Name:"+ni.getName());
                    socket.setNetworkInterface(ni);
                }
                socket.joinGroup(new InetSocketAddress(group, port), ni);

                while (true) {
                    DatagramPacket packet = new DatagramPacket(new byte[100], 100);
                    socket.receive(packet);
                    String s = new String(packet.getData(), 0, packet.getLength());
                    Log.i(TAG, "response " + s);
                    String board, accessToken = null;
                    String responses[] = s.split("\r\n");

                    /** NOTIFY
                     CLOUDIO
                     MjA4OTU5MDgtYTA5Mi00NGNmLTkwMDEtNjM2NTZmMmM2MDcx
                     4.0.3r|1|1 */

                    if (responses.length >= 4) {
                        if (responses[0].equals("NOTIFY")) {
                            board = responses[1];
                            accessToken = responses[2];
                            String otherData = responses[3];
                            Log.i(TAG, "otherData : " + otherData);
                            //Log.i(TAG, "token : " + responses[2]);
                        }
                    }

                    String address = packet.getAddress().getHostAddress();
                    Log.i(TAG, "address : " + address);

                    List<String> addresses = new ArrayList<>();
                    addresses.add(address + ":" + accessToken);

                    Intent intent = new Intent();
                    intent.setAction(Constants.RECEIVE);
                    intent.putStringArrayListExtra(Constants.ADDRESSES, (ArrayList<String>) addresses);
                    LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
                    localBroadcastManager.sendBroadcast(intent);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (socket != null) {
                    try {
                        socket.leaveGroup(group);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    socket.close();
                }
            }
            lock.release();
        }
    }
}
