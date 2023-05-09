package com.mobile.akumina.sample.test.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.mobile.akumina.sample.test.MainActivity;
import com.mobile.akumina.sample.test.R;

public class ErrorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error2);
        initView();
    }

    private void initView() {
        findViewById(R.id.homeButton).setOnClickListener(view -> goHome());
        String exception = getIntent().getStringExtra("exception");
        TextView textView = findViewById(R.id.errorTxt);
        textView.setText(exception);
    }
    private void goHome() {
        Intent intent =  new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}