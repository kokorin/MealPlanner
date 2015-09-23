package mealplanner.domain;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import mealplanner.xml.IdAdapter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public class ProductType implements Updatable<ProductType>, Identity {
    private final LongProperty id = new SimpleLongProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final LongProperty order = new SimpleLongProperty();

    public ProductType() {
    }

    @XmlID
    @XmlAttribute
    @XmlJavaTypeAdapter(value = ProductTypeIdAdapter.class, type = long.class)
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

    public long getOrder() {
        return order.get();
    }

    public LongProperty orderProperty() {
        return order;
    }

    public void setOrder(long order) {
        this.order.set(order);
    }

    public void updateBy(ProductType type) {
        setId(type.getId());
        setName(type.getName());
        setOrder(type.getOrder());
    }

    public static ProductType copy(ProductType type) {
        ProductType result = new ProductType();
        result.updateBy(type);
        return result;
    }

    @Override
    public String toString() {
        return "ProductType{" +
                "id=" + getId() +
                ", name=" + getName() +
                '}';
    }



    private static class ProductTypeIdAdapter extends IdAdapter {
        public ProductTypeIdAdapter() {
            super(ProductType.class);
        }
    }
}
