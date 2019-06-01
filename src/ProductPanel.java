import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingItem;


import java.io.IOException;

/**
 * @author oloft
 */
public class ProductPanel extends AnchorPane {

    @FXML
    protected ImageView imageView;
    @FXML
    protected ImageView favoriteItemImageView;
    @FXML
    protected Label nameLabel;
    @FXML
    protected Label priceLabel;
    @FXML
    protected Label ecoLabel;
    @FXML
    protected ImageView addButtonImageView;
    @FXML
    protected ImageView removeButtonImageView;
    @FXML
    protected ImageView middleSectionImageView;
    @FXML
    protected TextField countTextField;

    private final Model model = Model.getInstance();

    private Product product;

    private final static double kImageWidth = 100.0;
    private final static double kImageRatio = 0.75;

    private boolean favoriteIsHovered;
    private boolean countTxtfldIsHovered;
    private boolean removeBtnIsHovered;

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

        favoriteIsHovered = false;
        countTxtfldIsHovered = false;
        removeBtnIsHovered = false;
        nameLabel.setText(product.getName());
        updatePriceLabel();
        imageView.setImage(model.getImage(product, kImageWidth, kImageWidth * kImageRatio));
        updateFavoriteItemImageView();
        middleSectionImageView.setImage(getImage("images/middleSectionNotAdded.png"));
        addButtonImageView.setImage(getImage("images/addButton.png"));
        updateRemoveButtonImageView();
        updateCountLabel(true);
        updateTextFieldColor();
        if (!product.isEcological()) {
            ecoLabel.setText("");
            ecoLabel.setStyle("-fx-background-color:  white;");
        }

