package org.example.restaurantmangement.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private ImageView imageDemo;

    public void setImage(Image image) {
        imageDemo.setImage(image);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
