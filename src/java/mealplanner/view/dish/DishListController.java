package mealplanner.view.dish;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import mealplanner.domain.*;
import mealplanner.view.ListController;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class DishListController extends ListController<Dish> {
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

    private Data data;

    public static final URL DIALOG_LOCATION = DishListController.class.getResource("DishListDialog.fxml");

    public DishListController() {
        super(DishEditController.DIALOG_LOCATION, "Dish");
    }

    @Override
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
            StringProperty result = new SimpleStringProperty(getDietsLabel(cellData.getValue().getDiets()));
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

    @Override
    public void setData(Data data) {
        this.data = data;
        table.setItems(data.getDishes());
    }

    @Override
    protected Callback<Class<?>, Object> createEditorControllerFactory() {
        return controllerClass -> {
            Object result = null;

            try {
                result = controllerClass.newInstance();
            } catch (InstantiationException|IllegalAccessException e) {
                e.printStackTrace();
            }

            if (result instanceof DishEditController) {
                DishEditController controller = (DishEditController)result;
                controller.setDietList(data.getDiets());
                controller.setProductList(data.getProducts());
            }

            return result;
        };
    }

    @Override
    protected List<String> findUsages(Dish dish) {
        List<String> result = new ArrayList<>();
        for (Meal meal : data.getMeals()) {
            for (MealItem mealItem : meal.getItems()) {
                if (mealItem.getDish().equals(dish)) {
                    result.add(meal.getName());
                    break;
                }
            }
        }

        return result;
    }

    @Override
    protected boolean removeUsages(Dish dish) {
        for (Meal meal : data.getMeals()) {
            for (Iterator<MealItem> mealItemIterator = meal.getItems().iterator();
                    mealItemIterator.hasNext(); ) {
                if (mealItemIterator.next().getDish().equals(dish)) {
                    mealItemIterator.remove();
                }
            }
        }

        return true;
    }
}