        countTextField.focusedProperty().addListener((listener, oldVal, newVal) ->
                {
                    if (newVal) {
                        updateCountLabel(false);

                        Platform.runLater(() -> {
                            countTextField.selectAll();
                        });
                    } else {
                        addProductsTextField(countTextField.getText());
                        updateCountLabel(true);
                    }
                }
        );
    }

    @FXML
    private void handleAddAction() {
        System.out.println("Add " + product.getName());
        model.addToShoppingCart(product);
        updateCountLabel(true);
    }

    @FXML
    private void handleRemoveAction() {
        System.out.println("Remove " + product.getName());
        model.removeFromShoppingCart(product);
        updateCountLabel(true);
    }

    @FXML
    private void handleProdTxtFldHoverEnter() {
        countTxtfldIsHovered = true;
        updateTextFieldColor();
    }

    @FXML
    private void handleProdTxtFldHoverExit() {
        countTxtfldIsHovered = false;
        updateTextFieldColor();
    }

    @FXML
    private void handleFavoriteAction() {
        model.toggleFavorite(product);
        updateFavoriteItemImageView();
    }

    @FXML
    private void handleFavoriteHoverEnter() {
        favoriteIsHovered = true;
        setCursor(Cursor.HAND);
        updateFavoriteItemImageView();
    }

    @FXML
    private void handleFavoriteHoverExit() {
        favoriteIsHovered = false;
        setCursor(Cursor.DEFAULT);
        updateFavoriteItemImageView();
    }

    /**
     * Updates the star image based on if the picture is a favorite or not.
     * Favorite = filled
     * Not a favorite = just edges
     */
    private void updateFavoriteItemImageView() {
        String iconPath;
        if (model.checkIfFavorite(product)) {
            if (favoriteIsHovered) {
                iconPath = "images/Favorite/FavoriteFilledRemove.png";
            } else {
                iconPath = "images/Favorite/FavoriteFilled.png";
            }
        } else {
            if (favoriteIsHovered) {
                iconPath = "images/Favorite/FavoriteEmptyAdd.png";
            } else {
                iconPath = "images/Favorite/FavoriteEmpty.png";
            }
        }
        favoriteItemImageView.setImage(getImage(iconPath));
    }

    public void updateCountLabel(boolean appendPrefix) {
        int count = countCurrentItem();
        updateTextFieldColor();
        updateRemoveButtonImageView();
        String unitSuffix = product.getUnitSuffix();
        if (unitSuffix.equals("förp")) {
            unitSuffix = "st";
        }
        if (appendPrefix) {
            countTextField.setText(count + " " + unitSuffix);
        } else {
            countTextField.setText(count + "");
        }
    }

    private void updatePriceLabel() {
        if (product.getUnitSuffix().equals("förp")) {
            priceLabel.setText(String.format("%.2f", product.getPrice()) + " kr/st");
        } else {
            priceLabel.setText(String.format("%.2f", product.getPrice()) + " " + product.getUnit());
        }
    }

    private int countCurrentItem() {
        return model.getCountInShoppingCart(new ShoppingItem(product));
    }

    private void updateRemoveButtonImageView() {
        String iconPath;
        if (countCurrentItem() == 0) {
            iconPath = "images/removeButtonNotAvailable.png";
            if (removeBtnIsHovered) {
                removeBtnIsHovered = false;
                setCursor(Cursor.DEFAULT);
            }
        } else {
            if (removeBtnIsHovered) {
                iconPath = "images/removeButtonPliancy.png";
                setCursor(Cursor.HAND);
            } else {
                iconPath = "images/removeButtonAvailable.png";
                setCursor(Cursor.DEFAULT);
            }
        }
        removeButtonImageView.setImage(getImage(iconPath));
    }

    private void updateTextFieldColor() {
        if (countCurrentItem() > 0) {
            if (countTxtfldIsHovered) {
                countTextField.setStyle("-fx-background-color: b43133; -fx-text-fill: white;");
            } else {
                countTextField.setStyle("-fx-background-color: e64545; -fx-text-fill: white;");
            }
        } else {
            if (countTxtfldIsHovered) {
                countTextField.setStyle("-fx-background-color: cccccc;");
            } else {
                countTextField.setStyle("-fx-control-inner-background-color: white;");
            }
        }
    }


    private Image getImage(String path) {
        return new Image(getClass().getClassLoader().getResourceAsStream(path));
    }

    @FXML
    private void hoveringAddButton() {
        addButtonImageView.setImage(getImage("images/addButtonPliancy.png"));
        setCursor(Cursor.HAND);
    }

    @FXML
    private void exitedAddButton() {
        addButtonImageView.setImage(getImage("images/addButton.png"));
        setCursor(Cursor.DEFAULT);

    }

    @FXML
    private void hoveringRemoveButton() {
        if (countCurrentItem() > 0) {
            removeBtnIsHovered = true;
            updateRemoveButtonImageView();
//            removeButtonImageView.setImage(getImage("images/removeButtonPliancy.png"));
//            setCursor(Cursor.HAND);
        }
    }

    @FXML
    private void exitedRemoveButton() {
        removeBtnIsHovered = false;
        updateRemoveButtonImageView();
    }

    /**
     * Adds the amount of the product that is written in the text field
     */
    @FXML
    protected void handleOnKeyPressed(KeyEvent event) { // Catch cases where an empty textfield is entered
        if (event.getCode() == KeyCode.ENTER) {
            addProductsTextField();
        }
    }

    @FXML
    private void addProductsTextField() {
        addProductsTextField(countTextField.getText());
    }

    private void addProductsTextField(String inputText) { //Todo fix crash when double/float is entered
        if (isNumeric(inputText) || inputText.length() == 0) {    //If valid case
            int amount;
            if (inputText.length() == 0) {
                amount = 0;
            } else {
                amount = Integer.parseInt(inputText);
            }

            int oldAmount = countCurrentItem();                        // Get the old value
            int difference = oldAmount - amount;

            if (difference > 0) {
                model.removeFromShoppingCart(product, difference);
            } else if (difference < 0) {                               // if the new value is lower - removes the difference
                model.addToShoppingCart(product, difference * -1);
            }
        }
        updateCountLabel(true);
        updateRemoveButtonImageView();
        updateTextFieldColor();
        nameLabel.requestFocus();                                       // Remove focus from textField, any node will do
    }

    public static boolean isNumeric(String strNum) {
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

    public Product getProduct() {
        return product;
    }
}
