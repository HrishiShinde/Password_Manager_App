package com.pmapp.password_manager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button logout;
    FirebaseAuth auth;
    TextView welmsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Intent intent = new Intent(this, Login.class);
        //startActivity(intent);

        welmsg = findViewById(R.id.welTv);
        logout = findViewById(R.id.Logout);
        auth = FirebaseAuth.getInstance();

        Intent intent = getIntent();

        String receiver = intent.getStringExtra("name");
        Log.i("name", "name= " + receiver);
        welmsg.setText("Hello, Welcome "+receiver+"!");

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
    }
    /*
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser fbUser = auth.getCurrentUser();
        if(fbUser != null){

        }else{
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        }

    }*/
}