package edu.softmethod.ruburger;

import org.junit.Test;
import edu.softmethod.ruburger.model.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
public class SandwichTest {

    @Test
    public void testChickenWithLettuceAndTomatoes() {
        Sandwich sandwich = new Sandwich(
                Bread.WHEAT,
                Protein.CHICKEN,
                new ArrayList<>(List.of(AddOns.LETTUCE, AddOns.TOMATOES)),
                1
        );
        // CHICKEN = 8.99, LETTUCE + TOMATOES = 0.6
        assertEquals(9.59, sandwich.price(), 0.01);
    }

    @Test
    public void testSalmonWithCheeseAndAvocado() {
        Sandwich sandwich = new Sandwich(
                Bread.BRIOCHE,
                Protein.SALMON,
                new ArrayList<>(List.of(AddOns.CHEESE, AddOns.AVOCADO)),
                2
        );
        // (SALMON 9.99 + CHEESE 1.00 + AVOCADO 0.50) * 2
        assertEquals(22.98, sandwich.price(), 0.01);
    }
    @Test
    public void testRoastBeefWithEverything() {
        Sandwich sandwich = new Sandwich(
                Bread.SOURDOUGH,
                Protein.ROAST_BEEF,
                new ArrayList<>(List.of(AddOns.LETTUCE, AddOns.TOMATOES, AddOns.CHEESE, AddOns.ONIONS)),
                1
        );
        // ROAST_BEEF 10.99 + .3 + .3 + 1.0 + .3 = 12.89
        assertEquals(12.89, sandwich.price(), 0.01);
    }
}
