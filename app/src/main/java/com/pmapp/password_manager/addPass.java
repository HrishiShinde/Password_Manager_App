package com.pmapp.password_manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

public class addPass extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    TextInputLayout ipName, ipPass;
    TextView genPass;
    Button addPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pass);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigation_view);
        ipName = findViewById(R.id.Name);
        ipPass = findViewById(R.id.Password);
        genPass=findViewById(R.id.genPass);
        addPass=findViewById(R.id.addPassBtn);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.start, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);
        
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
                Toast.makeText(this, "This is addPass Activty!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.icProfile:
                Toast.makeText(this, "Redirecting to Profile Activty!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), profile.class));
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




















