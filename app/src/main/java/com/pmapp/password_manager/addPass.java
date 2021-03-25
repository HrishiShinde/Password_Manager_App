package com.pmapp.password_manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class addPass extends AppCompatActivity {

    TextInputLayout ipName, ipPass;
    TextView genPass;
    Button addPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pass);

        ipName = findViewById(R.id.Name);
        ipPass = findViewById(R.id.Password);
        genPass=findViewById(R.id.genPass);
        addPass=findViewById(R.id.addPassBtn);
        
        genPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String genedPass = secret.genpass(8);
                //ipPass.setText(genedPass);
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("genedPass", genedPass);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(addPass.this, "Password Copied to Clipboard!", Toast.LENGTH_SHORT).show();
            }
        });
        
        addPass.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}




















