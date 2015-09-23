package mealplanner.domain;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class Meal implements Updatable<Meal>{
    private final StringProperty name = new SimpleStringProperty();
    private final ObservableList<MealItem> items = FXCollections.observableArrayList();

    public Meal() {
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    @XmlElementWrapper(name = "MealItems")
    @XmlElement(name = "MealItem")
    public ObservableList<MealItem> getItems() {
        return items;
    }

    @Override
    public void updateBy(Meal meal) {
        this.setName(meal.getName());
        this.getItems().clear();
        this.getItems().addAll(meal.getItems());
    }

    @Override
    public String toString() {
        return "Meal{" +
                "name=" + getName() +
                ", items.size=" + items.size() +
                '}';
    }

    public static Meal copy(Meal meal) {
        Meal result = new Meal();
        result.updateBy(meal);
        return result;
    }
}
