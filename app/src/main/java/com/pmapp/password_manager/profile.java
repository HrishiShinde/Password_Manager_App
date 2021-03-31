package com.pmapp.password_manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class profile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    TextInputEditText ipUsername, ipEmail, ipPassword, ipName;
    Button ipUpdate, ipChang;
    FirebaseDatabase fDatabase;
    DatabaseReference dbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigation_view);
        ipName = findViewById(R.id.name);
        ipEmail = findViewById(R.id.email);
        ipUsername = findViewById(R.id.username);
        //ipPassword = findViewById(R.id.password);
        ipUpdate = findViewById(R.id.update);
        ipChang = findViewById(R.id.changPass);
        fDatabase = FirebaseDatabase.getInstance();
        dbRef = fDatabase.getReference("users");

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.start, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        String nameFromMA = intent.getStringExtra("name");
        String unameFromMA = intent.getStringExtra("uname");
        Log.i("asd", "onCreate: "+unameFromMA);

        Query checkUser = dbRef.orderByChild("username").equalTo(unameFromMA);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.i("Lol", "onDataChange: exists bypassed!");
                    ipName.setText(snapshot.child(unameFromMA).child("name").getValue(String.class));
                    ipEmail.setText(snapshot.child(unameFromMA).child("email").getValue(String.class));
                    ipUsername.setText(snapshot.child(unameFromMA).child("username").getValue(String.class));
                    //ipPassword.setText(snapshot.child("password").getValue(String.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        ipUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ipUsername.setText("hasherOP");
                ipUsername.setEnabled(false);
                ipName.setEnabled(false);
                ipEmail.setEnabled(false);

                String uname = ipUsername.getText().toString().trim();
                String name = ipName.getText().toString().trim();
                String email = ipEmail.getText().toString().trim();
            }
        });
        ipChang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), changePass.class);
                intent.putExtra("name",unameFromMA);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        toggle.onOptionsItemSelected(item);
        return true;
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.icHome:
                Toast.makeText(this, "Redirecting to Home Activty!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                break;
            case R.id.icAdd:
                Toast.makeText(this, "Redirecting to addPass Activty!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), addPass.class));
                break;
            case R.id.icProfile:
                Toast.makeText(this, "This is Profile Activty!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.icLogout:
                Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
                break;
        }
        return true;
    }
}