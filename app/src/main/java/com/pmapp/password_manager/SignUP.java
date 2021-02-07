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
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUP extends AppCompatActivity {

    TextInputEditText ipUsername, ipEmail, ipPassword, ipConfpass;
    Button ipSignup;
    ProgressBar ipProgress;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_u_p);

        ipUsername = findViewById(R.id.username);
        ipEmail = findViewById(R.id.email);
        ipPassword = findViewById(R.id.pass);
        ipConfpass = findViewById(R.id.confpass);
        ipSignup = findViewById(R.id.regBut);
        ipProgress = findViewById(R.id.progress);
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        ipSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i("info", " in onClick now!");
                String email = ipEmail.getText().toString().trim();
                String pass = ipPassword.getText().toString().trim();
                String cpass = ipConfpass.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    ipEmail.setError("Email is required!");
                    //Log.i("info", " in email check now!");
                    return;
                }
                if (TextUtils.isEmpty(pass)){
                    ipPassword.setError("Password is required!");
                    //Log.i("info", " in pass check now!");
                    return;
                }
                if (TextUtils.isEmpty(cpass)){
                    ipConfpass.setError("Must be same as password entered above!");
                    //Log.i("info", " in cpass check now!");
                    return;
                }
                //Log.i("info", " before pbar now!, values of email,pass,cpass are : "+email+pass+cpass);
                ipProgress.setVisibility(View.VISIBLE);

                //Log.i("info", " before pass check now!, values of pass,cpass are : "+pass+", "+cpass);
                if (pass.equals(cpass)){
                    //Log.i("info", " in pass check now!, values of pass,cpass are : "+pass+", "+cpass);
                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //Log.i("info", " in onComplete now!");
                            if(task.isSuccessful()){
                                //Log.i("info", " in isSuccessful now!");
                                Toast.makeText(SignUP.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                            else{
                                Toast.makeText(SignUP.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                //Log.e("error", task.getException().getMessage());
                                ipProgress.setVisibility(View.GONE);
                            }
                        }
                    });
                }
                else{
                    ipConfpass.setError("Must be same as password entered above!");
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