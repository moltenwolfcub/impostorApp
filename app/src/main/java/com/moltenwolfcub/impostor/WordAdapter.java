package com.moltenwolfcub.impostor;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder>{
    final List<String> wordList;
    EditListener editListener;

    public WordAdapter(List<String> wordList) {
        this.wordList = wordList;
    }

    public void setEditListener(EditListener listener) {
        this.editListener = listener;
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
        holder.editWordInput.setText(wordItem);

        holder.editButton.setOnClickListener(v -> {
            holder.displayRow.setVisibility(View.GONE);
            holder.editRow.setVisibility(View.VISIBLE);
            holder.editWordInput.requestFocus();

            InputMethodManager manager = (InputMethodManager) holder.itemView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (manager != null) {
                manager.showSoftInput(holder.editWordInput, InputMethodManager.SHOW_IMPLICIT);
            }

            if (editListener != null) {
                editListener.onEditStarted(holder.editWordInput, holder.editRow, holder.displayRow);
            }
        });

        holder.submitEditButton.setOnClickListener(view -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;

            String word = holder.editWordInput.getText().toString().trim();
            if (!word.isEmpty()) {
                wordList.set(pos, word);
            }

            holder.displayRow.setVisibility(View.VISIBLE);
            holder.editRow.setVisibility(View.GONE);

            InputMethodManager manager = (InputMethodManager) holder.itemView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (manager != null) {
                manager.hideSoftInputFromWindow(holder.editWordInput.getWindowToken(), 0);
            }

            notifyItemChanged(pos);
        });

        holder.editWordInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                holder.submitEditButton.performClick();
                return true;
            }
            return false;
        });

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
        final LinearLayout displayRow;
        final LinearLayout editRow;

        final TextView word;
        final ImageButton editButton;
        final ImageButton deleteButton;

        final Button submitEditButton;
        final EditText editWordInput;

        public WordViewHolder(@NonNull View view) {
            super(view);

            displayRow = view.findViewById(R.id.wordDisplayRow);
            editRow = view.findViewById(R.id.wordEditRow);

            word = view.findViewById(R.id.wordView);
            editButton = view.findViewById(R.id.wordEditButton);
            deleteButton = view.findViewById(R.id.wordDeleteButton);

            submitEditButton = view.findViewById(R.id.wordEditSubmitButton);
            editWordInput = view.findViewById(R.id.wordEditInput);
        }
    }
}
