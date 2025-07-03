package org.example.restaurantmangement.Controller;


import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.restaurantmangement.Model.Food;
import org.example.restaurantmangement.Model.Menu;

import java.net.URL;
import java.util.*;


public class MenuController implements Initializable {

    @FXML
    private TableColumn<Food, String> colPrice;
    @FXML
    private TableColumn<Food, String> colID;
    @FXML
    private TableColumn<Food, String> colName;
    @FXML
    private TableColumn<Food, String> colSize;
    @FXML
    private TableColumn<Food, String> colType;
    @FXML
    private Button enterFoods;
    @FXML
    private Button enterRevenue;
    @FXML
    private Button enterSetting;
    @FXML
    private TextField enterSearchFood;
    @FXML
    private Button enterAddFood;
    @FXML
    private Button enterUpdateFood;
    @FXML
    private HBox lineFoodsTool;
    @FXML
    private Button enterRemoveFood;
    @FXML
    private ImageView logoRestaurant;
    @FXML
    private TableView<Food> tableFoods;
    @FXML
    private AnchorPane anchorFoods;
    @FXML
    private AnchorPane anchorRevenue;
    @FXML
    private AnchorPane anchorSetting;
    private ObservableList<Food> foodList;
    private List<Pane> paneList;

    private void setVisiblePane(Pane pane) {
        for (Pane pane_ : paneList) {
            if (pane_ == pane) {

                pane_.setVisible(true);
                continue;
            }
            pane_.setVisible(false);
            enterSearchFood.setVisible(false);
            lineFoodsTool.setVisible(false);
        }
    }

    private void setTableFoods() {

        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colID.setStyle("-fx-alignment: CENTER;");

        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colName.setStyle("-fx-alignment: CENTER;");

        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colType.setStyle("-fx-alignment: CENTER;");


        colSize.setCellValueFactory(cellData -> {
            Food food = cellData.getValue();
            String sizes = String.join("\n", food.getPrice().keySet());
            return new SimpleStringProperty(sizes);
        });
        colSize.setCellFactory(column -> new TableCell<>() {
            private final Label label = new Label();

            {
                label.setWrapText(true);
                label.setStyle("-fx-alignment: center;");
                setGraphic(label);
                setPrefHeight(Control.USE_COMPUTED_SIZE);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    label.setText(null);
                } else {
                    label.setText(item);
                    label.setMinHeight(Control.USE_PREF_SIZE);
                }
            }
        });
        colSize.setStyle("-fx-alignment: CENTER;");

        colPrice.setCellValueFactory(cellData -> {
            Food food = cellData.getValue();
            StringBuilder builder = new StringBuilder();
            for (String size : food.getPrice().keySet()) {
                builder.append(food.getPrice().get(size)).append(" đ\n");
            }
            return new SimpleStringProperty(builder.toString().trim());
        });
        colPrice.setCellFactory(column -> new TableCell<>() {
            private final Label label = new Label();

            {
                label.setWrapText(true);
                label.setStyle("-fx-alignment: center;");
                setGraphic(label);
                setPrefHeight(Control.USE_COMPUTED_SIZE);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    label.setText(null);
                } else {
                    label.setText(item);
                    label.setMinHeight(Control.USE_PREF_SIZE);
                }
            }
        });
        colPrice.setStyle("-fx-alignment: CENTER;");

        tableFoods.setFixedCellSize(-1);
        tableFoods.setItems(foodList);

    }

    @FXML
    void actionAnchor(ActionEvent event) {
        if (event.getSource() == enterFoods) {
            setVisiblePane(anchorFoods);
            enterSearchFood.setVisible(true);
            lineFoodsTool.setVisible(true);
        } else if (event.getSource() == enterRevenue) {
            setVisiblePane(anchorRevenue);
        } else if (event.getSource() == enterSetting) {
            setVisiblePane(anchorSetting);
        }
    }

    @FXML
    void actionFood(ActionEvent event) {

    }

    @FXML
    void actionKeySearch(KeyEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        foodList = FXCollections.observableArrayList(Menu.getFoods().values());
        tableFoods.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        paneList = Arrays.asList(anchorFoods, anchorRevenue, anchorSetting);
        setTableFoods();
    }
}
