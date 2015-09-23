package mealplanner.domain;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import mealplanner.xml.IdAdapter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public class Diet implements Updatable<Diet>, Identity {
    private final LongProperty id = new SimpleLongProperty();
    private final StringProperty name = new SimpleStringProperty();

    public Diet() {}

    @XmlID
    @XmlAttribute
    @XmlJavaTypeAdapter(value = DietIdAdapter.class, type = long.class)
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

    public void updateBy(Diet diet) {
        setId(diet.getId());
        setName(diet.getName());
    }

    @Override
    public String toString() {
        return "Diet{" +
                "id=" + getId() +
                ", name=" + getName() +
                '}';
    }

    public static Diet copy(Diet diet) {
        Diet result = new Diet();
        result.updateBy(diet);
        return result;
    }

    private static class DietIdAdapter extends IdAdapter {
        public DietIdAdapter() {
            super(Diet.class);
        }
    }
}
