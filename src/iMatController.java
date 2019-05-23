/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.sun.org.apache.xpath.internal.operations.Mod;
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

import javax.jws.WebParam;
import java.net.URL;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


/**
 * @author oloft
 */
public class iMatController implements Initializable, ShoppingCartListener {


    private String headline;
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

    @FXML
    private Label categoryLabel;
    @FXML
    private Label noResultsLabel;

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
    @FXML
    private FlowPane timeSelectionPane;

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

    // Account Pane 2.0
    @FXML
    private AnchorPane newAccountPane;
    @FXML
    private FlowPane prevOrdersFlowPane;

    // Previous orders Pane
    @FXML
    private AnchorPane previousOrdersPane;

    //Category Pane
    @FXML
    TitledPane myProfile;
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
        categoryLabel.setText("Alla varor");
        noResultsLabel.setText("");

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
    private void handleYourProfileAction() {
        newAccountPane.toFront();
        categoryLabel.setText("Min profil");
    }

    @FXML
    private void handleShowAccountAction(ActionEvent event) {
        openAccountView();
    }

    @FXML
    private void handlePrevOrdersAction() {
        categoryLabel.setText("Tidigare inköp");
        updatePrevOrdersList();
        previousOrdersPane.toFront();
    }

    @FXML
    private void handleSearchAction() {
        String searchStr = searchField.getText();
        if (searchStr.length() > 0) {
            List<Product> matches = model.findProducts(searchField.getText());
            updateProductList(matches);
            productsScrollPane.toFront();
            categoryLabel.setText("Sökresultat");

            if (matches.size() == 0) {
                noResultsLabel.setText("Inget sökresultat för '" + searchField.getText() + "'");
            } else {
                noResultsLabel.setText(matches.size() + " st sökresultat för '" + searchField.getText() + "'");
            }
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
        noResultsLabel.setText("");
        productsScrollPane.toFront();

        List<Product> combinedProductList = new ArrayList<>();
        switch (id) {
            case ("favoritesCategory"): //set favorites to show
                category = "favoritesCategory";
                headline = "Favoriter";
                break;
            case ("breadCategory"): //set bread to show
                pc = ProductCategory.BREAD;
                headline = "Bröd";
                break;
            case ("drinksCategory"): //set drinks to show
                combinedProductList.addAll(model.getCategoryProducts(ProductCategory.COLD_DRINKS)); //fix this
                combinedProductList.addAll(model.getCategoryProducts(ProductCategory.HOT_DRINKS));
                headline = "Drycker";
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
                headline = "Frukt och grönt";
                break;
            case ("meatAndFishCategory"): //set meatAndFish to show
                combinedProductList.addAll(model.getCategoryProducts(ProductCategory.FISH));
                combinedProductList.addAll(model.getCategoryProducts(ProductCategory.MEAT));
                headline = "Kött och fisk";
                break;
            case ("pantryCategory"): //set pantry to show
                combinedProductList.addAll(model.getCategoryProducts(ProductCategory.FLOUR_SUGAR_SALT));
                combinedProductList.addAll(model.getCategoryProducts(ProductCategory.HERB));
                combinedProductList.addAll(model.getCategoryProducts(ProductCategory.POD));
                combinedProductList.addAll(model.getCategoryProducts(ProductCategory.PASTA));
                combinedProductList.addAll(model.getCategoryProducts(ProductCategory.POTATO_RICE));
                headline = "Skafferi";
                break;
            case ("snacksCategory"): //set snacks to show
                combinedProductList.addAll(model.getCategoryProducts(ProductCategory.NUTS_AND_SEEDS));
                combinedProductList.addAll(model.getCategoryProducts(ProductCategory.SWEET));
                headline = "Sötsaker och snacks";
                break;
            case ("coldDrinksCategory"):
                pc = ProductCategory.COLD_DRINKS;
                headline = "Kalla drycker";
                break;
            case ("hotDrinksCategory"):
                pc = ProductCategory.HOT_DRINKS;
                headline = "Varma drycker";
                break;
            case ("berriesCategory"):
                pc = ProductCategory.BERRY;
                headline = "Bär";
                break;
            case ("fruitsCategory"):
                combinedProductList.addAll(model.getCategoryProducts(ProductCategory.FRUIT));
                combinedProductList.addAll(model.getCategoryProducts(ProductCategory.CITRUS_FRUIT));
                combinedProductList.addAll(model.getCategoryProducts(ProductCategory.EXOTIC_FRUIT));
                combinedProductList.addAll(model.getCategoryProducts(ProductCategory.MELONS));
                headline = "Frukter";
                break;
            case ("vegetablesCategory"):
                pc = ProductCategory.VEGETABLE_FRUIT;
                headline = "Grönsaker";
                break;
            case ("cabbageCategory"):
                pc = ProductCategory.CABBAGE;
                headline = "Kål";
                break;
            case ("rootsCategory"):
                pc = ProductCategory.ROOT_VEGETABLE;
                headline = "Rotfrukter";
                break;
            case ("meatCategory"):
                pc = ProductCategory.MEAT;
                headline = "Kött";
                break;
            case ("fishCategory"):
                pc = ProductCategory.FISH;
                headline = "Fisk";
                break;
            case ("bakingCategory"):
                pc = ProductCategory.FLOUR_SUGAR_SALT;
                headline = "Bakning";
                break;
            case ("spicesCategory"):
                pc = ProductCategory.HERB;
                headline = "Kryddor";
                break;
            case ("podsCategory"):
                pc = ProductCategory.POD;
                headline = "Linser, bönor och ärtor";
                break;
            case ("pastaCategory"):
                pc = ProductCategory.PASTA;
                headline = "Pasta";
                break;
            case ("potatoAndRiceCategory"):
                pc = ProductCategory.POTATO_RICE;
                headline = "Potatis och ris";
                break;
            case ("nutsAndSeedsCategory"):
                pc = ProductCategory.NUTS_AND_SEEDS;
                headline = "Nötter och frön";
                break;
            case ("sweetsCategory"):
                pc = ProductCategory.SWEET;
                headline = "Sötsaker";
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
        categoryLabel.setText(headline);
    }

    // Cart Pane actions
    @FXML
    private void handleClearCartAction(ActionEvent event) {
        model.clearShoppingCart();
        cartProductsFlowPane.getChildren().clear();
        updateProductCounts();
    }

    // Checkout Pane actions

    @FXML
    private void handleShowEarlierTimes(){
        timeSelectionPane.getChildren().forEach(timeSelectionPanel -> ((TimeSelectionPanel) timeSelectionPanel).decrementDay());
    }

    @FXML
    private void handleShowLaterTimes(){
        timeSelectionPane.getChildren().forEach(timeSelectionPanel -> ((TimeSelectionPanel) timeSelectionPanel).incrementDay());
    }

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
        updateCheckoutTimetable();
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
        productsFlowPane.getChildren().forEach((productPanel) -> ((ProductPanel) productPanel).updateCountLabel(true));
    }

    private void updateProductList(List<Product> products) {
        productsFlowPane.getChildren().clear();

        for (Product product : products) {
            productsFlowPane.getChildren().add(new ProductPanel(product));
        }
    }

    private void updateCheckoutTimetable(){
        timeSelectionPane.getChildren().clear();
        Date d = new Date();
        for(int i = 0; i<4; i++){ //TODO FIX SO IT'S POSSIBLE TO HAVE 4 PANELS
            timeSelectionPane.getChildren().add(new TimeSelectionPanel(d));
            //plus a day
            d = new Date(d.getTime()+(24*60*60*1000));
        }
    }

    private void updatePrevOrdersList() {
        prevOrdersFlowPane.getChildren().clear();

        List<Order> orders = model.getOrders();
        orders.forEach((order) -> prevOrdersFlowPane.getChildren().add(new PreviousOrderPanel(order, this)));
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
