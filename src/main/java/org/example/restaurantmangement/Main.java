package org.example.restaurantmangement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.restaurantmangement.Model.Menu;
import org.example.restaurantmangement.Model.RevenueStatistics;

import java.io.IOException;

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
        RevenueStatistics revenueStatistics=new RevenueStatistics();
//        HashMap<String, int[]> hashMap = new HashMap<>();
//        int[] a={2,3};
//        hashMap.put("DD1", a);
//        hashMap.put("DD2", a);
//        RevenueStatistics.setAddBill(new Bill(hashMap,1));
//RevenueStatistics.setRemoveBill(RevenueStatistics.getBillList().get("HD20250802-1"));
    }

    public static void main(String[] args) {

        launch();
    }
}