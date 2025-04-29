/**
 * Represents a combo meal in the RU Burger ordering system.
 * <p>
 * A combo consists of a {@link Sandwich}, a {@link SideType} side, and a {@link Flavor} drink,
 * along with a quantity. A flat combo fee is added to the base sandwich price.
 * </p>
 *
 * <p>Authors: Abhinav Acharya, Aditya Rajesh</p>
 */

package edu.softmethod.ruburger.model;

public class Combo extends MenuItem {

    /** The sandwich included in the combo. */
    private Sandwich sandwich;

    /** The selected side item for the combo (e.g., Chips or Apple Slices). */
    private SideType sideType;

    /** The selected drink flavor for the combo. */
    private Flavor flavor;

    /** The additional fee applied to all combos. */
    private static final double COMBO_FEE = 2.00;

    /**
     * Constructs a Combo meal with the given sandwich, drink, side, and quantity.
     *
     * @param sandwich the sandwich included in the combo
     * @param flavor   the drink flavor
     * @param sideType the type of side item
     * @param quantity the number of combos
     */
    public Combo(Sandwich sandwich, Flavor flavor, SideType sideType, int quantity) {
        this.sandwich = sandwich;
        this.flavor = flavor;
        this.sideType = sideType;
        this.quantity = quantity;
    }

    /**
     * Calculates the total price of the combo meal.
     * <p>
     * Price is based on: (sandwich price + combo fee) × quantity.
     *
     * @return the total price of the combo
     */
    @Override
    public double price() {
        return (sandwich.price() + COMBO_FEE) * quantity;
    }

    /**
     * Returns a string representation of the combo,
     * including the sandwich description, selected side and drink,
     * quantity, and total price.
     *
     * @return formatted combo description
     */
    @Override
    public String toString() {
        return String.format("Combo x%d: [%s], Side: %s, Drink: %s — $%.2f",
                quantity, sandwich.toString(), sideType, flavor, this.price());
    }
}
