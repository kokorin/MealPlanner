package mealplanner.view.diet;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import mealplanner.domain.Diet;
import mealplanner.view.EditController;

import java.net.URL;

public class DietEditController extends EditController<Diet> {
    @FXML
    private TextField nameInput;
    private Diet diet;

    public static final URL DIALOG_LOCATION = DietEditController.class.getResource("DietEditDialog.fxml");

    @Override
    protected void initialize() {
        super.initialize();
        assert nameInput != null;
    }

    @Override
    public void setItem(Diet diet) {
        if (diet == null) {
            diet = new Diet();
        } else {
            diet = Diet.copy(diet);
        }

        this.diet = diet;
        nameInput.textProperty().bindBidirectional(diet.nameProperty());
    }

    @Override
    public Diet getItem() {
        return diet;
    }
}
