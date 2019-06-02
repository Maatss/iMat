import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import se.chalmers.cse.dat216.project.Order;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
    //    @FXML
////    protected AnchorPane bottomContainerAnchorPane;
    @FXML
    protected VBox bottomContainerVBox;

    private static final double TOP_BAR_HEIGHT = 58;

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
        this.setPrefHeight(TOP_BAR_HEIGHT);

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
        setTooltips();
    }

    private void setTooltips() {
        Tooltip.install(addToCartButton, new Tooltip("Klicka här för att lägga till alla varor från det här köpet i din varukorg."));
        Tooltip.install(topBarAnchorPane, new Tooltip("Klicka här för att visa mer detaljer om köpet."));
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
    protected void handlePanelExpand() {
        setPaneExpandedHeight();
    }

    @FXML
    private void handleMouseHoverEnter() {
        topBarAnchorPane.setStyle("-fx-background-color: #DEDEDE;");
        setCursor(Cursor.HAND);
    }

    @FXML
    private void handleMouseHoverExit() {
        if (isExpanded) {
            topBarAnchorPane.setStyle("-fx-background-color: #CECECE;");
        } else {
            topBarAnchorPane.setStyle("-fx-background-color: white;");
        }
        setCursor(Cursor.DEFAULT);
    }

    private void setPaneExpandedHeight() {
        if (isExpanded) {
            expandImageView.setRotate(270);
            this.setPrefHeight(TOP_BAR_HEIGHT);
            topBarAnchorPane.setStyle("-fx-background-color: white;");
            bottomContainerVBox.getChildren().clear();
            bottomContainerVBox.setPrefHeight(TOP_BAR_HEIGHT);
            isExpanded = false;
        } else {
            expandImageView.setRotate(0);
            topBarAnchorPane.setStyle("-fx-background-color: #CECECE;");
            bottomContainerVBox.getChildren().clear();
            for (int i = 0; i < order.getItems().size(); i++) {
//                double y = 5 + 20 * i;
                String name = order.getItems().get(i).getProduct().getName();
                String nmb = String.format("%.0f st", order.getItems().get(i).getAmount());
                String price = String.format("%.2f kr", order.getItems().get(i).getTotal());

                List<VBox> elementLabelVBoxes = new ArrayList<>();
                elementLabelVBoxes.add(generateOrderItemLabel(20, 0, name, false));
                elementLabelVBoxes.add(generateOrderItemLabel(930 - 70, 0, nmb, true));
                elementLabelVBoxes.add(generateOrderItemLabel(1035 - 40, 0, price, true));

                bottomContainerVBox.getChildren().add(generateOrderItemElement(elementLabelVBoxes, i));
            }
            this.setPrefHeight(TOP_BAR_HEIGHT + 8 + 29 * order.getItems().size());
            isExpanded = true;
        }
    }

    private VBox generateOrderItemLabel(double x, double y, String text, boolean doRightAlign) {
        Label label = new Label();
        label.setText(text);
        label.setStyle("-fx-font: 18 System;");

        VBox vBox = new VBox();
        if (doRightAlign) {
            vBox.setPrefSize(100, 12);
            vBox.setAlignment(Pos.CENTER_RIGHT);
        }
        vBox.setLayoutX(x);
        vBox.setLayoutY(y);
        vBox.getChildren().add(label);
        return vBox;
    }

    private AnchorPane generateOrderItemElement(List<VBox> vBoxes, int i) {
        AnchorPane elementPane = new AnchorPane();
        if ((i % 2) == 0) {
            elementPane.setStyle("-fx-background-color: white;");
            elementPane.setOnMouseEntered(pane -> handleElementHoverEnter(elementPane, "-fx-background-color: #DEDEDE;"));
            elementPane.setOnMouseExited(pane -> handleElementHoverExit(elementPane, "-fx-background-color: white;"));
        } else {
            elementPane.setStyle("-fx-background-color: #EEEEEE;");
            elementPane.setOnMouseEntered(pane -> handleElementHoverEnter(elementPane, "-fx-background-color: #DEDEDE;"));
            elementPane.setOnMouseExited(pane -> handleElementHoverExit(elementPane, "-fx-background-color: #EEEEEE;"));
        }
        elementPane.setLayoutX(0);
        elementPane.setPrefWidth(1210);
        elementPane.setPrefHeight(20);
        elementPane.getChildren().addAll(vBoxes);
        return elementPane;
    }

    private void handleElementHoverEnter(AnchorPane pane, String style) {
        pane.setStyle(style);
    }

    private void handleElementHoverExit(AnchorPane pane, String style) {
        pane.setStyle(style);
    }
}
