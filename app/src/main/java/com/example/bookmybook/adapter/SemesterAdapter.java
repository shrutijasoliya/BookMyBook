package com.example.bookmybook.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookmybook.R;
import com.example.bookmybook.activity.ShowBooksActivity;

public class SemesterAdapter extends RecyclerView.Adapter<SemesterAdapter.MyViewData> {
    private final String[] allSemNames;
    private Context activity;
    private String selectedCourse;

    public SemesterAdapter(String[] allSemNames, String selectedCourse, Context activity) {
        this.allSemNames = allSemNames;
        this.activity = activity;
        this.selectedCourse = selectedCourse;
    }

    @NonNull
    @Override
    public MyViewData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SemesterAdapter.MyViewData(LayoutInflater.from(activity).inflate(R.layout.item_rv_sem, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewData holder, @SuppressLint("RecyclerView") int position) {
        holder.tvRVSem.setText(allSemNames[position]);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedSem = allSemNames[position];
                Intent intent = new Intent(activity, ShowBooksActivity.class);
                intent.putExtra("selectedCourse", selectedCourse);
                intent.putExtra("selectedSem", selectedSem);
                Log.e("hefbcm", "onClick: " + selectedCourse + "\n " + selectedSem);
                activity.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return allSemNames.length;
    }

    public class MyViewData extends RecyclerView.ViewHolder {

        private final TextView tvRVSem;

        public MyViewData(@NonNull View itemView) {
            super(itemView);

            tvRVSem = itemView.findViewById(R.id.tvRVSem);
        }
    }
}
