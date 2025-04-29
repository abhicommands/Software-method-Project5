/**
 * Singleton class responsible for managing orders in the RU Burger ordering system.
 * <p>
 * Handles the current active order, a list of placed orders, and provides methods
 * for modifying, placing, canceling, and exporting orders to a file.
 * </p>
 *
 * <p>Authors: Abhinav Acharya, Aditya Rajesh</p>
 */

package edu.softmethod.ruburger.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class OrderManager {

    /** Singleton instance of OrderManager. */
    private static OrderManager instance;

    /** The current in-progress order. */
    private Order currentOrder;

    /** The list of all placed orders. */
    private ArrayList<Order> placedOrders;

    /** Decimal format for exporting totals. */
    private static final DecimalFormat df = new DecimalFormat("0.00");

    /**
     * Private constructor to enforce singleton pattern.
     * Initializes a new current order and an empty placed order list.
     */
    private OrderManager() {
        currentOrder = new Order();
        placedOrders = new ArrayList<>();
    }

    /**
     * Returns the singleton instance of OrderManager.
     *
     * @return the single {@code OrderManager} instance
     */
    public static OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }

    /**
     * Returns the current active order.
     *
     * @return the current {@link Order}
     */
    public Order getCurrentOrder() {
        return currentOrder;
    }

    /**
     * Returns the list of all previously placed orders.
     *
     * @return a list of {@link Order} objects
     */
    public ArrayList<Order> getPlacedOrders() {
        return placedOrders;
    }

    /**
     * Adds a {@link MenuItem} to the current order.
     *
     * @param item the item to add
     */
    public void addItemToCurrentOrder(MenuItem item) {
        currentOrder.addItem(item);
    }

    /**
     * Removes a {@link MenuItem} from the current order.
     *
     * @param item the item to remove
     */
    public void removeItemFromCurrentOrder(MenuItem item) {
        currentOrder.removeItem(item);
    }

    /**
     * Finalizes the current order and adds it to the placed orders list.
     * A new current order is initialized after placement.
     * If the current order is empty, it is not placed.
     */
    public void placeCurrentOrder() {
        if (!currentOrder.getItems().isEmpty()) {
            placedOrders.add(currentOrder);
            currentOrder = new Order(); // Create a new order with incremented number
        }
    }

    /**
     * Cancels a previously placed order.
     *
     * @param order the order to cancel and remove from the list
     */
    public void cancelOrder(Order order) {
        placedOrders.remove(order);
    }

    /**
     * Exports all placed orders to the specified file in a human-readable format.
     * Each order includes its items and total cost.
     *
     * @param file the file to write the orders to
     */
    public void exportOrders(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Order order : placedOrders) {
                writer.write("Order #" + order.getNumber() + "\n");
                for (MenuItem item : order.getItems()) {
                    writer.write("- " + item.toString() + "\n");
                }
                writer.write("Total: $" + df.format(order.getTotal()) + "\n");
                writer.write("====================================\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
