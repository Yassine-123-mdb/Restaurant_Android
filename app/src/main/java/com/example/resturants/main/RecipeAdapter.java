package com.example.resturants.main;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.resturants.R;
import com.example.resturants.model.Recipe;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private Context context;
    private List<Recipe> recipeList;
    private OnRecipeClickListener listener;
    // Constructor
    public RecipeAdapter(Context context, List<Recipe> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
    }

    public RecipeAdapter(Context context, List<Recipe> recipeList, OnRecipeClickListener listener) {
        this.context = context;
        this.recipeList = recipeList;
        this.listener = listener;
    }


    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);

        // Set recipe data to views
        holder.title.setText(recipe.getTitle());
        holder.difficulty.setText(recipe.getDifficulty());
        holder.price.setText("$" + recipe.getPrix());


        // Load image using Glide
        Glide.with(context)
                .load(recipe.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache images for performance
                .into(holder.image);

        // Add click listener for item
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderActivity.class);
            intent.putExtra("imageUrl", recipe.getImage());
            intent.putExtra("name", recipe.getTitle());
            if (recipe.getPrix() != null) {
                intent.putExtra("price", recipe.getPrix());
            } else {
                intent.putExtra("price", 0.0); // Valeur par défaut si prix est null
            }
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    // ViewHolder class
    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, difficulty, price;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize views
            image = itemView.findViewById(R.id.recipeImage);
            title = itemView.findViewById(R.id.recipeTitle);
            difficulty = itemView.findViewById(R.id.recipeDifficulty);
            price = itemView.findViewById(R.id.recipePrix); // View for displaying price
        }
    }
}
