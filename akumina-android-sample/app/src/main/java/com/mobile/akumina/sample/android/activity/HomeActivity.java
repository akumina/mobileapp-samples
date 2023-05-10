package com.mobile.akumina.sample.android.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.mobile.akumina.sample.android.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button button = findViewById(R.id.profile);
        button.setOnClickListener( view -> showProfile());
    }

    private void showProfile() {
        Intent intent = new Intent(getApplicationContext(), ProfilesActivity.class);
        startActivity(intent);
    }
}