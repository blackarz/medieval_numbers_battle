package com.medieval.numbers.battle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class LevelActivity extends AppCompatActivity {

    private ImageView level1Button, level2Button, level3Button,level4Button,level5Button,BackBtn,level6Button,level7Button,level8Button,level9Button,level10Button,level11Button,level12Button,level13Button,level14Button,level15Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);


        BackBtn = findViewById(R.id.BackBtn);

        level1Button = findViewById(R.id.level1Button);
        level2Button = findViewById(R.id.level2Button);
        level3Button = findViewById(R.id.level3Button);
        level4Button = findViewById(R.id.level4Button);
        level5Button = findViewById(R.id.level5Button);
        level6Button = findViewById(R.id.level6Button);
        level7Button = findViewById(R.id.level7Button);
        level8Button = findViewById(R.id.level8Button);
        level9Button = findViewById(R.id.level9Button);
        level10Button = findViewById(R.id.level10Button);
        level11Button = findViewById(R.id.level11Button);
        level12Button = findViewById(R.id.level12Button);
        level13Button = findViewById(R.id.level13Button);
        level14Button = findViewById(R.id.level14Button);
        level15Button = findViewById(R.id.level15Button);




        level1Button.setOnClickListener(v -> startLevel(1));
        level2Button.setOnClickListener(v -> startLevel(2));
        level3Button.setOnClickListener(v -> startLevel(3));
        level4Button.setOnClickListener(v -> startLevel(4));
        level5Button.setOnClickListener(v -> startLevel(5));
        level6Button.setOnClickListener(v -> startLevel(6 ));
        level7Button.setOnClickListener(v -> startLevel(7));
        level8Button.setOnClickListener(v -> startLevel(8));
        level9Button.setOnClickListener(v -> startLevel(9));
        level10Button.setOnClickListener(v -> startLevel(10));
        level11Button.setOnClickListener(v -> startLevel(11));
        level12Button.setOnClickListener(v -> startLevel(12));
        level13Button.setOnClickListener(v -> startLevel(13));
        level14Button.setOnClickListener(v -> startLevel(14));
        level15Button.setOnClickListener(v -> startLevel(15));




        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void startLevel(int level) {
        Intent intent = new Intent(LevelActivity.this, MainActivity.class);
        intent.putExtra("level", level);
        startActivity(intent);
    }




}
