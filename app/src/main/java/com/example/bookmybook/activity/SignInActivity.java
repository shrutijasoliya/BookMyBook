package com.example.bookmybook.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookmybook.R;
import com.example.bookmybook.others.PreferenceManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {

    private EditText etEmailSignIn;
    private EditText etPasswordSignIn;
    private ImageView ivPassHideShow;
    private TextView tvForgetPass;
    private Button btnSignIn;
    private LinearLayout llGoogleSignIn;
    private TextView tvSignUp;
    private GoogleSignInClient mGoogleSignInClient;
    private ProgressBar progressBarSignIn;
    private ProgressDialog loadingBar;
    //    private FirebaseAuth mAuth;
    private ProgressDialog progressDialogPass;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private String userId;
    PreferenceManager preferenceManager;
    private FirebaseFirestore firebaseFirestore;
    private String userFireBaseId;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        bindView();

        firebaseFirestore = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(this);
        progressDialogPass = new ProgressDialog(this);


        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = etEmailSignIn.getText().toString();
                String password = etPasswordSignIn.getText().toString();

                Log.d("fgarjab", email + password);
                if (TextUtils.isEmpty(email)) {
                    etEmailSignIn.setError("Email cannot be empty");
                    etEmailSignIn.requestFocus();
                }
                if (TextUtils.isEmpty(password)) {
                    etPasswordSignIn.setError("Password cannot be empty");
                    etPasswordSignIn.requestFocus();
                } else {
                    signInBtn(email, password);
                }
            }
        });

        llGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBarSignIn.setVisibility(View.VISIBLE);
                googleSignin();
                signIn();
            }
        });

        tvForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRecoverPasswordDialog();
            }
        });

    }


    void signInBtn(String Email, String Password) {
        auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(Email, Password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                String userId = preferenceManager.getUserId(SignInActivity.this);

                Log.e("jd45515", "onSuccess: " + userId);

                Log.d("egrjzt", Email + Password);
                Intent i = new Intent(SignInActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
//                startActivity(new Intent(SignInActivity.this, MainActivity.class));
                Toast.makeText(SignInActivity.this, "SignIn Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("1gs23456", Email + Password);
                Toast.makeText(SignInActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void googleSignin() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(SignInActivity.this, gso);

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 100) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.e("udgjbhsnx", "onActivityResult: " + account.getIdToken());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {

            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                progressBarSignIn.setVisibility(View.GONE);
                addUser();
//                startActivity(new Intent(SignInActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                Toast.makeText(SignInActivity.this, "SignIn Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignInActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addUser() {
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        Log.e("efkhjdcm", "addUser: "+ firebaseUser.getDisplayName() +"---"+ firebaseUser.getEmail() );
        Map<String, Object> user = new HashMap<>();
        user.put("fullName", firebaseUser.getDisplayName());
        user.put("mobileNumber", "");
        user.put("email", firebaseUser.getEmail());
        user.put("password", "");
        user.put("collegeName", "");

// Add a new document with a generated ID
        firebaseFirestore.collection("Users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("jdsbvh", "DocumentSnapshot added with ID: " + documentReference.getId());
                        userFireBaseId = documentReference.getId();
                        preferenceManager.setUserFireBaseId(SignInActivity.this, userFireBaseId);
                        startActivity(new Intent(SignInActivity.this, MainActivity.class));
                        Toast.makeText(SignInActivity.this, "Sign In Successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("dvhgncb", "Error adding document", e);
                    }
                });
    }


    private void showRecoverPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");
        LinearLayout linearLayout = new LinearLayout(this);
        final EditText emailet = new EditText(this);

        // write the email using which you registered
        emailet.setHint("Email");
        emailet.setMinEms(20);
        emailet.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        linearLayout.addView(emailet);
        linearLayout.setPadding(10, 10, 10, 10);
        builder.setView(linearLayout);

        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = emailet.getText().toString().trim();
                Log.d("12345fh", "onClick: " + email);
//              beginRecovery(email);

                progressDialogPass.setMessage("Please wait...");
                progressDialogPass.show();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseAuth mAuth = FirebaseAuth.getInstance();

                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialogPass.setMessage("Please Wait...");
                                progressDialogPass.show();
                                if (task.isSuccessful()) {
                                    progressDialogPass.dismiss();
                                    Toast.makeText(SignInActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                    emailet.setText("");
                                } else {
                                    Toast.makeText(SignInActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                    emailet.setText("");
                                }
                            }
                        });

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    public void ShowHidePass(View view) {

        if (view.getId() == R.id.ivPassHideShow) {
            if (etPasswordSignIn.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
//                ((ImageView)(view)).setImageResource(R.drawable.ic_visibility_off);
                etPasswordSignIn.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
//               ((ImageView)(view)).setImageResource(R.drawable.ic_visibility);
                etPasswordSignIn.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }
//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = auth.getCurrentUser();
//        if(currentUser != null){
//            currentUser.reload();
//        }
//    }


    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }

    private void bindView() {

        progressBarSignIn = findViewById(R.id.progressBarSignIn);
        etEmailSignIn = findViewById(R.id.etEmailSignIn);
        etPasswordSignIn = findViewById(R.id.etPasswordSignIn);
        ivPassHideShow = findViewById(R.id.ivPassHideShow);
        tvForgetPass = findViewById(R.id.tvForgetPass);
        btnSignIn = findViewById(R.id.btnSignIn);
        llGoogleSignIn = findViewById(R.id.llGoogleSignIn);
        tvSignUp = findViewById(R.id.tvSignUp);
    }


}