import javafx.event.ActionEvent;
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

    //    @FXML ImageView favoriteItemImageView; //behövs denna?
    @FXML
    Label productNameLabel;
    @FXML
    Label productPriceLabel;
    @FXML
    TextField productCountTextField;

    private final Model model = Model.getInstance();
    private Product product;


    public PricePanel(ShoppingItem shoppingItem) {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PricePanel.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.product = shoppingItem.getProduct();
        productNameLabel.setText(product.getName());
        productPriceLabel.setText(String.format("%.2f", product.getPrice()) + " " + product.getUnit());
        updateCountLabel();
    }

    @FXML
    private void handleAddAction() { //TODO copy final version of method in ProductPanel when it's done there
        System.out.println("Add " + product.getName());
        model.addToShoppingCart(product);
        updateCountLabel(); //todo update count in productpanels when closing shopping cart?
    }

    @FXML
    private void handleRemoveAction() {
        System.out.println("Remove " + product.getName());
        model.removeFromShoppingCart(product);
        updateCountLabel();
    }

    private Image getImage(String path) {
        return new Image(getClass().getClassLoader().getResourceAsStream(path));
    }

    //samma som den i ProductPanel
    private void updateCountLabel() {
        int count = countCurrentItem();
        productCountTextField.setText(count + "");
    }

    //samma som den i ProductPanel
    private int countCurrentItem() {
        int count = 0;
        for (ShoppingItem shoppingItem : model.getShoppingCart().getItems()) {
            if (shoppingItem.getProduct() == product) {
                count++;
            }
        }
        return count;
    }
}
