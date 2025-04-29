package edu.softmethod.ruburger;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.softmethod.ruburger.model.MenuItem;
import edu.softmethod.ruburger.model.Order;
import edu.softmethod.ruburger.model.OrderManager;

/**
 * Activity for viewing, managing, and canceling placed orders.
 * Displays placed orders in a spinner and shows the selected order's details.
 * Allows users to cancel an order or return to the main menu.
 *
 * Authors: Abhinav Acharya, Aditya Rajesh
 */
public class OrdersActivity extends AppCompatActivity {
    private Spinner spinnerOrderNumber;
    private TextView tvTotalAmount;
    private RecyclerView rvOrderItems;
    private Button btnCancelOrder;
    private Button btnMainMenu;

    private List<Order> placedOrders;
    private OrderItemsAdapter adapter;

    /**
     * Called when the activity is starting.
     * Initializes the views, loads placed orders, and sets up event listeners.
     *
     * @param savedInstanceState previously saved state (if any)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        // Bind views
        spinnerOrderNumber = findViewById(R.id.spinnerOrderNumber);
        tvTotalAmount      = findViewById(R.id.tvTotalAmount);
        rvOrderItems       = findViewById(R.id.rvOrderItems);
        btnCancelOrder     = findViewById(R.id.btnCancelOrder);
        btnMainMenu        = findViewById(R.id.btnMainMenuOrders);

        // Setup RecyclerView
        rvOrderItems.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderItemsAdapter();
        rvOrderItems.setAdapter(adapter);

        // Load placed orders
        placedOrders = new ArrayList<>(OrderManager.getInstance().getPlacedOrders());
        List<Integer> orderNumbers = new ArrayList<>();
        for (Order o : placedOrders) {
            orderNumbers.add(o.getNumber());
        }

        // Setup Spinner adapter
        ArrayAdapter<Integer> spinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, orderNumbers);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrderNumber.setAdapter(spinnerAdapter);

        // Show first order details if exists
        if (!orderNumbers.isEmpty()) {
            spinnerOrderNumber.setSelection(0);
            showOrderDetails(orderNumbers.get(0));
        }

        // Spinner selection listener
        spinnerOrderNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int orderNum = (Integer) parent.getSelectedItem();
                showOrderDetails(orderNum);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Cancel order button
        btnCancelOrder.setOnClickListener(v -> {
            Integer selected = (Integer) spinnerOrderNumber.getSelectedItem();
            if (selected != null) {
                Order order = getOrderByNumber(selected);
                if (order != null) {
                    OrderManager.getInstance().cancelOrder(order);
                    refreshOrders();
                }
            } else {
                Toast.makeText(this, "Please select an order", Toast.LENGTH_SHORT).show();
            }
        });

        // Main menu button
        btnMainMenu.setOnClickListener(v -> finish());
    }

    /**
     * Displays the details of a specific order number, including items and total amount.
     *
     * @param orderNumber the number of the order to display
     */
    private void showOrderDetails(int orderNumber) {
        Order order = getOrderByNumber(orderNumber);
        if (order != null) {
            adapter.setItems(order.getItems());
            String total = NumberFormat.getCurrencyInstance(Locale.US).format(order.getTotal());
            tvTotalAmount.setText(total);
        }
    }

    /**
     * Retrieves an Order object by its order number.
     *
     * @param number the order number
     * @return the matching Order, or null if not found
     */
    private Order getOrderByNumber(int number) {
        for (Order o : OrderManager.getInstance().getPlacedOrders()) {
            if (o.getNumber() == number) return o;
        }
        return null;
    }

    /**
     * Refreshes the list of placed orders and updates the spinner and RecyclerView.
     * Called after canceling an order.
     */
    private void refreshOrders() {
        placedOrders = new ArrayList<>(OrderManager.getInstance().getPlacedOrders());
        List<Integer> orderNumbers = new ArrayList<>();
        for (Order o : placedOrders) {
            orderNumbers.add(o.getNumber());
        }
        ArrayAdapter<Integer> adapterSpinner = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, orderNumbers);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrderNumber.setAdapter(adapterSpinner);

        if (!orderNumbers.isEmpty()) {
            spinnerOrderNumber.setSelection(0);
            showOrderDetails(orderNumbers.get(0));
        } else {
            adapter.setItems(new ArrayList<>());
            tvTotalAmount.setText("");
        }
    }

    /**
     * RecyclerView Adapter for displaying the items within a selected order.
     */
    private class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.ViewHolder> {
        private List<MenuItem> items = new ArrayList<>();

        /**
         * Sets the list of menu items to display and refreshes the RecyclerView.
         *
         * @param newItems the new list of MenuItems
         */
        void setItems(List<MenuItem> newItems) {
            items = newItems;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.textView.setText(items.get(position).toString());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        /**
         * ViewHolder for displaying a single menu item in the RecyclerView.
         */
        class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            /**
             * Constructs a ViewHolder for an order item.
             *
             * @param itemView the view representing the list item
             */
            ViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(android.R.id.text1);
            }
        }
    }
}
