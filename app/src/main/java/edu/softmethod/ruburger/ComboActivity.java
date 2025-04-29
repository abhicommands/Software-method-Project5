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

public class ComboActivity extends AppCompatActivity {
    private static final String TAG = "ComboActivity";

    private EditText sandwichEditText, priceEditText;
    private Spinner sideSpinner, drinkSpinner, quantitySpinner;
    private ImageView sideImageView, drinkImageView;
    private Button addToOrderButton, mainMenuButton;

    private Sandwich sandwichBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combo);

        //––– 1) Bind views
        sandwichEditText   = findViewById(R.id.edittext_selected_sandwich);
        sideSpinner        = findViewById(R.id.spinner_side);
        drinkSpinner       = findViewById(R.id.spinner_drink);
        quantitySpinner    = findViewById(R.id.spinner_quantity_combo);
        sideImageView      = findViewById(R.id.imageview_side);
        drinkImageView     = findViewById(R.id.imageview_drink);
        priceEditText      = findViewById(R.id.edittext_price_combo);
        addToOrderButton   = findViewById(R.id.button_add_to_order_combo);
        mainMenuButton     = findViewById(R.id.button_main_menu);

        //––– 2) Unwrap the Sandwich from the Intent
        Serializable raw = getIntent().getSerializableExtra(SandwichesActivity.EXTRA_SANDWICH);
        if (!(raw instanceof Sandwich)) {
            Log.e(TAG, "Expected Sandwich but got: " + raw);
            Toast.makeText(this, "Missing sandwich data", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        sandwichBase = (Sandwich) raw;

        // Display it
        sandwichEditText.setText(sandwichBase.toString());
        sandwichEditText.setFocusable(false);
        sandwichEditText.setClickable(false);

        //––– 3) Populate the spinners and set default selection to position 0 (“1”)
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
        // **This line fixes your “always 0” problem by choosing “1” by default**
        quantitySpinner.setSelection(0);

        // Disable editing price field
        priceEditText.setFocusable(false);
        priceEditText.setClickable(false);

        //––– 4) Unified listener to update images & price whenever a spinner changes
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

        //––– Button handlers
        addToOrderButton.setOnClickListener(v -> confirmAddToOrder());
        mainMenuButton.setOnClickListener(v -> finish());

        //––– Initial draw
        updateSideImage();
        updateDrinkImage();
        updatePrice();
    }

    // Map user-friendly label → SideType enum
    private SideType sideFromLabel(String label) {
        if (label.equals("Apple Slices")) return SideType.APPLE_SLICES;
        return SideType.CHIPS;
    }

    // Map user-friendly label → Flavor enum
    private Flavor drinkFromLabel(String label) {
        if (label.equals("Tea"))   return Flavor.TEA;
        if (label.equals("Juice")) return Flavor.JUICE;
        return Flavor.COLA;
    }

    private void updateSideImage() {
        SideType side = sideFromLabel(sideSpinner.getSelectedItem().toString());
        int res = (side == SideType.CHIPS)
                ? R.drawable.chips
                : R.drawable.apples;
        sideImageView.setImageResource(res);
    }

    private void updateDrinkImage() {
        Flavor fl = drinkFromLabel(drinkSpinner.getSelectedItem().toString());
        int res = (fl == Flavor.TEA)   ? R.drawable.tea
                : (fl == Flavor.JUICE) ? R.drawable.juice
                : R.drawable.cola;
        drinkImageView.setImageResource(res);
    }

    private void updatePrice() {
        int qty = Integer.parseInt(quantitySpinner.getSelectedItem().toString());
        SideType side = sideFromLabel(sideSpinner.getSelectedItem().toString());
        Flavor   dr   = drinkFromLabel(drinkSpinner.getSelectedItem().toString());

        // Log it so you can watch Logcat and confirm qty != 0
        Log.d(TAG, "Combo qty=" + qty + ", side=" + side + ", drink=" + dr);

        Combo combo = new Combo(sandwichBase, dr, side, qty);
        priceEditText.setText(String.format("$%.2f", combo.price()));
    }

    private void confirmAddToOrder() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm")
                .setMessage("Add this combo to your cart?")
                .setPositiveButton("Yes", (d, w) -> {
                    int qty = Integer.parseInt(quantitySpinner.getSelectedItem().toString());
                    SideType side = sideFromLabel(sideSpinner.getSelectedItem().toString());
                    Flavor   dr   = drinkFromLabel(drinkSpinner.getSelectedItem().toString());
                    Combo combo = new Combo(sandwichBase, dr, side, qty);
                    OrderManager.getInstance().addItemToCurrentOrder(combo);
                    Toast.makeText(this, "Combo added!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }
}
