package mealplanner.domain;

public interface Updatable<T> {
    void updateBy(T value);
}
