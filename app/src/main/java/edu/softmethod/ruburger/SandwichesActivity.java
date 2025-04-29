package edu.softmethod.ruburger;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Button;
import android.app.AlertDialog;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

import edu.softmethod.ruburger.model.Bread;
import edu.softmethod.ruburger.model.Protein;
import edu.softmethod.ruburger.model.AddOns;
import edu.softmethod.ruburger.model.Sandwich;
import edu.softmethod.ruburger.model.OrderManager;

/**
 * Activity for customizing and ordering a sandwich.
 * Allows users to select bread type, protein, add-ons, quantity, and calculates the total price.
 * Also offers an option to upgrade the sandwich into a combo meal.
 *
 * Authors: Abhinav Acharya, Aditya Rajesh
 */
public class SandwichesActivity extends AppCompatActivity {
    private static final String TAG = "SandwichesActivity";
    public static final String EXTRA_SANDWICH = "edu.softmethod.ruburger.SANDWICH";

    private Spinner breadSpinner, quantitySpinner;
    private RadioGroup proteinGroup;
    private RadioButton roastBeefRadio, salmonRadio, chickenRadio;
    private CheckBox lettuceCheck, tomatoesCheck, onionCheck, avocadoCheck, cheeseCheck;
    private EditText priceEditText;
    private Button addToOrderButton, comboButton, mainMenuButton;

    /**
     * Called when the activity is starting.
     * Sets up the UI components, listeners, and default values.
     *
     * @param savedInstanceState previously saved state (if any)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sandwiches);

        // 1) Bind views
        breadSpinner       = findViewById(R.id.spinner_bread);
        quantitySpinner    = findViewById(R.id.spinner_quantity_sandwich);
        proteinGroup       = findViewById(R.id.radioGroup_protein);
        roastBeefRadio     = findViewById(R.id.radio_roastbeef);
        salmonRadio        = findViewById(R.id.radio_salmon);
        chickenRadio       = findViewById(R.id.radio_chicken);
        lettuceCheck       = findViewById(R.id.check_lettuce_sandwich);
        tomatoesCheck      = findViewById(R.id.check_tomatoes_sandwich);
        onionCheck         = findViewById(R.id.check_onion_sandwich);
        avocadoCheck       = findViewById(R.id.check_avocado_sandwich);
        cheeseCheck        = findViewById(R.id.check_cheese_sandwich);
        priceEditText      = findViewById(R.id.edittext_price_sandwich);
        addToOrderButton   = findViewById(R.id.button_add_to_order_sandwich);
        comboButton        = findViewById(R.id.button_combo_sandwich);
        mainMenuButton     = findViewById(R.id.button_main_menu);

        // 2) Setup adapters
        ArrayAdapter<CharSequence> breadAdapter = ArrayAdapter.createFromResource(
                this, R.array.sandwich_bread_options, android.R.layout.simple_spinner_item);
        breadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        breadSpinner.setAdapter(breadAdapter);
        breadSpinner.setSelection(0);

        ArrayAdapter<CharSequence> qtyAdapter = ArrayAdapter.createFromResource(
                this, R.array.quantity_options, android.R.layout.simple_spinner_item);
        qtyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantitySpinner.setAdapter(qtyAdapter);
        quantitySpinner.setSelection(0);

        // Default protein selection
        salmonRadio.setChecked(true);

        // 3) Setup listeners for recalculating price
        AdapterView.OnItemSelectedListener recalcListener = new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) { updatePrice(); }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        };
        breadSpinner.setOnItemSelectedListener(recalcListener);
        quantitySpinner.setOnItemSelectedListener(recalcListener);
        proteinGroup.setOnCheckedChangeListener((grp, checkedId) -> updatePrice());
        lettuceCheck.setOnCheckedChangeListener((btn, chk) -> updatePrice());
        tomatoesCheck.setOnCheckedChangeListener((btn, chk) -> updatePrice());
        onionCheck.setOnCheckedChangeListener((btn, chk) -> updatePrice());
        avocadoCheck.setOnCheckedChangeListener((btn, chk) -> updatePrice());
        cheeseCheck.setOnCheckedChangeListener((btn, chk) -> updatePrice());

        // Setup button actions
        addToOrderButton.setOnClickListener(v -> confirmAddToOrder());
        comboButton.setOnClickListener(v -> openCombo());
        mainMenuButton.setOnClickListener(v -> finish());

        // Disable editing price field
        priceEditText.setFocusable(false);
        priceEditText.setClickable(false);

        // Initial price calculation
        updatePrice();
    }

    /**
     * Updates the displayed price based on the current sandwich selections.
     */
    private void updatePrice() {
        Sandwich s = buildSandwich();
        if (s == null) return;
        double p = s.price();
        priceEditText.setText(String.format("$%.2f", p));
        Log.d(TAG, "Built sandwich=" + s + "  price=" + p);
    }

    /**
     * Constructs a Sandwich object based on the user's selections.
     *
     * @return the built Sandwich object
     */
    private Sandwich buildSandwich() {
        // Bread selection
        String breadVal = breadSpinner.getSelectedItem().toString();
        Bread bread = Bread.valueOf(breadVal.toUpperCase());

        // Protein selection
        Protein protein = salmonRadio.isChecked() ? Protein.SALMON
                : roastBeefRadio.isChecked() ? Protein.ROAST_BEEF
                : Protein.CHICKEN;

        // Add-ons selection
        ArrayList<AddOns> addons = new ArrayList<>();
        if (lettuceCheck.isChecked())   addons.add(AddOns.LETTUCE);
        if (tomatoesCheck.isChecked())  addons.add(AddOns.TOMATOES);
        if (onionCheck.isChecked())     addons.add(AddOns.ONIONS);
        if (avocadoCheck.isChecked())   addons.add(AddOns.AVOCADO);
        if (cheeseCheck.isChecked())    addons.add(AddOns.CHEESE);

        // Quantity selection
        String qtyStr = quantitySpinner.getSelectedItem().toString();
        int qty = Integer.parseInt(qtyStr);
        Log.d(TAG, "Qty picked = " + qty);

        return new Sandwich(bread, protein, addons, qty);
    }

    /**
     * Shows a confirmation dialog to add the current sandwich to the cart.
     */
    private void confirmAddToOrder() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm")
                .setMessage("Add sandwich to order?")
                .setPositiveButton("Yes", (d, w) -> {
                    OrderManager.getInstance()
                            .addItemToCurrentOrder(buildSandwich());
                    Toast.makeText(this, "Added!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }

    /**
     * Opens the ComboActivity, passing the current sandwich for combo customization.
     */
    private void openCombo() {
        Sandwich s = buildSandwich();
        Intent i = new Intent(this, ComboActivity.class);
        i.putExtra(EXTRA_SANDWICH, (Serializable) s);
        startActivity(i);
    }
}
