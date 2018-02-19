package com.goodiebag.adverPizing.networks;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.goodiebag.adverPizing.AppController;
import com.goodiebag.adverPizing.utils.MethodUtils;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Kai on 18/02/18.
 */

public class UdpAsyncTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... strings) {
        Log.d("Time", "Call for ip");
        String ip =  MethodUtils.udpSendAndReceive();
        Log.d("Time", "Async" + ip);
        SharedPreferences.Editor editor = AppController.getAppContext().getSharedPreferences("PREF", MODE_PRIVATE).edit();
        editor.putString("ip", ip);
        editor.apply();

        return ip;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
