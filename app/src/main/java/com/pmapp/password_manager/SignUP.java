package com.pmapp.password_manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SignUP extends AppCompatActivity{

    TextInputEditText ipUsername, ipEmail, ipPassword, ipName;
    Button ipSignup;
    ProgressBar ipProgress;
    FirebaseAuth auth;
    FirebaseFirestore fStore;
    FirebaseDatabase fDatabase;
    DatabaseReference dbRef;
    String Uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_u_p);

        ipName = findViewById(R.id.name);
        ipUsername = findViewById(R.id.username);
        ipEmail = findViewById(R.id.email);
        ipPassword = findViewById(R.id.pass);
        ipSignup = findViewById(R.id.regBut);
        ipProgress = findViewById(R.id.progress);
        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fDatabase = FirebaseDatabase.getInstance();
        dbRef = fDatabase.getReference("users");

        ipSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i("info", " in onClick now!");
                String username = ipUsername.getText().toString();
                String email = ipEmail.getText().toString().trim();
                String password = ipPassword.getText().toString().trim();
                String name = ipName.getText().toString().trim();

                if (TextUtils.isEmpty(name)){
                    ipName.setError("Your full name is required!");
                    ipName.requestFocus();
                    //Log.i("info", " in name check now!");
                    return;
                }
                if (TextUtils.isEmpty(email)){
                    ipEmail.setError("Email is required!");
                    ipEmail.requestFocus();
                    //Log.i("info", " in email check now!");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    ipPassword.setError("Password is required!");
                    ipPassword.requestFocus();
                    //Log.i("info", " in pass check now!");
                    return;
                }

                //Log.i("info", " before pbar now!, values of email,pass,cpass are : "+email+pass+cpass);
                ipProgress.setVisibility(View.VISIBLE);

                //Log.i("info", " before pass check now!, values of pass,cpass are : "+pass+", "+cpass);
                if (password.length() >= 8){
                    //Log.i("info", " in pass check now!, values of pass,cpass are : "+pass+", "+cpass);

                    Query checkUser = dbRef.orderByChild("username").equalTo(username);
                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if(snapshot.exists()){
                                String unameFromDb = snapshot.child(username).child("username").getValue(String.class);

                                if(unameFromDb.equals(username)){
                                    ipUsername.setError("Username Exists!");
                                    ipUsername.requestFocus();
                                    ipProgress.setVisibility(View.GONE);
                                }
                            }
                            else{
                                try {
                                    String hashPass = secret.sha512Hasher(password,username);
                                    Log.i("pass", "onDataChange: Hashedpass= "+hashPass);
                                    UserHelper uh = new UserHelper(name,username,email,hashPass);
                                    dbRef.child(username).setValue(uh);
                                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                    intent.putExtra("name",name);
                                    startActivity(intent);
                                    finish();
                                } catch (NoSuchAlgorithmException e) {
                                    e.printStackTrace();
                                }
                                //ipProgress.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    /*auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //Log.i("info", " in onComplete now!");
                            if(task.isSuccessful()){
                                //Log.i("info", " in isSuccessful now!");
                                Toast.makeText(SignUP.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                                Uid = auth.getCurrentUser().getUid();
                                DocumentReference docRef = fStore.collection("users").document(Uid);
                                Map<String, Object> userMap = new HashMap<>();
                                userMap.put("Uname", username);
                                userMap.put("Email", email);

                                docRef.set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.i("TAG", "onSuccess: User doc created for" + Uid);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.i("TAG", "onFaliure: " + e.toString());
                                    }
                                });

                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                            else{
                                Toast.makeText(SignUP.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                //Log.e("error", task.getException().getMessage());
                                ipProgress.setVisibility(View.GONE);
                            }
                        }
                    });*/
                }
                else{
                    ipPassword.setError("Must be greater than or eqaual to 8 chars!");
                    //Log.i("info", " in else now!");
                    ipProgress.setVisibility(View.GONE);
                }
            }
        });

    }
    public void gotolog(View view){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}