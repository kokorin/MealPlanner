package mealplanner.view.dish;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.converter.DoubleStringConverter;
import mealplanner.domain.Ingredient;
import mealplanner.view.EditController;

import java.net.URL;

public class IngredientEditController extends EditController<Ingredient> {
    @FXML
    private Label productLabel;
    @FXML
    private TextField grossInput;
    @FXML
    private TextField netInput;

    private Ingredient item;

    public static final URL DIALOG_LOCATION = IngredientEditController.class.getResource("IngredientEditDialog.fxml");

    public IngredientEditController() {
    }

    @Override
    public void setItem(Ingredient item) {
        if (item == null) {
            throw new IllegalArgumentException("Ingredient is null");
        }

        item = Ingredient.copy(item);
        this.item = item;

        productLabel.setText(item.getProduct().getName());
        grossInput.textProperty().bindBidirectional(item.grossProperty().asObject(), new DoubleStringConverter());
        netInput.textProperty().bindBidirectional(item.netProperty().asObject(), new DoubleStringConverter());
    }

    @Override
    public Ingredient getItem() {
        return item;
    }
}
