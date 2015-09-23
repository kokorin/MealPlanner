package mealplanner.view;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import mealplanner.domain.Data;
import mealplanner.domain.Identity;
import mealplanner.domain.Updatable;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.stream.Collectors;

public abstract class ListController<T extends Updatable<T> & Identity> {
    private final URL editorLocation;
    private final String editorTitle;
    @FXML
    protected TableView<T> table;
    @FXML
    private Button addButton;
    @FXML
    private Button openButton;
    @FXML
    private Button removeButton;

    public ListController(URL editorLocation, String editorTitle) {
        this.editorLocation = editorLocation;
        this.editorTitle = editorTitle;
    }

    @FXML
    protected void initialize() {
        assert table != null;
        assert addButton != null;
        assert openButton != null;
        assert removeButton != null;

        openButton.setDisable(true);
        removeButton.setDisable(true);

        table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            boolean disabled = newValue == null;
            openButton.setDisable(disabled);
            removeButton.setDisable(disabled);
        });
    }

    public abstract void setData(Data data);

    @FXML
    private void addNewItem() {
        T result = EditController.edit(null, editorLocation, editorTitle, createEditorControllerFactory());
        if (result == null) {
            return;
        }

        long nextId;
        OptionalLong maxId = table.getItems().stream().mapToLong(item -> item.getId()).max();
        if (maxId.isPresent()) {
            nextId = maxId.getAsLong() + 1;
        } else {
            nextId = 1;
        }
        result.setId(nextId);

        table.getItems().add(result);
    }

    @FXML
    private void openSelectedItem() {
        T item = table.getSelectionModel().getSelectedItem();
        if (item == null) {
            return;
        }

        T result = EditController.edit(item, editorLocation, editorTitle, createEditorControllerFactory());
        if (result == null) {
            return;
        }

        item.updateBy(result);
    }

    protected Callback<Class<?>, Object> createEditorControllerFactory() {
        return null;
    }

    @FXML
    private void removeSelectedItem() {
        T item = table.getSelectionModel().getSelectedItem();
        if (item == null) {
            return;
        }

        List<String> usages = findUsages(item);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning");
        alert.setHeaderText("Are you sure you want to delete " + item);
        if (usages.size() > 0) {
            alert.setContentText("It is used in " + usages.stream().collect(Collectors.joining(", ")));
        }
        Optional<ButtonType> buttonType = alert.showAndWait();

        if (!buttonType.isPresent() || buttonType.get() != ButtonType.OK) {
            return;
        }

        if (removeUsages(item)) {
            table.getItems().remove(item);
        }
    }

    protected abstract List<String> findUsages(T item);
    protected abstract boolean removeUsages(T item);


    @FXML
    private void close(Event event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    public static void showList(URL location, String title, Data data) {
        try {
            FXMLLoader loader = new FXMLLoader(location);
            Parent root = loader.load();

            ListController<?> controller = loader.getController();
            controller.setData(data);

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
