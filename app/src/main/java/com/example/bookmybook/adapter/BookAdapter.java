package com.example.bookmybook.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookmybook.Book;
import com.example.bookmybook.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewData> {
    private ArrayList<Book> bookArrayList;
    private Context activity;
    private String userFirebaseId;
    private Boolean delete;

    public BookAdapter(ArrayList<Book> bookArrayList, Context activity, String userFirebaseId, Boolean delete) {
        this.bookArrayList = bookArrayList;
        this.activity = activity;
        this.userFirebaseId = userFirebaseId;
        this.delete = delete;
    }

    @NonNull
    @Override
    public MyViewData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookAdapter.MyViewData(LayoutInflater.from(activity).inflate(R.layout.item_rv_show_books, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewData holder, @SuppressLint("RecyclerView") int position) {

        Glide.with(activity).load(bookArrayList.get(position).bookImgUri).into(holder.ivBookRVShowBooks);
//        holder.ivBookRVShowBooks.setImageURI(Uri.parse(bookArrayList.get(position).bookImgUri));
        holder.tvRVSubNameShowBook.setText(bookArrayList.get(position).getBookSubName());
        holder.tvRVBookDescShowBook.setText(bookArrayList.get(position).getBookDesc());
        holder.tvRVBookPriceShowBook.setText(bookArrayList.get(position).getBookPrice());
        holder.tvRVOwnerNameShowBook.setText(bookArrayList.get(position).getBookOwnerName());
        holder.tvRVOwnerMailShowBook.setText(bookArrayList.get(position).getBookOwnerMail());

        if (delete) {
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnMessage.setVisibility(View.GONE);
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("ijfkdsdcncx", "onClick: " + bookArrayList.get(position).getBookDocumentId());
                    deleteBookItem(bookArrayList.get(position).getBookDocumentId(), bookArrayList.get(position).getBookCourse(),
                            bookArrayList.get(position).getBookSem(), bookArrayList.get(position).getBookIdPerUser());
                }
            });
        } else {
            holder.btnDelete.setVisibility(View.GONE);
            holder.btnMessage.setVisibility(View.VISIBLE);
            holder.btnMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(activity, "Clicked on message button", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void deleteBookItem(String bookDocumentId, String bookCourse, String bookSem, String bookIdPerUser) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(userFirebaseId).collection("Books").document(bookDocumentId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("dklsc", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("jfkvn", "Error deleting document", e);
                    }
                });

        db.collection("Books").document(bookCourse).collection(bookSem).document(bookIdPerUser)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("dklsc", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("jfkvn", "Error deleting document", e);
                    }
                });


    }

    @Override
    public int getItemCount() {
        return bookArrayList.size();
    }

    public class MyViewData extends RecyclerView.ViewHolder {
        private final ImageView ivBookRVShowBooks;
        private final TextView tvRVSubNameShowBook;
        private final TextView tvRVBookDescShowBook;
        private final TextView tvRVBookPriceShowBook;
        private final TextView tvRVOwnerNameShowBook;
        private final TextView tvRVOwnerMailShowBook;
        private final Button btnMessage;
        private final Button btnDelete;

        public MyViewData(@NonNull View itemView) {
            super(itemView);
            ivBookRVShowBooks = itemView.findViewById(R.id.ivBookRVShowBooks);
            tvRVSubNameShowBook = itemView.findViewById(R.id.tvRVSubNameShowBook);
            tvRVBookDescShowBook = itemView.findViewById(R.id.tvRVBookDescShowBook);
            tvRVBookPriceShowBook = itemView.findViewById(R.id.tvRVBookPriceShowBook);
            tvRVOwnerNameShowBook = itemView.findViewById(R.id.tvRVOwnerNameShowBook);
            tvRVOwnerMailShowBook = itemView.findViewById(R.id.tvRVOwnerMailShowBook);
            btnMessage = itemView.findViewById(R.id.btnMessage);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
