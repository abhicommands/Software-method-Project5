package edu.softmethod.ruburger;

import org.junit.Test;
import edu.softmethod.ruburger.model.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BurgerTest {

    @Test
    public void testSingleBurgerWithLettuceAndOnions() {
        Burger burger = new Burger(
                Bread.PRETZEL,
                false,
                new ArrayList<>(List.of(AddOns.LETTUCE, AddOns.ONIONS)),
                1
        );
        // Actual: 6.99 + 0.30 + 0.30 = 7.59
        assertEquals(7.59, burger.price(), 0.01);
    }

    @Test
    public void testDoubleBurgerWithCheeseAndTomatoes() {
        Burger burger = new Burger(
                Bread.BRIOCHE,
                true,
                new ArrayList<>(List.of(AddOns.CHEESE, AddOns.TOMATOES)),
                2
        );
        // Actual: (9.99 + 1.00 + 0.30) * 2 = 21.58
        assertEquals(21.58, burger.price(), 0.01);
    }
}
