package org.example.restaurantmangement.Controller;


import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

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
    private Button enterEditFood;
    @FXML
    private HBox lineFoodsTool;
    @FXML
    private HBox lineDetailTool;
    @FXML
    private Button enterRemoveFood;
    @FXML
    private Button enterDeleteFood;
    @FXML
    private Button enterUpdateFood;
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
    @FXML
    private AnchorPane anchorDetailFood;
    @FXML
    private ImageView imageFood;
    @FXML
    private TextField idFood;
    @FXML
    private TextField nameFood;
    @FXML
    private TextField typeFood;
    @FXML
    private TableColumn<Map.Entry<String, Long>, String> priceItem;
    @FXML
    private TableColumn<Map.Entry<String, Long>, String> sizeItem;
    @FXML
    private TableView<Map.Entry<String, Long>> sizePrices;


    private ObservableList<Food> foodList;
    private ObservableList<Map.Entry<String, Long>> sizePriceList;
    private List<Pane> paneList;
    private Food selectedFood;
    private Alert alert;
    private boolean isEditing;

    private void setVisiblePane(Pane pane) {
        for (Pane pane_ : paneList) {
            if (pane_ == pane) {
                pane_.setVisible(true);
                continue;
            }
            pane_.setVisible(false);
            enterSearchFood.setVisible(false);
            lineFoodsTool.setVisible(false);
            setVisibleRemoveAndEdit(false, false);
            setAcceptEdit(false);
            lineDetailTool.setVisible(false);
            setStyleItem("transparent");
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


    }

    private void processSelectedFood() {
        idFood.setText(selectedFood.getId());
        nameFood.setText(selectedFood.getName());
        typeFood.setText(selectedFood.getType());
        setPriceList();
        sizePrices.setItems(sizePriceList);
        imageFood.setImage(selectedFood.displayImage());

    }

    private void setTablePrices() {
        sizeItem.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getKey())
        );
        sizeItem.setCellFactory(TextFieldTableCell.forTableColumn());
        sizeItem.setOnEditCommit(event -> {
            Map.Entry<String, Long> entry = event.getRowValue();
            String oldSize = entry.getKey();
            String newSize = event.getNewValue();

            if (!newSize.isBlank()) {
                Menu.setUpdateSize(selectedFood.getId(), oldSize, newSize);
                selectedFood.setSize(oldSize, newSize);
                sizePrices.setItems(FXCollections.observableArrayList(selectedFood.getPrice().entrySet()));
                sizeItem.setStyle("-fx-border-color: transparent");
            } else {
                sizeItem.setStyle("-fx-border-color: red");
            }
        });


        priceItem.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getValue()))
        );
        priceItem.setCellFactory(TextFieldTableCell.forTableColumn());
        priceItem.setOnEditCommit(event -> {
            Map.Entry<String, Long> entry = event.getRowValue();
            String size = entry.getKey();
            try {
                long newPrice = Long.parseLong(event.getNewValue());
                selectedFood.setPrice(size, newPrice);
                sizePrices.refresh();
                Menu.setUpdatePrice(selectedFood.getId(), size, String.valueOf(newPrice));
                priceItem.setStyle("-fx-border-color: transparent");
            } catch (NumberFormatException e) {
                priceItem.setStyle("-fx-border-color: red");
            }
        });
        sizePrices.setFixedCellSize(-1);
    }

    private void showWarning(String content) {
        alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean showConfirm(String header) {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText(header);
        alert.setContentText(null);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void setAcceptEdit(boolean content) {
        nameFood.setEditable(content);
        typeFood.setEditable(content);
        sizeItem.setEditable(content);
        priceItem.setEditable(content);
        sizePrices.setEditable(content);
        isEditing = content;

    }

    private void setVisibleRemoveAndEdit(boolean content, boolean isDetailFood) {
        if (isDetailFood) {
            enterAddFood.setVisible(!content);
        } else {
            enterAddFood.setVisible(content);
        }

        enterRemoveFood.setVisible(content);
        enterEditFood.setVisible(content);
    }

    private void processUpdateFood() {
        String id = idFood.getText();
        String name = nameFood.getText();
        String type = typeFood.getText();


        if (!name.isBlank()) {
            Menu.setUpdateName(id, name);
            nameFood.setStyle("-fx-border-color: transparent");
        } else {
            nameFood.setStyle("-fx-border-color: red");
        }

        if (!type.isBlank()) {
            Menu.setUpdateType(id, type);
            typeFood.setStyle("-fx-border-color: transparent");
        } else {
            typeFood.setStyle("-fx-border-color: red");
        }


    }

    private void setStyleItem(String style) {
        idFood.setStyle("-fx-border-color: " + style);
        nameFood.setStyle("-fx-border-color: " + style);
        typeFood.setStyle("-fx-border-color: " + style);
        priceItem.setStyle("-fx-border-color: " + style);
    }

    private void setPriceList() {
        sizePriceList = FXCollections.observableArrayList(selectedFood.getPrice().entrySet());
    }

    private void setFoodsList() {
        foodList = FXCollections.observableArrayList(Menu.getFoods().values());
        tableFoods.setItems(foodList);
    }

    @FXML
    void actionAnchor(ActionEvent event) {
        if (event.getSource() == enterFoods) {
            setVisiblePane(anchorFoods);
            setFoodsList();
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
        if (event.getSource() == enterRemoveFood) {
            if (showConfirm("Are you sure you want to delete this item?")) {
                Menu.setDelete(selectedFood.getId());
                setVisiblePane(anchorFoods);
                setFoodsList();
            }
        } else if (event.getSource() == enterEditFood) {
            setVisiblePane(anchorDetailFood);
            lineDetailTool.setVisible(true);
            setAcceptEdit(true);


        } else if (event.getSource() == enterAddFood) {

        } else if (event.getSource() == enterDeleteFood) {
            if (showConfirm("Are you sure you want to delete this item?")) {
                Menu.setDelete(selectedFood.getId());
                setVisiblePane(anchorFoods);
                setFoodsList();
            }
        } else if (event.getSource() == enterUpdateFood) {
            if (showConfirm("Are you sure you want to update this item")) {
                processUpdateFood();
            }
        }
    }

    @FXML
    void actionKeySearch(KeyEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isEditing = false;
        setFoodsList();
        tableFoods.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        sizePrices.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        paneList = Arrays.asList(anchorFoods, anchorRevenue, anchorSetting, anchorDetailFood);
        setTableFoods();
        setTablePrices();
        tableFoods.setItems(foodList);
        tableFoods.setRowFactory(tv -> {
            TableRow<Food> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    selectedFood = row.getItem();
                    setVisiblePane(anchorDetailFood);
                    lineFoodsTool.setVisible(true);
                    setVisibleRemoveAndEdit(true, true);
                    processSelectedFood();
                } else if (!row.isEmpty() && event.getClickCount() == 1) {
                    setVisibleRemoveAndEdit(true, false);
                    setAcceptEdit(true);
                    selectedFood = row.getItem();
                    processSelectedFood();
                }
            });

            return row;
        });

        imageFood.setOnMouseClicked(event -> {
            if (isEditing) {
                Menu.setUpdateImage(selectedFood.getId());
            }
        });


    }
}
