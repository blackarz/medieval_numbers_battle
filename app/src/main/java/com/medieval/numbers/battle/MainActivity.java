package com.medieval.numbers.battle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    ImageView SettingsBtn, BackBtn;
    private TextView levelTextView;
    private int currentLevel;

    private SoundPool soundPool;
    private int clickSound;
    private boolean isSoundEffectsOn;

    private MediaPlayer backgroundMusic;
    private boolean isMusicOn;

    private GridLayout gridLayout;
    private ArrayList<Integer> numbers;
    private TextView timerTextView;
    private Button[][] buttons = new Button[4][4];
    private int emptyRow = 3, emptyCol = 3;
    private int level = 1;
    private CountDownTimer countDownTimer;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SettingsBtn = findViewById(R.id.SettingsBtn);
        BackBtn = findViewById(R.id.BackBtn);
        gridLayout = findViewById(R.id.gridLayout);
        timerTextView = findViewById(R.id.timerTextView);

        // Load settings from SharedPreferences
        preferences = getSharedPreferences("settings", MODE_PRIVATE);
        isMusicOn = preferences.getBoolean("musicOn", true);
        isSoundEffectsOn = preferences.getBoolean("soundEffectsOn", true);

        // Reference the TextView from the layout
        levelTextView = findViewById(R.id.levelTextView);

        // Get the level from the intent
        Intent intent = getIntent();
        currentLevel = intent.getIntExtra("level", 1);  // Default to 1 if no level is passed
        updateLevelText();

        // Buttons Settings and Back
        SettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Initialize background music and sound effects
        backgroundMusic = MediaPlayer.create(this, R.raw.background_music);
        backgroundMusic.setLooping(true);  // Loop the music
        if (isMusicOn) {
            backgroundMusic.start();  // Start music
        }

        soundPool = new SoundPool.Builder().setMaxStreams(1).build();
        clickSound = soundPool.load(this, R.raw.click_sound, 1);

        // Register broadcast receivers
        registerReceiver(musicPrefReceiver, new IntentFilter("UPDATE_MUSIC_PREF"));
        registerReceiver(soundEffectsPrefReceiver, new IntentFilter("UPDATE_SOUND_EFFECTS_PREF"));

        // Receive level from LevelActivity
        level = getIntent().getIntExtra("level", 1);

        setupGrid();
        startTimer();
    }

    private void setupGrid() {
        numbers = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            numbers.add(i);
        }
        numbers.add(0); // Represent the empty space with 0

        Collections.shuffle(numbers);  // Shuffle the numbers to create a random puzzle

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int num = numbers.get(i * 4 + j);
                Button btn = new Button(this);

                if (num != 0) {
                    btn.setText(String.valueOf(num));
                    setButtonBackground(btn, num);
                    btn.setTextColor(Color.WHITE);
                } else {
                    emptyRow = i;
                    emptyCol = j;
                    btn.setText("");
                    btn.setVisibility(View.INVISIBLE);
                    setEmptyButtonBackground(btn);
                }

                final int row = i;
                final int col = j;

                btn.setOnClickListener(v -> {
                    if (isSoundEffectsOn) {
                        soundPool.play(clickSound, 1, 1, 0, 0, 1);
                    }
                    moveTile(row, col);
                });

                gridLayout.addView(btn, i * 4 + j);
                buttons[i][j] = btn;
            }
        }
    }

    private void setButtonBackground(Button button, int number) {
        int resId = getResources().getIdentifier("bg_" + number, "drawable", getPackageName());
        if (resId != 0) {
            button.setBackgroundResource(resId);
        } else {
            button.setBackgroundResource(R.drawable.default_background);
        }
    }

    private void setEmptyButtonBackground(Button button) {
        button.setBackgroundResource(R.drawable.bg_empty);
    }

    private void moveTile(int row, int col) {
        if (Math.abs(row - emptyRow) + Math.abs(col - emptyCol) == 1) {
            String clickedText = buttons[row][col].getText().toString();

            if (clickedText.isEmpty()) {
                return;
            }

            buttons[emptyRow][emptyCol].setText(clickedText);
            buttons[emptyRow][emptyCol].setVisibility(View.VISIBLE);
            buttons[emptyRow][emptyCol].setTextColor(Color.WHITE);
            setButtonBackground(buttons[emptyRow][emptyCol], Integer.parseInt(clickedText));

            buttons[row][col].setText("");
            buttons[row][col].setVisibility(View.INVISIBLE);
            setEmptyButtonBackground(buttons[row][col]);

            emptyRow = row;
            emptyCol = col;

            if (isSolved()) {
                winLevel();
            }
        }
    }

    private boolean isSolved() {
        int correctNumber = 1;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == 3 && j == 3) {
                    continue;
                }

                String buttonText = buttons[i][j].getText().toString();

                if (buttonText.isEmpty()) {
                    return false;
                }

                int number = Integer.parseInt(buttonText);
                if (number != correctNumber) {
                    return false;
                }
                correctNumber++;
            }
        }
        return true;
    }

    private void winLevel() {
        countDownTimer.cancel();
        Toast.makeText(this, "Level " + level + " Complete!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, WinActivity.class);
        intent.putExtra("level", level);
        startActivity(intent);
        finish();
    }

    private void startTimer() {
        int timeLimit = 600000; // 10 minutes for each level
        timerTextView.setText("10:00");
        countDownTimer = new CountDownTimer(timeLimit, 1000) {
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                timerTextView.setText(String.format("%02d:%02d", seconds / 60, seconds % 60));
            }

            public void onFinish() {
                Toast.makeText(MainActivity.this, "Time's up! Try again.", Toast.LENGTH_SHORT).show();
                recreate();
            }
        }.start();
    }

    // BroadcastReceiver to update music preferences dynamically
    private BroadcastReceiver musicPrefReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("UPDATE_MUSIC_PREF")) {
                isMusicOn = preferences.getBoolean("musicOn", true);
                if (isMusicOn) {
                    backgroundMusic.start();
                } else {
                    backgroundMusic.pause();
                }
            }
        }
    };

    // BroadcastReceiver to update sound effects preferences dynamically
    private BroadcastReceiver soundEffectsPrefReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("UPDATE_SOUND_EFFECTS_PREF")) {
                isSoundEffectsOn = preferences.getBoolean("soundEffectsOn", true);
                if (isSoundEffectsOn) {
                    soundPool.autoResume();
                } else {
                    soundPool.autoPause();
                }
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        if (backgroundMusic != null && backgroundMusic.isPlaying()) {
            backgroundMusic.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isMusicOn && backgroundMusic != null && !backgroundMusic.isPlaying()) {
            backgroundMusic.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        if (backgroundMusic != null) {
            backgroundMusic.release();  // Release media player resources
        }

        if (soundPool != null) {
            soundPool.release();  // Release SoundPool resources
        }

        unregisterReceiver(musicPrefReceiver);
        unregisterReceiver(soundEffectsPrefReceiver);
    }

    private void updateLevelText() {
        levelTextView.setText("Level: " + currentLevel);
    }
}
