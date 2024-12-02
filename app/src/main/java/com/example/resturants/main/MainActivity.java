package com.example.resturants.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resturants.R;
import com.example.resturants.main.RecipeAdapter;
import com.example.resturants.model.Recipe;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private RecyclerView recipeRecyclerView;
    private RecipeAdapter recipeAdapter;
    private List<Recipe> recipeList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configurer la Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CardView orderHistoryButton = findViewById(R.id.order_history_button);
        orderHistoryButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OrderHistoryActivity.class);
            startActivity(intent);
        });

        // Configurer le DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Configurer le RecyclerView
        recipeRecyclerView = findViewById(R.id.recipeRecyclerView);
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeList = new ArrayList<>();
        recipeAdapter = new RecipeAdapter(this, recipeList, this::onRecipeClick);
        recipeRecyclerView.setAdapter(recipeAdapter);

        // Charger les recettes par défaut (par exemple : "Drinks")
        loadRecipes("Drinks");

        // CardView to open map for a specific location
        CardView margondaButton = findViewById(R.id.margonda_button);
        margondaButton.setOnClickListener(v -> openMap(-6.368282, 106.827183, "Restaurant L'Oliver Nabeul"));
    }

    private void loadRecipes(String category) {
        databaseReference = FirebaseDatabase.getInstance().getReference("repices").child(category);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recipeList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Recipe recipe = dataSnapshot.getValue(Recipe.class);
                    if (recipe != null) {
                        recipeList.add(recipe);
                    }
                }
                recipeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.menu_drinks) {
            loadRecipes("Drinks");
            Toast.makeText(this, "Drinks selected", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.menu_salades) {
            loadRecipes("Salads");
            Toast.makeText(this, "Salads selected", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.menu_desserts) {
            loadRecipes("Desserts");
            Toast.makeText(this, "Desserts selected", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.menu_Appetizers) {
            loadRecipes("Appetizers");
            Toast.makeText(this, "Appetizers selected", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.menu_snacks) {
            loadRecipes("Snacks");
            Toast.makeText(this, "Snacks selected", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.menu_Main_Dishes) {
            loadRecipes("Main Dishes");
            Toast.makeText(this, "Main Dishes selected", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Unknown selection", Toast.LENGTH_SHORT).show();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    // Gérer le clic sur une carte de recette
    private void onRecipeClick(Recipe recipe) {
        Intent intent = new Intent(this, OrderActivity.class);
        intent.putExtra("imageUrl", recipe.getImage());
        intent.putExtra("name", recipe.getTitle());
        intent.putExtra("price", recipe.getPrix());
        Log.d("MainActivity", "Sending data - Image: " + recipe.getImage() + ", Name: " + recipe.getTitle() + ", Price: " + recipe.getPrix());
        startActivity(intent);
    }

    private void openMap(double latitude, double longitude, String label) {
        Uri geoLocation = Uri.parse("geo:" + latitude + "," + longitude + "?q=" + Uri.encode(label));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, geoLocation);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Toast.makeText(this, "Google Maps n'est pas installé.", Toast.LENGTH_SHORT).show();
        }
    }
}
