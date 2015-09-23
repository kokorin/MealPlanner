package mealplanner.domain;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;
import java.util.List;
import java.util.stream.Collectors;

public class Ingredient {
    private final ObjectProperty<Product> product = new SimpleObjectProperty<>();
    private final DoubleProperty gross = new SimpleDoubleProperty();
    private final DoubleProperty net = new SimpleDoubleProperty();

    public Ingredient() {
    }

    @XmlAttribute
    @XmlIDREF
    public Product getProduct() {
        return product.get();
    }

    public ObjectProperty<Product> productProperty() {
        return product;
    }

    public void setProduct(Product product) {
        this.product.set(product);
    }

    public double getGross() {
        return gross.get();
    }

    public DoubleProperty grossProperty() {
        return gross;
    }

    public void setGross(double gross) {
        this.gross.set(gross);
    }

    public double getNet() {
        return net.get();
    }

    public DoubleProperty netProperty() {
        return net;
    }

    public void setNet(double net) {
        this.net.set(net);
    }

    public void updateBy(Ingredient ingredient) {
        setProduct(ingredient.getProduct());
        setGross(ingredient.getGross());
        setNet(ingredient.getNet());
    }

    public static Ingredient copy(Ingredient ingredient) {
        Ingredient result = new Ingredient();
        result.updateBy(ingredient);
        return result;
    }
}
