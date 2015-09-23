package mealplanner.view.product;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import mealplanner.domain.Product;
import mealplanner.domain.ProductType;
import mealplanner.domain.Unit;
import mealplanner.view.EditController;
import mealplanner.view.SelectController;

import java.net.URL;

public class ProductEditController extends EditController<Product> {
    @FXML
    private TextField nameInput;
    @FXML
    private Label typeLabel;
    @FXML
    private ComboBox<Unit> unitComboBox;

    private Product product;
    private ObservableList<ProductType> productTypes;

    public static final URL DIALOG_LOCATION = ProductEditController.class.getResource("ProductEditDialog.fxml");

    @Override
    protected void initialize() {
        super.initialize();
        assert nameInput != null;
        assert typeLabel != null;
        assert unitComboBox != null;

        unitComboBox.setItems(FXCollections.observableArrayList(Unit.values()));
    }

    @Override
    public void setItem(Product product) {
        if (product == null) {
            product = new Product();
        } else {
            product = Product.copy(product);
        }

        this.product = product;

        nameInput.textProperty().bindBidirectional(product.nameProperty());

        if (product.getType() != null) {
            typeLabel.textProperty().bind(product.getType().nameProperty());
        }
        product.typeProperty().addListener((observable, oldValue, newValue) -> {
            typeLabel.textProperty().unbind();
            typeLabel.textProperty().bind(newValue.nameProperty());
        });


        unitComboBox.getSelectionModel().select(product.getUnit());
        unitComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.product.setUnit(newValue);
        });
    }

    public void setProductTypes(ObservableList<ProductType> productTypes) {
        this.productTypes = productTypes;
    }

    @Override
    public Product getItem() {
        return product;
    }

    @FXML
    private void selectProductType() {
        ProductType selectedType = SelectController.selectValue(ProductTypeSelectController.DIALOG_LOCATION,
                                                                    "Select Product Type", productTypes);
        if (selectedType == null) {
            return;
        }

        product.setType(selectedType);
    }
}
