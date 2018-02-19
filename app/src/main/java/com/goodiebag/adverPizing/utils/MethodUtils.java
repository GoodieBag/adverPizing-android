package com.goodiebag.adverPizing.utils;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by Kai on 18/05/17.
 */

public class MethodUtils {

    static String BROADCAST_IP = "255.255.255.255";
    static int UDP_SEND_PORT = 9000;
    static int UDP_RECEIVE_PORT = 12345;

    public static String udpSendAndReceive()  {
        String ipFromPi="";
        int result = 0;
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
        } catch (SocketException e) {
            e.printStackTrace();
            result = -1;
        }catch (UnknownHostException e) {
            e.printStackTrace();
            result = -1;
        } catch (IOException e) {
            e.printStackTrace();
            result = -1;
        } catch (Exception e) {
            e.printStackTrace();
            result = -1;
        }finally {
            if (tx != null) {
                tx.close();
            }
            if (rx != null) {
                rx.close();
            }
        }

        return ipFromPi;
    }

}
