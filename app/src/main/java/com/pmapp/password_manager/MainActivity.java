package com.pmapp.password_manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    public String receivedName, receivedUname;

    Button logout;
    FirebaseAuth auth;
    TextView welmsg;
    FloatingActionButton addFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Intent intent = new Intent(this, Login.class);
        //startActivity(intent);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigation_view);
        welmsg = findViewById(R.id.welTv);
        logout = findViewById(R.id.Logout);
        addFab = findViewById(R.id.add_fab);
        auth = FirebaseAuth.getInstance();

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.start, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();

        receivedName = intent.getStringExtra("name");
        receivedUname = intent.getStringExtra("uname");
        Log.i("name", "name= " + receivedName);
        welmsg.setText("Hello, Welcome "+receivedName+"!");

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });

        addFab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), addPass.class));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        toggle.onOptionsItemSelected(item);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.icHome:
                Toast.makeText(this, "This is HomeScreen!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.icAdd:
                Toast.makeText(this, "redirecting to addPass Activty!", Toast.LENGTH_SHORT).show();
                Log.i("redirect", "onNavigationItemSelected: redirecting to addPass activity");
                startActivity(new Intent(getApplicationContext(), addPass.class));
                break;
            case R.id.icProfile:
                Toast.makeText(this, "redirecting to Profile Activty!", Toast.LENGTH_SHORT).show();
                Log.i("profile", "onNavigationItemSelected: Profile screen opening");
                Intent intent = new Intent(getApplicationContext(),profile.class);
                intent.putExtra("name",receivedName);
                intent.putExtra("uname",receivedUname);
                startActivity(intent);
                break;
            case R.id.icLogout:
                Toast.makeText(this, "Logout Activty!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
                break;
        }
        return true;
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