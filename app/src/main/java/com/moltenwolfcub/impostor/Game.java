package com.moltenwolfcub.impostor;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class Game implements Parcelable {
    private final List<Player> players;
    private int playerIndex = 0;
    public final String secretWord;

    public Game(List<Player> players, String secretWord) {
        this.players = players;
        this.secretWord = secretWord;
    }

    protected Game(Parcel in) {
        playerIndex = in.readInt();
        secretWord = in.readString();
        players = in.createTypedArrayList(Player.CREATOR);
    }

    public Player getCurrentPlayer() {
        return players.get(playerIndex);
    }

    public boolean hasNextPlayer() {
        return playerIndex < players.size() - 1;
    }

    public Player nextPlayer() {
        if (hasNextPlayer()) {
            playerIndex++;
            return players.get(playerIndex);
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(playerIndex);
        dest.writeString(secretWord);
        dest.writeTypedList(players);
    }


    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };
}
