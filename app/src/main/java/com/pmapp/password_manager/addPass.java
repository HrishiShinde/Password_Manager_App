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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class addPass extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    public String passFromDb;

    TextInputEditText ipName, ipPass;
    TextView genPass;
    Button addPass;

    FirebaseDatabase fDatabase;
    DatabaseReference dbRefPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pass);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigation_view);
        ipName = findViewById(R.id.name);
        ipPass = findViewById(R.id.password);
        genPass=findViewById(R.id.genPass);
        addPass=findViewById(R.id.addPassBtn);
        fDatabase = FirebaseDatabase.getInstance();
        dbRefPass = fDatabase.getReference("passwords");

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.start, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        Intent intent = getIntent();
        String unameFromMA = intent.getStringExtra("uname");
        String passFromMA = intent.getStringExtra("password");
        String reducedPass = "";
        Log.i("testpass", "(addPass)onCreate: "+unameFromMA+"-----"+passFromMA);
        for (int i = 0; i < 8; i++ ){
            reducedPass += passFromMA.charAt(i);
        }
        Log.i("testpass", "(addPass)onCreate: "+unameFromMA+"-----"+reducedPass);

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

        String finalReducedPass = reducedPass;
        addPass.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String sitename = ipName.getText().toString().trim();
                String sitepass = ipPass.getText().toString().trim();

                Log.i("testpass", "(addPass)onClick: pass= "+passFromMA);
                //secret sec = new secret(finalReducedPass);
                Log.i("testpass", "(addPass)onClick: before Try");
                try {
                    Log.i("testpass", "(addPass)onClick: in Try");
                    //String siteName = "test1174";
                    Log.i("testpass", "(addPass)onClick: before enc sitename= "+sitename+"---- sitepass= "+sitepass);
                    Log.i("testpass", "(addPass)onClick: before enc");

                    String encPass = secret.encrypt(sitepass, unameFromMA);

                    Log.i("testpass", "(addPass)onClick: encPass= "+encPass);
                    Log.i("testpass", "(addPass)onClick: decPass= "+secret.decrypt(encPass, unameFromMA));

                    PassHelper ph = new PassHelper(sitename, encPass);
                    dbRefPass.child(unameFromMA).child(sitename).setValue(ph);

                    Log.i("testpass", "(addPass)onClick: Stored!, now redirecting....");

                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    //intent.putExtra("name",name);
                    Toast.makeText(addPass.this, "Password saved!", Toast.LENGTH_SHORT).show();
                    finish();

                } catch (Exception e) {
                    Log.i("testpass", "(addPass)onClick: Exception: "+e.toString());
                }
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
                finish();
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




















