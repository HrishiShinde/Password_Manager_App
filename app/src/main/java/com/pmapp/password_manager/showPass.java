package com.pmapp.password_manager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class showPass extends AppCompatActivity {

    TextInputEditText ipName, ipPassword;
    Button upButt, copyButt, delButt, chgButt;
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

        Intent intent = getIntent();
        String nameFromMA = intent.getStringExtra("name");
        String passFromMA = intent.getStringExtra("pass");
        String unameFromMA = intent.getStringExtra("uname");
        ipName.setText(nameFromMA);
        ipPassword.setText(passFromMA);

        upButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        copyButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String genedPass = secret.genpass(8);
                String genedPass = ipPassword.getText().toString().trim();
                String key = secret.genKey(unameFromMA);
                String decPass = secret.decrypt(genedPass, key);
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

            }
        });
        chgButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        /*
        *
            <Button
                android:id="@+id/copyButt"
                android:layout_width="70dp"
                android:layout_height="40dp"
                android:layout_marginTop="5dp"
                android:text="@string/copy"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/cardTitle" />

            <Button
                android:id="@+id/chgButt"
                android:layout_width="90dp"
                android:layout_height="40dp"
                android:layout_marginTop="4dp"
                android:text="@string/change"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintHorizontal_bias="0.498"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/copyButt" />
        * */

    }
}