package com.example.smartpantry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.adapter.RecipeAdapter;
import com.example.smartpantry.db.DatabaseHelper;
import com.example.smartpantry.model.PantryItem;
import com.example.smartpantry.model.Recipe;
import com.example.smartpantry.util.IngredientMatcher;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

/**
 * Runs the strict-matching rule (Section 2.3 of the brief) against the
 * user's current pantry and lists only recipes they can make right now,
 * plus an optional "Almost There" list (missing exactly one ingredient).
 */
public class SuggestedRecipesActivity extends AppCompatActivity implements RecipeAdapter.Listener {

    private DatabaseHelper dbHelper;
    private RecipeAdapter suggestedAdapter;
    private RecipeAdapter almostAdapter;
    private TextView emptyView;
    private TextView almostHeader;
    private RecyclerView almostRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);

        dbHelper = DatabaseHelper.getInstance(this);
        emptyView = findViewById(R.id.text_empty_suggestions);
        almostHeader = findViewById(R.id.text_almost_header);
        almostRecycler = findViewById(R.id.recycler_almost);

        RecyclerView suggestedRecycler = findViewById(R.id.recycler_suggested);
        suggestedRecycler.setLayoutManager(new LinearLayoutManager(this));
        suggestedAdapter = new RecipeAdapter(this, "ready to cook");
        suggestedRecycler.setAdapter(suggestedAdapter);

        almostRecycler.setLayoutManager(new LinearLayoutManager(this));
        almostAdapter = new RecipeAdapter(this, "missing 1 ingredient");
        almostRecycler.setAdapter(almostAdapter);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setSelectedItemId(R.id.nav_suggested);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_suggested) {
                return true;
            } else if (id == R.id.nav_pantry) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_settings) {
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        runMatching();
    }

    private void runMatching() {
        List<PantryItem> pantry = dbHelper.getAllPantryItems();
        List<Recipe> allRecipes = dbHelper.getAllRecipes();

        List<Recipe> strictMatches = IngredientMatcher.getStrictMatches(allRecipes, pantry);
        List<Recipe> almostMatches = IngredientMatcher.getAlmostThereMatches(allRecipes, pantry);

        suggestedAdapter.setRecipes(strictMatches);
        emptyView.setVisibility(strictMatches.isEmpty() ? View.VISIBLE : View.GONE);

        if (almostMatches.isEmpty()) {
            almostHeader.setVisibility(View.GONE);
            almostRecycler.setVisibility(View.GONE);
        } else {
            almostHeader.setVisibility(View.VISIBLE);
            almostRecycler.setVisibility(View.VISIBLE);
            almostAdapter.setRecipes(almostMatches);
        }
    }

    @Override
    public void onRecipeClicked(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, recipe.getId());
        startActivity(intent);
    }
}
