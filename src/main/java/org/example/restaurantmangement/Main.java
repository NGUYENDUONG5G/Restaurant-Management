package org.example.restaurantmangement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.restaurantmangement.Model.Food;
import org.example.restaurantmangement.Model.Menu;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Menu");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
        Menu menu = new Menu();
//        HashMap<String, Long> hashMap = new HashMap<>();
//
//        hashMap.put("M", 10000);
//        hashMap.put("L", 18000);
//        menu.setInsert("dd1", "Trà sữa", "đồ uống", hashMap,
//                new File("C:\\Users\\Admin\\Pictures\\images.png"));
    }

    public static void main(String[] args) {

        launch();
    }
}