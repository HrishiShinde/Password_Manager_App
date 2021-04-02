package com.pmapp.password_manager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.textfield.TextInputEditText;

public class showPass extends AppCompatActivity {

    TextInputEditText ipName, ipPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pass);

        ipName = findViewById(R.id.name);
        ipPassword = findViewById(R.id.password);

        Intent intent = getIntent();
        String nameFromMA = intent.getStringExtra("name");
        String passFromMA = intent.getStringExtra("pass");

        ipName.setText(nameFromMA);
        ipPassword.setText(passFromMA);

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