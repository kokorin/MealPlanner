package mealplanner.view.product;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import mealplanner.domain.Product;
import mealplanner.domain.Unit;
import mealplanner.view.SelectController;

import java.net.URL;

public class ProductSelectController extends SelectController<Product> {
    @FXML
    private TableColumn<Product, Long> idColumn;
    @FXML
    private TableColumn<Product, String> nameColumn;
    @FXML
    private TableColumn<Product, String> typeColumn;
    @FXML
    private TableColumn<Product, Unit> unitColumn;

    public static final URL DIALOG_LOCATION = ProductSelectController.class.getResource("ProductSelectDialog.fxml");

    public ProductSelectController(){
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

            result.bind(cellData.getValue().getType().nameProperty());
            cellData.getValue().typeProperty().addListener((observable, oldValue, newValue) -> {
                result.unbind();
                result.bind(newValue.nameProperty());
            });

            return result;
        });
        unitColumn.setCellValueFactory(cellData -> cellData.getValue().unitProperty());
    }
}
