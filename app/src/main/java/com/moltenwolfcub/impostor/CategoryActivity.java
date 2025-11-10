package com.moltenwolfcub.impostor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryActivity extends AppCompatActivity {
    Category category;

    TextView categoryName;

    private RecyclerView wordRecycler;
    private WordAdapter wordAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_details);

        category = getIntent().getParcelableExtra("category");

        categoryName = findViewById(R.id.categoryTitle);
        categoryName.setText(category.getName());

        wordRecycler = findViewById(R.id.wordRecyclerView);
        wordAdapter = new WordAdapter(category.words);
        wordRecycler.setLayoutManager(new LinearLayoutManager(this));
        wordRecycler.setAdapter(wordAdapter);
    }

    @Override
    public void onBackPressed() {
        Intent result = new Intent();
        result.putExtra("updatedCategory", category);
        setResult(RESULT_OK, result);
        super.onBackPressed();
    }
}
