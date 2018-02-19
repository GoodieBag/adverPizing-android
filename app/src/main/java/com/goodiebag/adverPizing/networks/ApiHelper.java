package com.goodiebag.adverPizing.networks;

/**
 * Created by Kai on 19/02/18.
 */

public class ApiHelper {
    private static final String URL_PREFIX = "http://";
    private static final String PORT = ":3000/";

    public static String buildURL(String ip, String... pathArray){
        StringBuilder sbFinalPath = new StringBuilder();
        for(String path : pathArray){
            sbFinalPath.append(path);
        }
        return URL_PREFIX + ip +PORT + sbFinalPath.toString();
    }
}
