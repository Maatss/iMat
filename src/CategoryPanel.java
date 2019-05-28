import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CategoryPanel extends AnchorPane {
    @FXML
    private AnchorPane categoryHeaderAnchorPane;
    @FXML
    private Label categoryPanelNameLabel;
    @FXML
    private ImageView categoryExpandImageView;
    @FXML
    private VBox categoryChildVBox;

    private String name;
    private String id;
    private boolean hasSubs;
    private boolean isExpanded;
    private boolean isSelected;
    private boolean isHovered;
    private List<PliantButton> subCategoryButtons;

    public CategoryPanel(String name, String id, List<PliantButton> subCategoryButtons) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("categoryPanel.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.name = name;
        this.id = id;
        this.subCategoryButtons = subCategoryButtons;
        init();
    }

    public CategoryPanel(String name, String id) {
        this(name, id, new ArrayList<>());
    }

    private void init() {
        this.isExpanded = false;
        this.isHovered = false;
        this.isSelected = false;

        if (subCategoryButtons.size() < 1) {
            hasSubs = false;
            categoryExpandImageView.setVisible(false);
        } else {
            hasSubs = true;
            categoryChildVBox.getChildren().addAll(subCategoryButtons);
        }

        categoryChildVBox.setVisible(false);
        categoryPanelNameLabel.setText(name);
        this.setPrefHeight(40);
    }

    private void updateCategoryHeight() {
        this.setPrefHeight(45 + 36 * subCategoryButtons.size() + 5);
    }

    private void updateExpandState() {
        if (hasSubs && isExpanded) {
            categoryExpandImageView.setRotate(0);
            this.setPrefHeight(130);
            categoryChildVBox.setVisible(true);
            updateCategoryHeight();
        } else if (hasSubs) {
            categoryExpandImageView.setRotate(270);
            this.setPrefHeight(40);
            categoryChildVBox.setVisible(false);
        }
        updatePliancy();
    }

    private void updatePliancy() {
        String style = "";
        if (isHovered) {
            style = "-fx-background-color:  #D0D0D0;";      //Dark grey
        } else if (isExpanded) {
            style = "-fx-background-color:  #E0E0E0;";      //Grey
        } else {
            style = "-fx-background-color:  white;";        //White
        }

        if (isSelected) {
            style = style + " -fx-border-color: #e54545; -fx-border-radius: 2; -fx-border-width: 2;"; //Red border
        } else {
            style = style + " -fx-border-color: #AEAEAE;"; //Normal thin grey border
        }

        categoryHeaderAnchorPane.setStyle(style);
    }

    public void setExpanded(boolean isExpanded) {
        this.isExpanded = isExpanded;
        updateExpandState();
    }

    public boolean getExpanded() {
        return isExpanded;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
        updateExpandState();
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setIsHovered(boolean isHovered) {
        this.isHovered = isHovered;
        updatePliancy();
    }

    public void setText(String name) {
        this.name = name;
    }

    public String getText() {
        return name;
    }

    public List<PliantButton> getSubCategoryButtons() {
        return subCategoryButtons;
    }

    public String getCategoryId() {
        return id;
    }
}
