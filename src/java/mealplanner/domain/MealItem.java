package mealplanner.domain;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;

public class MealItem {
    private final ObjectProperty<Dish> dish = new SimpleObjectProperty<>();
    private final LongProperty quantity = new SimpleLongProperty();

    public MealItem() {
    }

    @XmlIDREF
    @XmlAttribute
    public Dish getDish() {
        return dish.get();
    }

    public ObjectProperty<Dish> dishProperty() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish.set(dish);
    }

    public long getQuantity() {
        return quantity.get();
    }

    public LongProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity.set(quantity);
    }

    @Override
    public String toString() {
        return "MealItem{" +
                "dish=" + getDish() +
                '}';
    }
}
