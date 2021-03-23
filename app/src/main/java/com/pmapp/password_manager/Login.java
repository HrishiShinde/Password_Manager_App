package com.pmapp.password_manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.NoSuchAlgorithmException;

public class Login extends AppCompatActivity {

    TextInputLayout ipUser, ipPass;
    Button login;
    TextView suTV;
    FirebaseAuth auth;
    ProgressBar ipProgress;
    FirebaseDatabase fDatabase;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //getActionBar().hide();
        //getActionBar().hide();

        ipUser = findViewById(R.id.userName);
        ipPass = findViewById(R.id.Password);
        login = findViewById(R.id.Login);
        ipProgress = findViewById(R.id.progress);
        suTV = findViewById(R.id.signUpTV);
        auth = FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance();
        dbRef = fDatabase.getReference("users");

        suTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUP.class));
            }
        });
        Log.i("info", "Before onCliclk!");
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("info", "onClick: before taking unams and pass ");
                String uname = ipUser.getEditText().getText().toString().trim();
                String pass = ipPass.getEditText().getText().toString().trim();

                try {
                    String hashPass = secret.sha512Hasher(pass,uname);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                Log.i("info", "onClick: uname, pass: "+uname+","+pass);

                if (TextUtils.isEmpty(uname)){
                    ipUser.setError("Email is required!");
                    //Log.i("info", " in email check now!");
                    return;
                }
                if (TextUtils.isEmpty(pass)){
                    ipPass.setError("Password is required!");
                    //Log.i("info", " in pass check now!");
                    return;
                }

                //ipProgress.setVisibility(View.VISIBLE);

                Query checkUser = dbRef.orderByChild("username").equalTo(uname);
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists()){
                            String passFromDb = snapshot.child(uname).child("password").getValue(String.class);

                            try {
                                String hashPass = secret.sha512Hasher(pass,uname);
                                Log.i("pass", "onDataChange: "+passFromDb+"===="+hashPass);jects
                                if(passFromDb.equals(hashPass)){
                                    String name = "";
                                    String nameFromDb = snapshot.child(uname).child("name").getValue(String.class);
                                    Log.i("name", "onDataChange: name= " + nameFromDb);
                                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                    intent.putExtra("name",nameFromDb);
                                    startActivity(intent);
                                }
                                else{
                                    ipPass.setError("Incorrect Password!");
                                    ipPass.requestFocus();
                                    //ipProgress.setVisibility(View.GONE);
                                }
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            ipUser.setError("Username doesn't exists!");
                            ipUser.requestFocus();
                            //ipProgress.setVisibility(View.GONE);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                /*
                auth.signInWithEmailAndPassword(uname, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this, "Logged in Successful!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        else{
                            Toast.makeText(Login.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            ipProgress.setVisibility(View.GONE);
                        }
                    }
                });*/
            }
        });
    }
}