package com.ocado.test;

import java.util.List;
import java.util.Map;

public interface BasketSplitterFeature {
    Map<String, List<String>> split(List<String> items);
}
