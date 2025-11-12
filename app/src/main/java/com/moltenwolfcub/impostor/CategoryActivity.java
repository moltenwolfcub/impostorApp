package com.moltenwolfcub.impostor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryActivity extends AppCompatActivity {
    Category category;

    TextView categoryName;

    private Button addWordButton;
    private LinearLayout addWordRow;
    private EditText wordInput;
    private Button submitWordButton;

    private RecyclerView wordRecycler;
    private WordAdapter wordAdapter;

    private LinearLayout titleDisplayRow;
    private LinearLayout titleEditRow;

    private ImageButton titleEditButton;

    private Button submitTitleButton;
    private EditText editTitleInput;

    private EditText activeEditWordInput;
    private View activeEditWordRow;
    private View activeDisplayWordRow;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_details);

        category = getIntent().getParcelableExtra("category");

        categoryName = findViewById(R.id.categoryTitle);
        addWordButton = findViewById(R.id.addWordButton);
        addWordRow = findViewById(R.id.addWordRow);
        wordInput = findViewById(R.id.addWordInput);
        submitWordButton = findViewById(R.id.addWordSubmitButton);
        titleDisplayRow = findViewById(R.id.titleDisplayRow);
        titleEditRow = findViewById(R.id.titleEditRow);
        titleEditButton = findViewById(R.id.categoryEditButton);
        submitTitleButton = findViewById(R.id.categoryEditSubmitButton);
        editTitleInput = findViewById(R.id.categoryEditInput);


        categoryName.setText(category.getName());
        editTitleInput.setText(category.getName());

        titleEditButton.setOnClickListener(v -> {
            titleDisplayRow.setVisibility(View.GONE);
            titleEditRow.setVisibility(View.VISIBLE);
            editTitleInput.requestFocus();

            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (manager != null) {
                manager.showSoftInput(editTitleInput, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        submitTitleButton.setOnClickListener(v -> {
            String newTitle = editTitleInput.getText().toString().trim();
            if (!newTitle.isEmpty()) {
                category.setName(newTitle);
                categoryName.setText(newTitle);
            }

            titleDisplayRow.setVisibility(View.VISIBLE);
            titleEditRow.setVisibility(View.GONE);

            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (manager != null) {
                manager.hideSoftInputFromWindow(editTitleInput.getWindowToken(), 0);
            }
        });

        editTitleInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                submitTitleButton.performClick();
                return true;
            }
            return false;
        });

        wordRecycler = findViewById(R.id.wordRecyclerView);
        wordAdapter = new WordAdapter(category.words);
        wordAdapter.setEditListener((editText, editRow, displayRow) -> {
            activeEditWordInput = editText;
            activeEditWordRow = editRow;
            activeDisplayWordRow = displayRow;
        });
        wordRecycler.setLayoutManager(new LinearLayoutManager(this));
        wordRecycler.setAdapter(wordAdapter);


        wordInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                submitWordButton.performClick();
                return true;
            }
            return false;
        });

        addWordButton.setOnClickListener(view -> {
            addWordButton.setVisibility(View.GONE);
            addWordRow.setVisibility(View.VISIBLE);
            wordInput.requestFocus();

            InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (manager != null) {
                manager.showSoftInput(wordInput, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        submitWordButton.setOnClickListener(view -> {
            String word = wordInput.getText().toString().trim();
            if (!word.isEmpty()) {
                wordAdapter.addWord(word);
                wordRecycler.scrollToPosition(wordAdapter.getItemCount() - 1);
                wordInput.setText("");
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent result = new Intent();
        result.putExtra("updatedCategory", category);
        setResult(RESULT_OK, result);
        super.onBackPressed();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (wordInput.hasFocus()) {
                Rect outRect = new Rect();
                addWordRow.getGlobalVisibleRect(outRect);

                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    wordInput.clearFocus();
                    addWordRow.setVisibility(View.GONE);
                    addWordButton.setVisibility(View.VISIBLE);

                    InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (manager != null) {
                        manager.hideSoftInputFromWindow(wordInput.getWindowToken(), 0);
                    }
                }
            }
            if (editTitleInput.hasFocus()) {
                Rect outRect = new Rect();
                titleEditRow.getGlobalVisibleRect(outRect);

                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    editTitleInput.clearFocus();
                    titleEditRow.setVisibility(View.GONE);
                    titleDisplayRow.setVisibility(View.VISIBLE);

                    InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (manager != null) {
                        manager.hideSoftInputFromWindow(editTitleInput.getWindowToken(), 0);
                    }
                }
            }
            if (activeEditWordInput != null && activeEditWordInput.hasFocus()) {
                Rect outRect = new Rect();
                activeEditWordRow.getGlobalVisibleRect(outRect);

                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    activeEditWordInput.clearFocus();
                    activeEditWordRow.setVisibility(View.GONE);
                    activeDisplayWordRow.setVisibility(View.VISIBLE);

                    InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (manager != null) {
                        manager.hideSoftInputFromWindow(activeEditWordInput.getWindowToken(), 0);
                    }

                    activeDisplayWordRow = null;
                    activeEditWordRow = null;
                    activeEditWordInput = null;
                }
            }
        }

        return super.dispatchTouchEvent(event);
    }
}
