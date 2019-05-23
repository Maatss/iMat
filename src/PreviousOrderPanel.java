import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import se.chalmers.cse.dat216.project.Order;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;

public class PreviousOrderPanel extends AnchorPane {
    @FXML
    protected AnchorPane topBarAnchorPane;
    @FXML
    protected ImageView expandImageView;
    @FXML
    protected Button addToCartButton;
    @FXML
    protected Label dayLabel;
    @FXML
    protected Label dateLabel;
    @FXML
    protected Label nmbArticlesLabel;
    @FXML
    protected Label priceLabel;
    @FXML
    protected AnchorPane bottomContainerAnchorPane;

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

        Format formatter = new SimpleDateFormat("EEEE");
        String day = formatter.format(order.getDate());
        formatter = new SimpleDateFormat("dd-MMM-yyyy  'kl:' HH:mm");
        String date = formatter.format(order.getDate());
        day = capitalizeFirstLetter(day);

        int count = 0;
        for (ShoppingItem cartItem : order.getItems()) {
            count += cartItem.getAmount();
        }

        dayLabel.setText(day);
        dateLabel.setText(date);
        nmbArticlesLabel.setText(count + " st");
        double totalPrice = 0;
        for (ShoppingItem item : order.getItems()) {
            totalPrice += item.getTotal();
        }
        String price = String.format("%.2f Kr", totalPrice);
        priceLabel.setText(price);
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
        order.getItems().forEach((ShoppingItem item) -> model.addToShoppingCart(item.getProduct(), item.getAmount()));
        imatController.openCartView();
    }

    @FXML
    private void handlePanelExpand() {
        setPaneExpandedHeight();
    }

    @FXML
    private void handleMouseHoverEnter() {
        setTopBarAnchorPaneColor(Color.rgb(200, 200, 200));
        setCursor(Cursor.HAND);
    }

    @FXML
    private void handleMouseHoverExit() {
        setTopBarAnchorPaneColor(Color.rgb(230, 230, 230));
        setCursor(Cursor.DEFAULT);
    }

    private void setPaneExpandedHeight() {
        if (isExpanded) {
            expandImageView.setRotate(90);
            this.setPrefHeight(35);
            bottomContainerAnchorPane.setPrefHeight(35);
            bottomContainerAnchorPane.getChildren().clear();
            isExpanded = false;
        } else {
            expandImageView.setRotate(0);
            bottomContainerAnchorPane.getChildren().clear();
            for (int i = 0; i < order.getItems().size(); i++) {
                double y = 5 + 20 * i;
                String name = order.getItems().get(i).getProduct().getName();
                String nmb = String.format("%.0f st", order.getItems().get(i).getAmount());
                String price = String.format("%.2f kr", order.getItems().get(i).getTotal());
                generateOrderItemElement(16, y, name);
                generateOrderItemElement(435, y, nmb);
                generateOrderItemElement(500, y, price);
            }
            bottomContainerAnchorPane.setPrefHeight(44 + 20 * order.getItems().size());
            this.setPrefHeight(44 + 20 * order.getItems().size() + 3);
            isExpanded = true;
        }
    }

    private void generateOrderItemElement(double x, double y, String text) {
        Label namelabel = new Label();
        namelabel.setText(text);
        namelabel.setLayoutX(x);
        namelabel.setLayoutY(y);
        bottomContainerAnchorPane.getChildren().add(namelabel);
    }

    private void setTopBarAnchorPaneColor(Color color) {
        BackgroundFill backgroundFill = new BackgroundFill(color,
                CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(backgroundFill);
        topBarAnchorPane.setBackground(background);
    }
}
