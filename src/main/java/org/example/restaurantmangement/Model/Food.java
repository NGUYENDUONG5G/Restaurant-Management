package org.example.restaurantmangement.Model;

import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.HashMap;

public class Food {
    private String name;
    private String id;
    private String type;
    private byte[] image;
    private HashMap<String, Long> price;

    public Food(String id, String name, String type, byte[] image) {
        this.name = name;
        this.id = id;
        this.type = type;
        this.image = image;
        this.price = new HashMap<>();
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

    public byte[] getImage() {
        return image;
    }

    public Image displayImage() {
        ByteArrayInputStream bis = new ByteArrayInputStream(image);
        return new Image(bis);
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id.toUpperCase();
    }

    public void setSize(String oldSize, String newSize) {
        price.put(newSize, price.get(oldSize));
        price.remove(oldSize);
    }

    public double getPrice(String size) {
        return this.price.get(size);
    }

    public HashMap<String, Long> getPrice() {
        return price;
    }

    public void setPrice(HashMap<String, Long> prices) {
        if (prices != null) {
            this.price = prices;
        }
    }

    public void setPrice(String size, long price) {
        this.price.put(size, price);
    }

    @Override
    public String toString() {
        return "Food{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", image=" + Arrays.toString(image) +
                ", price=" + price +
                '}';
    }
}
