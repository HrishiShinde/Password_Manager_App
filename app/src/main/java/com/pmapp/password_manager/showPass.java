package com.pmapp.password_manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class showPass extends AppCompatActivity {

    TextInputEditText ipName, ipPassword;
    Button upButt, copyButt, delButt, chgButt;

    FirebaseDatabase fDatabase;
    DatabaseReference dbRefPass, dbRefPassl;

    String oldName;

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pass);

        ipName = findViewById(R.id.name);
        ipPassword = findViewById(R.id.password);
        upButt = findViewById(R.id.upButt);
        copyButt = findViewById(R.id.copyButt);
        chgButt = findViewById(R.id.chgButt);
        delButt = findViewById(R.id.delButt);
        fDatabase = FirebaseDatabase.getInstance();
        dbRefPass = fDatabase.getReference("passwords");

        Intent intent = getIntent();
        String nameFromMA = intent.getStringExtra("name");
        String passFromMA = intent.getStringExtra("pass");
        String unameFromMA = intent.getStringExtra("uname");
        ipName.setText(nameFromMA);
        ipPassword.setText(passFromMA);


        upButt.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                //Log.i("showPass", "onClick: "+upButt.getId());

                if(upButt.getId() != 1){
                    ipName.setEnabled(true);
                    Log.i("showPass", "onClick: Prayaas going good! -- "+upButt.getId());
                    String oldName = ipName.getText().toString().trim();
                    setOldName(oldName);
                    upButt.setId(1);
                }
                else if(upButt.getId() == 1){
                    String name = ipName.getText().toString().trim();
                    String pass = ipPassword.getText().toString().trim();

                    if(name.isEmpty()){
                        ipName.setError("Field cannot be empty!");
                        ipName.requestFocus();
                    }
                    if(pass.isEmpty()){
                        ipPassword.setError("Field cannot be empty!");
                        ipPassword.requestFocus();
                    }

                    PassHelper ph = new PassHelper(name, pass);
                    dbRefPass.child(unameFromMA).child(name).setValue(ph);

                    dbRefPassl = fDatabase.getReference("passwords").child(unameFromMA).child(getOldName());
                    Query passQuery = dbRefPassl.orderByChild("username");
                    passQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot appleSnapshot: snapshot.getChildren()) {
                                appleSnapshot.getRef().removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    Toast.makeText(showPass.this, "Profile Updated!, Reload to see changes.", Toast.LENGTH_SHORT).show();
                    Log.i("showPass", "onClick:  Prayaas successful! -- "+upButt.getId());
                    ipName.setEnabled(false);
                    upButt.setId(2131296708);
                    finish();
                }
            }
        });
        copyButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String genedPass = secret.genpass(8);
                String genedPass = ipPassword.getText().toString().trim();
                String key = secret.genKey(unameFromMA);
                String decPass = secret.decrypt(genedPass, unameFromMA);
                //ipPass.setText(genedPass);

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("decPass", decPass);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(showPass.this, "Password Copied to Clipboard!", Toast.LENGTH_SHORT).show();
            }
        });
        delButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ipName.getText().toString().trim();
                dbRefPassl = fDatabase.getReference("passwords").child(unameFromMA).child(name);
                Query passQuery = dbRefPassl.orderByChild("username");
                passQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot appleSnapshot: snapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                            finish();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        chgButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ipName.getText().toString().trim();
                Intent intent = new Intent(getApplicationContext(), changePass.class);
                intent.putExtra("uname",unameFromMA);
                intent.putExtra("name",name);
                intent.putExtra("type","site");
                startActivity(intent);
                finish();
            }
        });

    }
}