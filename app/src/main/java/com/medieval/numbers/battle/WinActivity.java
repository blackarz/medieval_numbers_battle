package com.medieval.numbers.battle;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class WinActivity extends AppCompatActivity {

    private TextView winMessage;
    private Button restartButton;
    private ImageView nextLevelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        winMessage = findViewById(R.id.winMessage);
        nextLevelButton = findViewById(R.id.nextLevelButton);
        restartButton = findViewById(R.id.restartButton);

        int level = getIntent().getIntExtra("level", 1);
        winMessage.setText("Congratulations! You completed level " + level);

        nextLevelButton.setOnClickListener(v -> {
            Intent intent = new Intent(WinActivity.this, MainActivity.class);
            intent.putExtra("level", level + 1);
            startActivity(intent);
            finish();
        });

        restartButton.setOnClickListener(v -> {
            Intent intent = new Intent(WinActivity.this, LevelActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
