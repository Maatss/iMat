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
 *
 * @author sthugo
 */
public class PricePanel extends AnchorPane {

//    @FXML ImageView favoriteItemImageView; //behövs denna?
    @FXML Label nameLabel;
    @FXML Label priceLabel;
    @FXML TextField productCountTextField;

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
        nameLabel.setText(product.getName());
        priceLabel.setText(String.format("%.2f", product.getPrice()) + " " + product.getUnit());
    }

    @FXML
    private void handleAddAction(ActionEvent event) {
        System.out.println("Add " + product.getName());
        model.addToShoppingCart(product);
    }

    @FXML
    private void handleRemoveAction(ActionEvent event){
        System.out.println("Remove " + product.getName());
        model.removeFromShoppingCart(product);
    }

    private Image getImage(String path){
        return new Image(getClass().getClassLoader().getResourceAsStream(path));
    }
}
