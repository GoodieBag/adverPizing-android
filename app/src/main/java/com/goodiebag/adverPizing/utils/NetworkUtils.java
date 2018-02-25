package com.goodiebag.adverPizing.utils;

import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kai on 25/02/18.
 */

public class NetworkUtils {

    public static NetworkInterface findWifiNetworkInterface() {

        Enumeration<NetworkInterface> enumeration;

        try {
            enumeration = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
            return null;
        }


        NetworkInterface iface;
        InetAddress address;

        Map<String, NetworkInterface> iFaceNames = new HashMap<>();
        while (enumeration.hasMoreElements()) {

            iface = enumeration.nextElement();
            try {
                if (iface.isLoopback()) {
                    continue;
                }
                if (iface.isVirtual()) {
                    continue;
                }
                if (!iface.isUp()) {
                    continue;
                }
                if (iface.isPointToPoint()) {
                    continue;
                }
                if (iface.getHardwareAddress() == null) {
                    continue;
                }
                for (Enumeration<InetAddress> enumIpAddr = iface.getInetAddresses();
                     enumIpAddr.hasMoreElements(); ) {
                    address = enumIpAddr.nextElement();
                    if (address.getAddress().length == 4) {
                        //return inetAddress.getHostAddress();
                        Log.d("MYNW", iface.getName() + " - " + address.getHostAddress());
                        iFaceNames.put(iface.getName(), iface);
                        //return iface;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        NetworkInterface wlan0 = null;
        String[] resp, tokens;
        for (String intf : iFaceNames.keySet()) {
            resp = ShellExecutor.exec("ifconfig " + intf).split("\n");
            for (String line : resp) {
                tokens = line.toUpperCase().replaceAll("[^a-zA-Z]", " ").replaceAll(" +", " ").trim().split(" ");
                if (Arrays.asList(tokens).contains("MULTICAST")) {
                    wlan0 = iFaceNames.get(intf);
                    break;
                }
            }
            if (wlan0 != null)
                break;
        }

        return wlan0;
    }

}
