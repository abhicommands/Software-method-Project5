/**
 * Represents a customer's order in the RU Burger ordering system.
 * <p>
 * An order contains a list of {@link MenuItem} objects and maintains a unique order number.
 * It provides methods to add or remove items, calculate subtotal, tax, and total.
 * </p>
 *
 * <p>Authors: Abhinav Acharya, Aditya Rajesh</p>
 */

package edu.softmethod.ruburger.model;

import java.util.ArrayList;

public class Order {

    /** Static counter to assign unique order numbers. */
    private static int nextOrderNumber = 1;

    /** The unique number for this order. */
    private final int number;

    /** The list of menu items in this order. */
    private ArrayList<MenuItem> items;

    /** The sales tax rate applied to all orders (6.625%). */
    private static final double TAX_RATE = 0.06625;

    /**
     * Constructs a new Order with a unique order number and an empty item list.
     */
    public Order() {
        this.number = nextOrderNumber++;
        this.items = new ArrayList<>();
    }

    /**
     * Adds a menu item to the order.
     *
     * @param item the menu item to add
     */
    public void addItem(MenuItem item) {
        items.add(item);
    }

    /**
     * Removes a menu item from the order.
     *
     * @param item the menu item to remove
     */
    public void removeItem(MenuItem item) {
        items.remove(item);
    }

    /**
     * Clears all items from the order.
     */
    public void clear() {
        items.clear();
    }

    /**
     * Returns the unique order number.
     *
     * @return the order number
     */
    public int getNumber() {
        return number;
    }

    /**
     * Returns the list of items in the order.
     *
     * @return a list of {@link MenuItem} objects
     */
    public ArrayList<MenuItem> getItems() {
        return items;
    }

    /**
     * Calculates the subtotal (before tax) of the order.
     *
     * @return the subtotal amount
     */
    public double getSubtotal() {
        double total = 0.0;
        for (MenuItem item : items) {
            total += item.price();
        }
        return total;
    }

    /**
     * Calculates the tax based on the subtotal and a fixed tax rate.
     *
     * @return the tax amount
     */
    public double getTax() {
        return getSubtotal() * TAX_RATE;
    }

    /**
     * Calculates the total amount including subtotal and tax.
     *
     * @return the total price of the order
     */
    public double getTotal() {
        return getSubtotal() + getTax();
    }
}
