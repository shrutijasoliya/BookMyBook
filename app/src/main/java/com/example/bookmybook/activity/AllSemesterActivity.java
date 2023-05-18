package com.example.bookmybook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookmybook.R;
import com.example.bookmybook.adapter.SemesterAdapter;

public class AllSemesterActivity extends AppCompatActivity {

    private String[] allSemNames;
    private RecyclerView rvAllSem;
    private ImageView ivBackAllSem;
    private TextView tvBackAllSem;
    private String selectedCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_semester);
        bindView();

        Intent intent = getIntent();
        selectedCourse = intent.getStringExtra("selectedCourse");
        tvBackAllSem.setText(selectedCourse);
        allSemNames = new String[]{getResources().getString(R.string.sem_1), getResources().getString(R.string.sem_2),
                getResources().getString(R.string.sem_3), getResources().getString(R.string.sem_4),
                getResources().getString(R.string.sem_5), getResources().getString(R.string.sem_6),
                getResources().getString(R.string.sem_7), getResources().getString(R.string.sem_8)};

        GridLayoutManager gridLayoutManager = new GridLayoutManager(AllSemesterActivity.this, 2, RecyclerView.VERTICAL, false);
        rvAllSem.setLayoutManager(gridLayoutManager);
        SemesterAdapter semesterAdapter = new SemesterAdapter(allSemNames, selectedCourse, AllSemesterActivity.this);
        rvAllSem.setAdapter(semesterAdapter);

        ivBackAllSem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void bindView() {
        rvAllSem = findViewById(R.id.rvAllSem);
        ivBackAllSem = findViewById(R.id.ivBackAllSem);
        tvBackAllSem = findViewById(R.id.tvBackAllSem);

    }
}