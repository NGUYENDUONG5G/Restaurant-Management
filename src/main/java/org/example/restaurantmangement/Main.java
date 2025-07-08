package org.example.restaurantmangement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.restaurantmangement.Model.Food;
import org.example.restaurantmangement.Model.Menu;

import java.io.IOException;
import java.util.HashMap;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Menu");
        stage.setScene(scene);
        stage.show();
        Menu menu = new Menu();
        Menu.setStage(stage);
//        HashMap<String, Long> hashMap = new HashMap<>();
//
//        hashMap.put("M", 10000L);
//        hashMap.put("L", 20000L);
//        menu.setInsert("dd3", "Trà chanh", "đồ uống", hashMap);
    }

    public static void main(String[] args) {

        launch();
    }
}