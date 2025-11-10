package com.moltenwolfcub.impostor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    final List<Category> categoryList;

    public CategoryAdapter(List<Category> categoryList) {
        this.categoryList = categoryList;
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

        holder.name.setOnClickListener(v -> {
            Context ctx = v.getContext();
            Intent intent = new Intent(ctx, CategoryActivity.class);
            intent.putExtra("category", categoryItem);
            if (ctx instanceof MainActivity) {
                ((MainActivity) ctx).categoryLauncher.launch(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.categoryList.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        final TextView name;

        public CategoryViewHolder(@NonNull View view) {
            super(view);

            this.name = view.findViewById(R.id.categoryItemView);
        }
    }
}
