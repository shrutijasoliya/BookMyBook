package com.example.bookmybook.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.bookmybook.R;
import com.example.bookmybook.User;
import com.example.bookmybook.fragment.AddFragment;
import com.example.bookmybook.fragment.ChatFragment;
import com.example.bookmybook.fragment.HomeFragment;
import com.example.bookmybook.fragment.MyBooksFragment;
import com.example.bookmybook.fragment.ProfileFragment;
import com.example.bookmybook.others.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private FrameLayout frameHome;
    private FrameLayout frameMyBooks;
    private FrameLayout frameAdd;
    private FrameLayout frameChat;
    private FrameLayout frameProfile;
    private LinearLayout llHome;
    private LinearLayout llMyBooks;
    private LinearLayout llAdd;
    private LinearLayout llChat;
    private LinearLayout llProfile;
    private LinearLayout llFragmentContainer;
    FirebaseAuth firebaseAuth;
    private NavigationView navDrawerHome;
    private View headerView;
    private DrawerLayout drawerLayout;
    private ImageView ivMenuHome;
    private Toolbar toolBarHome;
    private TextView tvToolBarText;
    private ProgressDialog progressDialogPass;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    PreferenceManager preferenceManager;
    private CircleImageView civUserProfilePicNav;
    private TextView tvUserNameNav;
    private TextView tvUserEmailIDNav;
    private Uri uProfileUri;
    private String uId;
    private StorageReference storageReference;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTheme(R.style.FragmentAdd);
        bindView();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        progressDialogPass = new ProgressDialog(this);
        firebaseFirestore = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(MainActivity.this);
        headerView = navDrawerHome.getHeaderView(0);
        storageReference = FirebaseStorage.getInstance().getReference();
        sharedPreferences = getApplicationContext().getSharedPreferences("Rate", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        civUserProfilePicNav = headerView.findViewById(R.id.civUserProfilePicNav);
        tvUserNameNav = headerView.findViewById(R.id.tvUserNameNav);
        tvUserEmailIDNav = headerView.findViewById(R.id.tvUserEmailIDNav);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        HomeFragment homeFragment = new HomeFragment();
        loadFragment(homeFragment);

        if (firebaseUser != null) {
            uProfileUri = firebaseUser.getPhotoUrl();
            uId = firebaseUser.getUid();

            if (uProfileUri != null) {
                Glide.with(getApplicationContext())
                        .load(uProfileUri)
                        .into(civUserProfilePicNav);
            }
            getUser();
            getProfilePic();
            Log.e("dkfhrddkjh", "onCreateView: " + uId + "---" + uProfileUri);
        }
//        getUser();

        llHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment homeFragment = new HomeFragment();
                loadFragment(homeFragment);
                llHome.setVisibility(View.GONE);
                llMyBooks.setVisibility(View.VISIBLE);
                llAdd.setVisibility(View.VISIBLE);
                llChat.setVisibility(View.VISIBLE);
                llProfile.setVisibility(View.VISIBLE);
                frameHome.setVisibility(View.VISIBLE);
                frameMyBooks.setVisibility(View.GONE);
                frameAdd.setVisibility(View.GONE);
                frameChat.setVisibility(View.GONE);
                frameProfile.setVisibility(View.GONE);
                toolBarHome.setVisibility(View.VISIBLE);
                tvToolBarText.setText(R.string.app_name);

            }
        });

        llMyBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyBooksFragment myBooksFragment = new MyBooksFragment();
                loadFragment(myBooksFragment);
                llHome.setVisibility(View.VISIBLE);
                llMyBooks.setVisibility(View.GONE);
                llAdd.setVisibility(View.VISIBLE);
                llChat.setVisibility(View.VISIBLE);
                llProfile.setVisibility(View.VISIBLE);
                frameHome.setVisibility(View.GONE);
                frameMyBooks.setVisibility(View.VISIBLE);
                frameAdd.setVisibility(View.GONE);
                frameChat.setVisibility(View.GONE);
                frameProfile.setVisibility(View.GONE);
                toolBarHome.setVisibility(View.VISIBLE);
                tvToolBarText.setText("My books");
            }
        });

        llAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFragment addFragment = new AddFragment();
                loadFragment(addFragment);
                llHome.setVisibility(View.VISIBLE);
                llMyBooks.setVisibility(View.VISIBLE);
                llAdd.setVisibility(View.GONE);
                llChat.setVisibility(View.VISIBLE);
                llProfile.setVisibility(View.VISIBLE);
                frameHome.setVisibility(View.GONE);
                frameMyBooks.setVisibility(View.GONE);
                frameAdd.setVisibility(View.VISIBLE);
                frameChat.setVisibility(View.GONE);
                frameProfile.setVisibility(View.GONE);
                toolBarHome.setVisibility(View.VISIBLE);
                tvToolBarText.setText("Add your book");
            }
        });

        llChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatFragment chatFragment = new ChatFragment();
                loadFragment(chatFragment);
                llHome.setVisibility(View.VISIBLE);
                llMyBooks.setVisibility(View.VISIBLE);
                llAdd.setVisibility(View.VISIBLE);
                llChat.setVisibility(View.GONE);
                llProfile.setVisibility(View.VISIBLE);
                frameHome.setVisibility(View.GONE);
                frameMyBooks.setVisibility(View.GONE);
                frameAdd.setVisibility(View.GONE);
                frameChat.setVisibility(View.VISIBLE);
                frameProfile.setVisibility(View.GONE);
                toolBarHome.setVisibility(View.VISIBLE);
                tvToolBarText.setText("Chat");
            }
        });

        llProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment profileFragment = new ProfileFragment();
                loadFragment(profileFragment);
                llHome.setVisibility(View.VISIBLE);
                llMyBooks.setVisibility(View.VISIBLE);
                llAdd.setVisibility(View.VISIBLE);
                llChat.setVisibility(View.VISIBLE);
                llProfile.setVisibility(View.GONE);
                frameHome.setVisibility(View.GONE);
                frameMyBooks.setVisibility(View.GONE);
                frameAdd.setVisibility(View.GONE);
                frameChat.setVisibility(View.GONE);
                frameProfile.setVisibility(View.VISIBLE);
                toolBarHome.setVisibility(View.GONE);
            }
        });

        ivMenuHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navDrawerHome.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                switch (id) {

                    case R.id.navPrePaper:
//                        Toast.makeText(MainActivity.this, "Papers selected", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, PreYearPaperActivity.class));
                        break;

                    case R.id.navResetPass:
                        showRecoverPasswordDialog();
//                        startActivity(new Intent(MainActivity.this, ResetPassActivity.class));
//                        Toast.makeText(MainActivity.this, "Reset Pass selected", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.navPrivacyPolicy:
//                        Toast.makeText(MainActivity.this, "Privacy Policy selected", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class));
                        break;

                    case R.id.navRateApp:
                        Toast.makeText(MainActivity.this, "Rate App selected", Toast.LENGTH_SHORT).show();
//                        showRateDialog(getApplicationContext(), editor);
                        break;

                    case R.id.navShareApp:
                        Intent myIntent = new Intent(Intent.ACTION_SEND);
                        myIntent.setType("text/plain");
                        String body = "Your link here";
                        myIntent.putExtra(Intent.EXTRA_TEXT, body);
                        startActivity(Intent.createChooser(myIntent, "Share Using"));
//                        Toast.makeText(MainActivity.this, "Share App selected", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.navLogout:
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(MainActivity.this, "LoggedOut user", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, SignInActivity.class));
                        finish();
                        break;

                    default:
                        return true;

                }
                drawerLayout.closeDrawer(GravityCompat.START);

                return false;
            }
        });

    }

    private void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setTitle("Rate " + "BookMyBook");

        LinearLayout ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.VERTICAL);

        TextView tv = new TextView(mContext);
        tv.setText("If you enjoy using " + "BookMyBook" + ", please take a moment to rate it. Thanks for your support!");
        tv.setWidth(240);
        tv.setPadding(4, 0, 4, 10);
        ll.addView(tv);

        Button b1 = new Button(mContext);
        b1.setText("Rate " + "BookMyBook");
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "App Package Name")));
                dialog.dismiss();
            }
        });
        ll.addView(b1);

        Button b2 = new Button(mContext);
        b2.setText("Remind me later");
        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ll.addView(b2);

        Button b3 = new Button(mContext);
        b3.setText("No, thanks");
        b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                dialog.dismiss();
            }
        });
        ll.addView(b3);

        dialog.setContentView(ll);
        dialog.show();
    }


    private void getUser() {
        Log.e("kjdasbf", "getUser: " + preferenceManager.getUserFireBaseId(MainActivity.this));
        DocumentReference docRef = firebaseFirestore.collection("Users").document(preferenceManager.getUserFireBaseId(MainActivity.this));
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("sbssdxcs", "DocumentSnapshot data: " + document.getData());
                        User user = document.toObject(User.class);
                        tvUserNameNav.setText(user.getfullName());
                        tvUserEmailIDNav.setText(user.getemail());

                        Log.e("idnxj", "onComplete: " + tvUserNameNav.getText().toString() + "---" + tvUserEmailIDNav.getText().toString());
                    } else {
                        Log.d("basfdva", "No such document");
                    }
                } else {
                    Log.d("efjScnc", "get failed with ", task.getException());
                }
            }
        });

    }

    private void getProfilePic() {
        StorageReference profileRef = storageReference.child("Users/" + firebaseAuth.getCurrentUser().getUid() + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri).into(civUserProfilePicNav);
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
//        emailet.setPaddingRelative(10,10,10,10);
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

                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

                firebaseAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialogPass.setMessage("Please Wait...");
                                progressDialogPass.show();
                                if (task.isSuccessful()) {
                                    progressDialogPass.dismiss();
                                    Toast.makeText(MainActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                    emailet.setText("");
                                } else {
                                    Toast.makeText(MainActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(MainActivity.this, SignInActivity.class));
        }
        if (user != null) {
            getUser();
            getProfilePic();
//            tvUserNameNav.setText(user.getDisplayName());
//            tvUserEmailIDNav.setText(user.getEmail());
        }
    }

    private void bindView() {
//        ivSearchBar = findViewById(R.id.ivSearchBar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navDrawerHome = findViewById(R.id.navDrawerHome);
        ivMenuHome = findViewById(R.id.ivMenuHome);
        toolBarHome = findViewById(R.id.toolBarHome);
        tvToolBarText = findViewById(R.id.tvToolBarText);
        llFragmentContainer = findViewById(R.id.llFragmentContainer);
        frameHome = findViewById(R.id.frameHome);
        frameMyBooks = findViewById(R.id.frameMyBooks);
        frameAdd = findViewById(R.id.frameAdd);
        frameChat = findViewById(R.id.frameChat);
        frameProfile = findViewById(R.id.frameProfile);
        llHome = findViewById(R.id.llHome);
        llMyBooks = findViewById(R.id.llMyBooks);
        llAdd = findViewById(R.id.llAdd);
        llChat = findViewById(R.id.llChat);
        llProfile = findViewById(R.id.llProfile);
//        ivHome = findViewById(R.id.ivHome);
//        ivMyBooks = findViewById(R.id.ivMyBooks);
//        ivAdd = findViewById(R.id.ivAdd);
//        ivChat = findViewById(R.id.ivChat);
//        ivProfile = findViewById(R.id.ivProfile);

    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.llFragmentContainer, fragment);
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit Application?");
        alertDialogBuilder
                .setMessage("Click yes to exit!")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment f : fragments) {
                if (f instanceof AddFragment) {
                    f.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }
}