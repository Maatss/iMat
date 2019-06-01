import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Date;

public class TimeSelectionPanel extends AnchorPane {

    @FXML
    private Label dateLabel;
    @FXML
    private Label dayOfWeekLabel;
    @FXML
    private Button timeSelectOne;
    @FXML
    private Button timeSelectTwo;
    @FXML
    private Button timeSelectThree;
    @FXML
    private Button timeSelectFour;

    private Date deliveryDate;
    private iMatController controller;
    private String selectedStyle = "-fx-text-fill: white; -fx-background-color:  #e54545; -fx-background-radius: 8; -fx-border-color:  #101010; -fx-border-radius: 5; -fx-border-width: 1;";

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

        //Just to avoid NPE first time
        controller.selectedTimeButton = timeSelectFour;
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
        controller.clearSelectedTimeAndSaveNew(timeSelectOne);
        timeSelectOne.setStyle(selectedStyle);
    }
    @FXML private void handleTimeSelectTwo(){
        updateModelDeliveryDate();
        Model.setDeliveryTime("10-12");
        updateCheckoutTimeButton();
        controller.clearSelectedTimeAndSaveNew(timeSelectTwo);
        timeSelectTwo.setStyle(selectedStyle);
    }
    @FXML private void handleTimeSelectThree(){
        updateModelDeliveryDate();
        Model.setDeliveryTime("12-14");
        updateCheckoutTimeButton();
        controller.clearSelectedTimeAndSaveNew(timeSelectThree);
        timeSelectThree.setStyle(selectedStyle);
    }
    @FXML private void handleTimeSelectFour(){
        updateModelDeliveryDate();
        Model.setDeliveryTime("14-16");
        updateCheckoutTimeButton();
        controller.clearSelectedTimeAndSaveNew(timeSelectFour);
        timeSelectFour.setStyle(selectedStyle);
    }

    private void updateModelDeliveryDate(){
        Model.setDeliveryDateWeekday(dayOfWeekLabel.getText());
        Model.setDeliveryDateDDM(dateLabel.getText());

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

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
}
