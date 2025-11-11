package com.moltenwolfcub.impostor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder>{
    final List<String> wordList;

    public WordAdapter(List<String> wordList) {
        this.wordList = wordList;
    }

    @NonNull
    @Override
    public WordAdapter.WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_word, parent, false);
        return new WordAdapter.WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordAdapter.WordViewHolder holder, int position) {
        String wordItem = wordList.get(position);

        holder.word.setText(wordItem);

        holder.deleteButton.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;
            wordList.remove(pos);
            notifyItemRemoved(pos);
            notifyItemRangeChanged(pos, wordList.size());
        });
    }

    @Override
    public int getItemCount() {
        return this.wordList.size();
    }

    public void addWord(String word) {
        wordList.add(word);
        notifyItemInserted(wordList.size() - 1);
    }

    static class WordViewHolder extends RecyclerView.ViewHolder {
        final TextView word;
        final ImageButton editButton;
        final ImageButton deleteButton;

        public WordViewHolder(@NonNull View view) {
            super(view);

            this.word = view.findViewById(R.id.wordView);
            this.editButton = view.findViewById(R.id.wordEditButton);
            this.deleteButton = view.findViewById(R.id.wordDeleteButton);
        }
    }
}
