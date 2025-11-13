package com.moltenwolfcub.impostor;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {
    public Game game;

    private TextView playerName;
    private Button nextButton;

    private TextView imposterLabel;
    private TextView wordDisplay;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        playerName = findViewById(R.id.playerName);
        nextButton = findViewById(R.id.nextButton);
        imposterLabel = findViewById(R.id.imposterLabel);
        wordDisplay = findViewById(R.id.wordDisplay);

        game = getIntent().getParcelableExtra("game");
        
        updateUI();

        nextButton.setOnClickListener(view -> {
            if (game.hasNextPlayer()) {
                game.nextPlayer();
                updateUI();
            } else {
                //TODO names given out
                finish();
            }
        });
    }

    private void updateUI() {
        Player current = game.getCurrentPlayer();
        playerName.setText(current.name.getName());

        if (current.isImposter) {
            imposterLabel.setText("Imposter");
            wordDisplay.setText("You don't get to know the word");
        } else {
            imposterLabel.setText("Not imposter");
            wordDisplay.setText(game.secretWord);
        }
    }
}
