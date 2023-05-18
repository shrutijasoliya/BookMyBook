package com.example.bookmybook.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

public class MyBooksFragment extends Fragment {
    private RecyclerView rvMyBooks;
    private TextView tvNoData;
    private View view;
    private ArrayList<Book> bookList = new ArrayList<Book>();
    private PreferenceManager preferenceManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_my_books, container, false);

        bindView();
        preferenceManager = new PreferenceManager(getContext());
        tvNoData.setVisibility(View.VISIBLE);
        getBooks();
        return view;
    }

    private void getBooks() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("Users").document(preferenceManager.getUserFireBaseId(getContext())).collection("Books");
        collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String bookDocumentId = document.getId();
                        String bookIdPerUser = document.getData().get("bookIdPerUser").toString();
                        String bookCourse = document.getData().get("bookCourse").toString();
                        String bookDesc = document.getData().get("bookDesc").toString();
                        String bookImgUri = document.getData().get("bookImgUri").toString();
                        String bookPrice = document.getData().get("bookPrice").toString();
                        String bookSem = document.getData().get("bookSem").toString();
                        String bookSubName = document.getData().get("bookSubName").toString();
                        String bookOwnerName = document.getData().get("ownerName").toString();
                        String bookOwnerMail = document.getData().get("ownerMail").toString();

                        Book book = new Book(bookDocumentId, bookIdPerUser, bookCourse, bookDesc, bookImgUri, bookPrice, bookSem, bookSubName, bookOwnerName, bookOwnerMail);
                        bookList.add(book);
                        Log.e("wdhasdskn", "onComplete: " + bookList.size() + "--" + book.bookCourse + "--" + book.bookImgUri);
                    }
                    if (bookList.size() > 0) {
                        tvNoData.setVisibility(View.GONE);
                        setAdapter();
                    } else {
                        tvNoData.setVisibility(View.VISIBLE);
                    }
                } else {
                    // Handle errors here
                    Log.e("hdjsaxzsn", "onComplete: " + "failed");
                }
            }
        });
    }

    private void setAdapter() {
        Log.e("eoifudaskdx", "setAdapter: " + bookList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvMyBooks.setLayoutManager(linearLayoutManager);
        BookAdapter bookAdapter = new BookAdapter(bookList, getContext(), preferenceManager.getUserFireBaseId(getContext()), true);
        rvMyBooks.setAdapter(bookAdapter);
    }

    private void bindView() {
        rvMyBooks = view.findViewById(R.id.rvMyBooks);
        tvNoData = view.findViewById(R.id.tvNoData);
    }
}
