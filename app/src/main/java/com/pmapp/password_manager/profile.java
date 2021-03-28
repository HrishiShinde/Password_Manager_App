package com.pmapp.password_manager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;

public class profile extends AppCompatActivity {

    TextInputEditText ipUsername, ipEmail, ipPassword, ipName;
    EditText edtest;
    Button ipSet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ipName = findViewById(R.id.name);
        ipEmail = findViewById(R.id.email);
        ipUsername = findViewById(R.id.username);
        ipPassword = findViewById(R.id.password);
        ipSet = findViewById(R.id.setText);
        //edtest = findViewById(R.id.edTest);

        ipSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ipUsername.setText("hasherOP");
                ipPassword.setText("12312312");
                ipUsername.setEnabled(false);
                ipPassword.setEnabled(false);
            }
        });
    }
}