package com.example.bookmybook.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.bookmybook.R;
import com.example.bookmybook.User;
import com.example.bookmybook.others.PreferenceManager;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    protected static final int GALLERY_PICTURE = 1;
    FirebaseAuth firebaseAuth;
    GoogleSignInClient googleSignInClient;
    private ImageView ivBackProfile;
    private CircleImageView civUserProfilePic;
    private TextView tvChangeProfilePic;
    private EditText etUserName;
    private EditText etUserMobileNo;
    private EditText etUserEmailID;
    private EditText etUserClgName;
    private Button btnUpdateProfile;
    private final boolean edit_update = false;
    private String uId;
    private Uri uProfileUri;
    private View view;
    private DatabaseReference databaseReference;
    private FirebaseFirestore firebaseFirestore;
    PreferenceManager preferenceManager;
    User user;
    private Uri imageUri;
    private StorageReference storageReference;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile, container, false);
        bindView();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        preferenceManager = new PreferenceManager(getContext());
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference();

        if (firebaseUser != null) {
            uProfileUri = firebaseUser.getPhotoUrl();
            uId = firebaseUser.getUid();

            if (uProfileUri != null) {
                Glide.with(getContext())
                        .load(uProfileUri)
                        .into(civUserProfilePic);
            }
            getUser();
            getProfilePic();
            Log.e("dkfhrkjh", "onCreateView: " + uId + "---" + uProfileUri);

        }

        tvChangeProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 1000);

            }
        });

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser();
            }
        });

        return view;
    }

    private void updateUser() {
        if (isNameChanged() || isMobileChanged() || isCollegeChanged()) {
            Toast.makeText(getContext(), "Data has been updated", Toast.LENGTH_SHORT).show();
            Log.e("sjnfsd,c", "updateUser: " + "updated Name : " + etUserName.getText().toString()
                    + "\n" + "updated Mobile : " + etUserMobileNo.getText().toString()
                    + "\n" + "updated College : " + etUserClgName.getText().toString());
        } else {
            Toast.makeText(getContext(), "Data is same and can not be updated", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNameChanged() {
        if (!user.getfullName().equals(etUserName.getText().toString())) {
            DocumentReference docRef = firebaseFirestore.collection("Users").document(preferenceManager.getUserFireBaseId(getContext()));
            docRef.update("fullName", etUserName.getText().toString());
            user.setfullName(etUserName.getText().toString());
            return true;
        } else {
            return false;
        }
    }

    private boolean isMobileChanged() {
        if (!user.getmobileNumber().equals(etUserMobileNo.getText().toString())) {
            DocumentReference docRef = firebaseFirestore.collection("Users").document(preferenceManager.getUserFireBaseId(getContext()));
            docRef.update("mobileNumber", etUserMobileNo.getText().toString());
            user.setmobileNumber(etUserMobileNo.getText().toString());
            return true;
        } else {
            return false;
        }
    }

    private boolean isCollegeChanged() {
        if (!user.getCollegeName().equals(etUserClgName.getText().toString())) {
            DocumentReference docRef = firebaseFirestore.collection("Users").document(preferenceManager.getUserFireBaseId(getContext()));
            docRef.update("collegeName", etUserClgName.getText().toString());
            user.setCollegeName(etUserClgName.getText().toString());
            return true;
        } else {
            return false;
        }
    }

    private void getUser() {
        Log.e("kjbf", "getUser: " + preferenceManager.getUserFireBaseId(getContext()));
        DocumentReference docRef = firebaseFirestore.collection("Users").document(preferenceManager.getUserFireBaseId(getContext()));
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("sbss", "DocumentSnapshot data: " + document.getData());
                        user = document.toObject(User.class);

                        showUserData(user);
                    } else {
                        Log.d("bassa", "No such document");
                    }
                } else {
                    Log.d("efjnc", "get failed with ", task.getException());
                }
            }
        });

    }

    private void showUserData(User user) {
        etUserName.setText(user.getfullName());
        etUserEmailID.setText(user.getemail());
        etUserMobileNo.setText(user.getmobileNumber());
        etUserClgName.setText(user.getCollegeName());
    }

    private void getProfilePic() {
        StorageReference profileRef = storageReference.child("Users/" + firebaseAuth.getCurrentUser().getUid() + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getContext()).load(uri).into(civUserProfilePic);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                imageUri = data.getData();
//                civUserProfilePic.setImageURI(imageUri);

                uploadImageToFirebase(imageUri);
                Log.e("dsnfc", "onActivityResult: " + "dp" + imageUri);
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        StorageReference fileRef = storageReference.child("Users/" + firebaseAuth.getCurrentUser().getUid() + "/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getContext()).load(uri).into(civUserProfilePic);
                        Toast.makeText(getContext(), "Image updated", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bindView() {

        civUserProfilePic = view.findViewById(R.id.civUserProfilePic);
        tvChangeProfilePic = view.findViewById(R.id.tvChangeProfilePic);
        etUserName = view.findViewById(R.id.etUserName);
        etUserMobileNo = view.findViewById(R.id.etUserMobileNo);
        etUserEmailID = view.findViewById(R.id.etUserEmailID);
        etUserClgName = view.findViewById(R.id.etUserClgName);
        btnUpdateProfile = view.findViewById(R.id.btnUpdateProfile);
    }

}
