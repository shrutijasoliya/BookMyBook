package com.example.bookmybook.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.bookmybook.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ResetPassActivity extends AppCompatActivity {
    private EditText etCurrentPass;
    private EditText etNewPass;
    private Button btnSave;
    private DatabaseReference dbPDF;
    String pass, newpass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reset_pass);

        dbPDF = FirebaseDatabase.getInstance().getReference("users").child("password");

        etCurrentPass = (EditText)findViewById(R.id.etCurrentPass);
        etNewPass = (EditText)findViewById(R.id.etNewPass);

        btnSave = (Button) findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pass = etCurrentPass.getText().toString();
                newpass = etNewPass.getText().toString();


            }
        });
    }
}