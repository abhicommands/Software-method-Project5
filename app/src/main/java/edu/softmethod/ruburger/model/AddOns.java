/**
 * Represents additional toppings (add-ons) that can be added to a burger or sandwich
 * in the RU Burger system.
 * <p>
 * Each add-on has an associated additional price.
 * </p>
 *
 * <p>Authors: Abhinav Acharya, Aditya Rajesh</p>
 */

package edu.softmethod.ruburger.model;

public enum AddOns {
    /** Lettuce add-on ($0.30). */
    LETTUCE(0.30),

    /** Tomato add-on ($0.30). */
    TOMATOES(0.30),

    /** Onion add-on ($0.30). */
    ONIONS(0.30),

    /** Avocado add-on ($0.50). */
    AVOCADO(0.50),

    /** Cheese add-on ($1.00). */
    CHEESE(1.00);

    /** The price of the add-on. */
    private final double price;

    /**
     * Constructs an AddOns enum with the specified price.
     *
     * @param price The price of the add-on.
     */
    AddOns(double price) {
        this.price = price;
    }

    /**
     * Returns the price of the add-on.
     *
     * @return The price as a double.
     */
    public double getPrice() {
        return price;
    }
}
