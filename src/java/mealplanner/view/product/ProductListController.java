package mealplanner.view.product;

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

public class ProductListController extends ListController<Product> {
    @FXML
    private TableColumn<Product, Long> idColumn;
    @FXML
    private TableColumn<Product, String> nameColumn;
    @FXML
    private TableColumn<Product, String> typeColumn;
    @FXML
    private TableColumn<Product, Unit> unitColumn;

    private Data data;

    public static final URL DIALOG_LOCATION = ProductListController.class.getResource("ProductListDialog.fxml");

    public ProductListController() {
        super(ProductEditController.DIALOG_LOCATION, "Product");
    }

    @Override
    protected void initialize() {
        super.initialize();
        assert idColumn != null;
        assert nameColumn != null;
        assert typeColumn != null;
        assert unitColumn != null;

        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        typeColumn.setCellValueFactory(cellData -> {
            StringProperty result = new SimpleStringProperty();

            if (cellData.getValue().getType() != null) {
                result.bind(cellData.getValue().getType().nameProperty());
            }
            cellData.getValue().typeProperty().addListener((observable, oldValue, newValue) -> {
                result.unbind();
                result.bind(newValue.nameProperty());
            });

            return result;
        });
        unitColumn.setCellValueFactory(cellData -> cellData.getValue().unitProperty());
    }

    @Override
    public void setData(Data data) {
        this.data = data;
        table.setItems(data.getProducts());
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

            if (result instanceof ProductEditController) {
                ProductEditController controller = (ProductEditController)result;
                controller.setProductTypes(data.getProductTypes());
            }
            return result;
        };
    }
    @Override
    protected List<String> findUsages(Product product) {
        List<String> result = new ArrayList<>();
        for (Dish dish : data.getDishes()) {
            for (Ingredient ingredient : dish.getIngredients()) {
                if (ingredient.getProduct().equals(product)) {
                    result.add(dish.getName());
                    break;
                }
            }
        }
        return result;
    }

    @Override
    protected boolean removeUsages(Product product) {
        for (Dish dish : data.getDishes()) {
            for (Iterator<Ingredient> ingredientIterator = dish.getIngredients().iterator();
                        ingredientIterator.hasNext();) {
                if (ingredientIterator.next().getProduct().equals(product)) {
                    ingredientIterator.remove();
                }
            }
        }
        return true;
    }
}
