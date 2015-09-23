package mealplanner.view;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mealplanner.domain.*;
import mealplanner.util.TreeHelper;
import mealplanner.view.diet.DietListController;
import mealplanner.view.dish.DishListController;
import mealplanner.view.dish.DishSelectController;
import mealplanner.view.meal.MealEditController;
import mealplanner.view.product.ProductListController;
import mealplanner.view.product.ProductTypeListController;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainController {
    @FXML
    private MenuItem saveMenuItem;
    @FXML
    private MenuItem saveAsMenuItem;
    @FXML
    private Menu classifiersMenuItem;
    @FXML
    private Button addMealButton;
    @FXML
    private Button editMealButton;
    @FXML
    private Button addDishesButton;
    @FXML
    private Button replaceDishButton;
    @FXML
    private Button removeButton;

    @FXML
    private TreeTableView<Object> mealTreeTable;
    @FXML
    private TreeTableColumn<Object, String> nameColumn;
    @FXML
    private TreeTableColumn<Object, Long> quantityColumn;

    private final ObjectProperty<Data> data = new SimpleObjectProperty<>();
    private final StringProperty path = new SimpleStringProperty();

    private final TreeHelper<Data, Meal> treeHelper = new TreeHelper<>(
            data -> data.getMeals(),
            meal -> new TreeHelper<Meal, MealItem>(value -> value.getItems())
    );

    public Data getData() {
        return data.get();
    }

    public ObjectProperty<Data> dataProperty() {
        return data;
    }

    public String getPath() {
        return path.get();
    }

    public StringProperty pathProperty() {
        return path;
    }

    @FXML
    private void initialize() {
        assert saveMenuItem != null;
        assert saveAsMenuItem != null;
        assert classifiersMenuItem != null;

        assert mealTreeTable != null;
        assert nameColumn != null;
        assert quantityColumn != null;

        addMealButton.disableProperty().bind(data.isNull());

        editMealButton.setDisable(true);
        addDishesButton.setDisable(true);
        replaceDishButton.setDisable(true);
        mealTreeTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            editMealButton.setDisable(newValue == null || !(newValue.getValue() instanceof Meal));
            addDishesButton.setDisable(newValue == null || !(newValue.getValue() instanceof Meal));
            replaceDishButton.setDisable(newValue == null || !(newValue.getValue() instanceof MealItem));
        });

        removeButton.setDisable(true);
        mealTreeTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            removeButton.setDisable(newValue == null);
        });

        saveMenuItem.disableProperty().bind(data.isNull());
        saveAsMenuItem.disableProperty().bind(data.isNull());
        classifiersMenuItem.disableProperty().bind(data.isNull());

        nameColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().getValue();

            if (value instanceof Meal) {
                return ((Meal) value).nameProperty();
            }
            if (value instanceof MealItem) {
                MealItem mealItem = (MealItem)value;
                final StringProperty result = new SimpleStringProperty();
                if (mealItem.getDish() != null) {
                    result.bind(mealItem.getDish().nameProperty());
                }
                mealItem.dishProperty().addListener((observable, oldValue, newValue) -> {
                    result.unbind();
                    result.bind(newValue.nameProperty());
                });

                return result;
            }
            return null;
        });

        quantityColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().getValue();
            if (value instanceof MealItem) {
                return ((MealItem) value).quantityProperty().asObject();
            }
            return null;
        });

        data.addListener((observable, oldValue, newValue) -> {
            TreeItem<Object> treeRoot = new TreeItem<>(newValue);
            @SuppressWarnings("unchecked")
            TreeItem<Data> treeRootAsData = (TreeItem<Data>)(TreeItem)treeRoot;
            treeHelper.setRoot(treeRootAsData);

            mealTreeTable.setRoot(treeRoot);
        });
    }

    public void showDiets() {
        ListController.showList(DietListController.DIALOG_LOCATION, "Diets", data.getValue());
    }

    public void showProductTypes() {
        ListController.showList(ProductTypeListController.DIALOG_LOCATION, "Product Types", data.getValue());
    }

    public void showProducts() {
        ListController.showList(ProductListController.DIALOG_LOCATION, "Products", data.getValue());
    }

    public void showDishes() {
        ListController.showList(DishListController.DIALOG_LOCATION, "Diets", data.getValue());
    }

    public void newData() {
        this.data.set(new Data());
        this.path.set(null);
    }

    public void loadData() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Data File");
        fileChooser.getExtensionFilters().addAll( new FileChooser.ExtensionFilter("XML files", "*.xml"));
        Stage stage = new Stage();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile == null) {
            return;
        }

        Data data = null;
        try {
            Unmarshaller unmarshaller = JAXBContext.newInstance(Data.class).createUnmarshaller();
            data = (Data)unmarshaller.unmarshal(selectedFile);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        this.data.set(data);
        this.path.set(selectedFile.getAbsolutePath());
    }

    public void save() {
        saveData(path.get());
    }
    public void saveAs() {
        saveData(null);
    }

    private void saveData(String path) {
        File selectedFile = null;

        if (path == null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Data File");
            fileChooser.getExtensionFilters().addAll( new FileChooser.ExtensionFilter("XML files", "*.xml"));
            Stage stage = new Stage();
            selectedFile = fileChooser.showSaveDialog(stage);
        } else {
            selectedFile = new File(path);
        }

        if (selectedFile == null) {
            return;
        }

        try {
            Marshaller marshaller = JAXBContext.newInstance(Data.class).createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(getData(), selectedFile);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        this.path.set(selectedFile.getAbsolutePath());
    }

    @FXML
    private void addMeal() {
        if (data.getValue() == null) {
            return;
        }

        Meal meal = EditController.edit(null, MealEditController.DIALOG_LOCATION, "Meal");
        if (meal == null) {
            return;
        }

        data.getValue().getMeals().add(meal);
    }

    @FXML
    private void editSelectedMeal() {
        if (data.getValue() == null) {
            return;
        }

        Object selectedItem = mealTreeTable.getSelectionModel().getSelectedItem().getValue();
        if (!(selectedItem instanceof Meal)) {
            return;
        }

        Meal meal = (Meal)selectedItem;
        Meal result = EditController.edit(meal, MealEditController.DIALOG_LOCATION, "Meal");

        if (result == null) {
            return;
        }

        meal.updateBy(result);
    }

    @FXML
    private void addDish() {
        Object selectedItem = mealTreeTable.getSelectionModel().getSelectedItem().getValue();

        if (selectedItem instanceof Meal) {
            Meal meal = (Meal)selectedItem;

            List<Dish> dishes = SelectController.selectValueList(DishSelectController.DIALOG_LOCATION,
                                                                    "Select Dishes",
                                                                    data.getValue().getDishes());
            if (dishes != null) {
                for (Dish dish : dishes) {
                    MealItem mealItem = new MealItem();
                    mealItem.setDish(dish);
                    mealItem.setQuantity(System.currentTimeMillis());
                    meal.getItems().add(mealItem);
                }
            }
        }
    }

    @FXML
    private void replaceDish() {
        Object selectedItem = mealTreeTable.getSelectionModel().getSelectedItem().getValue();

        if (selectedItem instanceof MealItem) {
            MealItem mealItem = (MealItem)selectedItem;

            Dish dish = SelectController.selectValue(DishSelectController.DIALOG_LOCATION, "Select Dish",
                                                        data.getValue().getDishes());
            if (dish != null) {
                mealItem.setDish(dish);
                mealItem.setQuantity(System.currentTimeMillis());
            }
        }
    }

    @FXML
    private void remove() {
        Object selectedItem = mealTreeTable.getSelectionModel().getSelectedItem().getValue();

        if (selectedItem instanceof Meal) {
            Meal meal = (Meal)selectedItem;
            data.getValue().getMeals().remove(meal);
        }

        if (selectedItem instanceof MealItem) {
            MealItem mealItem = (MealItem)selectedItem;
            for (Meal meal : data.getValue().getMeals()) {
                meal.getItems().remove(mealItem);
            }
        }
        //TreeTableView doesn't clear selection, on deletion of selected item.
        mealTreeTable.getSelectionModel().clearSelection();
    }
}
