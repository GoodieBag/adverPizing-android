package com.goodiebag.adverPizing.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.goodiebag.adverPizing.R;
import com.goodiebag.adverPizing.utils.SharedPreferenceHelper;

public class SettingsActivity extends AppCompatActivity {
    private CheckBox cbVibration , cbNotificationSound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        cbVibration = findViewById(R.id.cbVibration);
        cbNotificationSound = findViewById(R.id.cbNotificiationSound);

        cbVibration.setChecked(SharedPreferenceHelper.getBooleanSharedPreference(SharedPreferenceHelper.VIBRATIONS, true));
        cbNotificationSound.setChecked(SharedPreferenceHelper.getBooleanSharedPreference(SharedPreferenceHelper.NOTIFICATION_SOUND, true));

        cbVibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferenceHelper.saveBooleanSharedPreference(SharedPreferenceHelper.VIBRATIONS, isChecked);
            }
        });

        cbNotificationSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferenceHelper.saveBooleanSharedPreference(SharedPreferenceHelper.NOTIFICATION_SOUND, isChecked);
            }
        });
    }
}
