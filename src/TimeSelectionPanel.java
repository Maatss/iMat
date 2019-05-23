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



    public TimeSelectionPanel(Date date){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("TimeSelectionPanel.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        dateLabel.setText(date.getDate() + "/" + date.getMonth());
        dayOfWeekLabel.setText(convertDateToDay(date));

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
                return "Error 352";
        }
    }

    @FXML private void handleTimeSelectOne(){
        setModelDeliveryDate();
        Model.setDeliveryTime("8-10");
    }
    @FXML private void handleTimeSelectTwo(){
        setModelDeliveryDate();
        Model.setDeliveryTime("10-12");
    }
    @FXML private void handleTimeSelectThree(){
        setModelDeliveryDate();
        Model.setDeliveryTime("12-14");
    }
    @FXML private void handleTimeSelectFour(){
        setModelDeliveryDate();
        Model.setDeliveryTime("14-16");
    }

    private void setModelDeliveryDate(){
        Model.setDeliveryDate(dayOfWeekLabel.getText());
    }
}
