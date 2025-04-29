package edu.softmethod.ruburger;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import edu.softmethod.ruburger.model.MenuItem;
import edu.softmethod.ruburger.model.Order;
import edu.softmethod.ruburger.model.OrderManager;

/**
 * Activity for viewing, managing, and placing orders from the cart.
 * Allows users to remove items, view cart total, and place an order.
 *
 * Authors: Abhinav Acharya, Aditya Rajesh
 */
public class CartActivity extends AppCompatActivity {
    private static final String TAG = "CartActivity";

    private EditText subtotalEditText;
    private EditText taxEditText;
    private EditText totalEditText;
    private RecyclerView cartRecyclerView;
    private TextView emptyTextView;
    private Button removeItemButton;
    private Button placeOrderButton;
    private Button mainMenuButton;

    private Order currentOrder;
    private List<MenuItem> itemList;
    private CartAdapter adapter;
    private int selectedPosition = RecyclerView.NO_POSITION;

    /**
     * Called when the activity is starting.
     * Sets up the views, listeners, and loads the cart data.
     *
     * @param savedInstanceState previously saved state (if any)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initializeViews();
        setupRecyclerView();
        setupListeners();
        loadCartItems();
    }

    /**
     * Initializes view components and makes price fields read-only.
     */
    private void initializeViews() {
        subtotalEditText    = findViewById(R.id.edittext_subtotal);
        taxEditText         = findViewById(R.id.edittext_sales_tax);
        totalEditText       = findViewById(R.id.edittext_total);
        cartRecyclerView    = findViewById(R.id.recyclerview_cart_items);
        emptyTextView       = findViewById(R.id.textview_cart_empty);
        removeItemButton    = findViewById(R.id.button_remove_item);
        placeOrderButton    = findViewById(R.id.button_place_order);
        mainMenuButton      = findViewById(R.id.button_main_menu);

        // Make price fields non-editable
        subtotalEditText.setFocusable(false);
        subtotalEditText.setClickable(false);
        taxEditText.setFocusable(false);
        taxEditText.setClickable(false);
        totalEditText.setFocusable(false);
        totalEditText.setClickable(false);
    }

    /**
     * Configures the RecyclerView to display cart items.
     */
    private void setupRecyclerView() {
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter();
        cartRecyclerView.setAdapter(adapter);
    }

    /**
     * Sets up listeners for the buttons.
     */
    private void setupListeners() {
        removeItemButton.setOnClickListener(v -> handleRemoveItem());
        placeOrderButton.setOnClickListener(v -> handlePlaceOrder());
        mainMenuButton.setOnClickListener(v -> finish());
    }

    /**
     * Loads the cart items into the RecyclerView and updates the price fields.
     */
    private void loadCartItems() {
        currentOrder = OrderManager.getInstance().getCurrentOrder();
        itemList = currentOrder.getItems();
        adapter.notifyDataSetChanged();
        updatePriceFields();
        emptyTextView.setVisibility(itemList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    /**
     * Updates the subtotal, tax, and total price fields.
     */
    private void updatePriceFields() {
        NumberFormat fmt = NumberFormat.getCurrencyInstance(Locale.US);
        subtotalEditText.setText(fmt.format(currentOrder.getSubtotal()));
        taxEditText.setText(fmt.format(currentOrder.getTax()));
        totalEditText.setText(fmt.format(currentOrder.getTotal()));
    }

    /**
     * Handles removing a selected item from the cart.
     * Displays a message if no item is selected.
     */
    private void handleRemoveItem() {
        if (selectedPosition == RecyclerView.NO_POSITION) {
            Toast.makeText(this, "Please select an item to remove.", Toast.LENGTH_SHORT).show();
            return;
        }
        MenuItem item = itemList.get(selectedPosition);
        OrderManager.getInstance().removeItemFromCurrentOrder(item);
        selectedPosition = RecyclerView.NO_POSITION;
        loadCartItems();
    }

    /**
     * Handles placing the current order after confirmation.
     */
    private void handlePlaceOrder() {
        if (itemList.isEmpty()) {
            Toast.makeText(this, "Cart is empty! Add something first.", Toast.LENGTH_SHORT).show();
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle("Place Order")
                .setMessage("Are you sure you want to place the order?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    OrderManager.getInstance().placeCurrentOrder();
                    loadCartItems();
                    Toast.makeText(this, "Order placed!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }

    /**
     * RecyclerView Adapter for displaying the list of cart items.
     */
    private class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_activated_1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            MenuItem item = itemList.get(position);
            holder.textView.setText(item.toString());
            holder.itemView.setActivated(position == selectedPosition);
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

        /**
         * ViewHolder class representing each cart item view.
         */
        class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            /**
             * Constructs a ViewHolder and sets up item selection handling.
             *
             * @param itemView the view representing an item
             */
            ViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(android.R.id.text1);
                itemView.setOnClickListener(v -> {
                    int prev = selectedPosition;
                    selectedPosition = getAdapterPosition();
                    notifyItemChanged(prev);
                    notifyItemChanged(selectedPosition);
                });
            }
        }
    }
}
