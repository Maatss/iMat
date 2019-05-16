/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Product;

import javax.swing.*;
import java.io.IOException;

/**
 *
 * @author oloft
 */
public class ProductPanel extends AnchorPane {

    @FXML ImageView imageView;
    @FXML ImageView favoriteItemImageView;

    @FXML Label nameLabel;
    @FXML Label prizeLabel;
    @FXML Label ecoLabel;
    @FXML Label countLabel;

    private Model model = Model.getInstance();

    private Product product;
    
    private final static double kImageWidth = 100.0;
    private final static double kImageRatio = 0.75;

    public ProductPanel(Product product) {
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ProductPanel.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.product = product;
        nameLabel.setText(product.getName());
        prizeLabel.setText(String.format("%.2f", product.getPrice()) + " " + product.getUnit());
        imageView.setImage(model.getImage(product, kImageWidth, kImageWidth*kImageRatio));
        if (!product.isEcological()) {
            ecoLabel.setText("");
        }
    }
    
    @FXML
    private void handleAddAction(ActionEvent event) {
        System.out.println("Add " + product.getName());
        model.addToShoppingCart(product);
    }

    @FXML
    private void handleRemoveAction(ActionEvent event){
        System.out.println("Remove " + product.getName());
        model.removeFromShoppingCart(product);
    }

    @FXML
    private void handleFavoriteAction(){
        model.toggleFavorite(product);
        String iconPath;
        if (model.checkIfFavorite(product)) {
            iconPath = "images/favoriteFilled.png";
        }else{
            iconPath = "images/favoriteEmpty.png";
        }
        favoriteItemImageView.setImage(getImage(iconPath));
    }

    private void setFavoriteItemImageView(){
        //todo kanske lägga till denna funktionen? behöver ha så den sätter rätt bild vid start av programmet
    }

    private Image getImage(String path){
        return new Image(getClass().getClassLoader().getResourceAsStream(path));
    }
}
