package com.moltenwolfcub.impostor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NameAdaptor adapter;
    private List<NameItem> nameList = new ArrayList<>();

    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.nameRecyclerView);
        addButton = findViewById(R.id.addPlayerButton);

        nameList.add(new NameItem("Oliver"));
        nameList.add(new NameItem("Jude"));
        nameList.add(new NameItem("Rhian"));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NameAdaptor(nameList);
        recyclerView.setAdapter(adapter);

        addButton.setOnClickListener(view -> addNameHandler());
    }

    private void addNameHandler() {
        EditText input = new EditText(this);
        input.setHint("Enter player name");

        new AlertDialog.Builder(this)
                .setTitle("Add Player")
                .setView(input)
                .setPositiveButton("Add", (dialog, which) -> {
                    String name = input.getText().toString().trim();
                    if (!name.isEmpty()) {
                        adapter.addName(new NameItem(name));
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }
}