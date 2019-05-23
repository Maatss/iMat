import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
 * @author sthugo
 */
public class PricePanel extends AnchorPane {

    @FXML
    Label productNameLabel;
    @FXML
    Label productPriceLabel;
    @FXML
    TextField productCountTextField;
    @FXML
    Label productPriceSumLabel;
    @FXML
    ImageView addButtonImageView;
    @FXML
    ImageView removeButtonImageView;

    private final Model model = Model.getInstance();
    private Product product;
    private int count;


    public PricePanel(Product productIn) {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PricePanel.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.product = productIn;
        productNameLabel.setText(product.getName());
        updateCountAndCountLabel();
        productPriceLabel.setText(String.format("%.2f", product.getPrice()) + " " + product.getUnit());
        updatePriceSumLabel();
    }

    @FXML
    private void handleAddAction() { //TODO copy final version of method in ProductPanel when it's done there
        System.out.println("Add " + product.getName());
        model.addToShoppingCart(product);
        updateCountAndCountLabel(); //todo update count in productpanels when closing shopping cart?
        updatePriceSumLabel();
    }

    @FXML
    private void handleRemoveAction() {
        System.out.println("Remove " + product.getName());
        model.removeFromShoppingCart(product);
        updateCountAndCountLabel();
        updatePriceSumLabel();
    }

    @FXML
    protected void handleOnKeyPressed(KeyEvent event) { // Catch cases where an empty textfield is entered
        if (event.getCode() == KeyCode.ENTER) {
            addProductsTextField();
        }
    }

    @FXML
    private void hoveringAddButton() {
        addButtonImageView.setImage(getImage("images/addButtonPliancy.png"));
    }

    @FXML
    private void releasedAddButton() {
        addButtonImageView.setImage(getImage("images/addButton.png"));

    }

    @FXML
    private void hoveringRemoveButton() {
        if (count > 0) {
            removeButtonImageView.setImage(getImage("images/removeButtonPliancy.png"));
        }
    }

    @FXML
    private void exitedRemoveButton() {
        if (count > 0) {
            removeButtonImageView.setImage(getImage("images/removeButtonAvailable.png"));
        } else {
            removeButtonImageView.setImage(getImage("images/removeButtonNotAvailable.png"));
        }
    }

    private Image getImage(String path) {
        return new Image(getClass().getClassLoader().getResourceAsStream(path));
    }

    private void updateCountAndCountLabel() {
        countCurrentItem();
        updateCountLabel(true);

    }

    private void updateCountLabel(boolean appendPrefix){
        updateTextFieldColor();
        updateRemoveButtonImageView();
        if (appendPrefix) {
            productCountTextField.setText(count + " st");
        } else {
            productCountTextField.setText(count + "");
        }
        //TODO do this, append "st" or "kg"
    }

    private void updateRemoveButtonImageView() {
        String iconPath;
        if (count == 0) {
            iconPath = "images/removeButtonNotAvailable.png";
        } else {
            iconPath = "images/removeButtonAvailable.png";
        }
        removeButtonImageView.setImage(getImage(iconPath));
    }

    private void updateTextFieldColor() {
        if (count > 0) {
            productCountTextField.setStyle("-fx-background-color: e64545; -fx-text-fill: white;");
        } else {
            productCountTextField.setStyle("-fx-control-inner-background-color: white;");
        }
    }

    public void addProductsTextField() {
        String inputText = productCountTextField.getText();
        if (isNumeric(inputText) || inputText.length() == 0) {    //If valid case
            int amount;
            if (inputText.length() == 0) {
                amount = 0;
            } else {
                amount = Integer.parseInt(inputText);
            }

            int oldAmount = count;                        // Get the old value
            int difference = oldAmount - amount;

            if (difference > 0) {
                model.removeFromShoppingCart(product, difference);
            } else if (difference < 0) {                               // if the new value is lower - removes the difference
                model.addToShoppingCart(product, -difference);
            }
        }
        updateCountAndCountLabel();
        updateRemoveButtonImageView();
        updateTextFieldColor();
        productNameLabel.requestFocus();         // Remove focus from textField, any node will do
    }


    private void updatePriceSumLabel() {
        productPriceSumLabel.setText(String.format("%.2f", product.getPrice() * count) + " kr");
    }

    private void countCurrentItem() {
        this.count = model.getCountInShoppingCart(new ShoppingItem(product));
    }

    public static boolean isNumeric(String strNum) {
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }
}
