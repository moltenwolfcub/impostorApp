package com.moltenwolfcub.impostor;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class Game implements Parcelable {
    private final List<Player> players;
    public final Player staringPlayer;
    public final Player imposter;
    private int playerIndex = 0;
    public final Word secretWord;

    public Game(List<Player> players, Word secretWord, Player staringPlayer, Player imposter) {
        this.players = players;
        this.secretWord = secretWord;
        this.staringPlayer = staringPlayer;
        this.imposter = imposter;
    }

    protected Game(Parcel in) {
        playerIndex = in.readInt();
        secretWord = in.readParcelable(Word.class.getClassLoader());
        staringPlayer = in.readParcelable(Player.class.getClassLoader());
        imposter = in.readParcelable(Player.class.getClassLoader());
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
        dest.writeParcelable(secretWord, flags);
        dest.writeParcelable(staringPlayer, flags);
        dest.writeParcelable(imposter, flags);
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
