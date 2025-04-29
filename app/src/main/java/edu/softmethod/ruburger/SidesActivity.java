package edu.softmethod.ruburger;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.view.View;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;

import edu.softmethod.ruburger.model.Side;
import edu.softmethod.ruburger.model.SideType;
import edu.softmethod.ruburger.model.Size;
import edu.softmethod.ruburger.model.OrderManager;

public class SidesActivity extends AppCompatActivity {
    private Spinner spinnerSide;
    private Spinner spinnerSizeSides;
    private Spinner spinnerQuantitySides;
    private TextView tvPriceSides;
    private Button btnAddToOrderSides;
    private Button btnMainMenuSides;

    private SideType selectedSideType;
    private Size selectedSize;
    private int selectedQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sides);

        // 1) bind views
        spinnerSide           = findViewById(R.id.spinnerSide);
        spinnerSizeSides      = findViewById(R.id.spinnerSizeSides);
        spinnerQuantitySides  = findViewById(R.id.spinnerQuantitySides);
        tvPriceSides          = findViewById(R.id.tvPriceSides);
        btnAddToOrderSides    = findViewById(R.id.btnAddToOrderSides);
        btnMainMenuSides      = findViewById(R.id.btnMainMenuSides);

        // 2) SideType spinner using enum values
        ArrayAdapter<SideType> sideAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                SideType.values()
        );
        sideAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSide.setAdapter(sideAdapter);
        spinnerSide.setSelection(0);
        spinnerSide.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                selectedSideType = sideAdapter.getItem(pos);
                updatePrice();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) { }
        });

        // 3) Size spinner using enum values
        ArrayAdapter<Size> sizeAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                Arrays.asList(Size.SMALL, Size.MEDIUM, Size.LARGE)
        );
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSizeSides.setAdapter(sizeAdapter);
        spinnerSizeSides.setSelection(0);
        spinnerSizeSides.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                selectedSize = sizeAdapter.getItem(pos);
                updatePrice();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) { }
        });

        // 4) Quantity spinner (still string-array or enum)
        ArrayAdapter<CharSequence> qtyAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.quantity_options,
                android.R.layout.simple_spinner_item
        );
        qtyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQuantitySides.setAdapter(qtyAdapter);
        spinnerQuantitySides.setSelection(0);
        spinnerQuantitySides.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                try {
                    selectedQuantity = Integer.parseInt(qtyAdapter.getItem(pos).toString());
                } catch (Exception e) {
                    selectedQuantity = 1;
                }
                updatePrice();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) { }
        });

        // 5) “Add to Order” handler (with proper dialog callback)
        btnAddToOrderSides.setOnClickListener(v -> {
            if (selectedSideType == null) {
                Toast.makeText(this, "Please select a side", Toast.LENGTH_SHORT).show();
                return;
            }
            new AlertDialog.Builder(this)
                    .setTitle("Confirm")
                    .setMessage("Add this side to your cart?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        try {
                            Side side = new Side(selectedSideType, selectedSize, selectedQuantity);
                            OrderManager.getInstance().addItemToCurrentOrder(side);
                            Toast.makeText(this, "Side added to order", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(this, "Error adding side", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        // 6) Main menu
        btnMainMenuSides.setOnClickListener(v -> finish());

        // 7) Defaults & initial price
        selectedSideType = SideType.FRIES;
        selectedSize     = Size.SMALL;
        selectedQuantity = 1;
        updatePrice();
    }

    private void updatePrice() {
        try {
            if (selectedSideType == null) {
                tvPriceSides.setText("$0.00");
                return;
            }
            Side side = new Side(selectedSideType, selectedSize, selectedQuantity);
            String formatted = NumberFormat.getCurrencyInstance(Locale.US)
                    .format(side.price());
            tvPriceSides.setText(formatted);
        } catch (Exception e) {
            tvPriceSides.setText("$0.00");
        }
    }
}
