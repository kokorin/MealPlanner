package mealplanner.view.diet;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import mealplanner.domain.Diet;
import mealplanner.view.SelectController;

import java.net.URL;

public class DietSelectController extends SelectController<Diet> {
    @FXML
    private TableColumn<Diet, Long> idColumn;
    @FXML
    private TableColumn<Diet, String> nameColumn;

    public static final URL DIALOG_LOCATION = DietSelectController.class.getResource("DietSelectDialog.fxml");

    @Override
    protected void initialize() {
        super.initialize();
        assert idColumn != null;
        assert nameColumn != null;

        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
    }
}
