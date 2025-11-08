package com.moltenwolfcub.impostor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
        TextView name;
        ImageButton editButton;
        ImageButton deleteButton;

        public NameViewHolder(@NonNull View view) {
            super(view);

            name = view.findViewById(R.id.nameItemView);
            editButton = view.findViewById(R.id.editButton);
            deleteButton = view.findViewById(R.id.deleteButton);
        }
    }
}
