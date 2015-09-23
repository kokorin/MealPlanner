package mealplanner.view.dish;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import mealplanner.domain.Diet;
import mealplanner.domain.Dish;
import mealplanner.domain.Ingredient;
import mealplanner.domain.Unit;
import mealplanner.view.SelectController;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class DishSelectController extends SelectController<Dish> {
    @FXML
    private TableColumn<Dish, Long> idColumn;
    @FXML
    private TableColumn<Dish, String> nameColumn;
    @FXML
    private TableColumn<Dish, Double> yieldColumn;
    @FXML
    private TableColumn<Dish, Unit> yieldUnitColumn;
    @FXML
    private TableColumn<Dish, String> dietsColumn;
    @FXML
    private TableColumn<Dish, String> ingredientsColumn;

    public static final URL DIALOG_LOCATION = DishSelectController.class.getResource("DishSelectDialog.fxml");

    @Override
    //TODO code duplicates DishListController.initialize
    protected void initialize() {
        super.initialize();
        assert idColumn != null;
        assert nameColumn != null;
        assert yieldColumn != null;
        assert yieldUnitColumn != null;
        assert dietsColumn != null;
        assert ingredientsColumn != null;

        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        yieldColumn.setCellValueFactory(cellData -> cellData.getValue().yieldProperty().asObject());
        yieldUnitColumn.setCellValueFactory(cellData -> cellData.getValue().yieldUnitProperty());
        dietsColumn.setCellValueFactory(cellData -> {
            StringProperty result = new SimpleStringProperty();
            cellData.getValue().getDiets().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    result.set(getDietsLabel(cellData.getValue().getDiets()));
                }
            });
            return result;
        });
        ingredientsColumn.setCellValueFactory(cellData -> {
            StringProperty result = new SimpleStringProperty(getIngredientsLabel(cellData.getValue().getIngredients()));
            cellData.getValue().getIngredients().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    result.set(getIngredientsLabel(cellData.getValue().getIngredients()));
                }
            });
            return result;
        });
    }

    private static String getDietsLabel(List<Diet> diets) {
        return diets.stream().
                map(Diet::getName).
                collect(Collectors.joining(", "));
    }

    private static String getIngredientsLabel(List<Ingredient> ingredients) {
        return ingredients.stream().
                map(ingredient -> ingredient.getProduct().getName()).
                collect(Collectors.joining(", "));
    }
}
