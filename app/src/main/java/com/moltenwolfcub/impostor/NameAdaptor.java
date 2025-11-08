package com.moltenwolfcub.impostor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NameAdaptor extends RecyclerView.Adapter<NameAdaptor.NameViewHolder> {
    List<NameItem> nameList;

    public NameAdaptor(List<NameItem> nameList) {
        this.nameList = nameList;
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

            notifyItemChanged(position);
        });

        holder.deleteButton.setOnClickListener(v -> {
            nameList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, nameList.size());
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

    static class NameViewHolder extends RecyclerView.ViewHolder {
        LinearLayout displayRow;
        LinearLayout editRow;

        TextView name;
        ImageButton editButton;
        ImageButton deleteButton;

        Button submitEditButton;
        EditText editNameInput;

        public NameViewHolder(@NonNull View view) {
            super(view);

            displayRow = view.findViewById(R.id.nameDisplayRow);
            editRow = view.findViewById(R.id.nameEditRow);

            name = view.findViewById(R.id.nameItemView);
            editButton = view.findViewById(R.id.nameEditButton);
            deleteButton = view.findViewById(R.id.nameDeleteButton);

            submitEditButton = view.findViewById(R.id.nameEditSubmitButton);
            editNameInput = view.findViewById(R.id.nameEditInput);
        }
    }
}
