package com.example.bookmybook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookmybook.Book;
import com.example.bookmybook.R;
import com.example.bookmybook.adapter.BookAdapter;
import com.example.bookmybook.others.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ShowBooksActivity extends AppCompatActivity {
    private ImageView ivBackShowBooks;
    private TextView tvBackShowBooks;
    private TextView tvNoData;
    private String selectedCourse;
    private String selectedSem;
    private RecyclerView rvShowBooks;
    private ArrayList<Book> bookList = new ArrayList<Book>();

    private FirebaseFirestore firebaseFirestore;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_books);
        bindView();

        Intent intent = getIntent();
        selectedCourse = intent.getStringExtra("selectedCourse");
        selectedSem = intent.getStringExtra("selectedSem");
        tvBackShowBooks.setText(selectedSem);
        tvNoData.setVisibility(View.VISIBLE);
        firebaseFirestore = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(ShowBooksActivity.this);

        getBooks();

        Log.e("ewiufdhvcjxbm", "onCreate: " + "opening sem view");
        ivBackShowBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getBooks() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.e("fjsgdxdv", "getBooks: " + "1");
        CollectionReference collectionRef = db.collection("Books").document(selectedCourse).collection(selectedSem);
        Log.e("fjdv", "getBooks: " + "2");
        collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String bookDocumentId = document.getId();
//                        String bookIdPerUser = document.getData().get("bookIdPerUser").toString();
                        String bookCourse = document.getData().get("bookCourse").toString();
                        String bookDesc = document.getData().get("bookDesc").toString();
                        String bookImgUri = document.getData().get("bookImgUri").toString();
                        String bookPrice = document.getData().get("bookPrice").toString();
                        String bookSem = document.getData().get("bookSem").toString();
                        String bookSubName = document.getData().get("bookSubName").toString();
                        String bookOwnerName = document.getData().get("ownerName").toString();
                        String bookOwnerMail = document.getData().get("ownerMail").toString();

                        Book book = new Book(bookDocumentId, null, bookCourse, bookDesc, bookImgUri, bookPrice, bookSem, bookSubName, bookOwnerName, bookOwnerMail);
                        bookList.add(book);
                        Log.e("wdhaskn", "onComplete: " + bookList.size() + "--" + book.bookCourse + "--" + book.bookImgUri);
                    }
                    if (bookList.size() > 0) {
                        tvNoData.setVisibility(View.GONE);
                        setAdapter();
                    } else {
                        tvNoData.setVisibility(View.VISIBLE);
                    }
                } else {
                    // Handle errors here
                    Log.e("hdjsn", "onComplete: " + "failed");
                }
            }
        });
    }

    private void setAdapter() {
        Log.e("eoifukdx", "setAdapter: " + bookList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowBooksActivity.this, LinearLayoutManager.VERTICAL, false);
        rvShowBooks.setLayoutManager(linearLayoutManager);
        BookAdapter bookAdapter = new BookAdapter(bookList, ShowBooksActivity.this, preferenceManager.getUserFireBaseId(ShowBooksActivity.this), false);
        rvShowBooks.setAdapter(bookAdapter);
    }

    private void bindView() {
        ivBackShowBooks = findViewById(R.id.ivBackShowBooks);
        tvBackShowBooks = findViewById(R.id.tvBackShowBooks);
        tvNoData = findViewById(R.id.tvNoData);
        rvShowBooks = findViewById(R.id.rvShowBooks);
    }
}