package com.pmapp.password_manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.NoSuchAlgorithmException;

public class forgotPass extends AppCompatActivity {
    TextInputEditText ipUname, ipPass;
    TextView genPass;
    Button changButton;

    FirebaseDatabase fDatabase;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        Intent intent = getIntent();
        String uname = intent.getStringExtra("uname");
        String nameFromPA = intent.getStringExtra("name");
        String type = intent.getStringExtra("type");
        ipUname = findViewById(R.id.passOld);
        ipPass = findViewById(R.id.passNew);
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
                Toast.makeText(forgotPass.this, "Password Copied to Clipboard!", Toast.LENGTH_SHORT).show();
            }
        });
        Log.i("chgpass", "---------------------------------------------");
        changButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("chgpass", "(changePass)onCLickListner: In onClick!");
                String uname = ipUname.getText().toString().trim();
                String pass = ipPass.getText().toString().trim();
                Log.i("chgpass", "(changePass)onCLickListner: pass1= "+uname+"---- pass2= "+pass);
                if(uname.isEmpty()){
                    ipUname.setError("Field cannot be empty!");
                    ipUname.requestFocus();
                }
                else if(pass.isEmpty()){
                    ipUname.setError("Field cannot be empty!");
                    ipPass.requestFocus();
                }
                else{
                    Log.i("chgpass", "(changePass)onDataChange: type = user");
                    dbRef = fDatabase.getReference("users");
                    Query checkUser = dbRef.orderByChild("username").equalTo(uname);
                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Log.i("chgpass", "(changePass)onDataChange: exists bypassed!");
                                String nameFromDB = snapshot.child(uname).child("name").getValue(String.class);
                                String emailFromDB = snapshot.child(uname).child("email").getValue(String.class);
                                String passFromDB = snapshot.child(uname).child("password").getValue(String.class);
                                String hashPass1 = "";
                                try {
                                    hashPass1 = secret.sha512Hasher(pass,uname);
                                    Log.i("chgpass", "(changePass)onDataChange: hashPass1= "+hashPass1);
                                } catch (NoSuchAlgorithmException e) {
                                    Log.i("chgpass", "(changePass)onDataChange: {hashPass1}Exception= "+e.toString());
                                }
                                if(uname.equals(pass)){
                                    ipPass.setError("New password cannot be same as the old one!");
                                    ipPass.requestFocus();
                                }
                                else{
                                    String hashPass2 = "";
                                    try {
                                        hashPass2 = secret.sha512Hasher(pass,uname);
                                        Log.i("chgpass", "(changePass)onDataChange: hashPass2= "+hashPass2);
                                    } catch (NoSuchAlgorithmException e) {
                                        Log.i("chgpass", "(changePass)onDataChange: {hashPass2}Exception= "+e.toString());
                                    }
                                    Log.i("chgpass", "(changePass)all done now storing..... ");
                                    UserHelper uh = new UserHelper(nameFromDB, uname, emailFromDB, hashPass2);
                                    dbRef.child(uname).setValue(uh);
                                    Log.i("chgpass", "(changePass)done now redirecting..... ");
                                    Toast.makeText(forgotPass.this, "Password Updated!", Toast.LENGTH_SHORT).show();
                                    //startActivity(new Intent(getApplicationContext(),profile.class));
                                    finish();
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
            }
        });
    }
}