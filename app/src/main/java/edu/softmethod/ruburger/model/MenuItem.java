/**
 * Abstract base class for all menu items in the RU Burger ordering system.
 * <p>
 * Each menu item has a quantity and must implement a method to calculate its total price.
 * Subclasses include {@link Burger}, {@link Sandwich}, {@link Beverage}, {@link Side}, and {@link Combo}.
 * </p>
 *
 * <p>Authors: Abhinav Acharya, Aditya Rajesh</p>
 */

package edu.softmethod.ruburger.model;

import java.io.Serializable;

public abstract class MenuItem implements Serializable {
    private static final long serialVersionUID = 1L;

    /** The quantity of the menu item ordered. */
    protected int quantity;

    /**
     * Sets the quantity of the menu item.
     *
     * @param quantity the number of items
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Returns the quantity of the menu item.
     *
     * @return the quantity ordered
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Calculates and returns the total price for the item based on quantity and item-specific pricing rules.
     *
     * @return the total price
     */
    public abstract double price();
}
