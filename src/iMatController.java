/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import se.chalmers.cse.dat216.project.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


/**
 * @author oloft
 */
public class iMatController implements Initializable, ShoppingCartListener {

    // Welcome Pane
    @FXML
    protected AnchorPane welcomePane; // Loot - I must use 'protected' for it to work?

    @FXML
    protected Button acceptHelpButton;

    @FXML
    protected Button denyHelpButton;

    // Shopping Pane
    @FXML
    protected Button homeButton;
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
    @FXML
    protected Button cartButton;
    @FXML
    protected ImageView cartButtonImage;

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
    @FXML
    private TitledPane favoritesCategory;
    @FXML
    private TitledPane breadCategory;
    @FXML
    private TitledPane drinksCategory;
    @FXML
    private TitledPane greensCategory;
    @FXML
    private TitledPane meatAndFishCategory;
    @FXML
    private TitledPane pantryCategory;
    @FXML
    private TitledPane snacksCategory;
    // Other variables
    private final Model model = Model.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model.getShoppingCart().addShoppingCartListener(this);

        updateProductList(model.getProducts());
        updateBottomPanel();

        setupAccountPane();
        maskTopButtons();
    }

    // Welcome pane actions
    @FXML
    protected void handleAcceptHelpAction(ActionEvent event) {
        model.setDoShowHelpWizard(true);
        welcomePane.toBack();
        shopPane.toFront();
    }

    @FXML
    protected void handleDenyHelpAction(ActionEvent event) {
        model.setDoShowHelpWizard(false);
        welcomePane.toBack();
        shopPane.toFront();
    }

    // Shop pane actions
    private void maskTopButtons() {
        maskHomeButton();
        cartButton.setClip(createRectMask(8, 4, 103, 69)); // Magic values fits the button's hitbox to image
    }

    private void maskHomeButton() {
        Circle circle = new Circle();
        double x = 124; //homeButton.getWidth(); Returns '0' for some reason
        double y = 112; //homeButton.getWidth(); Returns '0' for some reason
        circle.setCenterX(x / 2);
        circle.setCenterY(y / 2);
        circle.setRadius(32.5); //Magic number to fit mask to image (Hitbox is fit to image)

        Shape mask = circle;
        homeButton.setClip(mask);
    }

    private Shape createRectMask(double x, double y, double w, double h) {
        Rectangle rect = new Rectangle(x, y, w, h);
        Shape mask = rect;
        return mask;
    }

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
        if (!model.getShoppingCart().getItems().isEmpty()) {
            model.placeOrder();
            costLabel.setText("Köpet klart!");
        } else {
            costLabel.setText("Lägg till varor i varukorgen först");
        }
    }

    @FXML
    protected void handleShoppingCartHoverEnter() {
        cartButtonImage.setImage(new Image("images/shoppingCartDark.png"));
    }

    @FXML
    protected void handleShoppingCartHoverExit() {
        cartButtonImage.setImage(new Image("images/shoppingCart.png"));
    }

    // Account pane actions
    @FXML
    private void handleDoneAction(ActionEvent event) {
        closeAccountView();
    }

    // Category pane actions
    @FXML
    private void handleFavoriteSelectionAction() {
        handleCategorySelection("favoritesCategory");
    }

    @FXML
    private void handleBreadSelectionAction() {
        handleCategorySelection("breadCategory");
    }

    @FXML
    private void handleDrinksSelectionAction() {
        handleCategorySelection("drinksCategory");
    }

    @FXML
    private void handleGreensSelectionAction() {
        handleCategorySelection("greensCategory");
    }

    @FXML
    private void handleMeatAndFishSelectionAction() {
        handleCategorySelection("meatAndFishCategory");
    }

    @FXML
    private void handlePantrySelectionAction() {
        handleCategorySelection("pantryCategory");
    }

    @FXML
    private void handleSnacksSelectionAction() {
        handleCategorySelection("snacksCategory");
    }

    /**
     * See which category was pressed and update the product pane based on that
     */
    //todo Lägg till subkategorier och så att den faktiskt ändrar de som visas
    private void handleCategorySelection(String id) {
        String category = id;
        ProductCategory pc = ProductCategory.BERRY;
        switch (id) {
            case ("favoritesCategory"): //set favorites to show
                System.out.println("favo");
                category = "favoritesCategory";
                break;
            case ("breadCategory"): //set bread to show
                System.out.println("b");
                pc = ProductCategory.BREAD;
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
        if (category.equals("favoritesCategory")) {
            updateProductList(model.getFavoriteProducts());
        } else {
            updateProductList(model.getCategoryProducts(pc));
        }
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

    // Shop pane methods
    @Override
    public void shoppingCartChanged(CartEvent evt) {
        updateBottomPanel();
        updateProductCounts();
    }

    private void updateProductCounts() {
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
        costLabel.setText("Kostnad: " + String.format("%.2f", shoppingCart.getTotal()));

    }

    private void updateAccountPanel() {
        CreditCard card = model.getCreditCard();

        numberTextField.setText(card.getCardNumber());
        nameTextField.setText(card.getHoldersName());

        cardTypeCombo.getSelectionModel().select(card.getCardType());
        monthCombo.getSelectionModel().select("" + card.getValidMonth());
        yearCombo.getSelectionModel().select("" + card.getValidYear());

        cvcField.setText("" + card.getVerificationCode());

        purchasesLabel.setText(model.getNumberOfOrders() + " tidigare inköp hos iMat");
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
