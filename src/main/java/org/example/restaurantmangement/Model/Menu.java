package org.example.restaurantmangement.Model;

import java.util.List;

public class Menu {
    List<Food> foods;

    public Menu(List<Food> foods) {
        this.foods = foods;
    }

    public List<Food> getFoods() {
        return foods;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }

}
