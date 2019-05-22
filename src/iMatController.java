/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
import java.util.ArrayList;
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
    private ScrollPane productsScrollPane;
    @FXML
    protected Button cartButton;
    @FXML
    protected ImageView cartButtonImage;

    // Cart pane
    @FXML
    private AnchorPane cartPane;
    @FXML
    private AnchorPane cartPopupPane;
    @FXML
    private Button cartCloseButton;
    @FXML
    private Label cartTotalPriceLabel;
    @FXML
    private FlowPane cartProductsFlowPane;

    // Checkout pane ONE
    @FXML
    private AnchorPane checkoutPane;

    // Checkout pane TWO
    @FXML
    private AnchorPane checkoutTwoPane;

    // Thanks for your purchase pane
    @FXML
    private AnchorPane successfulPurchasePane;

    // Account Pane
    @FXML
    private AnchorPane accountPane;
    @FXML
    private ComboBox cardTypeCombo;
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
        updateTotalPrice();
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
        String searchStr = searchField.getText();
        if (searchStr.length() > 0) {
            List<Product> matches = model.findProducts(searchField.getText());
            updateProductList(matches);
            System.out.println("# matching products: " + matches.size());
        } else {
            searchField.setText("");
            cartTotalPriceLabel.requestFocus();
        }
    }

    @FXML
    private void handleBuyItemsAction(ActionEvent event) {
        if (!model.getShoppingCart().getItems().isEmpty()) {
            model.placeOrder();
            costLabel.setText("Köpet klart!");
        } else {
            costLabel.setText("Lägg till varor i varukorgen först");
        }
        closeCheckoutViewTwo();
        openSuccessfulPurchaseView();
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

    @FXML
    private void handleColdDrinksSelectionAction() {
        handleCategorySelection("coldDrinksCategory");
    }

    @FXML
    private void handleHotDrinksSelectionAction() {
        handleCategorySelection("hotDrinksCategory");
    }

    @FXML
    private void handleBerriesSelectionAction() {
        handleCategorySelection("berriesCategory");
    }

    @FXML
    private void handleFruitsSelectionAction() {
        handleCategorySelection("fruitsCategory");
    }

    @FXML
    private void handleVegetablesSelectionAction() {
        handleCategorySelection("vegetablesCategory");
    }

    @FXML
    private void handleCabbageSelectionAction() {
        handleCategorySelection("cabbageCategory");
    }

    @FXML
    private void handleRootsSelectionAction() {
        handleCategorySelection("rootsCategory");
    }

    @FXML
    private void handleFishSelectionAction() {
        handleCategorySelection("fishCategory");
    }

    @FXML
    private void handleMeatSelectionAction() {
        handleCategorySelection("meatCategory");
    }

    @FXML
    private void handleBakingSelectionAction() {
        handleCategorySelection("bakingCategory");
    }

    @FXML
    private void handleSpicesSelectionAction() {
        handleCategorySelection("spicesCategory");
    }

    @FXML
    private void handlePodsSelectionAction() {
        handleCategorySelection("podsCategory");
    }

    @FXML
    private void handlePastaSelectionAction() {
        handleCategorySelection("pastaCategory");
    }

    @FXML
    private void handlePotatoAndRiceSelectionAction() {
        handleCategorySelection("potatoAndRiceCategory");
    }

    @FXML
    private void handleNutsAndSeedsSelectionAction() {
        handleCategorySelection("nutsAndSeedsCategory");
    }

    @FXML
    private void handleSweetsSelectionAction() {
        handleCategorySelection("sweetsCategory");
    }

    /**
     * See which category was pressed and update the product pane based on that
     */
    private void handleCategorySelection(String id) {
        String category = id;
        ProductCategory pc = null;
        List<Product> combinedProductList = new ArrayList<>();
        switch (id) {
            case ("favoritesCategory"): //set favorites to show
                category = "favoritesCategory";
                break;
            case ("breadCategory"): //set bread to show
                pc = ProductCategory.BREAD;
                break;
            case ("drinksCategory"): //set drinks to show
                combinedProductList.addAll(model.getCategoryProducts(ProductCategory.COLD_DRINKS)); //fix this
                combinedProductList.addAll(model.getCategoryProducts(ProductCategory.HOT_DRINKS));
                break;
            case ("greensCategory"): //set greens to show
                combinedProductList.addAll(model.getCategoryProducts(ProductCategory.BERRY));
                combinedProductList.addAll(model.getCategoryProducts(ProductCategory.FRUIT));
                combinedProductList.addAll(model.getCategoryProducts(ProductCategory.CITRUS_FRUIT));
                combinedProductList.addAll(model.getCategoryProducts(ProductCategory.EXOTIC_FRUIT));
                combinedProductList.addAll(model.getCategoryProducts(ProductCategory.MELONS));
                combinedProductList.addAll(model.getCategoryProducts(ProductCategory.VEGETABLE_FRUIT));
                combinedProductList.addAll(model.getCategoryProducts(ProductCategory.CABBAGE));
                combinedProductList.addAll(model.getCategoryProducts(ProductCategory.ROOT_VEGETABLE));
                break;
            case ("meatAndFishCategory"): //set meatAndFish to show
                combinedProductList.addAll(model.getCategoryProducts(ProductCategory.FISH));
                combinedProductList.addAll(model.getCategoryProducts(ProductCategory.MEAT));
                break;
            case ("pantryCategory"): //set pantry to show
                combinedProductList.addAll(model.getCategoryProducts(ProductCategory.FLOUR_SUGAR_SALT));
                combinedProductList.addAll(model.getCategoryProducts(ProductCategory.HERB));
                combinedProductList.addAll(model.getCategoryProducts(ProductCategory.POD));
                combinedProductList.addAll(model.getCategoryProducts(ProductCategory.PASTA));
                combinedProductList.addAll(model.getCategoryProducts(ProductCategory.POTATO_RICE));
                break;
            case ("snacksCategory"): //set snacks to show
                combinedProductList.addAll(model.getCategoryProducts(ProductCategory.NUTS_AND_SEEDS));
                combinedProductList.addAll(model.getCategoryProducts(ProductCategory.SWEET));
                break;
            case ("coldDrinksCategory"):
                pc = ProductCategory.COLD_DRINKS;
                break;
            case ("hotDrinksCategory"):
                pc = ProductCategory.HOT_DRINKS;
                break;
            case ("berriesCategory"):
                pc = ProductCategory.BERRY;
                break;
            case ("fruitsCategory"):
                combinedProductList.addAll(model.getCategoryProducts(ProductCategory.FRUIT));
                combinedProductList.addAll(model.getCategoryProducts(ProductCategory.CITRUS_FRUIT));
                combinedProductList.addAll(model.getCategoryProducts(ProductCategory.EXOTIC_FRUIT));
                combinedProductList.addAll(model.getCategoryProducts(ProductCategory.MELONS));
                break;
            case ("vegetablesCategory"):
                pc = ProductCategory.VEGETABLE_FRUIT;
                break;
            case ("cabbageCategory"):
                pc = ProductCategory.CABBAGE;
                break;
            case ("rootsCategory"):
                pc = ProductCategory.ROOT_VEGETABLE;
                break;
            case ("meatCategory"):
                pc = ProductCategory.MEAT;
                break;
            case ("fishCategory"):
                pc = ProductCategory.FISH;
                break;
            case ("bakingCategory"):
                pc = ProductCategory.FLOUR_SUGAR_SALT;
                break;
            case ("spicesCategory"):
                pc = ProductCategory.HERB;
                break;
            case ("podsCategory"):
                pc = ProductCategory.POD;
                break;
            case ("pastaCategory"):
                pc = ProductCategory.PASTA;
                break;
            case ("potatoAndRiceCategory"):
                pc = ProductCategory.POTATO_RICE;
                break;
            case ("nutsAndSeedsCategory"):
                pc = ProductCategory.NUTS_AND_SEEDS;
                break;
            case ("sweetsCategory"):
                pc = ProductCategory.SWEET;
                break;
            default:
                System.out.println("no category matched :OOOOOOOO");
                System.out.println("showing all products");
                updateProductList(model.getProducts());
        }
        if (category.equals("favoritesCategory")) {
            updateProductList(model.getFavoriteProducts());
        } else if (pc != null) {
            updateProductList(model.getCategoryProducts(pc));
        } else { //show multiple subcategories
            updateProductList(combinedProductList);
        }
        productsScrollPane.setVvalue(0); //scroll to top
    }

    // Cart Pane actions
    @FXML
    private void handleClearCartAction(ActionEvent event) {
        model.clearShoppingCart();
        cartProductsFlowPane.getChildren().clear();
        updateProductCounts();
    }

    // Checkout Pane actions
    public void returnToCartViewFromCheckout() {
        closeCheckoutView();
        closeCheckoutViewTwo();
        openCartView();
    }

    public void returnToTimeSelectAction() {
        closeCheckoutViewTwo();
        openCheckoutView();
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

    public void openCartView() {
        updateCartViewProducts();
        cartPane.toFront();
    }

    public void closeCartView() {
        shopPane.toFront();
        cartPane.toBack();
    }

    public void openCheckoutView() {
        closeCartView();
        checkoutPane.toFront();
    }

    public void closeCheckoutView() {
        checkoutPane.toBack();
    }

    public void openCheckoutViewTwo() {
        checkoutPane.toBack();
        checkoutTwoPane.toFront();
    }

    public void closeCheckoutViewTwo() {
        checkoutTwoPane.toBack();
    }

    private void openSuccessfulPurchaseView(){
        closeCheckoutViewTwo();
        successfulPurchasePane.toFront();
    }

    public void closeSuccessfulPurchaseView(){
        successfulPurchasePane.toBack();
    }

    public void closePurchaseOpenRecipe(){
        closeSuccessfulPurchaseView();
        openReceiptsView();
    }

    private void openReceiptsView(){
        //todo / other branch
    }

    private void closeReceiptsView(){
        //todo / other branch
    }

    // Shop pane methods
    @Override
    public void shoppingCartChanged(CartEvent evt) {
        updateBottomPanel();
        updateProductCounts();
        updateTotalPrice();
    }

    private void updateProductCounts() {
        List<Node> productList = productsFlowPane.getChildren();
        ProductPanel current;
        for (Node n :
                productList) {
            current = (ProductPanel) n;
            current.updateCountLabel(true);
        }
    }

    private void updateProductList(List<Product> products) {
        productsFlowPane.getChildren().clear();

        for (Product product : products) {
            productsFlowPane.getChildren().add(new ProductPanel(product));
        }
    }

    private void updateCartViewProducts() {
        List<Product> productList = new ArrayList<>();
        Product temp;

        cartProductsFlowPane.getChildren().clear();
        ShoppingCart shoppingCart = model.getShoppingCart();

        for (ShoppingItem si : shoppingCart.getItems()) {
            temp = si.getProduct();
            if (!productList.contains(si.getProduct())) {
                cartProductsFlowPane.getChildren().add(new PricePanel(temp));
            }
            productList.add(si.getProduct());
        }
    }

    private void updateBottomPanel() {
        ShoppingCart shoppingCart = model.getShoppingCart();

        itemsLabel.setText("Antal varor: " + shoppingCart.getItems().size());
        costLabel.setText("Kostnad: " + String.format("%.2f", shoppingCart.getTotal()));

    }

    private void updateTotalPrice() {
        ShoppingCart shoppingCart = model.getShoppingCart();

        costLabel.setText("Kostnad: " + String.format("%.2f", shoppingCart.getTotal()));
        cartTotalPriceLabel.setText("Totalt: " + String.format("%.2f", shoppingCart.getTotal()));
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
