import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import org.omg.SendingContext.RunTime;

import java.io.IOException;

public class HelpHandler extends AnchorPane {
    private HelpPanel helpPanel;
    private static HelpHandler instance;

    private HelpHandler() {

    }

    public static HelpHandler getInstance() {
        if (instance == null) {
            instance = new HelpHandler();
            instance.init();
        }
        return instance;
    }

    private void init() {
        this.helpPanel = new HelpPanel();
    }
}
