package mealplanner.view;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public abstract class SelectController<T> {
    @FXML
    private TableView<T> table;
    @FXML
    private Button selectButton;

    private T selectedItem;
    private List<T> selectedItems;

    @FXML
    protected void initialize() {
        assert table != null;
        assert selectButton != null;

        selectButton.setDisable(true);
        table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            boolean disabled = newValue == null;
            selectButton.setDisable(disabled);
        });
    }

    public void allowMultipleSelection() {
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void setItems(ObservableList<T> items) {
        table.setItems(items);
    }

    public T getSelectedItem() {
        return selectedItem;
    }

    public List<T> getSelectedItems() {
        return selectedItems;
    }

    @FXML
    private void select(Event event) {
        selectedItem = table.getSelectionModel().getSelectedItem();
        selectedItems = table.getSelectionModel().getSelectedItems();
        cancel(event);
    }

    @FXML
    private void cancel(Event event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    public static <V> V selectValue(URL location, String title, ObservableList<V> values) {
        V result = null;
        try {
            FXMLLoader loader = new FXMLLoader(location);
            Parent root = loader.load();

            SelectController<V> controller = loader.getController();
            controller.setItems(values);

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
            result = controller.getSelectedItem();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static <V> List<V> selectValueList(URL location, String title, ObservableList<V> values) {
        List<V> result = Collections.emptyList();
        try {
            FXMLLoader loader = new FXMLLoader(location);
            Parent root = loader.load();

            SelectController<V> controller = loader.getController();
            controller.setItems(values);
            controller.allowMultipleSelection();

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
            result = controller.getSelectedItems();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
