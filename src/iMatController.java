/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import se.chalmers.cse.dat216.project.*;

import java.net.URL;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


/**
 * @author oloft
 */
public class iMatController implements Initializable, ShoppingCartListener {

    private boolean cardInfoIsShown;
    private String headline;
    private boolean isCardPayment;
    private boolean isInvoicePayment;


    // Welcome Pane
    @FXML
    protected AnchorPane welcomePane; // Loot - I must use 'protected' for it to work?
    @FXML
    protected Button acceptHelpButton;
    @FXML
    protected Button denyHelpButton;

    // Shopping Pane
    @FXML
    protected AnchorPane homeButtonAnchorPane;
    @FXML
    protected ImageView homeImageView;
    @FXML
    private AnchorPane shopPane;
    @FXML
    private TextField searchField;
    @FXML
    private Label itemsLabel;
    @FXML
    private AnchorPane productsAnchorPane;
    @FXML
    private FlowPane productsFlowPane;
    @FXML
    private ScrollPane productsScrollPane;
    @FXML
    protected AnchorPane cartAnchorPane;
    @FXML
    private Label categoryLabel;
    @FXML
    private Label noResultsLabel;
    @FXML
    private Label productsNoResultsTopLabel;
    @FXML
    private Label productsNoResultsBottomLabel;
    @FXML
    private Button searchButton;

    @FXML
    protected VBox userCategoriesVBox;
    @FXML
    protected VBox categoryVBox;
    private CategoriesHandler categoriesHandler;

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
    private VBox cartProductsVBox;
    @FXML
    private Button cartGoToCheckoutButton;
    @FXML
    private Button cartEmptyCartButton;
    @FXML
    private Label cartIsEmptyLabel;

    // Checkout pane ONE
    @FXML
    private AnchorPane checkoutPane;
    @FXML
    private FlowPane timeSelectionPane;
    @FXML
    private ImageView returnToCartImageView;

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
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField postCodeTextField;
    @FXML
    private TextField postAddressTextField;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private Button saveButton;
    @FXML
    private Label savedLabel;
    @FXML
    private RadioButton cardRadioButton;
    @FXML
    private RadioButton invoiceRadioButton;
    @FXML
    private GridPane profileGridPane;
    @FXML
    private ComboBox cardOptionComboBox;

    @FXML
    private Label cardOptionLabel;
    @FXML
    private Label cardNumberLabel;
    @FXML
    private Label cardNameLabel;
    @FXML
    private Label cardDateLabel;
    @FXML
    private Label cardCvcLabel;

    @FXML
    private Label firstNameMissingLabel;
    @FXML
    private Label lastNameMissingLabel;
    @FXML
    private Label addressMissingLabel;
    @FXML
    private Label postCodeMissingLabel;
    @FXML
    private Label postAddressMissingLabel;
    @FXML
    private Label phoneMissingLabel;
    @FXML
    private Label choosePaymentLabel;
    @FXML
    private Label cardNumberMissingLabel;
    @FXML
    private Label cardNameMissingLabel;
    @FXML
    private Label CVCmissingLabel;

    private ComboBox cardComboBox = new ComboBox();
    private TextField cardNameTF = new TextField();
    private TextField cardNumberTF = new TextField();
    private ComboBox cardMonthCombo = new ComboBox();
    private ComboBox cardYearCombo = new ComboBox();
    private TextField cvcTF = new TextField();


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
    private TitledPane fridgeCategory;
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
        savedLabel.setText("");
        productsNoResultsTopLabel.setText("");
        productsNoResultsBottomLabel.setText("");
        categoriesHandler = CategoriesHandler.getInstance(this, userCategoriesVBox, categoryVBox);

        profileLabelsHideVisibility();
        setCardInfoIsShown(false);
        fillProfileComboBoxes();

        updateProductList(model.getProducts());
        updateYourProfilePanel();

        maskHomeButton();
        setupAccountPane();
        updateTotalPrice();

