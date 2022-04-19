package com.pmapp.password_manager;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.concurrent.Executor;

public class Login extends AppCompatActivity {

    TextInputLayout ipUser, ipPass;
    Button login;
    TextView suTV, fpTV;
    FirebaseAuth auth;
    ProgressBar ipProgress;
    FirebaseDatabase fDatabase;
    DatabaseReference dbRef, dfRefLogUser;
    String Uid;
    String email;
    String pass;

    //FingerPrint
    private static int REQUEST_CODE = 101010;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //getActionBar().hide();

        ipUser = findViewById(R.id.userName);
        ipPass = findViewById(R.id.Password);
        login = findViewById(R.id.Login);
        ipProgress = findViewById(R.id.progress);
        suTV = findViewById(R.id.signUpTV);
        fpTV = findViewById(R.id.forgotPass);
        auth = FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance();
        dbRef = fDatabase.getReference("users");

        suTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUP.class));
            }
        });
        fpTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), forgotPass.class));
            }
        });

        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.e("MY_APP_TAG", "No biometric features available on this device.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                // Prompts the user to create credentials that your app accepts.
                final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
                startActivityForResult(enrollIntent, REQUEST_CODE);
                break;
        }
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(Login.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,@NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(), "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Log.i("fingerprint", "onAuthenticationSucceeded: "+result.getAuthenticationType());
                Toast.makeText(getApplicationContext(),"Authentication succeeded!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",Toast.LENGTH_SHORT).show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build();

        // Prompt appears when user clicks "Log in".
        // Consider integrating with the keystore to unlock cryptographic operations,
        // if needed by your app.
        ImageView biometricLoginButton = findViewById(R.id.fingerPrint);
        biometricLoginButton.setOnClickListener(view -> {
            biometricPrompt.authenticate(promptInfo);
        });

        Log.i("info", "Before onCliclk!");
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("info", "onClick: before taking unams and pass ");
                email = ipUser.getEditText().getText().toString().trim();
                pass = ipPass.getEditText().getText().toString().trim();

                try {
                    String hashPass = secret.sha512Hasher(pass,email);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                Log.d("info", "onClick: uname, pass: "+email+","+pass);

                if (TextUtils.isEmpty(email)){
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
                Log.i("nameop", "onDataChange: email= " + email);

                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this, "Logged in Successful!", Toast.LENGTH_SHORT).show();
                            String name = "";
                            Uid = auth.getCurrentUser().getUid();
                            String mail = auth.getCurrentUser().getEmail();
                            Log.i("name", "onDataChange: mail= " + mail);
                            dfRefLogUser = dbRef.child(Uid);
                            dfRefLogUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String nameFromDb = snapshot.child("name").getValue(String.class);
                                    Log.i("name", "onDataChange: name= " + nameFromDb);
                                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                    intent.putExtra("name",nameFromDb);
                                    intent.putExtra("uid",Uid);
                                    startActivity(intent);
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                        else{
                            Toast.makeText(Login.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            ipProgress.setVisibility(View.GONE);
                        }
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