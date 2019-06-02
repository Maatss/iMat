import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
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

    private iMatController imatController;
    private int helpIndex;
    private String[] helpArray;

    private Shape shadowRegion;
    private boolean nextButtonIsHovered;

    public HelpPanel(iMatController imatController) {
        this.imatController = imatController;
        nextButtonIsHovered = false;
        helpIndex = 0;
        helpArray = new String[]{
                "Välkommen till vår hjälpguide!\nHär ges en snabb genomgång av hur du genomför ett köp.\n För att hitta den här hjälpguide igen, trycker du bara här!",
                "Här kan du söka bland olika kategorier.",
                "Här kan du söka fritt med egna sökord.",
                "Här kan du ange och ändra dina personuppgifter samt betalningsmetod.",
                "Här hittar du dina kvitton. Du kan även välja att lägga till varor från ett tidigare köp i din varukorg härifrån.",
                "Tryck på denna ikon för att lägga till varan i varukorgen.\nOmvänt används '-' knappen för att plocka bort en vara från varukorgen.",
                "Genom att klicka på vår logga kan du alltid navigera till startsidan, oavsett vart du är i programmet.",
                "Tryck här för att hitta produkterna som du lagt till i din varukorg.\nDet är även här som du går vidare med ditt köp till kassan om du så önskar. Det var allt, tack för att du använder iMat!"
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
        imatController.handleLogoAction();
        switch (helpIndex) {
            case (0): //Välkommen
                helpDialogAnchorPane.setLayoutX(450);
                helpDialogAnchorPane.setLayoutY(350);
                break;
            case (1): //Categories
                helpDialogAnchorPane.setLayoutX(450);
                helpDialogAnchorPane.setLayoutY(350);
                break;
            case (2): //Search field
                helpDialogAnchorPane.setLayoutX(250);
                helpDialogAnchorPane.setLayoutY(260);
                break;
            case (3): // Min Profil sida
                helpDialogAnchorPane.setLayoutX(380);
                helpDialogAnchorPane.setLayoutY(80);
                break;
            case (4): //Tidigare köp
                helpDialogAnchorPane.setLayoutX(380);
                helpDialogAnchorPane.setLayoutY(127);
                break;
            case (5): //Lägga till vara i varukorg
                helpDialogAnchorPane.setLayoutX(100);
                helpDialogAnchorPane.setLayoutY(500);
                break;
            case (6): //Navigera till startsida via logga
                helpDialogAnchorPane.setLayoutX(240);
                helpDialogAnchorPane.setLayoutY(260);
                break;
            case (7): //Varukorg
                helpDialogAnchorPane.setLayoutX(900);
                helpDialogAnchorPane.setLayoutY(250);
                break;
            default:
                break;
        }
    }

    private void updateArrow() {
        switch (helpIndex) {
            case (0): //Välkommen
                helpArrowImageView.setLayoutX(1205);
                helpArrowImageView.setLayoutY(140);
                helpArrowImageView.setRotate(315);
                break;
            case (1): //Categories
                helpArrowImageView.setLayoutX(250);
                helpArrowImageView.setLayoutY(375);
                helpArrowImageView.setRotate(180);
                break;
            case (2): //Search field
                helpArrowImageView.setLayoutX(450);
                helpArrowImageView.setLayoutY(100);
                helpArrowImageView.setRotate(270);
                break;
            case (3): // Min Profil sida
                helpArrowImageView.setLayoutX(200);
                helpArrowImageView.setLayoutY(102);
                helpArrowImageView.setRotate(180);
                break;
            case (4): //Tidigare köp
                helpArrowImageView.setLayoutX(200);
                helpArrowImageView.setLayoutY(149);
                helpArrowImageView.setRotate(180);
                break;
            case (5): //Lägga till vara i varukorg
                helpArrowImageView.setLayoutX(315);
                helpArrowImageView.setLayoutY(250);
                helpArrowImageView.setRotate(90);
                break;
            case (6): //Navigera till startsida via logga
                helpArrowImageView.setLayoutX(115);
                helpArrowImageView.setLayoutY(120);
                helpArrowImageView.setRotate(235);
                break;
            case (7): //Varukorg
                helpArrowImageView.setLayoutX(1250);
                helpArrowImageView.setLayoutY(100);
                helpArrowImageView.setRotate(270);
                break;
            default:
                break;
        }
    }

    private void updateShadowRegion() {
        shadowRegion = new Rectangle(0, 0, 1500, 850);
        Shape mask = new Rectangle();
        switch (helpIndex) {
            case (0): //Välkommen
                mask = new Rectangle(1335, 110, 150, 50);
                break;
            case (1): //Categories
                mask = new Rectangle(0, 300, 210, 325);
                break;
            case (2): //Search field
                mask = new Rectangle(190, 20, 925, 75);
                break;
            case (3): // Min Profil sida
                mask = new Rectangle(0, 150, 210, 55);
                break;
            case (4): //Tidigare köp
                mask = new Rectangle(0, 197, 210, 55);
                break;
            case (5): //Lägga till vara i varukorg
                mask = new Rectangle(368, 412, 45, 45);
                break;
            case (6): //Navigera till startsida via logga
                mask = new Circle(99, 74, 80);
                break;
            case (7): //Varukorg
                mask = new Rectangle(1150, 0, 340, 107);
                break;
            default:
                break;
        }
        shadowRegion = Shape.subtract(shadowRegion, mask);
        setClip(shadowRegion);
    }

    private void updateButtons() {
        if (helpIndex + 1 >= helpArray.length) {
            helpQuitButton.setDisable(true);
        } else {
            helpQuitButton.setDisable(false);

        }

        if (helpIndex - 1 < 0) {
            helpPrevButton.setDisable(true);
        } else {
            helpPrevButton.setDisable(false);
        }
        updateNextButton();
    }

    private void updateNextButton() {
        if (helpIndex + 1 >= helpArray.length) {
            helpNextButton.setOnAction((actionEvent) -> hideHelp());
            helpNextButton.setText("Avsluta");
            if (nextButtonIsHovered) {
                helpNextButton.setStyle("-fx-background-color: #D57002;");
                setCursor(Cursor.HAND);
            } else {
                helpNextButton.setStyle("-fx-background-color: #F79204;");
                setCursor(Cursor.DEFAULT);
            }
        } else {
            helpNextButton.setOnAction((actionEvent) -> nextHelp());
            helpNextButton.setText("Nästa steg");
            if (nextButtonIsHovered) {
                helpNextButton.setStyle("-fx-background-color: #D43434;");
                setCursor(Cursor.HAND);
            } else {
                helpNextButton.setStyle("-fx-background-color: #E54545;");
                setCursor(Cursor.DEFAULT);
            }
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

    @FXML
    private void nextButtonHoverEnter() {
        nextButtonIsHovered = true;
        updateNextButton();
    }

    @FXML
    private void nextButtonHoverExit() {
        nextButtonIsHovered = false;
        updateNextButton();
    }
}
