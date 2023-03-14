package com.example.spacefighter;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.TextView;

public class HighScores extends AppCompatActivity {

    TextView t1,t2,t3,t4;
    SharedPreferences sharedPreferences;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_high_scores);

        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        t3 = findViewById(R.id.t3);
        t4 = findViewById(R.id.t4);

        sharedPreferences = getSharedPreferences("SPACE_FIGHTER", Context.MODE_PRIVATE);
        t1.setText("1."+sharedPreferences.getInt("score1",0));
        t2.setText("2."+sharedPreferences.getInt("score2",0));
        t3.setText("3."+sharedPreferences.getInt("score3",0));
        t4.setText("4."+sharedPreferences.getInt("score4",0));

    }
}