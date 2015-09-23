package mealplanner.view.meal;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import mealplanner.domain.Meal;
import mealplanner.view.EditController;

import java.net.URL;

public class MealEditController extends EditController<Meal> {
    @FXML
    private TextField nameInput;
    private Meal meal;

    public static final URL DIALOG_LOCATION = MealEditController.class.getResource("MealEditDialog.fxml");

    @Override
    protected void initialize() {
        super.initialize();
        assert nameInput != null;
    }

    @Override
    public void setItem(Meal meal) {
        if (meal == null) {
            meal = new Meal();
        } else {
            meal = Meal.copy(meal);
        }

        this.meal = meal;
        nameInput.textProperty().bindBidirectional(meal.nameProperty());
    }

    @Override
    public Meal getItem() {
        return meal;
    }
}
