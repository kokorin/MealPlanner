package mealplanner.view.diet;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import mealplanner.domain.Data;
import mealplanner.domain.Diet;
import mealplanner.domain.Dish;
import mealplanner.view.ListController;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.OptionalLong;

public class DietListController extends ListController<Diet> {
    @FXML
    private TableColumn<Diet, Long> idColumn;
    @FXML
    private TableColumn<Diet, String> nameColumn;

    private Data data;

    public static final URL DIALOG_LOCATION = DietListController.class.getResource("DietListDialog.fxml");

    public DietListController() {
        super(DietEditController.DIALOG_LOCATION, "Diet");
    }

    @Override
    protected void initialize() {
        super.initialize();
        assert idColumn != null;
        assert nameColumn != null;

        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
    }

    @Override
    public void setData(Data data) {
        table.setItems(data.getDiets());
        this.data = data;
    }

    @Override
    protected List<String> findUsages(Diet diet) {
        List<String> result = new ArrayList<>();

        for (Dish dish : data.getDishes()) {
            if (dish.getDiets().contains(diet)) {
                result.add(dish.getName());
            }
        }

        return result;
    }

    @Override
    protected boolean removeUsages(Diet diet) {
        for (Dish dish : data.getDishes()) {
            dish.getDiets().remove(diet);
        }

        return true;
    }
}
