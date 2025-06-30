package org.example.restaurantmangement.Model;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.management.MemoryUsage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

public class Menu {

    private Stage stage;
    private static Connection connection;
    public static PreparedStatement insertMenu, insertPrice, selectMenu, selectPrice, updateName,
            updateType, updateSize, updatePrice, updateImage, delete;
    private static final String insertQueryMenu = "INSERT INTO menu (id, name, type, image) VALUES(?, ?, ?, ?)";
    private static final String insertQueryPrice = "INSERT INTO price_of_size (id, size, price) VALUES (?, ?, ?)";
    private static final String selectQueryMenu = "SELECT * FROM menu WHERE id = ?";
    private static final String selectQueryPrice = "SELECT size, price FROM price_of_size WHERE id = ?";
    private static final String updateNameQuery = "UPDATE menu SET name = ? WHERE id = ?";
    private static final String updateTypeQuery = "UPDATE menu SET type = ? WHERE id = ?";
    private static final String updateSizeQuery = "UPDATE price_of_size SET size = ? WHERE id = ? AND size = ?";
    private static final String updatePriceQuery = "UPDATE price_of_size SET price = ? WHERE id = ? AND size = ? ";
    private static final String updateImageQuery = "UPDATE menu SET image = ? WHERE id = ?";
    private static final String deleteQueryMenu = "DELETE FROM menu WHERE id = ?";


    static {
        try {
            connection = ConnectionDB.getConnection();
            insertMenu = connection.prepareStatement(insertQueryMenu);
            insertPrice = connection.prepareStatement(insertQueryPrice);
            selectMenu = connection.prepareStatement(selectQueryMenu);
            selectPrice = connection.prepareStatement(selectQueryPrice);
            updateName = connection.prepareStatement(updateNameQuery);
            updateType = connection.prepareStatement(updateTypeQuery);
            updateSize = connection.prepareStatement(updateSizeQuery);
            updatePrice = connection.prepareStatement(updatePriceQuery);
            updateImage = connection.prepareStatement(updateImageQuery);
            delete = connection.prepareStatement(deleteQueryMenu);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void insert(String id, String name, String type, HashMap<String, Double> prices) {


        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Pictures", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(stage);
        byte[] byteImage;
        if (selectedFile != null) {
            try (FileInputStream fis = new FileInputStream(selectedFile)) {
                byteImage = new byte[(int) selectedFile.length()];
                fis.read(byteImage);

                insertMenu.setString(1, id);
                insertMenu.setString(2, name);
                insertMenu.setString(3, type);
                insertMenu.setBinaryStream(4, fis, fis.available());
                insertMenu.executeUpdate();

                for (String size_ : prices.keySet()) {
                    insertPrice.setString(1, id);
                    insertPrice.setString(2, size_);
                    insertPrice.setString(3, String.valueOf(prices.get(size_)));
                    insertPrice.executeUpdate();
                }
                insertPrice.close();
                insertMenu.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public void setUpdateName(String id, String newName) {
        try {
            updateName.setString(1, newName);
            updateName.setString(2, id);
            updateName.executeUpdate();
            updateName.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void setUpdateType(String id, String type) {
        try {
            updateType.setString(1, type);
            updateType.setString(2, id);
            updateType.executeUpdate();
            updateType.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void setUpdateImage(String id) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Pictures", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            try (FileInputStream fis = new FileInputStream(selectedFile)) {
                byte[] bytes = new byte[(int) selectedFile.length()];
                fis.read(bytes);

                updateImage.setBinaryStream(1, fis, fis.available());
                updateImage.setString(2, id);
                updateImage.executeUpdate();
                updatePrice.close();

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setUpdateSize(String id, String oldSize, String newSize) {
        try {
            updateSize.setString(1, newSize);
            updateSize.setString(2, id);
            updateSize.setString(3, oldSize);
            updateSize.executeUpdate();
            updateSize.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void setUpdatePrice(String id, String size, String price) {
        try {
            updatePrice.setString(1, price);
            updatePrice.setString(2, id);
            updatePrice.setString(3, size);
            updatePrice.executeUpdate();
            updatePrice.close();


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
