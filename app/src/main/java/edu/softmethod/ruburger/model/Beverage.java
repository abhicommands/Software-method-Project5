/**
 * Represents a beverage item in the RU Burger ordering system.
 * <p>
 * A beverage has a {@link Size}, a {@link Flavor}, and a quantity.
 * The price is determined based on size and multiplied by quantity.
 * </p>
 *
 * <p>Authors: Abhinav Acharya, Aditya Rajesh</p>
 */

package edu.softmethod.ruburger.model;

public class Beverage extends MenuItem {

    /** The size of the beverage (SMALL, MEDIUM, LARGE). */
    private Size size;

    /** The flavor of the beverage (COLA, TEA, JUICE, etc.). */
    private Flavor flavor;

    private static final double SMALL_PRICE = 1.99;
    private static final double MEDIUM_PRICE = 2.49;
    private static final double LARGE_PRICE = 2.99;

    /**
     * Constructs a Beverage with the specified size, flavor, and quantity.
     *
     * @param size     the size of the beverage
     * @param flavor   the flavor of the beverage
     * @param quantity the number of beverages ordered
     */
    public Beverage(Size size, Flavor flavor, int quantity) {
        this.size = size;
        this.flavor = flavor;
        this.quantity = quantity;
    }

    /**
     * Calculates the total price of the beverage based on its size and quantity.
     *
     * @return the total price as a double
     */
    @Override
    public double price() {
        double price = 0.0;
        switch (size) {
            case SMALL:
                price = SMALL_PRICE;
                break;
            case MEDIUM:
                price = MEDIUM_PRICE;
                break;
            case LARGE:
                price = LARGE_PRICE;
                break;
        }
        return price * quantity;
    }

    /**
     * Returns the size of the beverage.
     *
     * @return the beverage size
     */
    public Size getSize() {
        return size;
    }

    /**
     * Returns the flavor of the beverage.
     *
     * @return the beverage flavor
     */
    public Flavor getFlavor() {
        return flavor;
    }

    /**
     * Returns a string representation of the beverage item, including quantity,
     * flavor, size, and total price.
     *
     * @return a formatted string describing the beverage
     */
    @Override
    public String toString() {
        return String.format("Beverage x%d: [%s, %s] — $%.2f", quantity, flavor.name(), size.name(), this.price());
    }
}
