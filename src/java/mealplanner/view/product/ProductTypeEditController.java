package mealplanner.view.product;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.converter.LongStringConverter;
import mealplanner.domain.ProductType;
import mealplanner.view.EditController;

import java.net.URL;

public class ProductTypeEditController extends EditController<ProductType> {
    @FXML
    private TextField nameInput;
    @FXML
    private TextField orderInput;

    private ProductType item;

    public static final URL DIALOG_LOCATION = ProductTypeEditController.class.getResource("ProductTypeEditDialog.fxml");

    @Override
    protected void initialize() {
        super.initialize();
        assert nameInput != null;
        assert orderInput != null;
    }

    @Override
    public void setItem(ProductType item) {
        if (item == null) {
            item = new ProductType();
        } else {
            item = ProductType.copy(item);
        }

        this.item = item;

        nameInput.textProperty().bindBidirectional(item.nameProperty());
        orderInput.textProperty().bindBidirectional(item.orderProperty().asObject(), new LongStringConverter());
    }

    @Override
    public ProductType getItem() {
        return item;
    }
}
