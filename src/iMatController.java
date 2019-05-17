/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import se.chalmers.cse.dat216.project.*;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


/**
 *
 * @author oloft
 */
public class iMatController implements Initializable, ShoppingCartListener {
    
    // Shopping Pane
    @FXML
    private AnchorPane shopPane;
    @FXML
    private TextField searchField;
    @FXML
    private Label itemsLabel;
    @FXML
    private Label costLabel;
    @FXML
    private FlowPane productsFlowPane;
    
    // Account Pane
    @FXML
    private AnchorPane accountPane;
    @FXML 
    ComboBox cardTypeCombo;
    @FXML
    private TextField numberTextField;
    @FXML
    private TextField nameTextField;
    @FXML 
    private ComboBox monthCombo;
    @FXML
    private ComboBox yearCombo;
    @FXML
    private TextField cvcField;
    @FXML
    private Label purchasesLabel;

    //Category Pane
    @FXML private TitledPane favoritesCategory;
    @FXML private TitledPane breadCategory;
    @FXML private TitledPane drinksCategory;
    @FXML private TitledPane greensCategory;
    @FXML private TitledPane meatAndFishCategory;
    @FXML private TitledPane pantryCategory;
    @FXML private TitledPane snacksCategory;

    
    // Other variables
    private final Model model = Model.getInstance();

    // Shop pane actions
    @FXML
    private void handleShowAccountAction(ActionEvent event) {
        openAccountView();
    }
    
    @FXML
    private void handleSearchAction(ActionEvent event) {
        
        List<Product> matches = model.findProducts(searchField.getText());
        updateProductList(matches);
        System.out.println("# matching products: " + matches.size());

    }
    
    @FXML
    private void handleClearCartAction(ActionEvent event) {
        model.clearShoppingCart();
    }
    
    @FXML
    private void handleBuyItemsAction(ActionEvent event) {
        if(!model.getShoppingCart().getItems().isEmpty()){
            model.placeOrder();
            costLabel.setText("Köpet klart!");
        }else{
            costLabel.setText("Lägg till varor i varukorgen först");
        }
    }
    
    // Account pane actions
     @FXML
    private void handleDoneAction(ActionEvent event) {
        closeAccountView();
    }

    // Category pane actions
    @FXML private void handleFavoriteSelectionAction(){
        handleCategorySelection("favoritesCategory");
    }
    @FXML private void handleBreadSelectionAction(){
        handleCategorySelection("breadCategory");
    }
    @FXML private void handleDrinksSelectionAction(){
        handleCategorySelection("drinksCategory");
    }
    @FXML private void handleGreensSelectionAction(){
        handleCategorySelection("greensCategory");
    }
    @FXML private void handleMeatAndFishSelectionAction(){
        handleCategorySelection("meatAndFishCategory");
    }
    @FXML private void handlePantrySelectionAction(){
        handleCategorySelection("pantryCategory");
    }
    @FXML private void handleSnacksSelectionAction(){
        handleCategorySelection("snacksCategory");
    }

    /**
     *
     * See which category was pressed and update the product pane based on that
     */
    //todo Lägg till subkategorier och så att den faktiskt ändrar de som visas
    private void handleCategorySelection(String id){
        switch(id){
            case ("favoritesCategory"): //set favorites to show
                System.out.println("favo");
                break;
            case ("breadCategory"): //set bread to show
                System.out.println("b");
                break;
            case ("drinksCategory"): //set drinks to show
                System.out.println("d");
                break;
            case ("greensCategory"): //set greens to show
                System.out.println("g");
                break;
            case ("meatAndFishCategory"): //set meatAndFish to show
                System.out.println("maf");
                break;
            case ("pantryCategory"): //set pantry to show
                System.out.println("p");
                break;
            case ("snacksCategory"): //set snacks to show
                System.out.println("s");
                break;
            default:
                System.out.println("no category matched :OOOOOOOO");
                updateProductList(model.getProducts());
        }
    }
      
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model.getShoppingCart().addShoppingCartListener(this);

        updateProductList(model.getProducts());
        updateBottomPanel();
        
        setupAccountPane();
        
    }    
    
    // Navigation
    public void openAccountView() {
        updateAccountPanel();
        accountPane.toFront();
    }

    public void closeAccountView() {
        updateCreditCard();
        shopPane.toFront();
        accountPane.toBack();
    }
    
    // Shope pane methods
    @Override
     public void shoppingCartChanged(CartEvent evt) {
        updateBottomPanel();
        updateProductCounts();
    }


   private void updateProductCounts(){
        //todo lägg till antal i varje ProductPanel som uppdateras här, kanske inte går att lösas
       // utan får göra updateProductList istället
   }
    
    private void updateProductList(List<Product> products) {

        productsFlowPane.getChildren().clear();

        for (Product product : products) {
            productsFlowPane.getChildren().add(new ProductPanel(product));
        }

    }

    private void updateBottomPanel() {
        
        ShoppingCart shoppingCart = model.getShoppingCart();
        
        itemsLabel.setText("Antal varor: " + shoppingCart.getItems().size());
        costLabel.setText("Kostnad: " + String.format("%.2f",shoppingCart.getTotal()));
        
    }
    
    private void updateAccountPanel() {
        
        CreditCard card = model.getCreditCard();
        
        numberTextField.setText(card.getCardNumber());
        nameTextField.setText(card.getHoldersName());
        
        cardTypeCombo.getSelectionModel().select(card.getCardType());
        monthCombo.getSelectionModel().select(""+card.getValidMonth());
        yearCombo.getSelectionModel().select(""+card.getValidYear());

        cvcField.setText(""+card.getVerificationCode());
        
        purchasesLabel.setText(model.getNumberOfOrders()+ " tidigare inköp hos iMat");
        
    }
    
    private void updateCreditCard() {
        
        CreditCard card = model.getCreditCard();
        
        card.setCardNumber(numberTextField.getText());
        card.setHoldersName(nameTextField.getText());
        
        String selectedValue = (String) cardTypeCombo.getSelectionModel().getSelectedItem();
        card.setCardType(selectedValue);
        
        selectedValue = (String) monthCombo.getSelectionModel().getSelectedItem();
        card.setValidMonth(Integer.parseInt(selectedValue));
        
        selectedValue = (String) yearCombo.getSelectionModel().getSelectedItem();
        card.setValidYear(Integer.parseInt(selectedValue));
        
        card.setVerificationCode(Integer.parseInt(cvcField.getText()));

    }
    
    private void setupAccountPane() {
                
        cardTypeCombo.getItems().addAll(model.getCardTypes());
        
        monthCombo.getItems().addAll(model.getMonths());
        
        yearCombo.getItems().addAll(model.getYears());
        
    }
}
