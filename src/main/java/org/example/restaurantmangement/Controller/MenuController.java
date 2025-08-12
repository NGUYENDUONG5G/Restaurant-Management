package org.example.restaurantmangement.Controller;


import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;


import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.restaurantmangement.Model.*;
import org.example.restaurantmangement.Model.Menu;


import javax.swing.plaf.basic.BasicBorders;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
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
    private TableColumn<Invoice, String> colDetailInvoice;
    @FXML
    private TableColumn<Invoice, String> colIdInvoice;
    @FXML
    private TableColumn<Invoice, String> colTimeInvoice;
    @FXML
    private TableColumn<Invoice, String> colValueInvoice;
    @FXML
    private DatePicker enterDate;
    @FXML
    private Button buttonFoods;
    @FXML
    private Button buttonRevenue;
    @FXML
    private Button buttonSetting;
    @FXML
    private Button buttonPrint;
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
    private ToggleButton buttonChooseModel;
    @FXML
    private HBox lineFoodsTool;
    @FXML
    private Button buttonRemoveFood;
    @FXML
    private Button buttonChooseImage;
    @FXML
    private Button buttonCancel;
    @FXML
    private ImageView logoRestaurant;
    @FXML
    private TableView<Food> tableFoods;
    @FXML
    private TableView<Invoice> tableInvoices;
    @FXML
    private AnchorPane anchorStatistic;
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
    private AnchorPane anchorDetailInvoice;
    @FXML
    private AnchorPane anchorPrint;
    @FXML
    private AnchorPane anchorEditFood;
    @FXML
    private ImageView imageFood;
    @FXML
    private ImageView imageFoodEdit;
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
    private Label showRevenueTotal;
    @FXML
    private Label showIdFood;
    @FXML
    private Label showIdFoodEdit;
    @FXML
    private Label showNameFood;
    @FXML
    private Label showPriceL;
    @FXML
    private Label showPriceM;
    @FXML
    private Label showTypeFood;
    @FXML
    private Label showIdInvoice;
    @FXML
    private Label showTimeInvoice;
    @FXML
    private Label showValueInvoice;
    @FXML
    private TableColumn<String[], String> colShowAmount;
    @FXML
    private TableColumn<String[], String> colShowId;
    @FXML
    private TableColumn<String[], String> colShowName;
    @FXML
    private TableColumn<String[], String> colShowTotal;
    @FXML
    private TableColumn<String[], String> colShowSize;
    @FXML
    private TableView<String[]> tableDetailInvoice;
    @FXML
    private ComboBox<String> buttonChooseMonth;
    @FXML
    private ComboBox<String> buttonChooseYear;
    @FXML
    private TableColumn<String[], String> colNameRevenue;
    @FXML
    private TableColumn<String[], String> colPercentRevenue;
    @FXML
    private TableColumn<String[], String> colAmountRevenue;
    @FXML
    private TableColumn<String[], String> colIdRevenue;
    @FXML
    private TableView<String[]> tableRevenue;

    private static final Map<Node, List<Node>> map = new HashMap<>();
    private ObservableList<String[]> revenueList;
    private ObservableList<Food> foodList;
    private ObservableList<Invoice> invoiceList;
    private ObservableList<String[]> infoInvoice;
    private List<Pane> paneList;
    private Food selectedFood;
    private File selectedFile;
    private Invoice selectedInvoice;
    private Alert alert;
    private HashMap<TextField, String> findSizeFood;
    private Stack<Pane> paneStack = new Stack<>();

    private void setVisiblePane(Pane pane) {
        paneStack.push(pane);
        buttonCancel.setVisible(paneStack.size() >= 2);
        for (Pane pane_ : paneList) {
            if (pane_ == pane) {
                pane_.setVisible(true);
                continue;
            }
            pane_.setVisible(false);
            setStyleItem("transparent");
            setFoodsList(null);
        }
    }

    private void register(Node extraNode, Node... mainPanes) {
        BooleanBinding binding = Bindings.createBooleanBinding(() -> {
            for (Node pane : mainPanes) {
                if (pane.isVisible()) return true;
            }
            return false;
        }, Arrays.stream(mainPanes)
                .map(Node::visibleProperty)
                .toArray(BooleanProperty[]::new));

        extraNode.visibleProperty().bind(binding);
    }

    private void setShowNode() {
        register(enterSearchFood, anchorFoods);
        register(butonSearch, anchorFoods);
        register(buttonAddFood, anchorFoods);
        register(lineFoodsTool, anchorDetailFood);
        register(anchorPrint, anchorRevenue, anchorDetailInvoice, anchorStatistic);
        register(buttonChooseModel,anchorRevenue,anchorStatistic);

    }

    private void setTableRevenue() {
        colIdRevenue.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[0]));
        colIdRevenue.setStyle("-fx-alignment: CENTER;");
        colNameRevenue.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[1]));
        colNameRevenue.setStyle("-fx-alignment: CENTER;");
        colAmountRevenue.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[2]));
        colAmountRevenue.setStyle("-fx-alignment: CENTER;");
        colPercentRevenue.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[3]));
        colPercentRevenue.setStyle("-fx-alignment: CENTER;");
        tableRevenue.setFixedCellSize(-1);
    }

    private void setTableDetailInvoice() {
        colShowId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[0]));
        colShowId.setStyle("-fx-alignment: CENTER;");
        colShowName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[1]));
        colShowName.setStyle("-fx-alignment: CENTER;");
        colShowSize.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[2]));
        colShowSize.setStyle("-fx-alignment: CENTER;");
        colShowAmount.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[3]));
        colShowAmount.setStyle("-fx-alignment: CENTER;");
        colShowTotal.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[4]));
        colShowTotal.setStyle("-fx-alignment: CENTER;");

    }

    private void setTableInvoices() {
        colIdInvoice.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getIdInvoice()));
        colIdInvoice.setStyle("-fx-alignment: CENTER;");
        colTimeInvoice.setCellValueFactory(cellData -> {
            LocalDateTime time = cellData.getValue().getDateInvoice();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            return new SimpleStringProperty(formatter.format(time));
        });
        colTimeInvoice.setStyle("-fx-alignment: CENTER;");
        colValueInvoice.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getValueInvoice())));
        colValueInvoice.setStyle("-fx-alignment: CENTER;");
        colDetailInvoice.setCellValueFactory(cellData -> {
            return new SimpleStringProperty("»");
        });
        colDetailInvoice.setStyle("-fx-alignment: CENTER;");
        tableInvoices.setFixedCellSize(-1);
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

    private void processSelectedInvoice() {
        showIdInvoice.setText("ID: " + selectedInvoice.getIdInvoice());
        showTimeInvoice.setText("DateTime: " + selectedInvoice.getDateInvoice().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        showValueInvoice.setText("Value: " + selectedInvoice.getValueInvoice() + " VNĐ");
        setInfoInvoice();
    }

    private void processSelectedFood() {
        showIdFood.setText("  " + selectedFood.getId());
        showIdFoodEdit.setText("  " + selectedFood.getId());
        showNameFood.setText("  " + selectedFood.getName());
        enterNameFood.setText(selectedFood.getName());
        showTypeFood.setText("  " + selectedFood.getType());
        enterTypeFood.setText(selectedFood.getType());
        imageFood.setImage(selectedFood.displayImage());
        imageFoodEdit.setImage(selectedFood.displayImage());
        showPriceM.setText("  " + selectedFood.getPrice("M") + "");
        enterPriceM.setText(selectedFood.getPrice("M") + "");
        showPriceL.setText("  " + selectedFood.getPrice("L") + "");
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
            System.out.println("hello");
            Menu.setInsert(id, name, type, prices, selectedFile);
            tableFoods.refresh();
            setFoodsList(null);
        } else {
            result = false;
        }
        return result;
    }

    private boolean processUpdateFood() {
        boolean res = true;
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
        if (name.isBlank() && type.isBlank() && priceM.isBlank() && priceL.isBlank()) {
            res = false;
        }
        processSelectedFood();
        tableFoods.refresh();
        setFoodsList(null);
        return res;
    }

    private void setStyleItem(String style) {
        if (!style.isBlank()) {
            enterNameFood.setStyle("-fx-border-color: " + style);
            enterTypeFood.setStyle("-fx-border-color: " + style);
            enterPriceM.setStyle("-fx-border-color: " + style);
            enterPriceL.setStyle("-fx-border-color: " + style);
        }
    }

    private void setInfoInvoice() {
        infoInvoice = FXCollections.observableArrayList();
        for (Map.Entry<String, int[]> entry : selectedInvoice.getFoodList().entrySet()) {
            String id = entry.getKey();
            int[] prices = entry.getValue();
            Food food = Menu.getFoods().get(id);
            if (prices[0] != 0) {
                infoInvoice.add(new String[]{id, food.getName(), "M", String.valueOf(prices[0]),
                        String.valueOf(prices[0] * food.getPrice("M"))});

            }
            if (prices[1] != 0) {
                infoInvoice.add(new String[]{id, food.getName(), "L", String.valueOf(prices[1]),
                        String.valueOf(prices[1] * food.getPrice("L"))});
            }
        }

        tableDetailInvoice.setItems(infoInvoice);
    }

    private void setRevenueList(String yearMonth) {
        YearMonth pattern = YearMonth.parse(yearMonth);
        HashMap<String, int[]> item = new HashMap<>();
        long totalRevenue = 0;
        for (LocalDate localDate : RevenueStatistics.getInvoicesByDate().keySet()) {
            if (YearMonth.from(localDate).equals(pattern)) {
                List<Invoice> list = RevenueStatistics.getInvoicesByDate().get(localDate);
                for (Invoice invoice : list) {
                    totalRevenue += invoice.getValueInvoice();
                    for (String id : invoice.getFoodList().keySet()) {
                        item.putIfAbsent(id, new int[2]);
                        int[] arr = item.get(id);
                        int[] amount = invoice.getFoodList().get(id);
                        Food food = Menu.getFoods().get(id);
                        arr[0] += amount[0] + amount[1];
                        arr[1] += (int) (amount[0] * food.getPrice("M") + amount[1] * food.getPrice("L"));
                    }
                }
            }

        }

        revenueList = FXCollections.observableArrayList();
        for (Map.Entry<String, int[]> entry : item.entrySet()) {
            String id = entry.getKey();
            int[] amount = entry.getValue();
            Food food = Menu.getFoods().get(id);
            revenueList.add(new String[]{id, food.getName(), String.valueOf(amount[0]),
                    String.format("%.2f%%", (double) amount[1] * 100 / totalRevenue)});
        }
        showRevenueTotal.setText("Revenue Total: " + totalRevenue + " VND");
        tableRevenue.setItems(revenueList);
    }

    private void setFoodsList(ObservableList<Food> foodList_) {
        if (foodList_ == null) {
            foodList = FXCollections.observableArrayList(Menu.getFoods().values());
        } else {
            foodList = foodList_;
        }
        tableFoods.setItems(foodList);
    }

    private void setInvoicesList(LocalDate date) {
        if (date == null) {
            invoiceList = FXCollections.observableArrayList(RevenueStatistics.getInvoiceList().values());
        } else {
            List<Invoice> Invoices = RevenueStatistics.getInvoicesByDate().get(date);
            if (Invoices != null) {
                invoiceList = FXCollections.observableArrayList(Invoices);
            } else {
                invoiceList = FXCollections.observableArrayList();
            }
        }
        tableInvoices.setItems(invoiceList);
    }

    private void setVisibleID(boolean content) {
        enterIdFood.setVisible(content);
        showIdFood.setVisible(!content);
    }

    public void setMonthAndYear() {
        buttonChooseYear.setValue(LocalDate.now().getYear() + "");
        buttonChooseMonth.setValue(LocalDate.now().getMonthValue() + "");
        setRevenueList(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")));
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
                if (enterIdFood.isVisible()) {
                    if (processSaveFood()) {
                        setVisiblePane(anchorFoods);
                    }
                } else {
                    if (processUpdateFood()) {
                        setVisiblePane(anchorFoods);
                    }
                }
            }
        }
    }

    @FXML
    void actionAnchor(ActionEvent event) {
        if (event.getSource() == buttonFoods) {
            setVisiblePane(anchorFoods);
        } else if (event.getSource() == buttonRevenue) {
            setVisiblePane(anchorRevenue);
        } else if (event.getSource() == buttonSetting) {
            setVisiblePane(anchorSetting);
        } else if (event.getSource() == buttonPrint) {
            if (anchorRevenue.isVisible()) {
                PDFExporter.exportPaneToPDF(anchorRevenue);
            } else if (anchorDetailInvoice.isVisible()) {
                PDFExporter.exportPaneToPDF(anchorDetailInvoice);
            }else if(anchorStatistic.isVisible()){
                PDFExporter.exportPaneToPDF(anchorStatistic);
            }
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
            setVisiblePane(anchorEditFood);
            setVisibleID(false);

        } else if (event.getSource() == buttonAddFood) {
            setVisibleID(true);
            enterIdFood.clear();
            enterNameFood.clear();
            enterTypeFood.clear();
            enterPriceM.clear();
            enterPriceL.clear();
            imageFoodEdit.setImage(null);
            setVisiblePane(anchorEditFood);

        }
    }

    @FXML
    void handleDateSelected(ActionEvent event) {
        LocalDate selectedDate = enterDate.getValue();
        setInvoicesList(selectedDate);
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

    @FXML
    void actionCancel(MouseEvent event) {
        if (event.getSource() == buttonCancel) {
            if (paneStack.size() >= 2) {
                paneStack.pop();
                setVisiblePane(paneStack.pop());
                buttonCancel.setVisible(paneStack.size() >= 2);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setFoodsList(null);
        setInvoicesList(null);
        setShowNode();
        findSizeFood = new HashMap<>();
        findSizeFood.put(enterPriceM, "M");
        findSizeFood.put(enterPriceL, "L");
        paneStack.push(anchorFoods);
        tableFoods.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableInvoices.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableRevenue.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        paneList = Arrays.asList(anchorFoods, anchorRevenue, anchorSetting,
                anchorDetailFood, anchorListView, anchorDetailInvoice,
                anchorStatistic, anchorEditFood);
        setTableFoods();
        setTableInvoices();
        setTableDetailInvoice();
        setTableRevenue();
        tableFoods.setItems(foodList);
        tableFoods.setRowFactory(tv -> {
            TableRow<Food> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    selectedFood = row.getItem();
                    setVisiblePane(anchorDetailFood);
                    setVisibleID(false);
                    processSelectedFood();
                } else if (!row.isEmpty() && event.getClickCount() == 1) {
                    selectedFood = row.getItem();
                    processSelectedFood();
                }
            });

            return row;
        });
        tableRevenue.setRowFactory(tv -> {
            TableRow<String[]> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    selectedFood = Menu.getFoods().get(row.getItem()[0]);
                    setVisiblePane(anchorDetailFood);
                    processSelectedFood();

                } else if (!row.isEmpty() && event.getClickCount() == 1) {

                    selectedFood = Menu.getFoods().get(row.getItem()[0]);
                    processSelectedFood();
                }
            });

            return row;
        });
        tableDetailInvoice.setRowFactory(tv -> {
            TableRow<String[]> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    selectedFood = Menu.getFoods().get(row.getItem()[0]);
                    setVisiblePane(anchorDetailFood);

                    processSelectedFood();
                } else if (!row.isEmpty() && event.getClickCount() == 1) {

                    selectedFood = Menu.getFoods().get(row.getItem()[0]);
                    processSelectedFood();
                }
            });

            return row;
        });
        tableInvoices.setRowFactory(tv -> {
            TableRow<Invoice> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    selectedInvoice = row.getItem();
                    setVisiblePane(anchorDetailInvoice);
                    processSelectedInvoice();
                }
            });
            return row;
        });
        imageFoodEdit.setOnMouseClicked(event -> {
            processChooseImage();
            if (selectedFile != null) {
                imageFoodEdit.setImage(new Image(selectedFile.toURI().toString()));
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
                processSelectedFood();
            }
        });
        buttonChooseModel.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            buttonChooseModel.setText(isSelected ? "Food Statistic" : "Invoice List");
            if (buttonChooseModel.isVisible()) {
                if (!isSelected) {
                    setVisiblePane(anchorRevenue);

                } else {
                    setVisiblePane(anchorStatistic);
                }

            }
        });
        for (int i = 1; i <= 12; i++) {
            String str;

            if (i < 10) {
                str = "0" + i;
            } else {
                str = i + "";
            }
            buttonChooseMonth.getItems().add(str);
        }
        for (int y = LocalDate.now().getYear(); y >= RevenueStatistics.getMinYear(); y--) {
            buttonChooseYear.getItems().add(String.valueOf(y));
        }
        setMonthAndYear();

        buttonChooseMonth.valueProperty().addListener((obs, oldVal, newVal) -> {
            setRevenueList(buttonChooseYear.getValue() + "-" + newVal);
        });
        buttonChooseYear.valueProperty().addListener((obs, oldVal, newVal) -> {
            setRevenueList(newVal + "-" + buttonChooseMonth.getValue());
        });

    }
}
