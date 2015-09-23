package mealplanner.domain;

import javafx.beans.property.*;
import mealplanner.xml.IdAdapter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public class Product implements Updatable<Product>, Identity {
    private final LongProperty id = new SimpleLongProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final ObjectProperty<ProductType> type = new SimpleObjectProperty<>();
    private final ObjectProperty<Unit> unit = new SimpleObjectProperty<>();

    public Product() {
    }

    @XmlID
    @XmlAttribute
    @XmlJavaTypeAdapter(value = ProductIdAdapter.class, type = long.class)
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

    @XmlIDREF
    @XmlAttribute
    public ProductType getType() {
        return type.get();
    }

    public ObjectProperty<ProductType> typeProperty() {
        return type;
    }

    public void setType(ProductType type) {
        this.type.set(type);
    }

    public Unit getUnit() {
        return unit.get();
    }

    public ObjectProperty<Unit> unitProperty() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit.set(unit);
    }

    public void updateBy(Product product) {
        setId(product.getId());
        setName(product.getName());
        setType(product.getType());
        setUnit(product.getUnit());
    }

    public static Product copy(Product product) {
        Product result = new Product();
        result.updateBy(product);
        return result;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + getId() +
                ", name=" + getName() +
                ", type=" + getType() +
                ", unit=" + getUnit() +
                '}';
    }



    private static class ProductIdAdapter extends IdAdapter {
        public ProductIdAdapter() {
            super(Product.class);
        }
    }
}
