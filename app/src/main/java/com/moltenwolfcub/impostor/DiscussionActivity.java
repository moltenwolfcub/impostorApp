package com.moltenwolfcub.impostor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DiscussionActivity extends AppCompatActivity {
    private Game game;
    private SessionInfo session;

    private TextView startingPlayer;
    private View revealHolder;
    private View revealedData;
    private TextView revealImposter;
    private TextView revealWord;
    private View revealText;
    private Button finishGame;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);

        game = getIntent().getParcelableExtra("game");
        session = getIntent().getParcelableExtra("session");

        startingPlayer = findViewById(R.id.startingPlayer);
        revealHolder = findViewById(R.id.revealHolder);
        revealedData = findViewById(R.id.revealLayout);
        revealText = findViewById(R.id.revealText);
        revealImposter = findViewById(R.id.revealImposter);
        revealWord = findViewById(R.id.revealSecretWord);
        finishGame = findViewById(R.id.finishGame);

        String startingPlayerText = game.staringPlayer.name.getName() + " " + getString(R.string.text_discussionPlayer);
        startingPlayer.setText(startingPlayerText);

        revealImposter.setText(game.imposter.name.getName());
        revealWord.setText(game.secretWord);

        revealHolder.setOnClickListener(v -> {
            if (revealedData.getVisibility() == View.GONE) {
                revealedData.setVisibility(View.VISIBLE);
                revealText.setVisibility(View.GONE);
            }
        });

        finishGame.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("session", session);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        finishGame.performClick();
    }
}
