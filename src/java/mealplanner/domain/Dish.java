package mealplanner.domain;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mealplanner.xml.IdAdapter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public class Dish implements Updatable<Dish>, Identity {
    private final LongProperty id = new SimpleLongProperty();
    private final StringProperty name = new SimpleStringProperty();

    private final DoubleProperty yield = new SimpleDoubleProperty();
    private final ObjectProperty<Unit> yieldUnit = new SimpleObjectProperty<>();
    private final ObservableList<Diet> diets = FXCollections.observableArrayList();
    private final ObservableList<Ingredient> ingredients = FXCollections.observableArrayList();

    public Dish() {}

    @XmlID
    @XmlAttribute
    @XmlJavaTypeAdapter(value = DishIdAdapter.class, type = long.class)
    public long getId() {
        return id.get();
    }

    public LongProperty idProperty() {
        return id;
    }

    public void setId(long id) {
        this.id.set(id);
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

    public double getYield() {
        return yield.get();
    }

    public DoubleProperty yieldProperty() {
        return yield;
    }

    public void setYield(double yield) {
        this.yield.set(yield);
    }

    public Unit getYieldUnit() {
        return yieldUnit.get();
    }

    public ObjectProperty<Unit> yieldUnitProperty() {
        return yieldUnit;
    }

    public void setYieldUnit(Unit yieldUnit) {
        this.yieldUnit.set(yieldUnit);
    }

    @XmlIDREF
    @XmlList
    public ObservableList<Diet> getDiets() {
        return diets;
    }

    public ObservableList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void updateBy(Dish dish) {
        setId(dish.getId());
        setName(dish.getName());
        setYield(dish.getYield());
        setYieldUnit(dish.getYieldUnit());

        diets.clear();
        diets.addAll(dish.diets);

        ingredients.clear();
        //Ingredient is part of Dish and is edited with Dish.
        for (Ingredient ingredient : dish.ingredients) {
            ingredients.add(Ingredient.copy(ingredient));
        }
    }

    public static Dish copy(Dish dish) {
        Dish result = new Dish();
        result.updateBy(dish);
        return result;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + getId() +
                ", name=" + getName() +
                ", diets.size=" + diets.size() +
                ", ingredients.size=" + ingredients.size() +
                '}';
    }


    private static class DishIdAdapter extends IdAdapter {
        public DishIdAdapter() {
            super(Dish.class);
        }
    }
}
