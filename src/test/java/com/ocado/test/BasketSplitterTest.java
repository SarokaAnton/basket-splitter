package com.ocado.test;

import com.ocado.test.utils.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BasketSplitterTest {
    BasketSplitter basketSplitter;

    @BeforeEach
    public void init() {
        basketSplitter = new BasketSplitter("src/main/resources/config.json");
    }

    @Test
    void testThrowExceptionIfNonexistentElementInBasket() {
        List<String> basket = List.of("nonexistentItem");
        assertThrows(IllegalArgumentException.class, () -> basketSplitter.split(basket), "There is a product in the basket without a corresponding delivery option!");
    }

    @Test
    void testThrowExceptionIfBasketSizeExceedsLimit() {
        List<String> basket = Collections.nCopies(101, "item");
        assertThrows(IllegalArgumentException.class, () -> basketSplitter.split(basket), "Number of items in basket exceeds limit!");
    }

    @Test
    void testResultForFirstTestBasket() {
        List<String> basket = JsonReader.readBasketFromJson("src/test/resources/basket-1.json");
        Map<String, List<String>> result = basketSplitter.split(basket);

        Map<String, List<String>> assertResult = new HashMap<>();
        assertResult.put("Pick-up point", List.of("Fond - Chocolate"));
        assertResult.put("Courier", List.of("Cocoa Butter", "Tart - Raisin And Pecan", "Table Cloth 54x72 White", "Flower - Daisies", "Cookies - Englishbay Wht"));

        assertEquals(assertResult.toString(), result.toString());
    }

    @Test
    void testResultForSecondTestBasket() {
        List<String> basket = JsonReader.readBasketFromJson("src/test/resources/basket-2.json");
        Map<String, List<String>> result = basketSplitter.split(basket);

        Map<String, List<String>> assertResult = new HashMap<>();
        assertResult.put("Same day delivery", List.of("Sauce - Mint", "Numi - Assorted Teas", "Garlic - Peeled"));
        assertResult.put("Courier", List.of("Cake - Miini Cheesecake Cherry"));
        assertResult.put("Express Collection", List.of(
                "Fond - Chocolate", "Chocolate - Unsweetened", "Nut - Almond", "Blanched, Whole",
                "Haggis", "Mushroom - Porcini Frozen", "Longan", "Bag Clear 10 Lb", "Nantucket - Pomegranate Pear",
                "Puree - Strawberry", "Apples - Spartan", "Cabbage - Nappa", "Bagel - Whole White Sesame", "Tea - Apple Green Tea"
        ));

        assertEquals(assertResult.toString(), result.toString());
    }

    @Test
    void testThrowExceptionMoreThanTenDeliveries() {
        assertThrows(IllegalArgumentException.class, () -> new BasketSplitter("src/test/resources/config_11_deliveries.json"), "Number of deliveries exceeds limit!");
    }

    @Test
    void testInitConfigMapForBasketSplitter() {
        assertDoesNotThrow(() -> new BasketSplitter("src/main/resources/config.json"));
    }
}