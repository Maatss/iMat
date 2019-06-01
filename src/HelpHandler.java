import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import org.omg.SendingContext.RunTime;

import javax.management.MBeanConstructorInfo;
import java.io.IOException;

public class HelpHandler extends AnchorPane {
    private AnchorPane appPane;
    private HelpPanel helpPanel;
    private static HelpHandler instance;

    private HelpHandler() {

    }

    public static HelpHandler getInstance(iMatController imatController, AnchorPane appPane) {
        if (instance == null) {
            instance = new HelpHandler();
            instance.init(imatController, appPane);
        }
        return instance;
    }

    private void init(iMatController imatController, AnchorPane appPane) {
        this.appPane = appPane;
        this.helpPanel = new HelpPanel(imatController);
        appPane.getChildren().add(helpPanel);
        helpPanel.toBack();
//        helpPanel.toFront();
    }

    public void show() {
        show(0);
    }

    public void show(int index) {
        helpPanel.showHelp(index);
    }

    public void hide() {
        helpPanel.hideHelp();
    }
}
