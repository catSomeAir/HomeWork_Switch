package com.example.last_project.news;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.last_project.R;

public class NewsActivity extends AppCompatActivity {
ImageView imgv_news_x;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        imgv_news_x = findViewById(R.id.imgv_news_x);

        imgv_news_x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0,0);
            }
        });
    getSupportFragmentManager().beginTransaction().replace(R.id.container_news, new NewsNotExistFragment()).commit();


    }
}