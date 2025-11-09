package com.moltenwolfcub.impostor;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NameAdaptor adapter;
    private final List<NameItem> nameList = new ArrayList<>();

    private Button addButton;
    private LinearLayout addPlayerRow;
    private EditText nameInput;
    private Button submitButton;

    private EditText activeEditNameInput;
    private View activeEditNameRow;
    private View activeDisplayNameRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.nameRecyclerView);
        addButton = findViewById(R.id.addPlayerButton);
        addPlayerRow = findViewById(R.id.addNameRow);
        nameInput = findViewById(R.id.addNameInput);
        submitButton = findViewById(R.id.addNameSubmitButton);

        nameList.add(new NameItem("Oliver"));
        nameList.add(new NameItem("Jude"));
        nameList.add(new NameItem("Rhian"));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NameAdaptor(nameList);
        adapter.setEditListener((editText, editRow, displayRow) -> {
            activeEditNameInput = editText;
            activeEditNameRow = editRow;
            activeDisplayNameRow = displayRow;
        });
        recyclerView.setAdapter(adapter);

        nameInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                submitButton.performClick();
                return true;
            }
            return false;
        });

        addButton.setOnClickListener(view -> {
            addButton.setVisibility(View.GONE);
            addPlayerRow.setVisibility(View.VISIBLE);
            nameInput.requestFocus();

            InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (manager != null) {
                manager.showSoftInput(nameInput, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        submitButton.setOnClickListener(view -> {
            String name = nameInput.getText().toString().trim();
            if (!name.isEmpty()) {
                adapter.addName(new NameItem(name));
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                nameInput.setText("");
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (nameInput.hasFocus()) {
                Rect outRect = new Rect();
                addPlayerRow.getGlobalVisibleRect(outRect);

                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    nameInput.clearFocus();
                    addPlayerRow.setVisibility(View.GONE);
                    addButton.setVisibility(View.VISIBLE);

                    InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (manager != null) {
                        manager.hideSoftInputFromWindow(nameInput.getWindowToken(), 0);
                    }
                }
            }
            if (activeEditNameInput != null && activeEditNameInput.hasFocus()) {
                Rect outRect = new Rect();
                activeEditNameRow.getGlobalVisibleRect(outRect);

                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    activeEditNameInput.clearFocus();
                    activeEditNameRow.setVisibility(View.GONE);
                    activeDisplayNameRow.setVisibility(View.VISIBLE);

                    InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (manager != null) {
                        manager.hideSoftInputFromWindow(activeEditNameInput.getWindowToken(), 0);
                    }

                    activeDisplayNameRow = null;
                    activeEditNameRow = null;
                    activeEditNameInput = null;
                }
            }
        }

        return super.dispatchTouchEvent(event);
    }
}