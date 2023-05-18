package com.example.bookmybook.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookmybook.R;
import com.example.bookmybook.activity.AllSemesterActivity;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.MyViewData> {

    private final int[] imgCourse;
    private final String[] nameCourse;
    private Context activity;

    public CourseAdapter(int[] imgCourse, String[] nameCourse, Context activity) {
        this.imgCourse = imgCourse;
        this.nameCourse = nameCourse;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CourseAdapter.MyViewData(LayoutInflater.from(activity).inflate(R.layout.item_rv_course, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewData holder, @SuppressLint("RecyclerView") int position) {
        holder.tvRVCourse.setText(nameCourse[position]);
        holder.ivRVCourse.setImageResource(imgCourse[position]);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedCourse = nameCourse[position];
                Intent intent = new Intent(activity, AllSemesterActivity.class);
                intent.putExtra("selectedCourse", selectedCourse);
//                Toast.makeText(activity, "Selected course : " + nameCourse[position], Toast.LENGTH_SHORT).show();
                activity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return imgCourse.length;
    }


    class MyViewData extends RecyclerView.ViewHolder {
        private final ImageView ivRVCourse;
        private final TextView tvRVCourse;

        public MyViewData(@NonNull View itemView) {
            super(itemView);

            ivRVCourse = itemView.findViewById(R.id.ivRVCourse);
            tvRVCourse = itemView.findViewById(R.id.tvRVCourse);
        }
    }
}
