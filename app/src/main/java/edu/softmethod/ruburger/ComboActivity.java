package edu.softmethod.ruburger;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Button;
import android.app.AlertDialog;
import android.widget.Toast;
import android.view.View;

import java.io.Serializable;

import edu.softmethod.ruburger.model.SideType;
import edu.softmethod.ruburger.model.Flavor;
import edu.softmethod.ruburger.model.Sandwich;
import edu.softmethod.ruburger.model.Combo;
import edu.softmethod.ruburger.model.OrderManager;

/**
 * Activity for customizing and ordering a combo meal (sandwich + side + drink).
 * Allows users to select options, view a dynamic price, and add the combo to their cart.
 *
 * Authors: Abhinav Acharya, Aditya Rajesh
 */
public class ComboActivity extends AppCompatActivity {
    private static final String TAG = "ComboActivity";

    private EditText sandwichEditText, priceEditText;
    private Spinner sideSpinner, drinkSpinner, quantitySpinner;
    private ImageView sideImageView, drinkImageView;
    private Button addToOrderButton, mainMenuButton;

    private Sandwich sandwichBase;

    /**
     * Called when the activity is first created.
     * Initializes views, sets up spinners, listeners, and displays sandwich info.
     *
     * @param savedInstanceState previously saved state (if any)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combo);

        // 1) Bind views
        sandwichEditText   = findViewById(R.id.edittext_selected_sandwich);
        sideSpinner        = findViewById(R.id.spinner_side);
        drinkSpinner       = findViewById(R.id.spinner_drink);
        quantitySpinner    = findViewById(R.id.spinner_quantity_combo);
        sideImageView      = findViewById(R.id.imageview_side);
        drinkImageView     = findViewById(R.id.imageview_drink);
        priceEditText      = findViewById(R.id.edittext_price_combo);
        addToOrderButton   = findViewById(R.id.button_add_to_order_combo);
        mainMenuButton     = findViewById(R.id.button_main_menu);

        // 2) Retrieve the passed sandwich
        Serializable raw = getIntent().getSerializableExtra(SandwichesActivity.EXTRA_SANDWICH);
        if (!(raw instanceof Sandwich)) {
            Log.e(TAG, "Expected Sandwich but got: " + raw);
            Toast.makeText(this, "Missing sandwich data", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        sandwichBase = (Sandwich) raw;

        // Display sandwich name
        sandwichEditText.setText(sandwichBase.toString());
        sandwichEditText.setFocusable(false);
        sandwichEditText.setClickable(false);

        // 3) Populate spinners
        ArrayAdapter<CharSequence> sideAd = ArrayAdapter.createFromResource(
                this, R.array.combo_side_options, android.R.layout.simple_spinner_item);
        sideAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sideSpinner.setAdapter(sideAd);
        sideSpinner.setSelection(0);

        ArrayAdapter<CharSequence> drinkAd = ArrayAdapter.createFromResource(
                this, R.array.combo_drink_options, android.R.layout.simple_spinner_item);
        drinkAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drinkSpinner.setAdapter(drinkAd);
        drinkSpinner.setSelection(0);

        ArrayAdapter<CharSequence> qtyAd = ArrayAdapter.createFromResource(
                this, R.array.quantity_options, android.R.layout.simple_spinner_item);
        qtyAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantitySpinner.setAdapter(qtyAd);
        quantitySpinner.setSelection(0);

        priceEditText.setFocusable(false);
        priceEditText.setClickable(false);

        // 4) Set up spinner listeners to update images and price dynamically
        AdapterView.OnItemSelectedListener updateListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                updateSideImage();
                updateDrinkImage();
                updatePrice();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        };
        sideSpinner.setOnItemSelectedListener(updateListener);
        drinkSpinner.setOnItemSelectedListener(updateListener);
        quantitySpinner.setOnItemSelectedListener(updateListener);

        // Button actions
        addToOrderButton.setOnClickListener(v -> confirmAddToOrder());
        mainMenuButton.setOnClickListener(v -> finish());

        // Initial update
        updateSideImage();
        updateDrinkImage();
        updatePrice();
    }

    /**
     * Converts a side option label into a {@link SideType} enum.
     *
     * @param label side label selected from spinner
     * @return corresponding SideType
     */
    private SideType sideFromLabel(String label) {
        if (label.equals("Apple Slices")) return SideType.APPLE_SLICES;
        return SideType.CHIPS;
    }

    /**
     * Converts a drink option label into a {@link Flavor} enum.
     *
     * @param label drink label selected from spinner
     * @return corresponding Flavor
     */
    private Flavor drinkFromLabel(String label) {
        if (label.equals("Tea"))   return Flavor.TEA;
        if (label.equals("Juice")) return Flavor.JUICE;
        return Flavor.COLA;
    }

    /**
     * Updates the side image based on the selected side item.
     */
    private void updateSideImage() {
        SideType side = sideFromLabel(sideSpinner.getSelectedItem().toString());
        int res = (side == SideType.CHIPS)
                ? R.drawable.chips
                : R.drawable.apples;
        sideImageView.setImageResource(res);
    }

    /**
     * Updates the drink image based on the selected drink item.
     */
    private void updateDrinkImage() {
        Flavor fl = drinkFromLabel(drinkSpinner.getSelectedItem().toString());
        int res = (fl == Flavor.TEA)   ? R.drawable.tea
                : (fl == Flavor.JUICE) ? R.drawable.juice
                : R.drawable.cola;
        drinkImageView.setImageResource(res);
    }

    /**
     * Recalculates and displays the price of the combo based on current selections.
     */
    private void updatePrice() {
        int qty = Integer.parseInt(quantitySpinner.getSelectedItem().toString());
        SideType side = sideFromLabel(sideSpinner.getSelectedItem().toString());
        Flavor dr = drinkFromLabel(drinkSpinner.getSelectedItem().toString());

        Log.d(TAG, "Combo qty=" + qty + ", side=" + side + ", drink=" + dr);

        Combo combo = new Combo(sandwichBase, dr, side, qty);
        priceEditText.setText(String.format("$%.2f", combo.price()));
    }

    /**
     * Displays a confirmation dialog and adds the combo to the cart if confirmed.
     */
    private void confirmAddToOrder() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm")
                .setMessage("Add this combo to your cart?")
                .setPositiveButton("Yes", (d, w) -> {
                    int qty = Integer.parseInt(quantitySpinner.getSelectedItem().toString());
                    SideType side = sideFromLabel(sideSpinner.getSelectedItem().toString());
                    Flavor dr = drinkFromLabel(drinkSpinner.getSelectedItem().toString());
                    Combo combo = new Combo(sandwichBase, dr, side, qty);
                    OrderManager.getInstance().addItemToCurrentOrder(combo);
                    Toast.makeText(this, "Combo added!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }
}
