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

    public static HelpHandler getInstance(AnchorPane appPane) {
        if (instance == null) {
            instance = new HelpHandler();
            instance.init(appPane);
        }
        return instance;
    }

    private void init(AnchorPane appPane) {
        this.appPane = appPane;
        this.helpPanel = new HelpPanel();
        appPane.getChildren().add(helpPanel);
        helpPanel.toFront();
    }

    public void show() {
        helpPanel.showHelp();
    }

    public void hide() {
        helpPanel.hideHelp();
    }
}
