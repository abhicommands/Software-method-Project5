package edu.softmethod.ruburger;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import edu.softmethod.ruburger.model.AddOns;
import edu.softmethod.ruburger.model.Bread;
import edu.softmethod.ruburger.model.Burger;
import edu.softmethod.ruburger.model.OrderManager;

/**
 * Activity for customizing and ordering burgers.
 * Allows the user to select patty type, bread type, add-ons, quantity, and calculates total price.
 * Provides options to add to order or proceed to a combo meal.
 *
 * Authors: Abhinav Acharya, Aditya Rajesh
 */
public class BurgersActivity extends AppCompatActivity {
    private static final String TAG = "BurgersActivity";
    public static final String EXTRA_BURGER = "edu.softmethod.ruburger.SANDWICH";

    private RadioGroup pattyGroup;
    private RadioButton radioSingle, radioDouble;
    private RadioGroup breadGroup;
    private RadioButton radioBrioche, radioWheat, radioPretzel;
    private CheckBox checkLettuce, checkTomatoes, checkOnions, checkAvocadoes, checkCheese;
    private Spinner quantitySpinner;
    private EditText priceEditText;
    private Button buttonAddToOrder, buttonCombo, buttonMainMenu;

    private OrderManager orderManager = OrderManager.getInstance();

    /**
     * Initializes the activity, sets up the view, listeners, and default selections.
     *
     * @param savedInstanceState previously saved state (if any)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burgers);

        initializeViews();
        setupDefaults();
        setupListeners();
        updatePrice();
    }

    /**
     * Binds all the UI components from the layout to Java objects.
     */
    private void initializeViews() {
        pattyGroup = findViewById(R.id.radioGroup_patty);
        radioSingle = findViewById(R.id.radio_single);
        radioDouble = findViewById(R.id.radio_double);

        breadGroup = findViewById(R.id.radioGroup_bread);
        radioBrioche = findViewById(R.id.radio_brioche);
        radioWheat = findViewById(R.id.radio_wheat);
        radioPretzel = findViewById(R.id.radio_pretzel);

        checkLettuce = findViewById(R.id.check_lettuce);
        checkTomatoes = findViewById(R.id.check_tomatoes);
        checkOnions = findViewById(R.id.check_onions);
        checkAvocadoes = findViewById(R.id.check_avocadoes);
        checkCheese = findViewById(R.id.check_cheese);

        quantitySpinner = findViewById(R.id.spinner_quantity);
        priceEditText = findViewById(R.id.edittext_price);

        buttonAddToOrder = findViewById(R.id.button_add_to_order);
        buttonCombo = findViewById(R.id.button_combo);
        buttonMainMenu = findViewById(R.id.button_main_menu);

        // Populate quantity spinner
        ArrayAdapter<CharSequence> qtyAdapter = ArrayAdapter.createFromResource(
                this, R.array.quantity_options, android.R.layout.simple_spinner_item);
        qtyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantitySpinner.setAdapter(qtyAdapter);
        quantitySpinner.setSelection(0);

        priceEditText.setFocusable(false);
        priceEditText.setClickable(false);
    }

    /**
     * Sets the default selections for patty and bread options.
     */
    private void setupDefaults() {
        radioSingle.setChecked(true);
        radioBrioche.setChecked(true);
    }

    /**
     * Attaches listeners to UI components for reacting to user interactions.
     * Updates the burger price accordingly.
     */
    private void setupListeners() {
        pattyGroup.setOnCheckedChangeListener((grp, checkedId) -> updatePrice());
        breadGroup.setOnCheckedChangeListener((grp, checkedId) -> updatePrice());

        CompoundButton.OnCheckedChangeListener addOnListener = (btn, isChecked) -> updatePrice();
        checkLettuce.setOnCheckedChangeListener(addOnListener);
        checkTomatoes.setOnCheckedChangeListener(addOnListener);
        checkOnions.setOnCheckedChangeListener(addOnListener);
        checkAvocadoes.setOnCheckedChangeListener(addOnListener);
        checkCheese.setOnCheckedChangeListener(addOnListener);

        quantitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                updatePrice();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        buttonAddToOrder.setOnClickListener(v -> confirmAddToOrder());
        buttonCombo.setOnClickListener(v -> openComboPage());
        buttonMainMenu.setOnClickListener(v -> finish());
    }

    /**
     * Recalculates and updates the displayed price based on current burger selections.
     */
    private void updatePrice() {
        Burger burger = buildBurgerFromSelections();
        if (burger == null) return;
        double price = burger.price();
        NumberFormat fmt = NumberFormat.getCurrencyInstance(Locale.US);
        priceEditText.setText(fmt.format(price));
        Log.d(TAG, "Built burger=" + burger + " price=" + price);
    }

    /**
     * Builds a Burger object based on the current user selections on the UI.
     *
     * @return the constructed Burger object, or null if invalid input
     */
    private Burger buildBurgerFromSelections() {
        boolean isDouble = radioDouble.isChecked();

        Bread bread;
        int breadId = breadGroup.getCheckedRadioButtonId();
        if (breadId == R.id.radio_wheat) bread = Bread.WHEAT;
        else if (breadId == R.id.radio_pretzel) bread = Bread.PRETZEL;
        else bread = Bread.BRIOCHE;

        ArrayList<AddOns> addons = new ArrayList<>();
        if (checkLettuce.isChecked()) addons.add(AddOns.LETTUCE);
        if (checkTomatoes.isChecked()) addons.add(AddOns.TOMATOES);
        if (checkOnions.isChecked()) addons.add(AddOns.ONIONS);
        if (checkAvocadoes.isChecked()) addons.add(AddOns.AVOCADO);
        if (checkCheese.isChecked()) addons.add(AddOns.CHEESE);

        String qtyStr = quantitySpinner.getSelectedItem().toString();
        int qty;
        try {
            qty = Integer.parseInt(qtyStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid quantity", Toast.LENGTH_SHORT).show();
            return null;
        }
        Log.d(TAG, "Qty picked=" + qty);

        return new Burger(bread, isDouble, addons, qty);
    }

    /**
     * Shows a confirmation dialog before adding the burger to the current order.
     */
    private void confirmAddToOrder() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm")
                .setMessage("Add burger to order?")
                .setPositiveButton("Yes", (dlg, which) -> {
                    orderManager.addItemToCurrentOrder(buildBurgerFromSelections());
                    Toast.makeText(this, "Added!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }

    /**
     * Opens the ComboActivity, passing the current burger for combo customization.
     */
    private void openComboPage() {
        Burger burger = buildBurgerFromSelections();
        if (burger == null) {
            Toast.makeText(this, "Error creating burger for combo", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, ComboActivity.class);
        intent.putExtra(EXTRA_BURGER, burger);
        startActivity(intent);
    }
}
