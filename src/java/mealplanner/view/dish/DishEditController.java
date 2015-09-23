package mealplanner.view.dish;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import mealplanner.domain.*;
import mealplanner.view.EditController;
import mealplanner.view.SelectController;
import mealplanner.view.diet.DietSelectController;
import mealplanner.view.product.ProductSelectController;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class DishEditController extends EditController<Dish> {
    @FXML
    private TextField nameInput;
    @FXML
    private TextField yieldInput;
    @FXML
    private ComboBox<Unit> yieldUnitComboBox;
    @FXML
    private Button removeDietButton;

    @FXML
    private TableView<Diet> dietTable;
    @FXML
    private TableColumn<Diet, Long> dietIdColumn;
    @FXML
    private TableColumn<Diet, String> dietNameColumn;
    @FXML
    private Button openIngredientButton;


    @FXML
    private Button removeIngredientButton;
    @FXML
    private TableView<Ingredient> ingredientTable;
    @FXML
    private TableColumn<Ingredient, String> ingredientNameColumn;
    @FXML
    private TableColumn<Ingredient, Double> ingredientGrossColumn;
    @FXML
    private TableColumn<Ingredient, Double> ingredientNetColumn;

    private Dish dish;
    private ObservableList<Diet> dietList;
    private ObservableList<Product> productList;

    public static final URL DIALOG_LOCATION = DishEditController.class.getResource("DishEditDialog.fxml");

    @Override
    protected void initialize() {
        super.initialize();
        assert nameInput != null;
        assert yieldInput != null;
        assert yieldUnitComboBox != null;

        assert removeDietButton != null;
        assert dietTable != null;
        assert dietIdColumn != null;
        assert dietNameColumn != null;

        assert openIngredientButton != null;
        assert removeIngredientButton != null;
        assert ingredientTable != null;
        assert ingredientNameColumn != null;
        assert ingredientGrossColumn != null;
        assert ingredientNetColumn != null;

        yieldUnitComboBox.setItems(FXCollections.observableArrayList(Unit.values()));

        removeDietButton.setDisable(true);
        dietTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            removeDietButton.setDisable(newValue == null);
        });

        openIngredientButton.setDisable(true);
        removeIngredientButton.setDisable(true);
        ingredientTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            boolean disabled = newValue == null;
            openIngredientButton.setDisable(disabled);
            removeIngredientButton.setDisable(disabled);
        });

        dietIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        dietNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        ingredientNameColumn.setCellValueFactory(cellData -> {
            StringProperty result = new SimpleStringProperty(cellData.getValue().getProduct().getName());

            ChangeListener<String> nameListener = (observable, oldValue, newValue) -> {
                result.setValue(newValue);
            };

            cellData.getValue().productProperty().addListener((observable, oldValue, newValue) -> {
                oldValue.nameProperty().removeListener(nameListener);
                newValue.nameProperty().addListener(nameListener);
            });

            return result;
        });

        ingredientGrossColumn.setCellValueFactory(cellData -> cellData.getValue().grossProperty().asObject());
        ingredientNetColumn.setCellValueFactory(cellData -> cellData.getValue().netProperty().asObject());
    }

    @Override
    public void setItem(Dish dish) {
        if (dish == null) {
            dish = new Dish();
        } else {
            dish = Dish.copy(dish);
        }

        this.dish = dish;

        nameInput.textProperty().bindBidirectional(dish.nameProperty());
        yieldInput.textProperty().bindBidirectional(dish.yieldProperty().asObject(), new DoubleStringConverter());
        yieldUnitComboBox.getSelectionModel().select(dish.getYieldUnit());
        yieldUnitComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.dish.setYieldUnit(newValue);
        });

        dietTable.setItems(dish.getDiets());
        ingredientTable.setItems(dish.getIngredients());
    }

    public void setDietList(ObservableList<Diet> dietList) {
        this.dietList = dietList;
    }

    public void setProductList(ObservableList<Product> productList) {
        this.productList = productList;
    }

    @Override
    public Dish getItem() {
        return dish;
    }

    @FXML
    private void addDiet() {
        List<Diet> diets = dish.getDiets();
        List<Diet> selectedDiets = SelectController.selectValueList(DietSelectController.DIALOG_LOCATION, "Select Diet", dietList);
        if (selectedDiets == null) {
            return;
        }

        for (Diet diet : selectedDiets) {
            if (!diets.contains(diet)) {
                diets.add(diet);
            }
        }
    }

    @FXML
    private void removeSelectedDiet() {
        dish.getDiets().removeAll(dietTable.getSelectionModel().getSelectedItems());
    }

    @FXML
    private void addIngredient() {
        ObservableList<Product> notSelectedProducts = productList.filtered(product -> {
            for (Ingredient ingredient : dish.getIngredients()) {
                if (product.equals(ingredient.getProduct())) {
                    return false;
                }
            }
            return true;
        });

        Product product = SelectController.selectValue(ProductSelectController.DIALOG_LOCATION, "Select Product", notSelectedProducts);
        if (product == null) {
            return;
        }

        Ingredient ingredient = new Ingredient();
        ingredient.setProduct(product);
        Ingredient result = editIngredient(ingredient);
        if (result == null) {
            return;
        }

        dish.getIngredients().add(result);
    }

    @FXML
    private void openSelectedIngredient() {
        Ingredient ingredient = ingredientTable.getSelectionModel().getSelectedItem();
        if (ingredient == null) {
            return;
        }

        Ingredient result = editIngredient(ingredient);
        if (result == null) {
            return;
        }

        ingredient.updateBy(result);
    }

    private Ingredient editIngredient(Ingredient ingredient) {
        Ingredient result = null;
        try {
            FXMLLoader loader = new FXMLLoader(IngredientEditController.DIALOG_LOCATION);
            Parent root = loader.load();

            IngredientEditController editController = loader.getController();
            editController.setItem(ingredient);

            Stage stage = new Stage();
            stage.setTitle("Ingredient");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            result = editController.getItem();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @FXML
    private void removeSelectedIngredient() {
        Ingredient ingredient = ingredientTable.getSelectionModel().getSelectedItem();
        if (ingredient == null) {
            return;
        }

        dish.getIngredients().remove(ingredient);
    }
}
