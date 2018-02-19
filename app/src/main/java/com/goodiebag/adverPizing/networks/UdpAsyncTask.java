package com.goodiebag.adverPizing.networks;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.goodiebag.adverPizing.AppController;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Kai on 18/02/18.
 */

public class UdpAsyncTask extends AsyncTask<String, Void, String> {
    private static final String TAG = UdpAsyncTask.class.getSimpleName();

    private static String BROADCAST_IP = "255.255.255.255";
    private static int UDP_SEND_PORT = 9000;
    private static int UDP_RECEIVE_PORT = 12345;

    private UdpAsyncTaskInteraction mListener = null;

    @Override
    protected String doInBackground(String... strings) {
        Log.d(TAG, "Call for ip");
        String ip = udpSendAndReceive();
        Log.d(TAG, "Got ip : " + ip);
        return ip;
    }

    @Override
    protected void onPostExecute(String ip) {
        super.onPostExecute(ip);
        Log.d(TAG, "onPostExecute : " + ip);
        if (ip.isEmpty()) {
            if (mListener != null)
                mListener.onIpNull();
        } else {
            SharedPreferences.Editor editor = AppController.getAppContext().getSharedPreferences("PREF", MODE_PRIVATE).edit();
            editor.putString("ip", ip);
            editor.apply();
            if (mListener != null)
                mListener.onIpObtained(ip);
        }
    }

    private static String udpSendAndReceive() {
        String ipFromPi = "";
        String Msg = "Hello Pi! Whats your IP?";
        DatagramSocket tx = null, rx = null;
        try {
            //Broadcast message
            tx = new DatagramSocket();
            tx.setBroadcast(true);
            DatagramPacket dp;
            InetAddress broadcastIp = InetAddress.getByName(BROADCAST_IP);
            dp = new DatagramPacket(Msg.getBytes(), Msg.length(), broadcastIp, UDP_SEND_PORT);
            tx.send(dp);

            //Receive from PiZing
            byte[] messageFromServer = new byte[1024];
            rx = new DatagramSocket(UDP_RECEIVE_PORT);
            rx.setSoTimeout(10000);
            DatagramPacket packet;
            packet = new DatagramPacket(messageFromServer, messageFromServer.length);
            rx.receive(packet);
            ipFromPi = new String(messageFromServer, 0, packet.getLength());
            Log.d("Received text", ipFromPi);
            //piIpAddress.setText(ipFromPi);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (tx != null) {
                tx.close();
            }
            if (rx != null) {
                rx.close();
            }
        }
        return ipFromPi;
    }

    public void setListener(UdpAsyncTaskInteraction mListener) {
        this.mListener = mListener;
    }

    public interface UdpAsyncTaskInteraction {
        void onIpObtained(String ip);

        void onIpNull();
    }

}
