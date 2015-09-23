package mealplanner.view;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public abstract class EditController<T> {
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    private boolean saved = false;

    @FXML
    protected void initialize() {
        assert saveButton != null;
        assert cancelButton != null;
    }

    public abstract void setItem(T item);
    public abstract T getItem();

    public boolean isSaved() {
        return saved;
    }

    @FXML
    private void save(Event event) {
        saved = true;
        cancel(event);
    }

    @FXML
    private void cancel(Event event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    public static <V> V edit(V item, URL location, String title) {
        return edit(item, location, title, null);
    }

    public static <V> V edit(V item, URL location, String title,
                             Callback<Class<?>, Object> controllerFactory) {
        V result = null;

        try {
            FXMLLoader loader = new FXMLLoader(location);
            loader.setControllerFactory(controllerFactory);
            Parent root = loader.load();

            EditController<V> editController = loader.getController();
            editController.setItem(item);

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
            if (editController.isSaved()) {
                result = editController.getItem();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

}
