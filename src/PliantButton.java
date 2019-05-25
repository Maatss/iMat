import javafx.scene.control.Button;

public class PliantButton extends Button {
    private boolean isHovered;
    private boolean isSelected;
    private String id;

    public PliantButton(String id) {
        this.id = id;
        isHovered = false;
        isSelected = false;
    }

    public boolean isHovered() {
        return isHovered;
    }

    public void setHovered(boolean hovered) {
        isHovered = hovered;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getCategoryId() {
        return id;
    }
}
