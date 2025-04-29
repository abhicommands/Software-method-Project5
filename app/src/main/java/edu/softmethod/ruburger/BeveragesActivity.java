package edu.softmethod.ruburger;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import edu.softmethod.ruburger.model.Beverage;
import edu.softmethod.ruburger.model.Flavor;
import edu.softmethod.ruburger.model.OrderManager;
import edu.softmethod.ruburger.model.Size;

/**
 * Activity for selecting and ordering a beverage.
 * Handles flavor selection, size selection, quantity, price updates, and order placement.
 *
 * Authors: Abhinav Acharya, Aditya Rajesh
 */
public class BeveragesActivity extends AppCompatActivity {
    private RecyclerView rvFlavorList;
    private Spinner spinnerSize;
    private Spinner spinnerQuantity;
    private TextView tvPriceBeverage;
    private Button btnAddOrderBeverage;
    private Button btnMainMenuBeverage;

    private Flavor selectedFlavor;
    private Size selectedSize;
    private int selectedQuantity;

    /**
     * Initializes the activity, binds UI components, sets up event listeners, and populates spinners.
     *
     * @param savedInstanceState previously saved state (if any)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beverages);

        // Bind views
        rvFlavorList        = findViewById(R.id.rvFlavorList);
        spinnerSize         = findViewById(R.id.spinnerSize);
        spinnerQuantity     = findViewById(R.id.spinnerQuantity);
        tvPriceBeverage     = findViewById(R.id.tvPriceBeverage);
        btnAddOrderBeverage = findViewById(R.id.btnAddOrderBeverage);
        btnMainMenuBeverage = findViewById(R.id.btnMainMenuBeverage);

        // 1) Setup flavor RecyclerView
        rvFlavorList.setLayoutManager(new LinearLayoutManager(this));
        FlavorAdapter flavorAdapter = new FlavorAdapter(Arrays.asList(Flavor.values()));
        rvFlavorList.setAdapter(flavorAdapter);

        // 2) Populate size spinner
        ArrayAdapter<CharSequence> sizeAdapter = ArrayAdapter.createFromResource(
                this, R.array.beverage_size_options, android.R.layout.simple_spinner_item);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSize.setAdapter(sizeAdapter);
        spinnerSize.setSelection(0);
        spinnerSize.setOnItemSelectedListener(new SimpleItemSelectedListener() {
            @Override public void onItemSelected(int pos) {
                CharSequence cs = sizeAdapter.getItem(pos);
                try {
                    selectedSize = Size.valueOf(cs.toString().toUpperCase(Locale.US));
                } catch (Exception e) {
                    selectedSize = Size.SMALL;
                }
                updatePrice();
            }
        });

        // 3) Populate quantity spinner
        ArrayAdapter<CharSequence> qtyAdapter = ArrayAdapter.createFromResource(
                this, R.array.quantity_options, android.R.layout.simple_spinner_item);
        qtyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQuantity.setAdapter(qtyAdapter);
        spinnerQuantity.setSelection(0);
        spinnerQuantity.setOnItemSelectedListener(new SimpleItemSelectedListener() {
            @Override public void onItemSelected(int pos) {
                CharSequence cs = qtyAdapter.getItem(pos);
                try {
                    selectedQuantity = Integer.parseInt(cs.toString());
                } catch (Exception e) {
                    selectedQuantity = 1;
                }
                updatePrice();
            }
        });

        // 4) Add order button – now truly adds inside the positive callback
        btnAddOrderBeverage.setOnClickListener(v -> {
            if (selectedFlavor == null) {
                Toast.makeText(this, "Please select a flavor", Toast.LENGTH_SHORT).show();
                return;
            }
            new AlertDialog.Builder(this)
                    .setTitle("Confirm")
                    .setMessage("Add this beverage to your cart?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        try {
                            Beverage bev = new Beverage(selectedSize, selectedFlavor, selectedQuantity);
                            OrderManager.getInstance().addItemToCurrentOrder(bev);
                            Toast.makeText(this, "Beverage added to order", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(this, "Error adding beverage", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        // 5) Main menu button
        btnMainMenuBeverage.setOnClickListener(v -> finish());

        // Initialize defaults
        selectedSize = Size.SMALL;
        selectedQuantity = 1;
        updatePrice();
    }

    /**
     * Updates the displayed price of the beverage based on selected size, flavor, and quantity.
     */
    private void updatePrice() {
        if (selectedFlavor == null) {
            tvPriceBeverage.setText("$0.00");
            return;
        }
        try {
            Beverage bev = new Beverage(selectedSize, selectedFlavor, selectedQuantity);
            String formatted = NumberFormat.getCurrencyInstance(Locale.US)
                    .format(bev.price());
            tvPriceBeverage.setText(formatted);
        } catch (Exception e) {
            tvPriceBeverage.setText("$0.00");
        }
    }

    /**
     * RecyclerView Adapter for displaying a list of beverage flavors.
     */
    private class FlavorAdapter extends RecyclerView.Adapter<FlavorAdapter.ViewHolder> {
        private final List<Flavor> flavors;
        private int selectedPos = RecyclerView.NO_POSITION;

        /**
         * Constructs a FlavorAdapter with a given list of flavors.
         *
         * @param list list of available flavors
         */
        FlavorAdapter(List<Flavor> list) {
            this.flavors = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_activated_1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Flavor flavor = flavors.get(position);
            holder.text1.setText(flavor.name());
            holder.itemView.setActivated(position == selectedPos);
        }

        @Override
        public int getItemCount() {
            return flavors.size();
        }

        /**
         * ViewHolder for displaying each flavor option.
         */
        class ViewHolder extends RecyclerView.ViewHolder {
            TextView text1;

            /**
             * Constructs a ViewHolder, sets up click listener for selecting a flavor.
             *
             * @param itemView the view for the list item
             */
            ViewHolder(View itemView) {
                super(itemView);
                text1 = itemView.findViewById(android.R.id.text1);
                itemView.setOnClickListener(v -> {
                    int prev = selectedPos;
                    selectedPos = getAdapterPosition();
                    selectedFlavor = flavors.get(selectedPos);
                    notifyItemChanged(prev);
                    notifyItemChanged(selectedPos);
                    updatePrice();
                });
            }
        }
    }

    /**
     * Simplified listener for Spinner items, allowing only index-based handling.
     */
    private abstract class SimpleItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override public void onNothingSelected(AdapterView<?> parent) {}

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            onItemSelected(position);
        }

        /**
         * Called when an item is selected by index.
         *
         * @param position the position of the selected item
         */
        public abstract void onItemSelected(int position);
    }
}
