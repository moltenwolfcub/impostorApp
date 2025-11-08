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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.nameRecyclerView);
        addButton = findViewById(R.id.addPlayerButton);
        addPlayerRow = findViewById(R.id.addPlayerRow);
        nameInput = findViewById(R.id.nameInput);
        submitButton = findViewById(R.id.submitButton);

        nameList.add(new NameItem("Oliver"));
        nameList.add(new NameItem("Jude"));
        nameList.add(new NameItem("Rhian"));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NameAdaptor(nameList);
        recyclerView.setAdapter(adapter);

        nameInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                this.submitName(v);
                return true;
            }
            return false;
        });

        addButton.setOnClickListener(view -> {
            addButton.setVisibility(View.GONE);
            addPlayerRow.setVisibility(View.VISIBLE);
            nameInput.requestFocus();
        });

        submitButton.setOnClickListener(this::submitName);
    }

    private void submitName(View v) {
        String name = nameInput.getText().toString().trim();
        if (!name.isEmpty()) {
            adapter.addName(new NameItem(name));
            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            nameInput.setText("");
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && nameInput.hasFocus()) {
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

        return super.dispatchTouchEvent(event);
    }
}