package com.goodiebag.adverPizing.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Kai on 25/02/18.
 */

public class ShellExecutor {

    private ShellExecutor() {

    }

    public static String exec(String command) {

        StringBuilder output = new StringBuilder();

        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return output.toString();

    }
}
