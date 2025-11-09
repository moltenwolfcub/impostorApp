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

import java.util.Collections;
import java.util.List;

public class NameAdapter extends RecyclerView.Adapter<NameAdapter.NameViewHolder> {
    final List<NameItem> nameList;
    EditListener editListener;

    public NameAdapter(List<NameItem> nameList) {
        this.nameList = nameList;
    }

    public void setEditListener(EditListener listener) {
        this.editListener = listener;
    }

    @NonNull
    @Override
    public NameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.name_item, parent, false);
        return new NameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NameViewHolder holder, int position) {
        NameItem nameItem = nameList.get(position);

        holder.name.setText(nameItem.getName());
        holder.editNameInput.setText(nameItem.getName());

        holder.editButton.setOnClickListener(view -> {
            holder.displayRow.setVisibility(View.GONE);
            holder.editRow.setVisibility(View.VISIBLE);
            holder.editNameInput.requestFocus();

            InputMethodManager manager = (InputMethodManager) holder.itemView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (manager != null) {
                manager.showSoftInput(holder.editNameInput, InputMethodManager.SHOW_IMPLICIT);
            }

            if (editListener != null) {
                editListener.onEditStarted(holder.editNameInput, holder.editRow, holder.displayRow);
            }
        });

        holder.submitEditButton.setOnClickListener(view -> {
            String name = holder.editNameInput.getText().toString().trim();
            if (!name.isEmpty()) {
                nameItem.SetName(name);
            }

            holder.displayRow.setVisibility(View.VISIBLE);
            holder.editRow.setVisibility(View.GONE);

            InputMethodManager manager = (InputMethodManager) holder.itemView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (manager != null) {
                manager.hideSoftInputFromWindow(holder.editNameInput.getWindowToken(), 0);
            }

            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;
            notifyItemChanged(pos);
        });

        holder.editNameInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                holder.submitEditButton.performClick();
                return true;
            }
            return false;
        });

        holder.upButton.setEnabled(true);
        holder.downButton.setEnabled(true);
        holder.upButton.setAlpha(1.0f);
        holder.downButton.setAlpha(1.0f);

        int adapterPosition = holder.getAdapterPosition();
        if (adapterPosition != RecyclerView.NO_POSITION) {
            if (adapterPosition == 0) {
                holder.upButton.setEnabled(false);
                holder.upButton.setAlpha(0.3f);
            }
            if (adapterPosition == nameList.size() - 1) {
                holder.downButton.setEnabled(false);
                holder.downButton.setAlpha(0.3f);
            }
        }

        holder.upButton.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;
            Collections.swap(nameList, pos, pos-1);
            notifyItemMoved(pos, pos-1);

            notifyItemChanged(pos);
            notifyItemChanged(pos-1);
        });
        holder.downButton.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;
            Collections.swap(nameList, pos, pos+1);
            notifyItemMoved(pos, pos+1);

            notifyItemChanged(pos);
            notifyItemChanged(pos+1);
        });

        holder.deleteButton.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;
            nameList.remove(pos);
            notifyItemRemoved(pos);
            notifyItemRangeChanged(pos, nameList.size());
        });
    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }

    public void addName(NameItem name) {
        nameList.add(name);
        notifyItemInserted(nameList.size() - 1);
    }

    public interface EditListener {
        void onEditStarted(EditText editText, View editRow, View displayRow);
    }

    static class NameViewHolder extends RecyclerView.ViewHolder {
        final LinearLayout displayRow;
        final LinearLayout editRow;

        final TextView name;
        final ImageButton editButton;
        final ImageButton upButton;
        final ImageButton downButton;
        final ImageButton deleteButton;

        final Button submitEditButton;
        final EditText editNameInput;

        public NameViewHolder(@NonNull View view) {
            super(view);

            displayRow = view.findViewById(R.id.nameDisplayRow);
            editRow = view.findViewById(R.id.nameEditRow);

            name = view.findViewById(R.id.nameItemView);
            editButton = view.findViewById(R.id.nameEditButton);
            upButton = view.findViewById(R.id.nameUpButton);
            downButton = view.findViewById(R.id.nameDownButton);
            deleteButton = view.findViewById(R.id.nameDeleteButton);

            submitEditButton = view.findViewById(R.id.nameEditSubmitButton);
            editNameInput = view.findViewById(R.id.nameEditInput);
        }
    }
}
