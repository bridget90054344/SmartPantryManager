package com.example.smartpantry;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.smartpantry.db.DatabaseHelper;
import com.example.smartpantry.model.PantryItem;

/**
 * Handles both Create and Update for pantry items (and Delete, when editing
 * an existing one), satisfying the CRUD requirement from Section 3.2.
 */
public class AddEditIngredientActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_ID = "extra_item_id";
    private static final long NO_ID = -1;

    private DatabaseHelper dbHelper;
    private long editingItemId = NO_ID;

    private EditText nameField;
    private EditText quantityField;
    private Spinner unitSpinner;
    private EditText expiryField;
    private TextView nameError;
    private TextView quantityError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        dbHelper = DatabaseHelper.getInstance(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        nameField = findViewById(R.id.edit_name);
        quantityField = findViewById(R.id.edit_quantity);
        unitSpinner = findViewById(R.id.spinner_unit);
        expiryField = findViewById(R.id.edit_expiry);
        nameError = findViewById(R.id.text_name_error);
        quantityError = findViewById(R.id.text_quantity_error);

        ArrayAdapter<CharSequence> unitAdapter = ArrayAdapter.createFromResource(
                this, R.array.unit_options, android.R.layout.simple_spinner_item);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(unitAdapter);

        Button saveButton = findViewById(R.id.btn_save);
        Button deleteButton = findViewById(R.id.btn_delete);
        saveButton.setOnClickListener(v -> save());
        deleteButton.setOnClickListener(v -> confirmDelete());

        editingItemId = getIntent().getLongExtra(EXTRA_ITEM_ID, NO_ID);
        if (editingItemId != NO_ID) {
            toolbar.setTitle(R.string.title_edit_ingredient);
            deleteButton.setVisibility(View.VISIBLE);
            loadExistingItem(editingItemId);
        } else {
            toolbar.setTitle(R.string.title_add_ingredient);
        }
    }

    private void loadExistingItem(long id) {
        PantryItem item = dbHelper.getPantryItem(id);
        if (item == null) {
            finish();
            return;
        }
        nameField.setText(item.getName());
        quantityField.setText(formatQuantity(item.getQuantity()));
        expiryField.setText(item.getExpiryDate());

        ArrayAdapter adapter = (ArrayAdapter) unitSpinner.getAdapter();
        int position = adapter.getPosition(item.getUnit());
        if (position >= 0) {
            unitSpinner.setSelection(position);
        }
    }

    private void save() {
        String name = nameField.getText().toString().trim();
        String quantityText = quantityField.getText().toString().trim();
        String unit = unitSpinner.getSelectedItem() != null ? unitSpinner.getSelectedItem().toString() : "";
        String expiry = expiryField.getText().toString().trim();

        boolean valid = true;

        if (TextUtils.isEmpty(name)) {
            nameError.setText(R.string.error_name_required);
            nameError.setVisibility(View.VISIBLE);
            valid = false;
        } else {
            nameError.setVisibility(View.GONE);
        }

        double quantity = 0;
        try {
            quantity = Double.parseDouble(quantityText);
            if (quantity <= 0) throw new NumberFormatException();
            quantityError.setVisibility(View.GONE);
        } catch (NumberFormatException e) {
            quantityError.setText(R.string.error_quantity_invalid);
            quantityError.setVisibility(View.VISIBLE);
            valid = false;
        }

        if (!valid) return;

        PantryItem item = new PantryItem();
        item.setId(editingItemId == NO_ID ? 0 : editingItemId);
        item.setName(name);
        item.setQuantity(quantity);
        item.setUnit(unit);
        item.setExpiryDate(TextUtils.isEmpty(expiry) ? null : expiry);

        if (editingItemId == NO_ID) {
            dbHelper.addPantryItem(item);
        } else {
            dbHelper.updatePantryItem(item);
        }
        finish();
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Delete ingredient")
                .setMessage("Remove this ingredient from your pantry?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    dbHelper.deletePantryItem(editingItemId);
                    finish();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private String formatQuantity(double quantity) {
        if (quantity == Math.floor(quantity)) {
            return String.valueOf((long) quantity);
        }
        return String.valueOf(quantity);
    }
}
