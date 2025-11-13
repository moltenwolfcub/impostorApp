package com.moltenwolfcub.impostor;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {
    public Game game;

    private TextView playerNameFront;
    private TextView playerNameBack;
    private Button nextButton;
    private TextView secretWordDisplay;

    private FrameLayout cardContainer;
    private View frontCard;
    private View backCard;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        playerNameFront = findViewById(R.id.playerNameFront);
        playerNameBack = findViewById(R.id.playerNameBack);
        nextButton = findViewById(R.id.nextButton);
        secretWordDisplay = findViewById(R.id.secretWord);

        cardContainer = findViewById(R.id.cardContainer);
        frontCard = findViewById(R.id.frontCard);
        backCard = findViewById(R.id.backCard);

        game = getIntent().getParcelableExtra("game");
        
        updateUI();

        nextButton.setOnClickListener(view -> {
            if (game.hasNextPlayer()) {
                game.nextPlayer();
                backCard.setVisibility(View.GONE);
                frontCard.setVisibility(View.VISIBLE);
                updateUI();
            } else {
                //TODO names given out
                finish();
            }
        });

        cardContainer.setOnTouchListener((view, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    frontCard.setVisibility(View.GONE);
                    backCard.setVisibility(View.VISIBLE);
                    return true;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    backCard.setVisibility(View.GONE);
                    frontCard.setVisibility(View.VISIBLE);
                    return true;
            }
            return false;
        });
    }

    private void updateUI() {
        Player current = game.getCurrentPlayer();
        playerNameFront.setText(current.name.getName());
        playerNameBack.setText(current.name.getName());

        if (current.isImposter) {
            secretWordDisplay.setText(R.string.text_imposterInfo);
        } else {
            secretWordDisplay.setText(game.secretWord);
        }
    }
}
