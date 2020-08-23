package com.ag.multiplechoice;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Menu extends AppCompatActivity {
    private Button startQuizButton;
    private Button goToGuide;

    private final int REQUEST_CODE = 1;
    private TextView recentScoreView;
    private TextView topScoreView;

    private static final String MESSAGE_ID = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        startQuizButton = findViewById(R.id.startQuizButton);
        recentScoreView = findViewById(R.id.recent_score);
        topScoreView = findViewById(R.id.top_score);
        goToGuide = findViewById(R.id.go_to_guide);

        //go to MainActivity
        startQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Menu.this, MainActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        //go to user guide
        goToGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent guide = new Intent(Menu.this, userGuide.class);
                startActivityForResult(guide, REQUEST_CODE);
            }
        });

    }

    //to receive top score from MainActivity:
    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if(data != null){
                //get stuff sent from MainActivity if there's any
                String recent_score = data.getStringExtra("recent_score");
                recentScoreView.setText("New Score: " + recent_score);

                String top_score = data.getStringExtra("score");
                topScoreView.setText("Your top score: " + top_score);
            }
        }

    }
}



