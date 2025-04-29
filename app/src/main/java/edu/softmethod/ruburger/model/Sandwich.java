/**
 * Represents a customizable sandwich in the RU Burger ordering system.
 * <p>
 * A sandwich includes a type of {@link Bread}, a {@link Protein}, optional {@link AddOns},
 * and a quantity. The price is based on the selected protein and any additional add-ons.
 * </p>
 *
 * <p>Authors: Abhinav Acharya, Aditya Rajesh</p>
 */

package edu.softmethod.ruburger.model;

import java.util.ArrayList;

public class Sandwich extends MenuItem {

    /** The type of bread used for the sandwich. */
    protected Bread bread;

    /** The protein (e.g., chicken, salmon, roast beef) used in the sandwich. */
    protected Protein protein;

    /** List of optional add-ons selected by the user. */
    protected ArrayList<AddOns> addOns;

    /** Fixed price for roast beef sandwiches. */
    private static final double ROAST_BEEF_PRICE = 10.99;

    /** Fixed price for chicken sandwiches. */
    private static final double CHICKEN_PRICE = 8.99;

    /** Fixed price for salmon sandwiches. */
    private static final double SALMON_PRICE = 9.99;

    /**
     * Constructs a sandwich with specified bread, protein, add-ons, and quantity.
     *
     * @param bread    the type of bread
     * @param protein  the protein type
     * @param addOns   a list of selected add-ons
     * @param quantity the number of sandwiches ordered
     */
    public Sandwich(Bread bread, Protein protein, ArrayList<AddOns> addOns, int quantity) {
        this.bread = bread;
        this.protein = protein;
        this.addOns = addOns;
        this.quantity = quantity;
    }

    /**
     * Calculates the total price of the sandwich based on protein, add-ons, and quantity.
     *
     * @return total price of the sandwich order
     */
    @Override
    public double price() {
        double basePrice = 0.0;
        switch (protein) {
            case ROAST_BEEF:
                basePrice = ROAST_BEEF_PRICE;
                break;
            case CHICKEN:
                basePrice = CHICKEN_PRICE;
                break;
            case SALMON:
                basePrice = SALMON_PRICE;
                break;
        }

        double addOnPrice = 0.0;
        for (AddOns addOn : addOns) {
            addOnPrice += addOn.getPrice();
        }

        return (basePrice + addOnPrice) * quantity;
    }

    /**
     * Returns the bread type used for the sandwich.
     *
     * @return the selected {@link Bread}
     */
    public Bread getBread() {
        return bread;
    }

    /**
     * Returns the protein used in the sandwich.
     *
     * @return the selected {@link Protein}
     */
    public Protein getProtein() {
        return protein;
    }

    /**
     * Returns the list of selected add-ons.
     *
     * @return list of {@link AddOns}
     */
    public ArrayList<AddOns> getAddOns() {
        return addOns;
    }

    /**
     * Returns a formatted string representation of the sandwich,
     * including protein, bread, add-ons, quantity, and total price.
     *
     * @return string summary of the sandwich
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Sandwich, ");
        sb.append(protein.name()).append(" (").append(bread.name()).append(")");
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
