import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class CategoriesHandler {

    protected VBox userCategoriesVBox;
    protected VBox categoryVBox;

    private List<CategoryPanel> categoryPanels;
    private List<PliantButton> subCategoryButtons;

    private static CategoriesHandler instance;
    private iMatController imatController;

    private CategoriesHandler() {

    }

    public static CategoriesHandler getInstance(iMatController imatController, VBox userCategoriesVBox, VBox categoryVBox) {
        if (instance == null) {
            instance = new CategoriesHandler();
            instance.init(imatController, userCategoriesVBox, categoryVBox);
        }
        return instance;
    }

    private void init(iMatController imatController, VBox userCategoriesVBox, VBox categoryVBox) {
        this.imatController = imatController;
        this.userCategoriesVBox = userCategoriesVBox;
        this.categoryVBox = categoryVBox;
        this.categoryPanels = new ArrayList<>();
        this.subCategoryButtons = new ArrayList<>();
        setupVBoxes();

        userCategoriesVBox.toFront();
        categoryVBox.toFront();
    }


    private void setupVBoxes() {
        List<PliantButton> tempButtons;

        userCategoriesVBox.getChildren().add(generateCategoryPanel("Min profil", "profileCategory"));
        userCategoriesVBox.getChildren().add(generateCategoryPanel("Tidigare köp", "prevOrderCategory"));
        userCategoriesVBox.getChildren().add(generateCategoryPanel("Favoriter", "favoritesCategory"));

        categoryVBox.getChildren().add(generateCategoryPanel("Bröd", "breadCategory"));

        tempButtons = new ArrayList<>();
        tempButtons.add(generateSubCategoryButton("Kalla drycker", "coldDrinksCategory"));
        tempButtons.add(generateSubCategoryButton("Mejeri", "dairyCategory"));
        tempButtons.add(generateSubCategoryButton("Varma drycker", "hotDrinksCategory"));
        categoryVBox.getChildren().add(generateCategoryPanel("Kylvaror", "fridgeCategory", tempButtons));

        tempButtons = new ArrayList<>();
        tempButtons.add(generateSubCategoryButton("Bär", "berriesCategory"));
        tempButtons.add(generateSubCategoryButton("Frukter", "fruitsCategory"));
        tempButtons.add(generateSubCategoryButton("Grönsaker", "vegetablesCategory"));
        tempButtons.add(generateSubCategoryButton("Kål", "cabbageCategory"));
        tempButtons.add(generateSubCategoryButton("Rotfrukter", "rootsCategory"));
        categoryVBox.getChildren().add(generateCategoryPanel("Frukt och grönt", "greensCategory", tempButtons));

        tempButtons = new ArrayList<>();
        tempButtons.add(generateSubCategoryButton("Fisk", "fishCategory"));
        tempButtons.add(generateSubCategoryButton("Kött", "meatCategory"));
        categoryVBox.getChildren().add(generateCategoryPanel("Kött och fisk", "meatAndFishCategory", tempButtons));

        tempButtons = new ArrayList<>();
        tempButtons.add(generateSubCategoryButton("Bakning", "bakingCategory"));
        tempButtons.add(generateSubCategoryButton("Kryddor", "spicesCategory"));
        tempButtons.add(generateSubCategoryButton("Linser, bönor och ärtor", "podsCategory"));
        tempButtons.add(generateSubCategoryButton("Pasta", "pastaCategory"));
        tempButtons.add(generateSubCategoryButton("Potatis och ris", "potatoAndRiceCategory"));
        categoryVBox.getChildren().add(generateCategoryPanel("Skafferi", "pantryCategory", tempButtons));

        tempButtons = new ArrayList<>();
        tempButtons.add(generateSubCategoryButton("Nötter och frön", "nutsAndSeedsCategory"));
        tempButtons.add(generateSubCategoryButton("Sötsaker", "sweetsCategory"));
        categoryVBox.getChildren().add(generateCategoryPanel("Sötsaker och snacks", "snacksCategory", tempButtons));
    }

    private CategoryPanel generateCategoryPanel(String name, String id) {
        return generateCategoryPanel(name, id, null);
    }

    private CategoryPanel generateCategoryPanel(String name, String id, List<PliantButton> subCategoryButtons) {
        CategoryPanel categoryPanel;
        if (subCategoryButtons != null) {
            categoryPanel = new CategoryPanel(name, id, subCategoryButtons);
        } else {
            categoryPanel = new CategoryPanel(name, id);
        }
        
        categoryPanel.setText(name);
        categoryPanel.getCategoryHeaderAnchorPane().setOnMouseReleased((eventHandler) -> handleMainCategoryOnAction(categoryPanel));
        categoryPanel.getCategoryHeaderAnchorPane().setOnMouseEntered((eventHandler) -> handleMainCategoryOnHoverEnter(categoryPanel));
        categoryPanel.getCategoryHeaderAnchorPane().setOnMouseExited((eventHandler) -> handleMainCategoryOnHoverExit(categoryPanel));
        categoryPanels.add(categoryPanel);
        return categoryPanel;
    }

    private void handleMainCategoryOnAction(CategoryPanel mainCategoryPanel) {
        boolean wasExpanded = mainCategoryPanel.getExpanded();
        boolean wasSelected = mainCategoryPanel.getSelected();
        clearCategorySelection(true);       //Clear any selection and collapse any category
        if (wasSelected) {
            mainCategoryPanel.setExpanded(!wasExpanded);
        } else {
            mainCategoryPanel.setExpanded(true);
        }
        mainCategoryPanel.setSelected(true);
        imatController.handleCategorySelection(mainCategoryPanel.getCategoryId());
    }

    private void handleMainCategoryOnHoverEnter(CategoryPanel mainCategoryPanel) {
        mainCategoryPanel.setIsHovered(true);
        mainCategoryPanel.setCursor(Cursor.HAND);
    }

    private void handleMainCategoryOnHoverExit(CategoryPanel mainCategoryPanel) {
        mainCategoryPanel.setIsHovered(false);
        mainCategoryPanel.setCursor(Cursor.DEFAULT);
    }

    private PliantButton generateSubCategoryButton(String name, String id) {
        PliantButton button = new PliantButton(id);
        button.setText(name);
        button.setPrefWidth(180);
        button.setStyle("-fx-background-color: white; -fx-border-radius: 2; -fx-border-color: AEAEAE; -fx-border-width: 1; -fx-text-fill: black;");
//        button.setVisible(false);
        button.setOnAction((eventHandler) -> handleSubCategoryOnAction(button));
        button.setOnMouseEntered((eventHandler) -> handleSubCategoryOnHoverEnter(button));
        button.setOnMouseExited((eventHandler) -> handleSubCategoryOnHoverExit(button));
        subCategoryButtons.add(button);
        return button;
    }

    private void handleSubCategoryOnAction(PliantButton subCategoryButton) {
        boolean wasSelected = subCategoryButton.isSelected();
        clearCategorySelection(false); //Clear all selections from any category
        subCategoryButton.setSelected(!wasSelected);
        updateSubCategoryButtonPlicany(subCategoryButton);
        imatController.handleCategorySelection(subCategoryButton.getCategoryId());
    }

    private void handleSubCategoryOnHoverEnter(PliantButton subCategoryButton) {
        subCategoryButton.setHovered(true);
        subCategoryButton.setCursor(Cursor.HAND);
        updateSubCategoryButtonPlicany(subCategoryButton);
    }

    private void handleSubCategoryOnHoverExit(PliantButton subCategoryButton) {
        subCategoryButton.setHovered(false);
        subCategoryButton.setCursor(Cursor.DEFAULT);
        updateSubCategoryButtonPlicany(subCategoryButton);
    }

    private void updateSubCategoryButtonPlicany(PliantButton subCategoryButton) {
        if (subCategoryButton.isHovered() && subCategoryButton.isSelected()) {
            subCategoryButton.setStyle("-fx-background-color: #D0D0D0; -fx-border-color: #e54545; -fx-border-radius: 2; -fx-border-width: 1; -fx-font-size: 14px; -fx-text-fill: black;");
        } else if (subCategoryButton.isHovered()) {
            subCategoryButton.setStyle("-fx-background-color: #D0D0D0; -fx-border-color: #AEAEAE; -fx-border-radius: 2; -fx-border-width: 1; -fx-font-size: 14px; -fx-text-fill: black;");
        } else if (subCategoryButton.isSelected()) {
            subCategoryButton.setStyle("-fx-background-color: #E0E0E0; -fx-border-color: #e54545;  -fx-border-radius: 2; -fx-border-width: 1; -fx-font-size: 14px; -fx-text-fill: black;");
        } else {
            subCategoryButton.setStyle("-fx-background-color: white; -fx-border-color: #AEAEAE; -fx-border-radius: 2; -fx-border-width: 1; -fx-font-size: 14px; -fx-text-fill: black;");
        }
    }

    public void clearCategorySelection(boolean doClearExpansion) {
        subCategoryButtons.forEach(button -> {               //Remove selection from all other subCategory
            button.setSelected(false);
            updateSubCategoryButtonPlicany(button);
        });
        if (doClearExpansion) {
            categoryPanels.forEach(panel -> {                    //Remove selection from all category
                panel.setSelected(false);
                panel.setExpanded(false);
            });
        } else {
            categoryPanels.forEach(panel -> {                    //Remove selection from all category
                panel.setSelected(false);
            });
        }
    }

}
