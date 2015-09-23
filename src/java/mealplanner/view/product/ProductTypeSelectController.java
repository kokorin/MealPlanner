package mealplanner.view.product;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import mealplanner.domain.ProductType;
import mealplanner.view.SelectController;

import java.net.URL;

public class ProductTypeSelectController extends SelectController<ProductType> {
    @FXML
    private TableColumn<ProductType, Long> idColumn;
    @FXML
    private TableColumn<ProductType, String> nameColumn;
    @FXML
    private TableColumn<ProductType, Long> orderColumn;

    public static final URL DIALOG_LOCATION = ProductTypeSelectController.class.getResource("ProductTypeSelectDialog.fxml");

    @Override
    protected void initialize() {
        super.initialize();
        assert idColumn != null;
        assert nameColumn != null;
        assert orderColumn != null;

        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        orderColumn.setCellValueFactory(cellData -> cellData.getValue().orderProperty().asObject());
    }
}
