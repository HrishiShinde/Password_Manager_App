package com.pmapp.password_manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    public String receivedName, receivedUname, passFromDb;

    Button logout;
    ImageButton reloadButt;
    FirebaseAuth auth;
    TextView welmsg;
    FloatingActionButton addFab;

    RecyclerView recycler;
    List<String> titles, passName, passPass;
    Adapter adapter;

    FirebaseDatabase fDatabase;
    DatabaseReference dbRef, dbRefPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Intent intent1 = new Intent(this, MainActivity.class);
        //startActivity(intent);

        //this.recreate();

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigation_view);
        welmsg = findViewById(R.id.welTv);
        logout = findViewById(R.id.Logout);
        addFab = findViewById(R.id.add_fab);
        reloadButt = findViewById(R.id.reloadButt);

        recycler = findViewById(R.id.recyclerView);
        titles = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance();
        dbRef = fDatabase.getReference("users");

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.start, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);


        titles.add("Hrishii");
        titles.add("Too OP Hrishi");
        titles.add("Even more OP Hrishii");
        titles.add("OPest Hrishi");
        titles.add("Hrishii");
        titles.add("Too OP Hrishi");
        titles.add("Even more OP Hrishii");
        titles.add("OPest Hrishi");

        Intent intent = getIntent();

        receivedName = intent.getStringExtra("name");
        receivedUname = intent.getStringExtra("uname");
        Log.i("name", "name= " + receivedName);
        welmsg.setText("Hello, Welcome "+receivedName+"!");

        dbRefPass = fDatabase.getReference("passwords").child(receivedUname);
        Query passQuery = dbRefPass.orderByChild("username");
        passQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int passCount = (int) snapshot.child("").getChildrenCount();
                    String lol = snapshot.toString();
                    HashMap<String, HashMap<String, String>> val = (HashMap<String, HashMap<String, String>>) snapshot.getValue();
                    passName = new ArrayList<String>();
                    passPass = new ArrayList<String>();
                    for (HashMap<String, String> i : val.values()) {
                        HashMap<String, String> data = i;
                        passName.add(data.get("name"));
                        passPass.add(data.get("password"));
                        Log.i("testpassc", "onDataChange: Data=> " + i + ",------->" + data.get("name"));
                    }
                    Log.i("testpassc", "onDataChange: pass= "+passCount+"------"+lol+"  "+receivedUname+"------"+passName+"=="+passPass);
                    adapter = new Adapter(getApplicationContext(), passName);
                    adapter.setPassPass(passPass);
                    adapter.setUname(receivedUname);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2, GridLayoutManager.VERTICAL, false);
                    recycler.setLayoutManager(gridLayoutManager);
                    recycler.setAdapter(adapter);
                    //passCountTV.setText(Integer.toString(passCount));
                }
                else{
                    Log.i("testpassc", "onDataChange: pass = losssssssss");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reloadButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(0, 0);
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });

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
                Intent intent = new Intent(getApplicationContext(), addPass.class);
                Query checkUser = dbRef.orderByChild("username").equalTo(receivedUname);
                Log.i("testpass", "(Main)onClick: before event listener");
                checkUser.addValueEventListener(new ValueEventListener() {
                    @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.i("testpass", "(Main)onDataChange: before if");
                    if (snapshot.exists()) {
                        Log.i("testpass", "(Main)onDataChange: exists bypassed!");
                        passFromDb = snapshot.child(receivedUname).child("password").getValue(String.class);
                        Log.i("testpass", "(Main)onClick:-------- pass= "+passFromDb);
                        intent.putExtra("uname", receivedUname);
                        intent.putExtra("password", passFromDb);
                        startActivity(intent);
                    }
                    else {
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.i("canac", "onCancelled: " + error.toString());
                }
            });
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
                Intent intent = new Intent(getApplicationContext(), addPass.class);
                Query checkUser = dbRef.orderByChild("username").equalTo(receivedUname);
                Log.i("testpass", "(Main)onClick: before event listener");
                checkUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.i("testpass", "(Main)onDataChange: before if");
                        if (snapshot.exists()) {
                            Log.i("testpass", "(Main)onDataChange: exists bypassed!");
                            passFromDb = snapshot.child(receivedUname).child("password").getValue(String.class);
                            Log.i("testpass", "(Main)onClick:-------- pass= " + passFromDb);
                            intent.putExtra("uname", receivedUname);
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
                Toast.makeText(this, "redirecting to Profile Activty!", Toast.LENGTH_SHORT).show();
                Log.i("profile", "onNavigationItemSelected: Profile screen opening");
                Intent intento = new Intent(getApplicationContext(),profile.class);
                intento.putExtra("name",receivedName);
                intento.putExtra("uname",receivedUname);
                startActivity(intento);
                break;
            case R.id.icLogout:
                Toast.makeText(this, "Logout Activty!", Toast.LENGTH_SHORT).show();
                auth.signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
                break;
        }
        return true;
    }
}