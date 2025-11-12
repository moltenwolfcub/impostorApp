package com.moltenwolfcub.impostor;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    final List<Category> categoryList;
    private ActivityResultLauncher<Intent> launcher;

    public CategoryAdapter(List<Category> categoryList, ActivityResultLauncher<Intent> launcher) {
        this.categoryList = categoryList;
        this.launcher = launcher;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoryAdapter.CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category categoryItem = categoryList.get(position);

        holder.name.setText(categoryItem.getName());
        if (categoryItem.enabled) {
            holder.enableButton.setImageResource(R.drawable.icon_add_circle_24px);
        } else {
            holder.enableButton.setImageResource(R.drawable.icon_add_circle_24px_empty);
        }

        holder.name.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), CategoryActivity.class);
            intent.putExtra("category", categoryItem);
            launcher.launch(intent);
        });

        holder.enableButton.setOnClickListener(v -> {
            categoryItem.enabled = !categoryItem.enabled;

            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;
            notifyItemChanged(pos);
        });
    }

    @Override
    public int getItemCount() {
        return this.categoryList.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final ImageButton enableButton;

        public CategoryViewHolder(@NonNull View view) {
            super(view);

            this.name = view.findViewById(R.id.categoryItemView);
            this.enableButton = view.findViewById(R.id.categoryEnableButton);
        }
    }
}
