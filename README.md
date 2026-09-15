# Smart Pantry Manager

An Android app (Java) that helps reduce food waste by tracking the ingredients you
actually have at home and suggesting recipes you can cook **right now** — strictly
using what's already in your pantry, no shopping trip required.

## Core rule

A recipe is only suggested if **every single ingredient** it needs is currently in
the pantry, in at least the required quantity. Partial matches are excluded from the
main list. Recipes missing exactly one ingredient are shown separately under
"Almost there".

## Database choice: SQLite

This project uses **SQLite** via `SQLiteOpenHelper` (`DatabaseHelper.java`) rather
than Firebase or PostgreSQL, because:

- The brief's scope is entirely local to the device (the user's own pantry) — there's
  no need for real-time sync between multiple devices or users.
- It works fully offline, which matches how a kitchen app is actually used.
- It avoids external hosting/config (Firebase project setup, or standing up a
  PostgreSQL instance + REST API).
- It's directly covered in the module's persistent-data content.

Three tables: `pantry` (user's ingredients, full CRUD), `recipes` +
`recipe_ingredients` (seeded once with 20 recipes on first run — see
`RecipeSeeder.java`), and `settings` (key/value preferences).

## Screens

1. **Pantry List** (`MainActivity`) — RecyclerView of everything in the pantry; add,
   edit, delete.
2. **Add/Edit Ingredient** (`AddEditIngredientActivity`) — form with validation.
3. **Suggested Recipes** (`SuggestedRecipesActivity`) — runs the strict-matching rule.
4. **Recipe Detail** (`RecipeDetailActivity`) — full ingredient list (with
   present/missing indicators) and method.
5. **Settings** (`SettingsActivity`) — expiring-soon alert toggle, unit system
   preference.

## Matching logic

See `util/IngredientMatcher.java`. Ingredient names are normalized (lower-cased,
trimmed, naive singularization) so "Tomatoes" matches a pantry entry of "tomato".
Units are converted into a shared base unit per family (grams for mass, millilitres
for volume) before quantities are compared, so "0.2 kg" correctly satisfies a recipe
that needs "200 g".

## Setup / run instructions

1. Open this folder in Android Studio (Giraffe or newer).
2. Let Gradle sync — if prompted about the Gradle wrapper, allow Android Studio to
   regenerate it (or point it at a local Gradle 8.4+ installation).
3. Run on an emulator or physical device (minSdk 24 / Android 7.0+).
4. The recipe database is seeded automatically the first time the app runs.

## Repository

GitHub: <ADD YOUR REPOSITORY LINK HERE BEFORE SUBMITTING>
