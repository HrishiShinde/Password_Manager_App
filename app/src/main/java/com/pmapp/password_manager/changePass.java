package com.pmapp.password_manager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class changePass extends AppCompatActivity {

    TextInputLayout ipPass1, ipPass2;
    TextView genPass;
    Button changButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        Intent intent = getIntent();
        String unameFromPA = intent.getStringExtra("name");
        ipPass1 = findViewById(R.id.Pass1);
        ipPass2 = findViewById(R.id.Pass2);
        genPass=findViewById(R.id.genPass);
        changButton=findViewById(R.id.changBTN);

    }
}