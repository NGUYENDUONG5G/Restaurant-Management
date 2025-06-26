package org.example.restaurantmangement.Model;

import java.util.Date;
import java.util.HashMap;

public class Food {
    private String name;
    private String id;
    private String type;
    private String size;
    private static String[] sizes = {"M", "L"};
    private HashMap<String, Double> price;

    public Food(String name, String id, String type, String size) {
        this.name = name;
        this.id = id;
        this.type = type;
        this.size = size;

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type.toUpperCase();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {

        this.name = capitalizeSentences(name);
    }

    private static String capitalizeSentences(String text) {
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (capitalizeNext && Character.isLetter(ch)) {
                result.append(Character.toUpperCase(ch));
                capitalizeNext = false;
            } else {
                result.append(ch);
            }
            if (ch == '.' || ch == '!' || ch == '?') {
                capitalizeNext = true;
            }
        }

        return result.toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id.toUpperCase();
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size.toUpperCase();
    }

    public double getPrice(String size) {
        return this.price.get(size);
    }

    public void setPrice(String size, double price) {
        this.price.put(size, price);
    }
}
