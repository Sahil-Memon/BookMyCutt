package com.example.bookmycutt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;

public class MainActivity extends AppCompatActivity {
    Button owner,user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        owner = findViewById(R.id.owner);
        user = findViewById(R.id.user);

        owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start Register activity
                startActivity(new Intent(MainActivity.this,Ologin.class));
            }
        });

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });
    }
}
