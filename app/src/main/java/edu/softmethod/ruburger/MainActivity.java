package edu.softmethod.ruburger;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * MainActivity for RU Burger app.
 * Handles navigation to Burgers, Sandwiches, Beverages, Sides, Cart, and Orders screens.
 *
 * Authors: Abhinav Acharya, Aditya Rajesh
 */
public class MainActivity extends AppCompatActivity {

    private LinearLayout orderBurgers;
    private LinearLayout orderSandwiches;
    private LinearLayout orderBeverages;
    private LinearLayout orderSides;
    private ImageButton openCart;
    private ImageButton openOrders;

    /**
     * Called when the activity is starting.
     * Sets up the main navigation menu and view initialization.
     *
     * @param savedInstanceState previously saved state (if any)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Handle system bars padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupListeners();
    }

    /**
     * Binds UI elements to their corresponding views in the layout.
     */
    private void initializeViews() {
        orderBurgers = findViewById(R.id.orderBurgers);
        orderSandwiches = findViewById(R.id.orderSandwiches);
        orderBeverages = findViewById(R.id.orderBeverages);
        orderSides = findViewById(R.id.orderSides);
        openCart = findViewById(R.id.openCart);
        openOrders = findViewById(R.id.openOrders);
    }

    /**
     * Attaches click listeners to navigation options, launching respective activities.
     */
    private void setupListeners() {
        orderBurgers.setOnClickListener(v -> startActivity(new Intent(this, BurgersActivity.class)));
        orderSandwiches.setOnClickListener(v -> startActivity(new Intent(this, SandwichesActivity.class)));
        orderBeverages.setOnClickListener(v -> startActivity(new Intent(this, BeveragesActivity.class)));
        orderSides.setOnClickListener(v -> startActivity(new Intent(this, SidesActivity.class)));
        openCart.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
        openOrders.setOnClickListener(v -> startActivity(new Intent(this, OrdersActivity.class)));
    }
}
