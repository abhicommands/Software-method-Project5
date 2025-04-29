/**
 * Represents a side item in the RU Burger ordering system.
 * <p>
 * A side includes a {@link SideType} (e.g., fries, chips, etc.), a {@link Size},
 * and a quantity. Pricing is based on the type of side and its size, with specific
 * base prices and additional costs for medium and large sizes.
 * </p>
 *
 * <p>Authors: Abhinav Acharya, Aditya Rajesh</p>
 */

package edu.softmethod.ruburger.model;

public class Side extends MenuItem {

    /** The size of the side (SMALL, MEDIUM, LARGE). */
    private Size size;

    /** The type of side (CHIPS, FRIES, etc.). */
    private SideType type;

    /** Base price for chips. */
    private static final double BASE_CHIPS = 1.99;

    /** Base price for fries. */
    private static final double BASE_FRIES = 2.49;

    /** Base price for onion rings. */
    private static final double BASE_ONION_RINGS = 3.29;

    /** Base price for apple slices. */
    private static final double BASE_APPLE = 1.29;

    /** Price added for upgrading to medium size. */
    private static final double SIZE_MEDIUM_ADD = 0.50;

    /** Price added for upgrading to large size. */
    private static final double SIZE_LARGE_ADD = 1.00;

    /**
     * Constructs a Side object with the specified type, size, and quantity.
     *
     * @param type     the type of side
     * @param size     the size of the side
     * @param quantity the quantity ordered
     */
    public Side(SideType type, Size size, int quantity) {
        this.type = type;
        this.size = size;
        this.quantity = quantity;
    }

    /**
     * Calculates the total price of the side item.
     * <p>
     * Total price = (base price based on side type + size adjustment) × quantity.
     *
     * @return total cost of the side order
     */
    @Override
    public double price() {
        double basePrice = 0.0;
        switch (type) {
            case CHIPS:
                basePrice = BASE_CHIPS;
                break;
            case FRIES:
                basePrice = BASE_FRIES;
                break;
            case ONION_RINGS:
                basePrice = BASE_ONION_RINGS;
                break;
            case APPLE_SLICES:
                basePrice = BASE_APPLE;
                break;
        }

        double sizeAdjustment = 0.0;
        switch (size) {
            case SMALL:
                sizeAdjustment = 0.0;
                break;
            case MEDIUM:
                sizeAdjustment = SIZE_MEDIUM_ADD;
                break;
            case LARGE:
                sizeAdjustment = SIZE_LARGE_ADD;
                break;
        }

        return (basePrice + sizeAdjustment) * quantity;
    }

    /**
     * Returns the type of side item.
     *
     * @return the selected {@link SideType}
     */
    public SideType getType() {
        return type;
    }

    /**
     * Returns the size of the side item.
     *
     * @return the selected {@link Size}
     */
    public Size getSize() {
        return size;
    }

    /**
     * Returns a formatted string representation of the side item,
     * including type, size, quantity, and total price.
     *
     * @return formatted string of the side item
     */
    @Override
    public String toString() {
        return String.format("Side x%d: [%s, %s] — $%.2f", quantity, type.name(), size.name(), this.price());
    }
}
