package com.example.michael.archerygame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button newGameButton = (Button) findViewById(R.id.newGameButton);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newGameIntent = new Intent(MainActivity.this, CreateGameActivity.class);
                startActivity(newGameIntent);
            }
        });

        Button loadGameButton = (Button) findViewById(R.id.loadGameButton);
        loadGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loadGameIntent = new Intent(MainActivity.this, LoadGameActivity.class);
                startActivity(loadGameIntent);
            }
        });
    }

}
