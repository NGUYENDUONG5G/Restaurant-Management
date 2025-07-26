package org.example.restaurantmangement.Controller;


import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;


import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.restaurantmangement.Model.Food;
import org.example.restaurantmangement.Model.Menu;


import java.io.File;
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
    private Button buttonFoods;
    @FXML
    private Button buttonRevenue;
    @FXML
    private Button buttonSetting;
    @FXML
    private TextField enterSearchFood;
    @FXML
    private Button buttonSave;
    @FXML
    private Button butonSearch;
    @FXML
    private Button buttonAddFood;
    @FXML
    private Button buttonEditFood;
    @FXML
    private HBox lineFoodsTool;
    @FXML
    private HBox lineDetailTool;
    @FXML
    private Button buttonRemoveFood;
    @FXML
    private Button buttonDeleteFood;
    @FXML
    private Button buttonUpdateFood;
    @FXML
    private Button buttonChooseImage;
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
    private AnchorPane anchorListView;
    @FXML
    private ImageView imageFood;
    @FXML
    private TextField enterIdFood;
    @FXML
    private TextField enterNameFood;
    @FXML
    private TextField enterTypeFood;
    @FXML
    private TextField enterPriceL;
    @FXML
    private TextField enterPriceM;
    @FXML
    private ListView<String> resultsSearch;
    @FXML
    private Label showIdFood;
    @FXML
    private Label showNameFood;
    @FXML
    private Label showPriceL;
    @FXML
    private Label showPriceM;
    @FXML
    private Label showTypeFood;


    private ObservableList<Food> foodList;
    private List<Pane> paneList;
    private Food selectedFood;
    private File selectedFile;
    private Alert alert;
    private boolean isEditing;
    private HashMap<TextField, String> findSizeFood;

    private void setVisiblePane(Pane pane) {
        for (Pane pane_ : paneList) {
            if (pane_ == pane) {
                pane_.setVisible(true);
                continue;
            }
            pane_.setVisible(false);
            enterSearchFood.setVisible(false);
            butonSearch.setVisible(false);
            setAcceptEdit(false);
            buttonAddFood.setVisible(false);
            lineFoodsTool.setVisible(false);
            lineDetailTool.setVisible(false);
            setStyleItem("transparent");
            setFoodsList(null);
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
        showIdFood.setText(selectedFood.getId());
        showNameFood.setText(selectedFood.getName());
        enterNameFood.setText(selectedFood.getName());
        showTypeFood.setText(selectedFood.getType());
        enterTypeFood.setText(selectedFood.getType());
        imageFood.setImage(selectedFood.displayImage());
        showPriceM.setText(selectedFood.getPrice("M") + "");
        enterPriceM.setText(selectedFood.getPrice("M") + "");
        showPriceL.setText(selectedFood.getPrice("L") + "");
        enterPriceL.setText(selectedFood.getPrice("L") + "");

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
        enterNameFood.setVisible(content);
        enterTypeFood.setVisible(content);
        enterPriceL.setVisible(content);
        enterPriceM.setVisible(content);
        buttonChooseImage.setVisible(content);
        showNameFood.setVisible(!content);
        showTypeFood.setVisible(!content);
        showPriceM.setVisible(!content);
        showPriceL.setVisible(!content);
        isEditing = content;
        lineDetailTool.setVisible(content);
        lineFoodsTool.setVisible(!content);
    }

    private boolean processSaveFood() {
        boolean result = true;
        String id = enterIdFood.getText();
        String name = enterNameFood.getText();
        String type = enterTypeFood.getText();
        String priceM = enterPriceM.getText();
        String priceL = enterPriceL.getText();

        if (!id.isBlank()) {
            enterIdFood.setStyle("-fx-border-color: transparent");
        } else {
            enterIdFood.setStyle("-fx-border-color: red");

        }
        if (!name.isBlank()) {
            enterNameFood.setStyle("-fx-border-color: transparent");
        } else {
            enterNameFood.setStyle("-fx-border-color: red");
        }

        if (!type.isBlank()) {
            enterTypeFood.setStyle("-fx-border-color: transparent");
        } else {
            enterTypeFood.setStyle("-fx-border-color: red");
        }
        if (selectedFile != null) {
            buttonChooseImage.setStyle("-fx-border-color: transparent");
        } else {
            buttonChooseImage.setStyle("-fx-border-color: red");
        }
        HashMap<String, Long> prices = new HashMap<>();
        try {
            if (!priceM.isBlank()) {

                prices.put("M", Long.parseLong(priceM));
                enterPriceM.setStyle("-fx-border-color: transparent");
            } else {
                enterPriceM.setStyle("-fx-border-color: red");
            }
        } catch (NumberFormatException e) {
            enterPriceM.setStyle("-fx-border-color: red");
        }
        try {
            if (!priceL.isBlank()) {

                prices.put("L", Long.parseLong(priceL));
                enterPriceL.setStyle("-fx-border-color: transparent");
            } else {
                enterPriceL.setStyle("-fx-border-color: red");
            }
        } catch (NumberFormatException e) {
            enterPriceL.setStyle("-fx-border-color: red");
        }
        if (!name.isBlank() && !type.isBlank() && !priceM.isBlank() && !priceL.isBlank()) {
            Menu.setInsert(id, name, type, prices, selectedFile);
            tableFoods.refresh();
            setFoodsList(null);
        } else {
            result = false;
        }
        return result;
    }

    private void processUpdateFood() {
        String id = showIdFood.getText();
        String name = enterNameFood.getText();
        String type = enterTypeFood.getText();
        String priceM = enterPriceM.getText();
        String priceL = enterPriceL.getText();
        if (!name.isBlank()) {
            Menu.setUpdateName(id, name);
            enterNameFood.setStyle("-fx-border-color: transparent");
        } else {
            enterNameFood.setStyle("-fx-border-color: red");
        }

        if (!type.isBlank()) {
            Menu.setUpdateType(id, type);
            enterTypeFood.setStyle("-fx-border-color: transparent");
        } else {
            enterTypeFood.setStyle("-fx-border-color: red");
        }
        if (selectedFile != null) {
            Menu.setUpdateImage(id, selectedFile);
        }
        try {
            if (!priceM.isBlank()) {
                Long.parseLong(priceM);
                Menu.setUpdatePrice(id, findSizeFood.get(enterPriceM), priceM);
                enterPriceM.setStyle("-fx-border-color: transparent");
            } else {
                enterPriceM.setStyle("-fx-border-color: red");
            }
        } catch (NumberFormatException e) {
            enterPriceM.setStyle("-fx-border-color: red");
        }
        try {
            if (!priceL.isBlank()) {
                Long.parseLong(priceL);
                Menu.setUpdatePrice(id, findSizeFood.get(enterPriceL), priceL);
                enterPriceL.setStyle("-fx-border-color: transparent");
            } else {

                enterPriceL.setStyle("-fx-border-color: red");
            }
        } catch (NumberFormatException e) {
            enterPriceL.setStyle("-fx-border-color: red");
        }
        if (!name.isBlank() && !type.isBlank() && !priceM.isBlank() && !priceL.isBlank()) {
            setAcceptEdit(false);
        }
        processSelectedFood();
        tableFoods.refresh();
        setFoodsList(null);
    }

    private void setStyleItem(String style) {
        enterNameFood.setStyle("-fx-border-color: " + style);
        enterTypeFood.setStyle("-fx-border-color: " + style);
        enterPriceM.setStyle("-fx-border-color: " + style);
        enterPriceL.setStyle("-fx-border-color: " + style);
    }


    private void setFoodsList(ObservableList<Food> foodList_) {
        if (foodList_ == null) {
            foodList = FXCollections.observableArrayList(Menu.getFoods().values());
        } else {
            foodList = foodList_;
        }
        tableFoods.setItems(foodList);
    }

    private void setVisibleId(boolean content) {
        showIdFood.setVisible(content);
        enterIdFood.setVisible(!content);
        buttonSave.setVisible(!content);
    }

    private void processChooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Pictures", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        selectedFile = fileChooser.showOpenDialog((Stage) tableFoods.getScene().getWindow());

    }

    @FXML
    void actionChooseImage(MouseEvent event) {
        if (event.getSource() == buttonChooseImage) {
            processChooseImage();
            if (selectedFile != null) {
                imageFood.setImage(new Image(selectedFile.toURI().toString()));
            }
        }
    }

    @FXML
    void actionSaveInfo(MouseEvent event) {
        if (event.getSource() == buttonSave) {
            if (showConfirm("Are you sure you want to save?")) {
                if (processSaveFood()) {
                    setVisiblePane(anchorFoods);
                }
            }
        }
    }

    @FXML
    void actionAnchor(ActionEvent event) {
        if (event.getSource() == buttonFoods) {
            setVisiblePane(anchorFoods);
            enterSearchFood.setVisible(true);
            butonSearch.setVisible(true);
            buttonAddFood.setVisible(true);

        } else if (event.getSource() == buttonRevenue) {
            setVisiblePane(anchorRevenue);
        } else if (event.getSource() == buttonSetting) {
            setVisiblePane(anchorSetting);
        }
    }

    @FXML
    void actionFood(ActionEvent event) {
        if (event.getSource() == buttonRemoveFood) {
            if (showConfirm("Are you sure you want to delete this item?")) {
                Menu.setDelete(selectedFood.getId());
                setVisiblePane(anchorFoods);
                enterSearchFood.setVisible(true);
                butonSearch.setVisible(true);

            }
        } else if (event.getSource() == buttonEditFood) {
            setVisiblePane(anchorDetailFood);
            lineDetailTool.setVisible(true);
            setAcceptEdit(true);
            setVisibleId(true);


        } else if (event.getSource() == buttonAddFood) {
            enterIdFood.clear();
            enterNameFood.clear();
            enterTypeFood.clear();
            enterPriceM.clear();
            enterPriceL.clear();
            imageFood.setImage(null);
            setVisiblePane(anchorDetailFood);
            setAcceptEdit(true);
            buttonChooseImage.setVisible(true);
            setVisibleId(false);
            lineDetailTool.setVisible(false);
        } else if (event.getSource() == buttonDeleteFood) {
            if (showConfirm("Are you sure you want to delete this item?")) {
                Menu.setDelete(selectedFood.getId());
                setVisiblePane(anchorFoods);
                enterSearchFood.setVisible(true);
                butonSearch.setVisible(true);

            }
        } else if (event.getSource() == buttonUpdateFood) {
            if (showConfirm("Are you sure you want to update this item")) {
                processUpdateFood();
            }
        }
    }

    @FXML
    void actionKeySearch(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String searchInfo = enterSearchFood.getText();
            ObservableList<Food> filteredSuggestions = FXCollections.observableArrayList();
            for (Food food : Menu.getFoods().values()) {
                String suggestion = food.toString();
                if (suggestion.toLowerCase().contains(searchInfo.toLowerCase())) {
                    filteredSuggestions.add(food);
                }
            }
            setFoodsList(filteredSuggestions);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isEditing = false;
        setFoodsList(null);
        findSizeFood = new HashMap<>();
        findSizeFood.put(enterPriceM, "M");
        findSizeFood.put(enterPriceL, "L");
        tableFoods.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        paneList = Arrays.asList(anchorFoods, anchorRevenue, anchorSetting, anchorDetailFood, anchorListView);
        setTableFoods();
        tableFoods.setItems(foodList);
        tableFoods.setRowFactory(tv -> {
            TableRow<Food> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    selectedFood = row.getItem();
                    setVisiblePane(anchorDetailFood);
                    lineFoodsTool.setVisible(true);
                    processSelectedFood();
                    setVisibleId(true);
                } else if (!row.isEmpty() && event.getClickCount() == 1) {
                    lineFoodsTool.setVisible(true);
                    selectedFood = row.getItem();
                    processSelectedFood();
                }
            });

            return row;
        });

        imageFood.setOnMouseClicked(event -> {
            if (isEditing) {
                processChooseImage();
                if (selectedFile != null) {
                    imageFood.setImage(new Image(selectedFile.toURI().toString()));
                }
            }
        });

        enterSearchFood.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                anchorListView.setVisible(false);
            } else {
                ObservableList<String> filteredSuggestions = FXCollections.observableArrayList();
                for (Food food : Menu.getFoods().values()) {
                    String suggestion = food.toString();
                    if (suggestion.toLowerCase().contains(newValue.toLowerCase())) {
                        filteredSuggestions.add(suggestion);
                    }
                }

                resultsSearch.setItems(filteredSuggestions);
                anchorListView.setVisible(!filteredSuggestions.isEmpty());

            }
        });

        resultsSearch.setOnMouseClicked(event -> {
            String selectedItem = resultsSearch.getSelectionModel().getSelectedItem();
            String[] splitStr = selectedItem.split(" | ");
            if (selectedItem != null) {
                selectedFood = Menu.getFoods().get(splitStr[0]);
                setVisiblePane(anchorDetailFood);
                setVisibleId(true);
                lineFoodsTool.setVisible(true);
                processSelectedFood();
            }
        });
    }
}
