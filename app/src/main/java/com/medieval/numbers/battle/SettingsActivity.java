package com.medieval.numbers.battle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    ImageView BackBtn;

    private Switch musicSwitch;
    private Switch soundEffectsSwitch;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        BackBtn = findViewById(R.id.BackBtn);


        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        preferences = getSharedPreferences("settings", MODE_PRIVATE);

        // Initialize switches
        musicSwitch = findViewById(R.id.musicSwitch);
        soundEffectsSwitch = findViewById(R.id.soundEffectsSwitch);

        // Set initial switch states based on preferences
        musicSwitch.setChecked(preferences.getBoolean("musicOn", true));
        soundEffectsSwitch.setChecked(preferences.getBoolean("soundEffectsOn", true));

        // Music switch listener
        musicSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("musicOn", isChecked).apply();
            Intent intent = new Intent("UPDATE_MUSIC_PREF");
            sendBroadcast(intent);
        });

        // Sound effects switch listener
        soundEffectsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("soundEffectsOn", isChecked).apply();
            Intent intent = new Intent("UPDATE_SOUND_EFFECTS_PREF");
            sendBroadcast(intent);
        });
    }
}
