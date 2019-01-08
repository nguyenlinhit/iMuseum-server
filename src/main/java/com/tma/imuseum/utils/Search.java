package com.tma.imuseum.utils;

import java.util.HashMap;
import java.util.Map;

public class Search {

    // Split search sentence to keys
    // Search string will like: [normal string to search in current table] {[key to search]:[value to search]}
    public Map<String, String> searchOptimize(String search) {
        Map<String, String> searchParameter = new HashMap<String, String>();
        // Split by ":"
        String[] pattern = search.split(":");
        int length = pattern.length;

        if (length > 1) {
            // Get normal string 
            int last = pattern[0].lastIndexOf(" ");
            if (pattern[0].lastIndexOf(" ") > 0) {
                search = pattern[0].substring(0, last);
            } else
                search = "";

            // Get the first key
            String key = pattern[0].substring(pattern[0].lastIndexOf(" ") + 1);

            // Get the last value
            String valueLast = pattern[length - 1];
            for (int i = 1; i < length - 1; i++) {
                String value = pattern[i].substring(0, pattern[i].lastIndexOf(" "));
                searchParameter.put(key, value.trim());
                key = pattern[i].substring(pattern[i].lastIndexOf(" ") + 1);
            }
            searchParameter.put(key, valueLast.trim());
        }

        // Add normal search
        searchParameter.put("normal", search.trim());
        return searchParameter;
    }
}