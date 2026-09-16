package com.example.smartpantry;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantry.db.DatabaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Simple preferences screen (Section 2.2): whether to show expiring-soon
 * alerts, and a preferred unit system. Persisted in the settings key/value table.
 */
public class SettingsActivity extends AppCompatActivity {

    private static final String KEY_EXPIRY_ALERTS = "expiry_alerts_enabled";
    private static final String KEY_UNIT_SYSTEM = "unit_system";

    private DatabaseHelper dbHelper;
    private Switch expiryAlertsSwitch;
    private Spinner unitSystemSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        dbHelper = DatabaseHelper.getInstance(this);

        expiryAlertsSwitch = findViewById(R.id.switch_expiry_alerts);
        unitSystemSpinner = findViewById(R.id.spinner_units);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.unit_system_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSystemSpinner.setAdapter(adapter);

        loadSettings();

        expiryAlertsSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                dbHelper.setSetting(KEY_EXPIRY_ALERTS, String.valueOf(isChecked)));

        unitSystemSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                dbHelper.setSetting(KEY_UNIT_SYSTEM, parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setSelectedItemId(R.id.nav_settings);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_settings) {
                return true;
            } else if (itemId == R.id.nav_pantry) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_suggested) {
                startActivity(new Intent(this, SuggestedRecipesActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }

    private void loadSettings() {
        boolean alertsEnabled = Boolean.parseBoolean(dbHelper.getSetting(KEY_EXPIRY_ALERTS, "true"));
        expiryAlertsSwitch.setChecked(alertsEnabled);

        String unitSystem = dbHelper.getSetting(KEY_UNIT_SYSTEM, "Metric");
        ArrayAdapter adapter = (ArrayAdapter) unitSystemSpinner.getAdapter();
        int position = adapter.getPosition(unitSystem);
        if (position >= 0) {
            unitSystemSpinner.setSelection(position);
        }
    }
}
