import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.io.IOException;

public class HelpPanel extends AnchorPane {
    @FXML
    private AnchorPane helpDialogAnchorPane;

    @FXML
    private ImageView helpArrowImageView;

    @FXML
    private Label helpTitleLabel;

    @FXML
    private Label helpTextLabel;

    @FXML
    private Button helpQuitButton;

    @FXML
    private Button helpNextButton;

    @FXML
    private Button helpPrevButton;

    private int helpIndex;
    private String[] helpArray;

    private Shape shadowRegion;

    public HelpPanel() {
        this.helpIndex = 0;
        this.helpArray = new String[]{
                "Här kan du söka bland olika kategorier.",
                "Här kan du söka fritt via ett sökord.",
                "Här kan du ange och ändra personuppgifter samt betalningsmetod.",
                "Här kan du kolla och lägga till varor från tidigare inköp.",
                "Tryck på denna ikon för att lägga till varan i varukorgen.",
                "Genom att klicka på vår logga kan du alltid navigera till startsidan, oavsett vart du är i programmet.",
                "Här kan du se vad som ligger i varukorgen och även betala när du är färdig med ditt besök."
        };

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("helpPanel.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        setStyle("-fx-background-color: #000000DD");
        updateHelpContent();
    }

    private void updateHelpContent() {
        String helpTitle = String.format("Hjälpsteg %d/%d", helpIndex + 1, helpArray.length);
        String helpText = helpArray[helpIndex];
        helpTitleLabel.setText(helpTitle);
        helpTextLabel.setText(helpText);
        updateShadowRegion();
        updateDialog();
        updateArrow();
        updateButtons();
    }

    private void updateDialog() {
        switch (helpIndex) {
            case (0):

                break;
            case (1):

                break;
            case (2):

                break;
            case (3):

                break;
            case (4):

                break;
            case (5):
                helpDialogAnchorPane.setLayoutX(240);
                helpDialogAnchorPane.setLayoutY(260);
                break;
            case (6):

                break;
            default:
                break;
        }
    }

    private void updateArrow() {
        switch (helpIndex) {
            case (0):

                break;
            case (1):

                break;
            case (2):

                break;
            case (3):

                break;
            case (4):

                break;
            case (5):
                helpArrowImageView.setLayoutX(115);
                helpArrowImageView.setLayoutY(120);
                helpArrowImageView.setRotate(235);
                break;
            case (6):

                break;
            default:
                break;
        }
    }

    private void updateShadowRegion() {
        shadowRegion = new Rectangle(0, 0, 1500, 850);
        Shape mask;
        switch (helpIndex) {
            case (0):

                break;
            case (1):

                break;
            case (2):

                break;
            case (3):

                break;
            case (4):

                break;
            case (5):
                mask = new Circle(99, 74, 80);
                shadowRegion = Shape.subtract(shadowRegion, mask);
                break;
            case (6):

                break;
            default:
                break;
        }
        setClip(shadowRegion);
    }

    private void updateButtons() {
        if (helpIndex + 1 >= helpArray.length) {
            helpNextButton.setDisable(true);
        } else {
            helpNextButton.setDisable(false);
        }

        if (helpIndex - 1 < 0) {
            helpPrevButton.setDisable(true);
        } else {
            helpPrevButton.setDisable(false);
        }

    }

    public void showHelp() {
        showHelp(helpIndex);
    }

    public void showHelp(int index) {
        toFront();
        helpIndex = index;
        updateHelpContent();
    }

    @FXML
    private void nextHelp() {
        helpIndex++;
        updateHelpContent();
    }

    @FXML
    private void prevHelp() {
        helpIndex--;
        updateHelpContent();
    }

    @FXML
    public void hideHelp() {
        toBack();
    }
}
