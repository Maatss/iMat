import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Order;

import java.io.IOException;

public class PreviousOrderPanel extends AnchorPane {
    @FXML
    protected Button addToCart;
    @FXML
    protected Label dateLabel;
    @FXML
    protected Label articlesLabel;
    @FXML
    protected Label nmbArticlesLabel;
    @FXML
    protected Label priceLabel;

    private Order order;

    public PreviousOrderPanel(Order order) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PreviousOrderPanel.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.order = order;
        dateLabel.setText(order.getDate().toString());
        articlesLabel.setText(order.getItems().get(0).getProduct().getName());
        nmbArticlesLabel.setText(Integer.toString(order.getItems().size()));
        priceLabel.setText("1");
    }


}
