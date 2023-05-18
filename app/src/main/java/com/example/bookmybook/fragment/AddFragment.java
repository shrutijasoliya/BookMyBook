package com.example.bookmybook.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.bookmybook.R;
import com.example.bookmybook.User;
import com.example.bookmybook.others.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddFragment extends Fragment {

    private MaterialSpinner spinnerSelCourse;
    private MaterialSpinner spinnerSelSem;
    private ImageView ivAddImg;
    private TextView tvAddImg;
    private EditText etSubNameAdd;
    private EditText etBookDesc;
    private EditText etBookPrice;
    private Button btnAddBook;
    private View view;
    private Uri bookImgUri;
    private Uri firebaseBookImgUri;
    private String selectedCourse;
    private String selectedSem;
    private String subName;
    private String bookDesc;
    private String bookPrice;
    private String currentDateTime;
    FirebaseAuth firebaseAuth;
    private User user;
    private FirebaseFirestore firebaseFirestore;
    private String bookId;
    private StorageReference storageReference;
    private PreferenceManager preferenceManager;

    private String ownerName;
    private String ownerMail;

    public AddFragment() {

    }

    public static AddFragment newInstance(String param1, String param2) {
        AddFragment fragment = new AddFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add, container, false);
        bindView();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getContext());
        storageReference = FirebaseStorage.getInstance().getReference();
        getUser();
        ivAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
