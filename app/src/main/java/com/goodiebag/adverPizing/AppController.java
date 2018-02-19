package com.goodiebag.adverPizing;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * Created by Kai on 18/02/18.
 */

public class AppController extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getAppContext() {
        return context;
    }
}
