/**
 * Represents a Burger item in the RU Burger ordering system.
 * <p>
 * A burger is a specific type of {@link Sandwich} that always uses {@link Protein#ROAST_BEEF}
 * and can be either a single or double patty. It supports various {@link AddOns} and quantity-based ordering.
 * </p>
 *
 * <p>Authors: Abhinav Acharya, Aditya Rajesh</p>
 */

package edu.softmethod.ruburger.model;

import java.util.ArrayList;

public class Burger extends Sandwich {

    /** Whether the burger has a double patty or not. */
    private boolean doublePatty;

    /** Base price for a single-patty burger. */
    private static final double SINGLE_PRICE = 6.99;

    /** Additional surcharge for double patty. */
    private static final double DOUBLE_SURCHARGE = 2.50;

    /**
     * Constructs a Burger with the specified bread, patty count, add-ons, and quantity.
     * The protein is always set to {@link Protein#ROAST_BEEF}.
     *
     * @param bread        the bread type for the burger
     * @param doublePatty  true if the burger should have a double patty, false for single
     * @param addOns       a list of additional toppings
     * @param quantity     the number of burgers to order
     */
    public Burger(Bread bread, boolean doublePatty, ArrayList<AddOns> addOns, int quantity) {
        super(bread, Protein.ROAST_BEEF, addOns, quantity);
        this.doublePatty = doublePatty;
    }

    /**
     * Calculates the total price of the burger including:
     * <ul>
     *     <li>Base price based on single or double patty</li>
     *     <li>Total price of all selected add-ons</li>
     *     <li>Multiplied by the selected quantity</li>
     * </ul>
     *
     * @return total calculated price
     */
    @Override
    public double price() {
        double basePrice = SINGLE_PRICE + (doublePatty ? DOUBLE_SURCHARGE : 0.0);

        double addOnPrice = 0.0;
        for (AddOns addOn : addOns) {
            addOnPrice += addOn.getPrice();
        }

        return (basePrice + addOnPrice) * quantity;
    }

    /**
     * Returns whether the burger is a double patty.
     *
     * @return true if double patty, false if single
     */
    public boolean isDoublePatty() {
        return doublePatty;
    }

    /**
     * Returns a formatted string representing the burger, including
     * patty type, bread, add-ons, quantity, and total price.
     *
     * @return string representation of the burger
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Burger, ");
        sb.append(doublePatty ? "double" : "single");
        sb.append(" (").append(bread.name()).append(")");
        if (!addOns.isEmpty()) {
            sb.append(" [");
            for (int i = 0; i < addOns.size(); i++) {
                sb.append(addOns.get(i).name());
                if (i < addOns.size() - 1) sb.append(", ");
            }
            sb.append("]");
        }
        sb.append(" x").append(quantity);
        sb.append(" — $").append(String.format("%.2f", this.price()));
        return sb.toString();
    }
}
