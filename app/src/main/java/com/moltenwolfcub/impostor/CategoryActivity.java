package com.moltenwolfcub.impostor;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CategoryActivity extends AppCompatActivity {
    Category category;

    TextView categoryName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_details);

        category = getIntent().getParcelableExtra("category");

        categoryName = findViewById(R.id.categoryTitle);
        categoryName.setText(category.getName());
    }
}
