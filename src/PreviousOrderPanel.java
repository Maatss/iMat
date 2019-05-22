import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Order;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;

public class PreviousOrderPanel extends AnchorPane {
    @FXML
    protected ImageView expandImageView;
    @FXML
    protected Button addToCartButton;
    @FXML
    protected Label dateLabel;
    @FXML
    protected Label articlesLabel;
    @FXML
    protected Label nmbArticlesLabel;
    @FXML
    protected Label priceLabel;

    private static final Model model = Model.getInstance();
    private static iMatController imatController;
    private Order order;
    private boolean isExpanded;

    public PreviousOrderPanel(Order order, iMatController imatController) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PreviousOrderPanel.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.order = order;
        this.imatController = imatController;
        isExpanded = false;
        this.setPrefHeight(35);

        Format formatter = new SimpleDateFormat("EEEE\t dd-MMM-yyyy  'kl:' HH:mm");
        String date = formatter.format(order.getDate());
        date = capitalizeFirstLetter(date);

        dateLabel.setText(date);
        articlesLabel.setText("");
//        articlesLabel.setText(order.getItems().get(0).getProduct().getName());
        nmbArticlesLabel.setText(Integer.toString(order.getItems().size()) + " st");
        priceLabel.setText("1 kr");
    }

    private String capitalizeFirstLetter(String inString) {
        String outString;

        outString = inString.substring(0, 1);
        outString = outString.toUpperCase();
        outString = outString + inString.substring(1);

        return outString;
    }

    @FXML
    private void handlePrevOrderAdd() {
        System.out.println("Add order nmb: " + order.getOrderNumber() + " | Date: " + order.getDate());
        order.getItems().forEach((item) -> model.addToShoppingCart(item.getProduct()));
        imatController.openCartView();
    }

    @FXML
    private void handlePanelExpand() {
        if(isExpanded) {
            expandImageView.setRotate(90);
//            this.setHeight(24);
            this.setPrefHeight(35);
            isExpanded = false;
        }else {
            expandImageView.setRotate(0);
//            this.setHeight(200);
            this.setPrefHeight(150);
            isExpanded = true;
        }
    }
}
