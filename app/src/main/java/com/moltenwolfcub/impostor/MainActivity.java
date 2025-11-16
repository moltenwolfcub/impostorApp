package com.moltenwolfcub.impostor;

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
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private Random rand;


    private RecyclerView nameRecycler;
    private NameAdapter nameAdapter;

    private RecyclerView categoryRecycler;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;

    private Button addCategoryButton;

    private Button addPlayerButton;
    private LinearLayout addPlayerRow;
    private EditText nameInput;
    private Button submitButton;

    private EditText activeEditNameInput;
    private View activeEditNameRow;
    private View activeDisplayNameRow;

    private Button startGameButton;

    private final ActivityResultLauncher<Intent> categoryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Category updatedCategory = result.getData().getParcelableExtra("updatedCategory");
                    if (updatedCategory != null) {
                        for (int i = 0; i < categoryList.size(); i++) {
                            if (categoryList.get(i).id.equals(updatedCategory.id)) {
                                categoryList.set(i, updatedCategory);
                                categoryAdapter.notifyItemChanged(i);

                                CategoryStoreHelper.writeCategories(this, CategoryStoreHelper.toProto(categoryList));

                                break;
                            }
                        }
                    }
                } else if (result.getResultCode() == RESULT_FIRST_USER && result.getData() != null) {
                    Category deletedCategory = result.getData().getParcelableExtra("deletedCategory");
                    if (deletedCategory.id != null) {
                        for (int i = 0; i < categoryList.size(); i++) {
                            if (categoryList.get(i).id.equals(deletedCategory.id)) {
                                categoryList.remove(i);
                                categoryAdapter.notifyItemRemoved(i);

                                CategoryStoreHelper.writeCategories(this, CategoryStoreHelper.toProto(categoryList));

                                break;
                            }
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SessionInfo session = getIntent().getParcelableExtra("session");
        if (session == null) {
            session = new SessionInfo(this);
        }
        categoryList = session.categories;

        rand = new Random();

        addPlayerButton = findViewById(R.id.addPlayerButton);
        addPlayerRow = findViewById(R.id.addNameRow);
        nameInput = findViewById(R.id.addNameInput);
        submitButton = findViewById(R.id.addNameSubmitButton);
        nameRecycler = findViewById(R.id.nameRecyclerView);
        categoryRecycler = findViewById(R.id.categoryRecyclerView);
        addCategoryButton = findViewById(R.id.addCategoryButton);
        startGameButton = findViewById(R.id.startGameButton);

        nameAdapter = new NameAdapter(session.currentPlayers);
        nameAdapter.setEditListener((editText, editRow, displayRow) -> {
            activeEditNameInput = editText;
            activeEditNameRow = editRow;
            activeDisplayNameRow = displayRow;
        });
        nameRecycler.setLayoutManager(new LinearLayoutManager(this));
        nameRecycler.setAdapter(nameAdapter);

        categoryAdapter = new CategoryAdapter(categoryList, categoryLauncher);
        categoryRecycler.setLayoutManager(new LinearLayoutManager(this));
        categoryRecycler.setAdapter(categoryAdapter);

        nameInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                submitButton.performClick();
                return true;
            }
            return false;
        });

        addPlayerButton.setOnClickListener(view -> {
            addPlayerButton.setVisibility(View.GONE);
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
                nameAdapter.addName(new NameItem(name));
                nameRecycler.scrollToPosition(nameAdapter.getItemCount() - 1);
                nameInput.setText("");
            }
        });

        addCategoryButton.setOnClickListener(View -> {
            Category newCategory = new Category("New Category");

            categoryList.add(newCategory);
            categoryAdapter.notifyItemInserted(categoryList.size()-1);

            CategoryStoreHelper.writeCategories(this, CategoryStoreHelper.toProto(categoryList));

            Intent intent = new Intent(this, CategoryActivity.class);
            intent.putExtra("category", newCategory);
            intent.putExtra("isNew", true);
            categoryLauncher.launch(intent);
        });

        SessionInfo finalSession = session;
        startGameButton.setOnClickListener(view -> {
            List<Player> players = new ArrayList<>();
            for (int i = 0; i < finalSession.currentPlayers.size(); i++) {
                NameItem name = finalSession.currentPlayers.get(i);

                players.add(new Player(name));
            }

            if (!players.isEmpty()) {
                Player imposter = players.get(rand.nextInt(players.size()));
                imposter.isImposter = true;

                Player startingPlayer = players.get(rand.nextInt(players.size()));

                List<Category> activeCategories = new ArrayList<>();
                for (int i = 0; i < categoryList.size(); i++) {
                    Category currentCategory = categoryList.get(i);
                    if (currentCategory.enabled) {
                        activeCategories.add(currentCategory);
                    }
                }
                if (activeCategories.isEmpty()) {
                    activeCategories.addAll(categoryList);
                }
                List<String> enabledWords = new ArrayList<>();
                for (int i = 0; i < activeCategories.size(); i++) {
                    Category currentCategory = activeCategories.get(i);
                    enabledWords.addAll(currentCategory.words);
                }

                if (enabledWords.isEmpty()) {
                    //TODO some kind of feedback if no words are available
                    return;
                }

                String word = enabledWords.get(rand.nextInt(enabledWords.size()));

                Intent intent = new Intent(this, GameActivity.class);
                intent.putExtra("game", new Game(players, word, startingPlayer, imposter));
                intent.putExtra("session", finalSession);
                startActivity(intent);
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
                    addPlayerButton.setVisibility(View.VISIBLE);

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