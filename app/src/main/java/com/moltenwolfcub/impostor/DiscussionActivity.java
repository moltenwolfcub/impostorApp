package com.moltenwolfcub.impostor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DiscussionActivity extends AppCompatActivity {
    private Game game;

    private TextView startingPlayer;
    private Button finishGame;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);

        game = getIntent().getParcelableExtra("game");

        startingPlayer = findViewById(R.id.startingPlayer);
        finishGame = findViewById(R.id.finishGame);

        String startingPlayerText = game.staringPlayer.name.getName() + " " + getString(R.string.text_discussionPlayer);
        startingPlayer.setText(startingPlayerText);

        finishGame.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
}
