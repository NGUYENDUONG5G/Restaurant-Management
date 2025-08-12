package org.example.restaurantmangement.Model;


import java.io.*;
import java.nio.file.Files;
import java.sql.*;
import java.util.HashMap;

public class Menu {


    public static PreparedStatement insertMenu, insertPrice, selectMenu, selectPrice, updateName,
            updateType, updateSize, updatePrice, updateImage, delete;
    private static final String insertQueryMenu = "INSERT INTO menu (id, name, type, image) VALUES(?, ?, ?, ?)";
    private static final String insertQueryPrice = "INSERT INTO price_of_size (id, size, price) VALUES (?, ?, ?)";
    private static final String selectQueryMenu = "SELECT * FROM menu";
    private static final String selectQueryPrice = "SELECT size, price FROM price_of_size WHERE id = ?";
    private static final String updateNameQuery = "UPDATE menu SET name = ? WHERE id = ?";
    private static final String updateTypeQuery = "UPDATE menu SET type = ? WHERE id = ?";
    private static final String updateSizeQuery = "UPDATE price_of_size SET size = ? WHERE id = ? AND size = ?";
    private static final String updatePriceQuery = "UPDATE price_of_size SET price = ? WHERE id = ? AND size = ? ";
    private static final String updateImageQuery = "UPDATE menu SET image = ? WHERE id = ?";
    private static final String deleteQueryMenu = "DELETE FROM menu WHERE id = ?";
    private static final HashMap<String, Food> foods = new HashMap<>();

    static {
        try {
            Connection connection = ConnectionDB.getConnection();
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
            setSelectMenu();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, Food> getFoods() {
        return foods;
    }


    public static void setInsert(String id, String name, String type, HashMap<String, Long> prices, File selectedFile) {
        if (foods.containsKey(id)) {
            return;
        }
        byte[] byteImage;
        if (selectedFile != null) {
            try (FileInputStream fis = new FileInputStream(selectedFile)) {
                byteImage = Files.readAllBytes(selectedFile.toPath());
                ByteArrayInputStream bais = new ByteArrayInputStream(byteImage);
                Food food = new Food(id, name, type, byteImage);
                food.setPrice(prices);
                foods.put(id, food);

                insertMenu.setString(1, food.getId());
                insertMenu.setString(2, food.getName());
                insertMenu.setString(3, food.getType());
                insertMenu.setBinaryStream(4, fis, byteImage.length);
                insertMenu.executeUpdate();

                for (String size_ : prices.keySet()) {
                    insertPrice.setString(1, id);
                    insertPrice.setString(2, size_);
                    insertPrice.setString(3, String.valueOf(prices.get(size_)));
                    insertPrice.executeUpdate();

                }


            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public static void setUpdateName(String id, String newName) {
        try {
            id=id.trim();
            foods.get(id).setName(newName.trim());
            updateName.setString(1, foods.get(id).getName());
            updateName.setString(2, id);
            updateName.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void setUpdateType(String id, String type) {
        try {
            id=id.trim();
            foods.get(id).setType(type.trim());
            updateType.setString(1, foods.get(id).getType());
            updateType.setString(2, id);
            updateType.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void setUpdateImage(String id, File selectedFile) {

        if (selectedFile != null) {
            id=id.trim();
            try (FileInputStream fis = new FileInputStream(selectedFile)) {
                byte[] byteImage = Files.readAllBytes(selectedFile.toPath());
                ByteArrayInputStream bais = new ByteArrayInputStream(byteImage);

                updateImage.setBinaryStream(1, fis, fis.available());
                updateImage.setString(2, id);
                updateImage.executeUpdate();
                foods.get(id).setImage(byteImage);

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setUpdateSize(String id, String oldSize, String newSize) {
        try {

            updateSize.setString(1, newSize);
            updateSize.setString(2, id);
            updateSize.setString(3, oldSize);
            updateSize.executeUpdate();
            foods.get(id).setSize(oldSize, newSize);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void setUpdatePrice(String id, String size, String price) {
        try {
            id=id.trim();
            price=price.trim();
            updatePrice.setLong(1, Long.parseLong(price));
            updatePrice.setString(2, id);
            updatePrice.setString(3, size);
            updatePrice.executeUpdate();
            foods.get(id).setPrice(size, Long.parseLong(price));

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Invalid price format: " + price);
        }

    }

    public static void setSelectMenu() {
        try (ResultSet resultSet = selectMenu.executeQuery()) {
            while (resultSet.next()) {

                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String type = resultSet.getString("type");
                Blob blob = resultSet.getBlob("image");
                byte[] bytes = blob.getBytes(1, (int) blob.length());


                selectPrice.setString(1, id);
                ResultSet resultSet1 = selectPrice.executeQuery();
                Food food = new Food(id, name, type, bytes);
                while (resultSet1.next()) {
                    String size = resultSet1.getString("size");
                    long price = resultSet1.getLong("price");
                    food.setPrice(size, price);
                }
                foods.put(id, food);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setDelete(String id) {
        try {
            delete.setString(1, id);
            delete.executeUpdate();
            foods.remove(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
