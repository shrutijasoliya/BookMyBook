package com.example.bookmybook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookmybook.R;
import com.example.bookmybook.User;
import com.example.bookmybook.others.PreferenceManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private EditText etFullName;
    private EditText etPhone;
    private EditText etEmailSignUp;
    private EditText etPasswordSignUp;
    private EditText etConPasswordSignUp;
    private ImageView ivPassHideShow;
    private ImageView ivPassHideShow1;
    private Button btnSignUp;
    private LinearLayout llGoogleSignUp;
    private TextView tvSignIn;
    private String userId;
    private String userFireBaseId;
    PreferenceManager preferenceManager;
    private String fullName;
    private String mobileNumber;
    private String email;
    private String password;
    private String conpassword;

    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private static final String USERS = "users";
    private FirebaseAuth auth;
    private User user;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        bindView();

        firebaseFirestore = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference(USERS);
        auth = FirebaseAuth.getInstance();

        preferenceManager = new PreferenceManager(this);
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(etEmailSignUp.getText().toString())) {
                    etEmailSignUp.setError("Email cannot be empty");
                    etEmailSignUp.requestFocus();
                }
                if (TextUtils.isEmpty(etPasswordSignUp.getText().toString())) {
                    etPasswordSignUp.setError("Password cannot be empty");
                    etPasswordSignUp.requestFocus();
                }
//                if (password!=etConPasswordSignUp.getText().toString()) {
//                    etConPasswordSignUp.setError("Please enter confirm password as password");
//                    etConPasswordSignUp.requestFocus();
//                }
                else {
                    fullName = etFullName.getText().toString();
                    mobileNumber = etPhone.getText().toString();
                    email = etEmailSignUp.getText().toString();
                    password = etPasswordSignUp.getText().toString();
                    conpassword = etConPasswordSignUp.getText().toString();

                    Log.e("bnmfv", "onClick: " + email + password);
//                    user = new User(fullName, mobileNumber, email);
                    SignUP(email, password);
                }
            }
        });
    }

    void SignUP(String Email, String Password) {
        auth.createUserWithEmailAndPassword(Email, Password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser user = auth.getCurrentUser();
                userId = user.getUid();

                preferenceManager.setUserId(SignUpActivity.this, userId);
                Log.e("jd455", "onSuccess: " + userId);

                addUser();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("bvnmzcxv", "onFailure: " + "signUp Failed");
                Toast.makeText(SignUpActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addUser() {
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("fullName", fullName);
        user.put("mobileNumber", mobileNumber);
        user.put("email", email);
        user.put("password", password);
        user.put("collegeName", "");

// Add a new document with a generated ID
        firebaseFirestore.collection("Users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("jdsbvh", "DocumentSnapshot added with ID: " + documentReference.getId());
                        userFireBaseId = documentReference.getId();
                        preferenceManager.setUserFireBaseId(SignUpActivity.this, userFireBaseId);
                        startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                        Toast.makeText(SignUpActivity.this, "Sign Up Successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("dvhgncb", "Error adding document", e);
                    }
                });
    }

    public void ShowHidePass(View view) {

        if (view.getId() == R.id.ivPassHideShow) {
            if (etPasswordSignUp.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
//                ((ImageView)(view)).setImageResource(R.drawable.ic_visibility_off);
                etPasswordSignUp.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
//               ((ImageView)(view)).setImageResource(R.drawable.ic_visibility);
                etPasswordSignUp.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }

        if (view.getId() == R.id.ivPassHideShow1) {
            if (etConPasswordSignUp.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
//                ((ImageView)(view)).setImageResource(R.drawable.ic_visibility_off);
                etConPasswordSignUp.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
//               ((ImageView)(view)).setImageResource(R.drawable.ic_visibility);
                etConPasswordSignUp.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

    private void bindView() {

        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        etEmailSignUp = findViewById(R.id.etEmailSignUp);
        etPasswordSignUp = findViewById(R.id.etPasswordSignUp);
        etConPasswordSignUp = findViewById(R.id.etConPasswordSignUp);
        ivPassHideShow = findViewById(R.id.ivPassHideShow);
        ivPassHideShow1 = findViewById(R.id.ivPassHideShow1);
        btnSignUp = findViewById(R.id.btnSignUp);
        llGoogleSignUp = findViewById(R.id.llGoogleSignUp);
        tvSignIn = findViewById(R.id.tvSignIn);
    }

}