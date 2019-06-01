import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class HelpPanel extends AnchorPane {
    @FXML
    private AnchorPane bottomHelpAnchorPane;

    @FXML
    private AnchorPane helpAnchorPane;

    @FXML
    private Label helpTitleLabel;

    @FXML
    private Label helpTextLabel;

    @FXML
    private Button helpNextButton;

    @FXML
    private Button helpPrevButton;

    private int helpIndex;
    private String[] helpArray;
//    private boolean showHelp;

    public HelpPanel() {
        this.helpIndex = 0;
        this.helpArray = new String[]{
                "Här kan du söka bland olika kategorier",
                "Här kan du söka fritt via ett sökord",
                "Här kan du ändra personuppgifter samt betalningsmetod",
                "Här kan du kolla och lägga till varor från tidigare inköp",
                "Tryck på denna ikon för att lägga till varan i varukorgen",
                "Tryck här för att komma tillbaka till startsidan",
                "Här kan du se vad som ligger i varukorgen och även betala när du är färdig med ditt besök"
        };
    }

    private void init() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("helpPanel.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void updateHelpContent() {
        String helpTitle = String.format("Hjälpsteg %d/%d", helpIndex + 1, helpArray.length);
        String helpText = helpArray[helpIndex];
        helpTitleLabel.setText(helpTitle);
        helpTextLabel.setText(helpText);
    }

    public void showHelp(int index) {
        System.out.println("Help handler showing");
        bottomHelpAnchorPane.toFront();
        helpAnchorPane.toFront();
        helpIndex = index;
        updateHelpContent();
//        bottomHelpAnchorPane.toFront();
//        Platform.runLater(() -> {
//        });
    }

    @FXML
    private void nextHelp() {
        if (helpIndex + 1 >= helpArray.length) {
            helpNextButton.setDisable(true);
        } else {
            helpNextButton.setDisable(false);
            helpIndex++;
        }
        updateHelpContent();
    }

    @FXML
    private void prevHelp() {
        if (helpIndex - 1 <= 0) {
            helpNextButton.setDisable(true);
        } else {
            helpNextButton.setDisable(false);
            helpIndex--;
        }
        updateHelpContent();
    }

    @FXML
    public void hideHelp() {
//        bottomHelpAnchorPane.toBack();
        bottomHelpAnchorPane.toBack();
    }
}
