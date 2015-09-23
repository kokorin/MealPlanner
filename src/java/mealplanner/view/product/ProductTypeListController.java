package mealplanner.view.product;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import mealplanner.domain.Data;
import mealplanner.domain.Diet;
import mealplanner.domain.Product;
import mealplanner.domain.ProductType;
import mealplanner.view.ListController;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.OptionalLong;

public class ProductTypeListController extends ListController<ProductType> {
    @FXML
    private TableColumn<ProductType, Long> idColumn;
    @FXML
    private TableColumn<ProductType, String> nameColumn;
    @FXML
    private TableColumn<ProductType, Long> orderColumn;

    private Data data;

    public static final URL DIALOG_LOCATION = ProductTypeListController.class.getResource("ProductTypeListDialog.fxml");

    public ProductTypeListController() {
        super(ProductTypeEditController.DIALOG_LOCATION, "Product Type");
    }

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

    @Override
    public void setData(Data data) {
        this.data = data;
        table.setItems(data.getProductTypes());
    }

    @Override
    protected List<String> findUsages(ProductType productType) {
        List<String> result = new ArrayList<>();

        for (Product product : data.getProducts()) {
            if (product.getType().equals(productType)) {
                result.add(product.getName());
            }
        }

        return result;
    }

    @Override
    protected boolean removeUsages(ProductType productType) {
        for (Product product : data.getProducts()) {
            if (product.getType().equals(productType)) {
                product.setType(null);
            }
        }

        return true;
    }
}
