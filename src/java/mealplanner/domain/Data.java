package mealplanner.domain;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Data")
@XmlType(propOrder = {
        "diets",
        "productTypes",
        "products",
        "dishes",
        "meals"
})
public class Data {
    private final ObservableList<Diet> diets = FXCollections.observableArrayList();
    private final ObservableList<ProductType> productTypes = FXCollections.observableArrayList();
    private final ObservableList<Product> products = FXCollections.observableArrayList();
    private final ObservableList<Dish> dishes = FXCollections.observableArrayList();
    private final ObservableList<Meal> meals = FXCollections.observableArrayList();

    public Data() {
    }

    @XmlElementWrapper(name = "Diets")
    @XmlElement(name = "Diet")
    public ObservableList<Diet> getDiets() {
        return diets;
    }

    @XmlElementWrapper(name = "ProductTypes")
    @XmlElement(name = "ProductType")
    public ObservableList<ProductType> getProductTypes() {
        return productTypes;
    }

    @XmlElementWrapper(name = "Products")
    @XmlElement(name = "Product")
    public ObservableList<Product> getProducts() {
        return products;
    }

    @XmlElementWrapper(name = "Dishes")
    @XmlElement(name = "Dish")
    public ObservableList<Dish> getDishes() {
        return dishes;
    }

    @XmlElementWrapper(name = "Meals")
    @XmlElement(name = "Meal")
    public ObservableList<Meal> getMeals() {
        return meals;
    }

    @Override
    public String toString() {
        return "Data{" +
                "diets.size=" + diets.size() +
                ", productTypes.size=" + productTypes.size() +
                ", products.size=" + products.size() +
                ", dishes.size=" + dishes.size() +
                ", meals.size=" + meals.size() +
                '}';
    }
}