        searchField.focusedProperty().addListener((listener, oldVal, newVal) -> { //Select text when focused
            if (newVal) {
                Platform.runLater(() -> {
                    searchField.selectAll();
                });
            }
        });
    }

    // Welcome pane actions
    @FXML
    protected void handleAcceptHelpAction(ActionEvent event) {
        model.setDoShowHelpWizard(true);
        welcomePane.toBack();
        showProductsPane();
    }

    @FXML
    protected void handleDenyHelpAction(ActionEvent event) {
        model.setDoShowHelpWizard(false);
        welcomePane.toBack();
        showProductsPane();
    }

    // Shop pane actions
    private void maskHomeButton() {
        Circle circle = new Circle();
        circle.setCenterX(73.3);
        circle.setCenterY(70);
        circle.setRadius(70);
        Shape mask = circle;
        homeImageView.setClip(mask);
    }

    @FXML
    private void handleLogoAction() {
        categoryLabel.setText("Alla varor");
        noResultsLabel.setText("");
        List<Product> matches = model.findProducts("");
        updateProductList(matches);
        showProductsPane();
        categoriesHandler.clearCategorySelection(true);
    }

    @FXML
    private void handleLogoHoverEnter() {
        homeImageView.setCursor(Cursor.HAND);
    }

    @FXML
    private void handleLogoHoverExit() {
        homeImageView.setCursor(Cursor.DEFAULT);
    }

    @FXML
    private void handleShowAccountAction(ActionEvent event) {
        openAccountView();
    }

    @FXML
    private void handleSearchAction() {
        String searchStr = searchField.getText();
        if (searchStr.length() > 0) {
            List<Product> matches = model.findProducts(searchField.getText());
            updateProductList(matches);
            showProductsPane();
            categoryLabel.setText("Sökresultat");

            if (matches.size() == 0) {
                noResultsLabel.setText("Inget sökresultat för '" + searchField.getText() + "'");
            } else {
                noResultsLabel.setText(matches.size() + " st sökresultat för '" + searchField.getText() + "'");
            }
            System.out.println("# matching products: " + matches.size());
            cartTotalPriceLabel.requestFocus();
        } else {
            searchField.setText("");
            cartTotalPriceLabel.requestFocus();
        }
        categoriesHandler.clearCategorySelection(true);
    }

    @FXML
    private void handleSearchButtonHoverEnter() {
        searchButton.setStyle("-fx-background-color:  #D43434; -fx-background-radius: 14; -fx-border-color:  #101010A0; -fx-border-radius: 7; -fx-border-width: 3;");
        searchButton.setCursor(Cursor.HAND);
    }

    @FXML
    private void handleSearchButtonHoverExit() {
        searchButton.setStyle("-fx-background-color:  #e54545; -fx-background-radius: 14; -fx-border-color:  #101010A0; -fx-border-radius: 7; -fx-border-width: 3;");
        searchButton.setCursor(Cursor.DEFAULT);
    }

    private void showProductsPane() {
        shopPane.toFront();
        productsAnchorPane.toFront();
        if (productsFlowPane.getChildren().size() > 0) {
            productsNoResultsTopLabel.setText("");
            productsNoResultsBottomLabel.setText("");
        } else {
            productsNoResultsTopLabel.setText("Inga sökträffar.");
            productsNoResultsBottomLabel.setText("Kanske hittar du det du söker i någon av våra kategorier.");
        }
//        productsNoResultsTopLabel.toFront();
//        productsNoResultsBottomLabel.toFront();
    }

    @FXML
    private void handleBuyItemsAction() {
        if (!model.getShoppingCart().getItems().isEmpty()) {
            model.placeOrder();
//            costLabel.setText("Köpet klart!");
            //todo add error message
        } else {
//            costLabel.setText("Lägg till varor i varukorgen först");
            //todo add error message
        }
        closeCheckoutViewTwo();
        openSuccessfulPurchaseView();
    }

    @FXML
    protected void handleShoppingCartHoverEnter() {
        cartAnchorPane.setCursor(Cursor.HAND);
        cartAnchorPane.setStyle("-fx-background-color: #b43133EE; -fx-background-radius: 7; -fx-border-color: #000000; -fx-border-width: 3; -fx-border-radius: 7;");
    }

    @FXML
    protected void handleShoppingCartHoverExit() {
        cartAnchorPane.setCursor(Cursor.DEFAULT);
        cartAnchorPane.setStyle("-fx-background-color: #e54545EE; -fx-background-radius: 7; -fx-border-color: #000000; -fx-border-width: 3; -fx-border-radius: 7;");
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
    private void handleFridgeSelectionAction() {
        handleCategorySelection("fridgeCategory");
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
    private void handleDairySelectionAction() {
        handleCategorySelection("dairyCategory");
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
    protected void handleCategorySelection(String id) {
        String category = id;
        ProductCategory pc = null;
        noResultsLabel.setText("");

        List<Product> combinedProductList = new ArrayList<>();
        switch (id) {
            case ("profileCategory"): //set profile to show
                category = "profileCategory";
                headline = "Min profil";
                break;
            case ("prevOrderCategory"): //set previous orders to show
                category = "prevOrderCategory";
                headline = "Tidigare inköp";
                break;
            case ("favoritesCategory"): //set favorites to show
                category = "favoritesCategory";
                headline = "Favoriter";
                break;
            case ("breadCategory"): //set bread to show
                pc = ProductCategory.BREAD;
                headline = "Bröd";
                break;
            case ("fridgeCategory"): //set fridgestuff to show
                combinedProductList.addAll(model.getCategoryProducts(ProductCategory.COLD_DRINKS));
                combinedProductList.addAll(model.getCategoryProducts(ProductCategory.DAIRIES));
                combinedProductList.addAll(model.getCategoryProducts(ProductCategory.HOT_DRINKS));
                headline = "Kylvaror";
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
            case ("dairyCategory"):
                pc = ProductCategory.DAIRIES;
                headline = "Mejeri";
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

        showProductsPane(); //Basically resets resultscreen

        if (category.equals("profileCategory")) {
            newAccountPane.toFront();
            updateYourProfilePanel();
        } else if (category.equals("prevOrderCategory")) {
            handleShowPrevOrdersPanel(false);
        } else if (category.equals("favoritesCategory")) {
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
        cartProductsVBox.getChildren().clear();
        updateProductCounts();
    }

    // Checkout Pane actions

    @FXML
    private void handleShowEarlierTimes() {
        timeSelectionPane.getChildren().forEach(timeSelectionPanel -> ((TimeSelectionPanel) timeSelectionPanel).decrementDay());
    }

    @FXML
    private void handleShowLaterTimes() {
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
        showProductsPane();
        accountPane.toBack();
    }

    public void openCartView() {
        updateCartViewProducts();
        cartPane.toFront();
        updateCartCheckoutButton(true);
    }

    public void updateCartCheckoutButton(boolean allowEmptyLabel) {
        if (model.getCountInShoppingCart() > 0) {
            cartGoToCheckoutButton.setDisable(false);
            cartEmptyCartButton.setDisable(false);
            if (allowEmptyLabel) {
                cartIsEmptyLabel.toBack();
            }
        } else {
            cartGoToCheckoutButton.setDisable(true);
            cartEmptyCartButton.setDisable(true);
            if (allowEmptyLabel) {
                cartIsEmptyLabel.toFront();
            }
        }
    }

    public void closeCartView() {
        showProductsPane();
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

    private void openSuccessfulPurchaseView() {
        closeCheckoutViewTwo();
        successfulPurchasePane.toFront();
    }

    public void closeSuccessfulPurchaseView() {
        successfulPurchasePane.toBack();
    }

    public void closePurchaseOpenRecipe() {
        closeSuccessfulPurchaseView();
        handleShowPrevOrdersPanel(true);
    }

    private void handleShowPrevOrdersPanel(boolean expandLatest) {
        categoryLabel.setText("Tidigare inköp");
        updatePrevOrdersList(expandLatest);
        previousOrdersPane.toFront();
    }

    @FXML
    private void handleReturnToCartHoverEnter() {
        returnToCartImageView.setImage(new Image("images/shoppingCartBackDark.png"));
    }

    @FXML
    private void handleReturnToCartHoverExit() {
        returnToCartImageView.setImage(new Image("images/shoppingCartBack.png"));
    }

    private void closeReceiptsView() {
        //todo / other branch
    }

    // Shop pane methods
    @Override
    public void shoppingCartChanged(CartEvent evt) {
        updateProductCounts();
        updateTotalPrice();
        updateCartCheckoutButton(false);
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

    private void updateCheckoutTimetable() {
        timeSelectionPane.getChildren().clear();
        Date d = new Date();
        for (int i = 0; i < 4; i++) {
            timeSelectionPane.getChildren().add(new TimeSelectionPanel(d));
            //plus a day
            d = new Date(d.getTime() + (24 * 60 * 60 * 1000));
        }
    }

    private void updatePrevOrdersList(boolean expandLatest) {
        prevOrdersFlowPane.getChildren().clear();

        List<Order> orders = model.getOrders();
        orders.forEach((order) -> prevOrdersFlowPane.getChildren().add(new PreviousOrderPanel(order, this)));
        if (expandLatest) {
            ((PreviousOrderPanel) prevOrdersFlowPane.getChildren().get(0)).handlePanelExpand();
        }
    }

    private void updateCartViewProducts() {
        List<Product> productList = new ArrayList<>();
        Product temp;

        cartProductsVBox.getChildren().clear();
        ShoppingCart shoppingCart = model.getShoppingCart();

        for (ShoppingItem si : shoppingCart.getItems()) {
            temp = si.getProduct();
            if (!productList.contains(si.getProduct())) {
                cartProductsVBox.getChildren().add(new PricePanel(temp));
            }
            productList.add(si.getProduct());
        }
    }

    private void updateTotalPrice() {
        itemsLabel.setText(String.format("%d st\n%.2f kr", model.getCountInShoppingCart(), model.getShoppingCart().getTotal()));
        cartTotalPriceLabel.setText("Totalt: " + String.format("%.2f", model.getShoppingCart().getTotal()));
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

    private void updateCard() {

        CreditCard card = model.getCreditCard();

        card.setCardNumber(cardNumberTF.getText());
        card.setHoldersName(cardNameTF.getText());

        String selectedValue = (String) cardComboBox.getSelectionModel().getSelectedItem();
        card.setCardType(selectedValue);

        selectedValue = (String) cardMonthCombo.getSelectionModel().getSelectedItem();
        card.setValidMonth(Integer.parseInt(selectedValue));

        selectedValue = (String) cardYearCombo.getSelectionModel().getSelectedItem();
        card.setValidYear(Integer.parseInt(selectedValue));

        card.setVerificationCode(Integer.parseInt(cvcTF.getText()));

    }

    private void updateYourProfilePanel() {
        Customer customer = model.getCustomer();

        firstNameTextField.setText(customer.getFirstName());
        lastNameTextField.setText(customer.getLastName());
        addressTextField.setText(customer.getAddress());
        postCodeTextField.setText(customer.getPostCode());
        postAddressTextField.setText(customer.getPostAddress());
        phoneNumberTextField.setText(customer.getPhoneNumber());

        CreditCard card = model.getCreditCard();

        cardNumberTF.setText(card.getCardNumber());
        cardNameTF.setText(card.getHoldersName());

        cardComboBox.getSelectionModel().select(card.getCardType());
        cardMonthCombo.getSelectionModel().select("" + card.getValidMonth());
        cardYearCombo.getSelectionModel().select("" + card.getValidYear());

        cvcTF.setText("" + card.getVerificationCode());
    }

    private void updateCustomer() {
        Customer customer = model.getCustomer();

        customer.setFirstName(firstNameTextField.getText());
        customer.setLastName(lastNameTextField.getText());
        customer.setAddress(addressTextField.getText());
        customer.setPostCode(postCodeTextField.getText());
        customer.setPostAddress(postAddressTextField.getText());
        customer.setPhoneNumber(phoneNumberTextField.getText());
    }

    @FXML
    private void handleSaveAction(ActionEvent event) {
        if (allFieldsFilled()) {
            updateCustomer();
            if (isCardPayment) {
                updateCard();
            }
            savedLabel.setText("Sparat!");
            fadeTransition(savedLabel);
            hideAllProfileErrorMessages();
        }
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


    private void fadeTransition(Node node) {
        TranslateTransition transition = new TranslateTransition();

        transition.setOnFinished((e) -> {
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(3), node);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.play();
        });
        transition.play();

    }

    @FXML
    public void handleCardOption() {
        profileGridPane.setVisible(true);
        showCardInformation();
        setInvoicePayment(false);
        setCardPayment(true);
        choosePaymentLabel.setVisible(false);
        cardRadioButton.setStyle("");
        invoiceRadioButton.setStyle("");

        if (!cardInfoIsShown) {

            profileGridPane.add(cardComboBox, 0, 0);
            profileGridPane.add(cardNumberTF, 0, 1);
            profileGridPane.add(cardNameTF, 0, 2);
            profileGridPane.add(cardMonthCombo, 0, 3);
            profileGridPane.add(cardYearCombo, 1, 3);
            profileGridPane.add(cvcTF, 0, 4);

            setCardInfoIsShown(true);


        }
    }

    @FXML
    public void handleInvoiceOption() {
        profileGridPane.setVisible(false);
        hideCardInformation();
        removeCardErrorStyle();
        setCardPayment(false);
        setInvoicePayment(true);
        choosePaymentLabel.setVisible(false);
        cardRadioButton.setStyle("");
        invoiceRadioButton.setStyle("");

    }


    private void profileLabelsHideVisibility() {
        cardOptionLabel.setVisible(false);
        cardNumberLabel.setVisible(false);
        cardNameLabel.setVisible(false);
        cardDateLabel.setVisible(false);
        cardCvcLabel.setVisible(false);
        firstNameMissingLabel.setVisible(false);
        lastNameMissingLabel.setVisible(false);
        addressMissingLabel.setVisible(false);
        postCodeMissingLabel.setVisible(false);
        postAddressMissingLabel.setVisible(false);
        phoneMissingLabel.setVisible(false);
        choosePaymentLabel.setVisible(false);
        cardNumberMissingLabel.setVisible(false);
        cardNameMissingLabel.setVisible(false);
        CVCmissingLabel.setVisible(false);

    }

    private void showCardInformation() {
        cardOptionLabel.setVisible(true);
        cardNumberLabel.setVisible(true);
        cardNameLabel.setVisible(true);
        cardDateLabel.setVisible(true);
        cardCvcLabel.setVisible(true);
    }

    private void hideCardInformation() {
        cardOptionLabel.setVisible(false);
        cardNumberLabel.setVisible(false);
        cardNameLabel.setVisible(false);
        cardDateLabel.setVisible(false);
        cardCvcLabel.setVisible(false);
        cardNumberMissingLabel.setVisible(false);
        cardNameMissingLabel.setVisible(false);
        CVCmissingLabel.setVisible(false);
    }


    private void setCardInfoIsShown(boolean set) {
        this.cardInfoIsShown = set;
    }


    private void fillProfileComboBoxes() {
        cardComboBox.getItems().addAll("Visa", "Mastercard", "American Express");
        cardYearCombo.getItems().addAll("19", "20", "21", "22", "23", "24", "25");
        cardMonthCombo.getItems().addAll("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");
    }

    private boolean allFieldsFilled() {
        if (firstNameTFisfilled() && lastNameTFisfilled() && addressTFisFilled() && postCodeTFisFilled() &&
                postAddressTFisFilled() && phoneTFisFilled() && (isCardPayment || isInvoicePayment)) {
            if (isCardPayment) {
                if (allCardFieldsAreFilled()) {
                    return true;
                } else {
                    checkMissingFields();
                }

            } else {
                return true;
            }

        } else {
            checkMissingFields();
        }
        return false;
    }

    private boolean firstNameTFisfilled() {
        return firstNameTextField.getText().length() > 0;
    }

    private boolean allCardFieldsAreFilled() {
        return (cardNameIsFilled() && cardNumberTFisFilled() && CVCisFilled());
    }

    private boolean lastNameTFisfilled() {
        return lastNameTextField.getText().length() > 0;

    }

    private boolean addressTFisFilled() {
        return addressTextField.getText().length() > 0;
    }

    private boolean postCodeTFisFilled() {
        return postCodeTextField.getText().length() > 0;
    }

    private boolean postAddressTFisFilled() {
        return postAddressTextField.getText().length() > 0;
    }

    private boolean phoneTFisFilled() {
        return phoneNumberTextField.getText().length() > 0;
    }

    private boolean cardNumberTFisFilled() {
        return cardNumberTF.getText().length() > 0;
    }

    private boolean cardNameIsFilled() {
        return cardNameTF.getText().length() > 0;
    }

    private boolean CVCisFilled() {
        return cvcTF.getText().length() > 0;
    }


    private void checkMissingFields() {
        checkCustomerFields();

        if (isCardPayment) {
            checkCardFields();
        }
    }

    private void checkCustomerFields() {
        if (!firstNameTFisfilled()) {
            firstNameMissingLabel.setVisible(true);
            firstNameTextField.setStyle("-fx-border-color: FF0000; -fx-border-width: 4");
        } else {
            firstNameMissingLabel.setVisible(false);
            firstNameTextField.setStyle("");
        }
        if (!lastNameTFisfilled()) {
            lastNameMissingLabel.setVisible(true);
            lastNameTextField.setStyle("-fx-border-color: FF0000; -fx-border-width: 4");
        } else {
            lastNameMissingLabel.setVisible(false);
            lastNameTextField.setStyle("");
        }
        if (!addressTFisFilled()) {
            addressMissingLabel.setVisible(true);
            addressTextField.setStyle("-fx-border-color: FF0000; -fx-border-width: 4");
        } else {
            addressMissingLabel.setVisible(false);
            addressTextField.setStyle("");
        }
        if (!postCodeTFisFilled()) {
            postCodeMissingLabel.setVisible(true);
            postCodeTextField.setStyle("-fx-border-color: FF0000; -fx-border-width: 4");
        } else {
            postCodeMissingLabel.setVisible(false);
            postCodeTextField.setStyle("");
        }

        if (!postAddressTFisFilled()) {
            postAddressMissingLabel.setVisible(true);
            postAddressTextField.setStyle("-fx-border-color: FF0000; -fx-border-width: 4");
        } else {
            postAddressMissingLabel.setVisible(false);
            postAddressTextField.setStyle("");
        }
        if (!phoneTFisFilled()) {
            phoneMissingLabel.setVisible(true);
            phoneNumberTextField.setStyle("-fx-border-color: FF0000; -fx-border-width: 4");
        } else {
            phoneMissingLabel.setVisible(false);
            phoneNumberTextField.setStyle("");
        }
        if (!(isCardPayment || isInvoicePayment)) {
            choosePaymentLabel.setVisible(true);
            cardRadioButton.setStyle("-fx-border-color: FF0000; -fx-border-width: 4");
            invoiceRadioButton.setStyle("-fx-border-color: FF0000; -fx-border-width: 4");
        } else {
            choosePaymentLabel.setVisible(false);
            cardRadioButton.setStyle("");
            invoiceRadioButton.setStyle("");
        }
    }

    private void checkCardFields() {
        if (!cardNameIsFilled()) {
            cardNameMissingLabel.setVisible(true);
            cardNameTF.setStyle("-fx-border-color: FF0000; -fx-border-width: 4");
        } else {
            cardNameMissingLabel.setVisible(false);
            cardNameTF.setStyle("");
        }
        if (!cardNumberTFisFilled()) {
            cardNumberMissingLabel.setVisible(true);
            cardNumberTF.setStyle("-fx-border-color: FF0000; -fx-border-width: 4");
        } else {
            cardNumberMissingLabel.setVisible(false);
            cardNumberTF.setStyle("");
        }
        if (!CVCisFilled()) {
            CVCmissingLabel.setVisible(true);
            cvcTF.setStyle("-fx-border-color: FF0000; -fx-border-width: 4");
        } else {
            CVCmissingLabel.setVisible(false);
            cvcTF.setStyle("");
        }

    }

    private void hideAllProfileErrorMessages() {
        firstNameMissingLabel.setVisible(false);
        lastNameMissingLabel.setVisible(false);
        addressMissingLabel.setVisible(false);
        postCodeMissingLabel.setVisible(false);
        postAddressMissingLabel.setVisible(false);
        phoneMissingLabel.setVisible(false);
        choosePaymentLabel.setVisible(false);
        cardNumberMissingLabel.setVisible(false);
        cardNameMissingLabel.setVisible(false);
        CVCmissingLabel.setVisible(false);
        removeErrorStyle();
    }

    public void setCardPayment(boolean cardPayment) {
        isCardPayment = cardPayment;
    }

    public void setInvoicePayment(boolean invoicePayment) {
        isInvoicePayment = invoicePayment;
    }

    private void removeErrorStyle() {
        cardRadioButton.setStyle("");
        invoiceRadioButton.setStyle("");
        phoneNumberTextField.setStyle("");
        postAddressTextField.setStyle("");
        postCodeTextField.setStyle("");
        addressTextField.setStyle("");
        firstNameTextField.setStyle("");
        lastNameTextField.setStyle("");
        removeCardErrorStyle();
    }

    private void removeCardErrorStyle() {

        cvcTF.setStyle("");
        cardNumberTF.setStyle("");
        cardNameTF.setStyle("");
    }


}
