import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
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
    @FXML
    Label productPriceSumLabel;

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

    private Image getImage(String path) {
        return new Image(getClass().getClassLoader().getResourceAsStream(path));
    }

    private void updateCountAndCountLabel() {
        countCurrentItem();
        productCountTextField.setText(count + "");
    }

    private void updatePriceSumLabel(){
        productPriceSumLabel.setText(String.format("%.2f", product.getPrice() * count) + " kr");
    }

    private void countCurrentItem() {
        int count = 0;
        for (ShoppingItem shoppingItem : model.getShoppingCart().getItems()) {
            if (shoppingItem.getProduct() == product) {
                count++;
            }
        }
        this.count = count;
    }
}
