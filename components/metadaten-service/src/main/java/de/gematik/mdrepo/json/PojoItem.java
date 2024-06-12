package de.gematik.mdrepo.json;

import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.*;

public class PojoItem {

    //private Map<String, List<String>> items = new HashMap<>();
    private Map<String, Object> items = new LinkedHashMap<>();

/*
    public Map<String, List<String>> getItems() {
        return items;
    }
*/
//    @JsonAnySetter
//    public void setItem(String key, List<String> values) {
//        this.items.put(key, values);
//    }

    public Map<String, Object> getItems() {
        return items;
    }

    @JsonAnySetter
    public void setItems(String key, Object value) {
        this.items.put(key, value);
    }

}