//                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, GALLERY_PICTURE);
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 1000);
            }
        });

        spinnerSelCourse.setItems("Computer Engineering", "Mechanical Engineering", "Information Technology",
                "Electrical Engineering", "Civil Engineering", "Chemical Engineering",
                "Electronics and Communication", "Textile Engineering", "Aeronotical Engineering");
        spinnerSelCourse.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                spinnerSelCourse.clearFocus();
                selectedCourse = item.toString();
            }
        });

        spinnerSelSem.setItems(getContext().getResources().getString(R.string.sem_1), getContext().getResources().getString(R.string.sem_2),
                getContext().getResources().getString(R.string.sem_3), getContext().getResources().getString(R.string.sem_4),
                getContext().getResources().getString(R.string.sem_5), getContext().getResources().getString(R.string.sem_6),
                getContext().getResources().getString(R.string.sem_7), getContext().getResources().getString(R.string.sem_8));
        spinnerSelSem.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                spinnerSelSem.clearFocus();
                selectedSem = item.toString();
            }
        });

        btnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subName = etSubNameAdd.getText().toString();
                bookDesc = etBookDesc.getText().toString();
                bookPrice = etBookPrice.getText().toString();
                if (TextUtils.isEmpty(selectedCourse)) {
                    spinnerSelCourse.setError("Please select course");
                    spinnerSelCourse.requestFocus();
                }
                if (TextUtils.isEmpty(selectedSem)) {
                    spinnerSelSem.setError("Please select semester");
                    spinnerSelSem.requestFocus();
                }
                if (TextUtils.isEmpty(etSubNameAdd.getText().toString())) {
                    etSubNameAdd.setError("Please add subject name");
                    etSubNameAdd.requestFocus();
                }
                if (TextUtils.isEmpty(etBookDesc.getText().toString())) {
                    etBookDesc.setError("Please add book description");
                    etBookDesc.requestFocus();
                }
                if (TextUtils.isEmpty(etBookPrice.getText().toString())) {
                    etBookPrice.setError("Please add book price");
                    etBookPrice.requestFocus();
                }
                if (bookImgUri != null) {
                    if (bookImgUri.toString().length() > 0 && selectedCourse.length() != 0 && selectedSem.length() != 0
                            && subName.length() != 0 && bookDesc.length() != 0 && bookPrice.length() != 0) {

                        Log.e("uhwdb", "onClick: " + "Course: " + selectedCourse
                                + "\n" + "Semester: " + selectedSem
                                + "\n" + "Sub Name: " + subName
                                + "\n" + "Book Desc: " + bookDesc
                                + "\n" + "Book Price: " + bookPrice);

                        addBookToFirebase();
                    } else {
                        Toast.makeText(getContext(), "Please fill all details", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        return view;
    }

    private void getUser() {
        Log.e("kjdasbf", "getUser: " + preferenceManager.getUserFireBaseId(getContext()));
        DocumentReference docRef = firebaseFirestore.collection("Users").document(preferenceManager.getUserFireBaseId(getContext()));
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("sbssdxcs", "DocumentSnapshot data: " + document.getData());
                        User user = document.toObject(User.class);
                        ownerName = user.getfullName();
                        ownerMail = user.getemail();

//                        Log.e("idnxj", "onComplete: " + tvUserNameNav.getText().toString() + "---" + tvUserEmailIDNav.getText().toString());
                    } else {
                        Log.d("basfdva", "No such document");
                    }
                } else {
                    Log.d("efjScnc", "get failed with ", task.getException());
                }
            }
        });

    }

    private String getCurrentDateTime() {
        String currentDate = new SimpleDateFormat("dd_MM_yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH_mm_ss", Locale.getDefault()).format(new Date());

        String currentDateTime = currentDate + "_" + currentTime;
        Log.e("ehgbvdcnx", "getCurrentDateTime: " + currentDateTime);
        return currentDateTime;
    }

    private void addBookToFirebase() {
        Map<String, Object> book = new HashMap<>();
        book.put("bookImgUri", firebaseBookImgUri.toString());
        book.put("bookCourse", selectedCourse);
        book.put("bookSem", selectedSem);
        book.put("bookSubName", subName);
        book.put("bookDesc", bookDesc);
        book.put("bookPrice", bookPrice);
        book.put("ownerName", ownerName);
        book.put("ownerMail", ownerMail);

        Log.e("eoifdshkcjx", "addBookToFirebase: " + preferenceManager.getUserId(getContext()) + "---" + preferenceManager.getUserFireBaseId(getContext()));

        firebaseFirestore.collection("Books").document(selectedCourse).collection(selectedSem)
                .add(book)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("jdasdsbvh", "DocumentSnapshot added with ID: " + documentReference.getId());
                        bookId = documentReference.getId();
                        Toast.makeText(getContext(), "Book Added Successfully", Toast.LENGTH_SHORT).show();
                        etSubNameAdd.setText("");
                        etBookDesc.setText("");
                        etBookPrice.setText("");
                        firebaseBookImgUri = null;
                        ivAddImg.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_add_img));
                        book.put("bookIdPerUser", documentReference.getId());
                        addBookToFirebasePerUser(book);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("dvhgndgscb", "Error adding document", e);
                    }
                });

    }

    private void addBookToFirebasePerUser(Map<String, Object> book) {
        firebaseFirestore.collection("Users").document(preferenceManager.getUserFireBaseId(getContext())).collection("Books")
                .add(book)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("jdasdsbvh", "DocumentSnapshot added with ID: " + documentReference.getId());

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("dvhgndgscb", "Error adding document", e);
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
//                bookImgUri = data.getData();
//                ivAddImg.setImageURI(bookImgUri);
                bookImgUri = data.getData();
//                civUserProfilePic.setImageURI(imageUri);
                currentDateTime = getCurrentDateTime();
                uploadBookImageToFirebase(bookImgUri, currentDateTime);
                Log.e("dsndfzcxfc", "onActivityResult: " + "bookUri" + bookImgUri);
            }
        }
    }

    private void uploadBookImageToFirebase(Uri bookImgUri, String currentDateTime) {
        StorageReference fileRef = storageReference.child(firebaseAuth.getCurrentUser().getUid() + "_" + currentDateTime + ".jpg");
        fileRef.putFile(bookImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getContext()).load(uri).into(ivAddImg);
                        Toast.makeText(getContext(), "Image updated", Toast.LENGTH_SHORT).show();
                        Log.e("hwkusdjc", "onSuccess: " + uri);
                        firebaseBookImgUri = uri;

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


    //    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == GALLERY_PICTURE && resultCode == RESULT_OK && null != data) {
//            Uri selectedImg = data.getData();
//            String[] filePathColumn = {MediaStore.Images.Media.DATA};
//            ContentResolver resolver = getActivity().getContentResolver();
//
//            Cursor cursor = resolver.query(selectedImg,
//                    filePathColumn, null, null, null);
//            cursor.moveToFirst();
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            String picturePath = cursor.getString(columnIndex);
////            imgcover = (ImageView) findViewById(R.id.newcover_img);
//            ivAddImg.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//            Log.e("1234", "onActivityResult: " + picturePath);
//            cursor.close();
//        }
//    }

    private void bindView() {
        spinnerSelCourse = view.findViewById(R.id.spinnerSelCourse);
        spinnerSelSem = view.findViewById(R.id.spinnerSelSem);
        ivAddImg = view.findViewById(R.id.ivAddImg);
        tvAddImg = view.findViewById(R.id.tvAddImg);
        etSubNameAdd = view.findViewById(R.id.etSubNameAdd);
        etBookDesc = view.findViewById(R.id.etBookDesc);
        etBookPrice = view.findViewById(R.id.etBookPrice);
        btnAddBook = view.findViewById(R.id.btnAddBook);
    }

}
