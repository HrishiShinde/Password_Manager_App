package com.pmapp.password_manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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
    public String unameFromMA, passFromDb;

    TextView passCountTV;
    TextInputEditText ipUsername, ipEmail, ipName;
    Button ipUpdate, ipChang;
    FirebaseDatabase fDatabase;
    DatabaseReference dbRef, passRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigation_view);
        passCountTV = findViewById(R.id.passCount);
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
        unameFromMA = intent.getStringExtra("uname");
        Log.i("asd", "onCreate: "+unameFromMA);

        passRef = fDatabase.getReference("passwords").child(unameFromMA);
        Query passQuery = passRef.orderByChild("username");
        passQuery.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int passCount = (int) snapshot.child("").getChildrenCount();
                    String lol = snapshot.toString();
                    Log.i("testpassc", "onDataChange: pass= "+passCount+"------"+lol+"  "+unameFromMA);
                    passCountTV.setText(Integer.toString(passCount));
                }
                else {
                    Log.i("testpassc", "onDataChange: pass = loss");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Query checkUser = dbRef.orderByChild("username").equalTo(unameFromMA);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.i("Lol", "onDataChange: exists bypassed!");
                    ipName.setText(snapshot.child(unameFromMA).child("name").getValue(String.class));
                    ipEmail.setText(snapshot.child(unameFromMA).child("email").getValue(String.class));
                    ipUsername.setText(snapshot.child(unameFromMA).child("username").getValue(String.class));
                    passFromDb = snapshot.child(unameFromMA).child("password").getValue(String.class);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        //ipUpdate.setId(0);
        ipUpdate.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                //ipUsername.setText("hasherOP");
                int idx = ipUpdate.getId();
                if(ipUpdate.getId() != 1){
                    ipUsername.setEnabled(true);
                    ipName.setEnabled(true);
                    ipEmail.setEnabled(true);
                    Log.i("prayaas", "onClick: Prayaas going good! -- "+ipUpdate.getId());
                    ipUpdate.setId(1);
                }
                else if(ipUpdate.getId() == 1){
                    String uname = ipUsername.getText().toString().trim();
                    String name = ipName.getText().toString().trim();
                    String email = ipEmail.getText().toString().trim();

                    if(uname.isEmpty()){
                        ipUsername.setError("Field cannot be empty!");
                        ipUsername.requestFocus();
                    }
                    if(name.isEmpty()){
                        ipName.setError("Field cannot be empty!");
                        ipName.requestFocus();
                    }
                    if(email.isEmpty()){
                        ipEmail.setError("Field cannot be empty!");
                        ipEmail.requestFocus();
                    }

                    UserHelper uh = new UserHelper(name, uname, email, passFromDb);
                    dbRef.child(uname).setValue(uh);

                    Toast.makeText(profile.this, "Profile Updated!", Toast.LENGTH_SHORT).show();
                    Log.i("prayaas", "onClick:  Prayaas successful! -- "+ipUpdate.getId()+uname+name+email+"    "+passFromDb);
                    ipUsername.setEnabled(false);
                    ipName.setEnabled(false);
                    ipEmail.setEnabled(false);
                    ipUpdate.setId(2131296704);
                }
            }
        });
        ipChang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), changePass.class);
                intent.putExtra("name",unameFromMA);
                intent.putExtra("type","user");
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
                Intent intent = new Intent(getApplicationContext(), addPass.class);
                Query checkUser = dbRef.orderByChild("username").equalTo(unameFromMA);
                Log.i("testpass", "(Main)onClick: before event listener");
                checkUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.i("testpass", "(Main)onDataChange: before if");
                        if (snapshot.exists()) {
                            Log.i("testpass", "(Main)onDataChange: exists bypassed!");
                            String passFromDb = snapshot.child(unameFromMA).child("password").getValue(String.class);
                            Log.i("testpass", "(Main)onClick:-------- pass= "+passFromDb);
                            intent.putExtra("uname", unameFromMA);
                            intent.putExtra("password", passFromDb);
                            startActivity(intent);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.i("canac", "onCancelled: " + error.toString());
                    }
                });
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