import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Date;

public class TimeSelectionPanel extends AnchorPane {

    @FXML
    private Label dateLabel;

    @FXML
    private Label dayOfWeekLabel;

    Date deliveryDate;
    private iMatController controller;

    public TimeSelectionPanel(Date date, iMatController controller){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("TimeSelectionPanel.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        deliveryDate = date;
        this.controller = controller;
        updateDateLabels();
    }

    private String convertDateToDay(Date date){
        switch(date.getDay()){
            case (0):
                return "Söndag";
            case(1):
                return "Måndag";
            case (2):
                return "Tisdag";
            case (3):
                return "Onsdag";
            case(4):
                return "Torsdag";
            case (5):
                return "Fredag";
            case (6):
                return "Lördag";
            default:
                return "Error when converting Date to String";
        }
    }

    @FXML private void handleTimeSelectOne(){
        updateModelDeliveryDate();
        Model.setDeliveryTime("8-10");
        updateCheckoutTimeButton();
    }
    @FXML private void handleTimeSelectTwo(){
        updateModelDeliveryDate();
        Model.setDeliveryTime("10-12");
        updateCheckoutTimeButton();
    }
    @FXML private void handleTimeSelectThree(){
        updateModelDeliveryDate();
        Model.setDeliveryTime("12-14");
        updateCheckoutTimeButton();
    }
    @FXML private void handleTimeSelectFour(){
        updateModelDeliveryDate();
        Model.setDeliveryTime("14-16");
        updateCheckoutTimeButton();
    }

    private void updateModelDeliveryDate(){
        Model.setDeliveryDateDay(dayOfWeekLabel.getText());
        Model.setDeliveryDate(dateLabel.getText());
    }

    private void updateCheckoutTimeButton(){
        controller.updateCheckoutTimeButton();
    }

    private void updateDateLabels(){
        dateLabel.setText(deliveryDate.getDate() + "/" + (deliveryDate.getMonth() + 1));
        dayOfWeekLabel.setText(convertDateToDay(deliveryDate));
    }

    public void decrementDay(){
        deliveryDate = new Date(deliveryDate.getTime()-(24*60*60*1000));
        updateDateLabels();
        updateModelDeliveryDate();
    }

    public void incrementDay(){
        deliveryDate = new Date(deliveryDate.getTime()+(24*60*60*1000));
        updateDateLabels();
        updateModelDeliveryDate();
    }
}
