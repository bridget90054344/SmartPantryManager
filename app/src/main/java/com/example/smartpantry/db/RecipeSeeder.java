package com.example.smartpantry.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Populates the recipes / recipe_ingredients tables with a fixed reference
 * set of 20 recipes the very first time the database is created.
 */
class RecipeSeeder {

    static void seed(SQLiteDatabase db) {
        add(db, "Tomato Onion Omelette",
                "1. Whisk the eggs with a pinch of salt.\n2. Dice the tomato and onion and soften in oil.\n3. Pour in the eggs and cook until set.",
                new String[][]{
                        {"egg", "3", "pcs"}, {"tomato", "1", "pcs"}, {"onion", "1", "pcs"},
                        {"salt", "1", "tsp"}, {"oil", "1", "tbsp"}});

        add(db, "Garlic Butter Pasta",
                "1. Boil the pasta until al dente.\n2. Melt butter and gently fry the chopped garlic.\n3. Toss the pasta through the garlic butter with salt.",
                new String[][]{
                        {"pasta", "200", "g"}, {"butter", "30", "g"}, {"garlic", "3", "pcs"}, {"salt", "1", "tsp"}});

        add(db, "Vegetable Fried Rice",
                "1. Scramble the eggs and set aside.\n2. Stir-fry the diced onion and carrot.\n3. Add the rice, egg and soy sauce and toss until hot.",
                new String[][]{
                        {"rice", "300", "g"}, {"egg", "2", "pcs"}, {"carrot", "1", "pcs"},
                        {"onion", "1", "pcs"}, {"soy sauce", "2", "tbsp"}});

        add(db, "Cheese Toast",
                "1. Butter one side of each slice of bread.\n2. Layer the cheese between the unbuttered sides.\n3. Toast in a pan until golden and the cheese melts.",
                new String[][]{
                        {"bread", "2", "pcs"}, {"cheese", "50", "g"}, {"butter", "10", "g"}});

        add(db, "Tomato Soup",
                "1. Soften the onion and garlic.\n2. Add chopped tomato and water, simmer 15 minutes.\n3. Blend until smooth and season with salt.",
                new String[][]{
                        {"tomato", "4", "pcs"}, {"onion", "1", "pcs"}, {"garlic", "2", "pcs"},
                        {"salt", "1", "tsp"}, {"water", "500", "ml"}});

        add(db, "Chicken Stir Fry",
                "1. Slice the chicken and stir-fry until browned.\n2. Add onion and garlic, cook until fragrant.\n3. Finish with soy sauce and a drizzle of oil.",
                new String[][]{
                        {"chicken", "300", "g"}, {"onion", "1", "pcs"}, {"garlic", "2", "pcs"},
                        {"soy sauce", "2", "tbsp"}, {"oil", "1", "tbsp"}});

        add(db, "Banana Pancakes",
                "1. Mash the banana and whisk with egg and milk.\n2. Fold in the flour to make a batter.\n3. Fry spoonfuls of batter until golden on both sides.",
                new String[][]{
                        {"flour", "200", "g"}, {"banana", "2", "pcs"}, {"egg", "1", "pcs"}, {"milk", "200", "ml"}});

        add(db, "Potato Salad",
                "1. Boil the potatoes until tender, then cool and cube.\n2. Dice the onion finely.\n3. Mix potato, onion, mayonnaise and salt together.",
                new String[][]{
                        {"potato", "400", "g"}, {"onion", "1", "pcs"}, {"mayonnaise", "60", "g"}, {"salt", "1", "tsp"}});

        add(db, "Lentil Curry",
                "1. Soften the onion, garlic and tomato in a pot.\n2. Add lentils and curry powder, cover with water.\n3. Simmer until the lentils are soft.",
                new String[][]{
                        {"lentils", "200", "g"}, {"onion", "1", "pcs"}, {"garlic", "2", "pcs"},
                        {"tomato", "1", "pcs"}, {"curry powder", "1", "tbsp"}});

        add(db, "Grilled Cheese Sandwich",
                "1. Butter the outside of two bread slices.\n2. Place cheese between the unbuttered sides.\n3. Grill on a pan until crisp and melted.",
                new String[][]{
                        {"bread", "2", "pcs"}, {"cheese", "50", "g"}, {"butter", "10", "g"}});

        add(db, "Vegetable Soup",
                "1. Chop the carrot, potato, onion and celery.\n2. Add all vegetables and water to a pot and season.\n3. Simmer for 25 minutes until soft.",
                new String[][]{
                        {"carrot", "1", "pcs"}, {"potato", "1", "pcs"}, {"onion", "1", "pcs"},
                        {"celery", "1", "pcs"}, {"water", "500", "ml"}, {"salt", "1", "tsp"}});

        add(db, "Egg Fried Rice",
                "1. Scramble the eggs in a hot pan.\n2. Add the rice and diced onion, stir-fry together.\n3. Season with soy sauce and serve.",
                new String[][]{
                        {"rice", "300", "g"}, {"egg", "2", "pcs"}, {"soy sauce", "1", "tbsp"}, {"onion", "1", "pcs"}});

        add(db, "Spaghetti Aglio e Olio",
                "1. Boil the pasta until al dente.\n2. Gently fry sliced garlic and chili flakes in olive oil.\n3. Toss the pasta through the garlic oil.",
                new String[][]{
                        {"pasta", "200", "g"}, {"garlic", "4", "pcs"}, {"olive oil", "3", "tbsp"}, {"chili flakes", "1", "tsp"}});

        add(db, "Chicken Soup",
                "1. Simmer the chicken in water until cooked through.\n2. Add chopped carrot, onion and celery.\n3. Continue simmering until the vegetables are tender.",
                new String[][]{
                        {"chicken", "200", "g"}, {"carrot", "1", "pcs"}, {"onion", "1", "pcs"},
                        {"celery", "1", "pcs"}, {"water", "600", "ml"}});

        add(db, "Peanut Butter Banana Toast",
                "1. Toast the bread slices.\n2. Spread peanut butter over each slice.\n3. Top with sliced banana.",
                new String[][]{
                        {"bread", "2", "pcs"}, {"peanut butter", "30", "g"}, {"banana", "1", "pcs"}});

        add(db, "Rice and Beans",
                "1. Soften the onion and garlic in a pot.\n2. Add rice and beans with enough water to cook.\n3. Simmer until the rice is tender.",
                new String[][]{
                        {"rice", "250", "g"}, {"beans", "200", "g"}, {"onion", "1", "pcs"}, {"garlic", "2", "pcs"}});

        add(db, "Mushroom Risotto",
                "1. Soften the onion in butter.\n2. Add rice and mushroom, stir to coat.\n3. Gradually add stock, stirring, until creamy and cooked through.",
                new String[][]{
                        {"rice", "200", "g"}, {"mushroom", "150", "g"}, {"onion", "1", "pcs"},
                        {"butter", "20", "g"}, {"vegetable stock", "500", "ml"}});

        add(db, "Fruit Salad",
                "1. Chop the apple, banana and orange into bite-sized pieces.\n2. Combine in a bowl.\n3. Drizzle with honey and toss gently.",
                new String[][]{
                        {"apple", "1", "pcs"}, {"banana", "1", "pcs"}, {"orange", "1", "pcs"}, {"honey", "1", "tbsp"}});

        add(db, "Cucumber Tomato Salad",
                "1. Slice the cucumber, tomato and onion.\n2. Combine in a bowl.\n3. Dress with olive oil and salt.",
                new String[][]{
                        {"cucumber", "1", "pcs"}, {"tomato", "2", "pcs"}, {"onion", "1", "pcs"},
                        {"olive oil", "1", "tbsp"}, {"salt", "1", "tsp"}});

        add(db, "Scrambled Eggs",
                "1. Whisk the eggs with milk and salt.\n2. Melt butter in a pan over low heat.\n3. Add the eggs and stir gently until softly set.",
                new String[][]{
                        {"egg", "3", "pcs"}, {"butter", "10", "g"}, {"salt", "1", "tsp"}, {"milk", "30", "ml"}});
    }

    private static void add(SQLiteDatabase db, String name, String steps, String[][] ingredients) {
        ContentValues recipeValues = new ContentValues();
        recipeValues.put("name", name);
        recipeValues.put("steps", steps);
        long recipeId = db.insert(DatabaseHelper.TABLE_RECIPES, null, recipeValues);

        for (String[] ingredient : ingredients) {
            ContentValues ingredientValues = new ContentValues();
            ingredientValues.put("recipe_id", recipeId);
            ingredientValues.put("name", ingredient[0]);
            ingredientValues.put("quantity", Double.parseDouble(ingredient[1]));
            ingredientValues.put("unit", ingredient[2]);
            db.insert(DatabaseHelper.TABLE_RECIPE_INGREDIENTS, null, ingredientValues);
        }
    }
}
