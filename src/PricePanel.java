import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
        productCountTextField.setText(count + "");
    }

    private void updateCountLabel(){
        //TODO do this, append "st" or "kg"
    }

    private void addProductsTextField() {
//        addProductsTextField(countTextField.getText());
        //TODO fix this aswell so you can write in textfield
    }


    private void updatePriceSumLabel() {
        productPriceSumLabel.setText(String.format("%.2f", product.getPrice() * count) + " kr");
    }

    private void countCurrentItem() {
        this.count = model.getCountInShoppingCart(new ShoppingItem(product));
    }
}
