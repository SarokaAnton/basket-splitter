package com.ocado.test;

import com.ocado.test.utils.JsonReader;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class BasketSplitter implements BasketSplitterFeature {
    private static final int NUMBER_OF_PRODUCTS_CONSTRAINT = 1000;
    private static final int NUMBER_OF_DELIVERIES_CONSTRAINT = 10;
    private static final int NUMBER_OF_ITEMS_IN_BASKET_CONSTRAINT = 100;
    private final Map<String, List<String>> configMap;

    public BasketSplitter(String absolutePathToConfigFile) {
        Map<String, List<String>> readedConfigMap = JsonReader.readConfigJson(absolutePathToConfigFile);
        checkNumberOfProductsConstraint(readedConfigMap);
        checkNumberOfDeliveriesConstraint(readedConfigMap);
        this.configMap = readedConfigMap;
    }

    @Override
    public Map<String, List<String>> split(List<String> items) {
        if (items.isEmpty()) {
            return Collections.emptyMap();
        }

        checkNumberOfItemsInBasketConstraint(items);

        Map<String, List<String>> resultDelivery = new HashMap<>();
        Map<String, List<String>> allDeliveries = getAllPossibleDeliveriesForBasket(items);

        String priorityDelivery = Collections.max(allDeliveries.entrySet(), Comparator.comparingInt(e -> e.getValue().size())).getKey();

        List<String> priorityDeliveryItems = allDeliveries.remove(priorityDelivery);
        resultDelivery.put(priorityDelivery, priorityDeliveryItems);

        items.removeAll(priorityDeliveryItems);

        resultDelivery.putAll(split(items));

        return resultDelivery;
    }

    private Map<String, List<String>> getAllPossibleDeliveriesForBasket(List<String> items) {
        Map<String, List<String>> allDeliveries = new HashMap<>();

        for (String item : items) {
            List<String> deliveries = configMap.get(item);

            if (deliveries != null) {
                for (String delivery : deliveries) {
                    allDeliveries.computeIfAbsent(delivery, k -> new ArrayList<>()).add(item);
                }
            } else {
                throw new IllegalArgumentException("There is a product in the basket without a corresponding delivery option!");
            }
        }
        return allDeliveries;
    }

    private static void checkNumberOfItemsInBasketConstraint(List<String> items) {
        if (items.size() > NUMBER_OF_ITEMS_IN_BASKET_CONSTRAINT) {
            throw new IllegalArgumentException("Number of items in basket exceeds limit!");
        }
    }

    private void checkNumberOfDeliveriesConstraint(Map<String, List<String>> configMap) {
        Set<String> deliverySet = new HashSet<>();

        for (List<String> deliveries : configMap.values()) {
            deliverySet.addAll(deliveries);
        }

        if (deliverySet.size() > NUMBER_OF_DELIVERIES_CONSTRAINT) {
            throw new IllegalArgumentException("Number of deliveries exceeds limit!");
        }
    }

    private void checkNumberOfProductsConstraint(Map<String, List<String>> configMap) {
        if (configMap.keySet().size() > NUMBER_OF_PRODUCTS_CONSTRAINT) {
            throw new IllegalArgumentException("Number of products exceeds limit!");
        }
    }
}
