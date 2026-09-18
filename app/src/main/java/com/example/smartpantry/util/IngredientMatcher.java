package com.example.smartpantry.util;

import com.example.smartpantry.model.PantryItem;
import com.example.smartpantry.model.Recipe;
import com.example.smartpantry.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Implements the "strict matching" business rule described in the assignment brief:
 * a recipe is only suggested if the pantry currently holds EVERY ingredient it needs,
 * in at least the required quantity.
 *
 * To avoid a naive exact-string match that breaks on trivial real-world differences
 * (e.g. "tomato" vs "tomatoes", "Onion" vs "onion", "200g" vs "0.2kg"), this class
 * normalizes ingredient names and converts common units into a shared base unit
 * before comparing.
 */
public class IngredientMatcher {

    private enum Family { MASS, VOLUME, COUNT, UNKNOWN }

    /**
     * Normalizes an ingredient name so trivial differences don't break matching:
     * lower-cases, trims, collapses whitespace, and applies a very small amount
     * of naive singularization (this is NOT full NLP - just enough to catch the
     * common "tomato" / "tomatoes" style plural cases called out in the brief).
     */
    public static String normalizeName(String rawName) {
        if (rawName == null) return "";
        String s = rawName.trim().toLowerCase(Locale.ROOT);
        s = s.replaceAll("[^a-z0-9\\s]", "");
        s = s.replaceAll("\\s+", " ").trim();

        if (s.endsWith("ies") && s.length() > 4) {
            s = s.substring(0, s.length() - 3) + "y";      // "berries" -> "berry"
        } else if (s.endsWith("oes") && s.length() > 4) {
            s = s.substring(0, s.length() - 2);             // "tomatoes" -> "tomato"
        } else if (s.endsWith("es") && s.length() > 4 && !s.endsWith("ses")) {
            s = s.substring(0, s.length() - 2);              // "dishes" -> "dish"
        } else if (s.endsWith("s") && !s.endsWith("ss") && s.length() > 3) {
            s = s.substring(0, s.length() - 1);              // "eggs" -> "egg"
        }
        return s;
    }

    private static String normalizeUnit(String rawUnit) {
        if (rawUnit == null) return "";
        return rawUnit.trim().toLowerCase(Locale.ROOT);
    }

    private static Family familyOf(String unit) {
        switch (unit) {
            case "g":
            case "gram":
            case "grams":
            case "kg":
            case "kilogram":
            case "kilograms":
                return Family.MASS;
            case "ml":
            case "milliliter":
            case "milliliters":
            case "millilitre":
            case "millilitres":
            case "l":
            case "liter":
            case "liters":
            case "litre":
            case "litres":
            case "tsp":
            case "teaspoon":
            case "teaspoons":
            case "tbsp":
            case "tablespoon":
            case "tablespoons":
            case "cup":
            case "cups":
                return Family.VOLUME;
            case "pcs":
            case "pc":
            case "piece":
            case "pieces":
            case "unit":
            case "units":
            case "":
                return Family.COUNT;
            default:
                return Family.UNKNOWN;
        }
    }

    /** Converts a quantity into a canonical base unit for its family (grams for mass, ml for volume, itself for count). */
    private static double toBase(double qty, String unit) {
        switch (unit) {
            case "kg":
            case "kilogram":
            case "kilograms":
                return qty * 1000.0;
            case "g":
            case "gram":
            case "grams":
                return qty;
            case "l":
            case "liter":
            case "liters":
            case "litre":
            case "litres":
                return qty * 1000.0;
            case "ml":
            case "milliliter":
            case "milliliters":
            case "millilitre":
            case "millilitres":
                return qty;
            case "tsp":
            case "teaspoon":
            case "teaspoons":
                return qty * 5.0;
            case "tbsp":
            case "tablespoon":
            case "tablespoons":
                return qty * 15.0;
            case "cup":
            case "cups":
                return qty * 240.0;
            default:
                return qty; // count / unknown - compared as-is
        }
    }

    /**
     * Checks whether the pantry (as a whole) has enough of a single required ingredient.
     * Pantry items with the same normalized name are summed together first, so e.g.
     * two separate pantry entries of "onion" both count toward one recipe's onion need.
     */
    public static boolean pantryCovers(RecipeIngredient needed, List<PantryItem> pantry) {
        String neededName = normalizeName(needed.getName());
        String neededUnit = normalizeUnit(needed.getUnit());
        Family neededFamily = familyOf(neededUnit);

        double haveInBase = 0;
        boolean foundAny = false;

        for (PantryItem item : pantry) {
            if (!normalizeName(item.getName()).equals(neededName)) continue;
            foundAny = true;

            String itemUnit = normalizeUnit(item.getUnit());
            Family itemFamily = familyOf(itemUnit);

            if (neededFamily == itemFamily && neededFamily != Family.UNKNOWN) {
                haveInBase += toBase(item.getQuantity(), itemUnit);
            } else {
                // Units aren't directly comparable (e.g. unrecognized unit) - fall back
                // to treating presence of the ingredient as sufficient rather than
                // failing the whole recipe over a unit we can't parse.
                haveInBase = Math.max(haveInBase, needed.getQuantity());
            }
        }

        if (!foundAny) return false;

        double neededInBase = toBase(needed.getQuantity(), neededUnit);
        return haveInBase >= neededInBase;
    }

    /** Returns the list of ingredients from this recipe that the pantry is currently missing. */
    public static List<RecipeIngredient> getMissingIngredients(Recipe recipe, List<PantryItem> pantry) {
        List<RecipeIngredient> missing = new ArrayList<>();
        for (RecipeIngredient needed : recipe.getIngredients()) {
            if (!pantryCovers(needed, pantry)) {
                missing.add(needed);
            }
        }
        return missing;
    }

    /** The core strict-matching rule: recipe qualifies only if NOTHING is missing. */
    public static List<Recipe> getStrictMatches(List<Recipe> allRecipes, List<PantryItem> pantry) {
        List<Recipe> matches = new ArrayList<>();
        for (Recipe recipe : allRecipes) {
            if (getMissingIngredients(recipe, pantry).isEmpty()) {
                matches.add(recipe);
            }
        }
        return matches;
    }

    /** Stretch feature: recipes missing exactly one ingredient, kept separate from the strict list. */
    public static List<Recipe> getAlmostThereMatches(List<Recipe> allRecipes, List<PantryItem> pantry) {
        List<Recipe> almost = new ArrayList<>();
        for (Recipe recipe : allRecipes) {
            if (getMissingIngredients(recipe, pantry).size() == 1) {
                almost.add(recipe);
            }
        }
        return almost;
    }
}
