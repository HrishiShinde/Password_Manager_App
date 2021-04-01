package com.pmapp.password_manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.NoSuchAlgorithmException;

public class changePass extends AppCompatActivity {

    TextInputEditText ipPass1, ipPass2;
    TextView genPass;
    Button changButton;

    FirebaseDatabase fDatabase;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        Intent intent = getIntent();
        String unameFromPA = intent.getStringExtra("name");
        String type = intent.getStringExtra("type");
        ipPass1 = findViewById(R.id.passOld);
        ipPass2 = findViewById(R.id.passNew);
        genPass=findViewById(R.id.genPass);
        changButton=findViewById(R.id.changBTN);
        fDatabase = FirebaseDatabase.getInstance();

        genPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String genedPass = secret.genpass(8);
                //ipPass.setText(genedPass);
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("genedPass", genedPass);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(changePass.this, "Password Copied to Clipboard!", Toast.LENGTH_SHORT).show();
            }
        });
        Log.i("chgpass", "---------------------------------------------");
        changButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("chgpass", "(changePass)onCLickListner: In onClick!");
                String pass1 = ipPass1.getText().toString().trim();
                String pass2 = ipPass2.getText().toString().trim();
                Log.i("chgpass", "(changePass)onCLickListner: pass1= "+pass1+"---- pass2= "+pass2);
                if(pass1.isEmpty()){
                    ipPass1.setError("Field cannot be empty!");
                    ipPass1.requestFocus();
                }
                if(pass2.isEmpty()){
                    ipPass1.setError("Field cannot be empty!");
                    ipPass2.requestFocus();
                }
                if(type.equals("user")){
                    Log.i("chgpass", "(changePass)onDataChange: type = user");
                    dbRef = fDatabase.getReference("users");
                    Query checkUser = dbRef.orderByChild("username").equalTo(unameFromPA);
                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Log.i("chgpass", "(changePass)onDataChange: exists bypassed!");
                                String nameFromDB = snapshot.child(unameFromPA).child("name").getValue(String.class);
                                String emailFromDB = snapshot.child(unameFromPA).child("email").getValue(String.class);
                                String passFromDB = snapshot.child(unameFromPA).child("password").getValue(String.class);
                                String hashPass1 = "";
                                try {
                                    hashPass1 = secret.sha512Hasher(pass1,unameFromPA);
                                    Log.i("chgpass", "(changePass)onDataChange: hashPass1= "+hashPass1);
                                } catch (NoSuchAlgorithmException e) {
                                    Log.i("chgpass", "(changePass)onDataChange: {hashPass1}Exception= "+e.toString());
                                }
                                if(hashPass1.equals(passFromDB)){
                                    if(pass1.equals(pass2)){
                                        ipPass2.setError("New password cannot be same as the old one!");
                                        ipPass2.requestFocus();
                                    }
                                    else{
                                        String hashPass2 = "";
                                        try {
                                            hashPass2 = secret.sha512Hasher(pass2,unameFromPA);
                                            Log.i("chgpass", "(changePass)onDataChange: hashPass2= "+hashPass2);
                                        } catch (NoSuchAlgorithmException e) {
                                            Log.i("chgpass", "(changePass)onDataChange: {hashPass2}Exception= "+e.toString());
                                        }
                                        Log.i("chgpass", "(changePass)all done now storing..... ");
                                        UserHelper uh = new UserHelper(nameFromDB, unameFromPA, emailFromDB, hashPass2);
                                        dbRef.child(unameFromPA).setValue(uh);
                                        Log.i("chgpass", "(changePass)done now redirecting..... ");
                                        Toast.makeText(changePass.this, "Password Updated!", Toast.LENGTH_SHORT).show();
                                        //startActivity(new Intent(getApplicationContext(),profile.class));
                                        finish();
                                    }
                                }
                                else{
                                    ipPass1.setError("Password doesn't match!");
                                    ipPass1.requestFocus();
                                }
                            }
                            else{
                                Log.i("chgpass", "onDataChange: Loss bc!");
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
                else if(type.equals("site")) {
                    if(pass1.equals(pass2)){
                        ipPass2.setError("New password cannot be same!");
                        ipPass2.requestFocus();
                    }
                    else{
                        dbRef = fDatabase.getReference("passwords");
                        Query checkUser = dbRef.orderByChild("username").equalTo(unameFromPA);
                        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    Log.i("Lol", "onDataChange: exists bypassed!");
                                    String nameFromDB = snapshot.child(unameFromPA).child("name").getValue(String.class);
                                    String emailFromDB = snapshot.child(unameFromPA).child("email").getValue(String.class);
                                    //String emailFromDB = snapshot.child(unameFromPA).child("username").getValue(String.class);
                                    String passFromDB = snapshot.child(unameFromPA).child("password").getValue(String.class);
                                    //ipPassword.setText(snapshot.child("password").getValue(String.class));

                                    UserHelper uh = new UserHelper(nameFromDB, unameFromPA, emailFromDB, pass2);
                                    dbRef.child(unameFromPA).setValue(uh);
                                    Toast.makeText(changePass.this, "Password Updated!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                }
            }
        });
    }
}