package com.example.smartpantry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.smartpantry.db.DatabaseHelper;
import com.example.smartpantry.model.PantryItem;
import com.example.smartpantry.model.Recipe;
import com.example.smartpantry.model.RecipeIngredient;
import com.example.smartpantry.util.IngredientMatcher;

import java.text.MessageFormat;
import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "extra_recipe_id";

    private DatabaseHelper dbHelper;
    private long recipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        dbHelper = DatabaseHelper.getInstance(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        recipeId = getIntent().getLongExtra(EXTRA_RECIPE_ID, -1);
        render();
    }

    @Override
    protected void onResume() {
        super.onResume();
        render(); // re-check pantry status in case it changed since this screen opened
    }

    private void render() {
        Recipe recipe = dbHelper.getRecipe(recipeId);
        if (recipe == null) {
            finish();
            return;
        }
        List<PantryItem> pantry = dbHelper.getAllPantryItems();

        TextView title = findViewById(R.id.text_recipe_title);
        title.setText(recipe.getName());

        TextView steps = findViewById(R.id.text_recipe_steps);
        steps.setText(recipe.getSteps());

        LinearLayout ingredientsLayout = findViewById(R.id.layout_ingredients);
        ingredientsLayout.removeAllViews();

        for (RecipeIngredient ingredient : recipe.getIngredients()) {
            boolean available = IngredientMatcher.pantryCovers(ingredient, pantry);

            View row = LayoutInflater.from(this).inflate(R.layout.item_ingredient_row, ingredientsLayout, false);
            ImageView icon = row.findViewById(R.id.icon_status);
            TextView text = row.findViewById(R.id.text_ingredient);

            icon.setImageResource(available ? R.drawable.ic_check_circle : R.drawable.ic_cancel_circle);
            text.setText(MessageFormat.format("{0} {1} {2}", formatQuantity(ingredient.getQuantity()), ingredient.getUnit(), ingredient.getName()));
            text.setTextColor(getResources().getColor(available ? R.color.present : R.color.missing));

            ingredientsLayout.addView(row);
        }
    }

    private String formatQuantity(double quantity) {
        if (quantity == Math.floor(quantity)) {
            return String.valueOf((long) quantity);
        }
        return String.valueOf(quantity);
    }
}
