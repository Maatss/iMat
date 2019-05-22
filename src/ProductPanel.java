import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingItem;


import java.io.IOException;

/**
 * @author oloft
 */
public class ProductPanel extends AnchorPane {

    @FXML
    ImageView imageView;
    @FXML
    ImageView favoriteItemImageView;
    @FXML
    Label nameLabel;
    @FXML
    Label prizeLabel;
    @FXML
    Label ecoLabel;
    @FXML
    ImageView addButtonImageView;
    @FXML
    ImageView removeButtonImageView;
    @FXML
    ImageView middleSectionImageView;
    @FXML
    TextField countTextField;
    @FXML
    Label stLabel;

    private final Model model = Model.getInstance();

    private Product product;

    private final static double kImageWidth = 100.0;
    private final static double kImageRatio = 0.75;

    private boolean favoriteIsHovered;
    private int productCount;

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
        nameLabel.setText(product.getName());
        prizeLabel.setText(String.format("%.2f", product.getPrice()) + " " + product.getUnit());
        imageView.setImage(model.getImage(product, kImageWidth, kImageWidth * kImageRatio));
        updateFavoriteItemImageView();
        middleSectionImageView.setImage(getImage("images/middleSectionNotAdded.png"));
        addButtonImageView.setImage(getImage("images/addButton.png"));
        updateRemoveButtonImageView();
        updateCountLabel(true);
        updateTextFieldColor();
        if (!product.isEcological()) {
            ecoLabel.setText("");
        }

        countTextField.focusedProperty().addListener((listener, oldVal, newVal) ->
                {
                    if (newVal) {
                        updateCountLabel(false);

                        Platform.runLater(() -> {
                            countTextField.selectAll();
                        });
                    } else {
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
        updateRemoveButtonImageView();
        updateTextFieldColor();
    }


    @FXML
    private void handleRemoveAction() {
        System.out.println("Remove " + product.getName());
        model.removeFromShoppingCart(product);
        updateCountLabel(true);
        updateRemoveButtonImageView();
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
        updateFavoriteItemImageView();
    }

    @FXML
    private void handleFavoriteHoverExit() {
        favoriteIsHovered = false;
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

    private void updateCountLabel(boolean appendPrefix) {
        int count = countCurrentItem();
        if (appendPrefix) {
            countTextField.setText(count + " st");
        } else {
            countTextField.setText(count + "");
        }
    }

    private int countCurrentItem() {
        int count = 0;
        for (ShoppingItem shoppingItem : model.getShoppingCart().getItems()) {
            if (shoppingItem.getProduct() == product) {
                count++;
            }
        }
        return count;

    }

    private void updateRemoveButtonImageView() {
        String iconPath;
        if (countCurrentItem() == 0) {
            iconPath = "images/removeButtonNotAvailable.png";
        } else {
            iconPath = "images/removeButtonAvailable.png";
        }
        removeButtonImageView.setImage(getImage(iconPath));

    }

    private void updateTextFieldColor() {
        String iconPath;
        if (countCurrentItem() > 0) {
            countTextField.setStyle("-fx-background-color: e64545; -fx-text-fill: white;");

        } else {
            countTextField.setStyle("-fx-control-inner-background-color: white;");

        }


    }


    private Image getImage(String path) {
        return new Image(getClass().getClassLoader().getResourceAsStream(path));
    }

    @FXML
    private void hoveringAddButton() {
        addButtonImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream("images/addButtonPliancy.png")));
    }

    @FXML
    private void releasedAddButton() {
        addButtonImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream("images/addButton.png")));

    }

    @FXML
    private void hoveringRemoveButton() {
        if (countCurrentItem() > 0) {
            removeButtonImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream("images/removeButtonPliancy.png")));
        }
    }

    @FXML
    private void exitedRemoveButton() {
        if (countCurrentItem() > 0) {
            removeButtonImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream("images/removeButtonAvailable.png")));
        } else {
            removeButtonImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream("images/removeButtonNotAvailable.png")));
        }
    }

    /* Adds the amount of the product that is written in the text field*/

    @FXML
    private void addProductsTextField() {

        if (isNumeric(countTextField.getText())) {            //checks if the string is numeric


            int amount = Integer.parseInt(countTextField.getText());  // the value that is written in the text field
            int oldAmount = countCurrentItem();                        // the old value

            int difference = amount - oldAmount;

            if (difference > 0) {
                for (int i = 0; i < difference; i++) {                 // if the new value is higher - adds the difference
                    model.addToShoppingCart(product);
                }
                updateCountLabel(true);

            }
            if (difference < 0) {                                      // if the new value is lower - removes the difference
                for (int i = 0; i > difference; i--) {
                    model.removeFromShoppingCart(product);
                }
                updateCountLabel(true);

            }
        } else {                                                      // if the input is not numeric - sets the label to previous value
            updateCountLabel(true);

        }
        updateRemoveButtonImageView();
        updateTextFieldColor();
        nameLabel.requestFocus();
    }

    /* Checks if a string is numeric*/

    public static boolean isNumeric(String strNum) {
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }


}
