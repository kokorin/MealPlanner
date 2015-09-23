package mealplanner.util;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.util.Callback;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class TreeHelper<P, C> {
    private TreeItem<P> root;
    private final Callback<P,ObservableList<C>> childrenCallback;
    private final Callback<C, TreeHelper<C, ?>> childrenHelperCallback;

    private final ListChangeListener<C> childrenListener = change -> {
        while (change.next()) {
            if (change.wasPermutated()) {

            } else if (change.wasUpdated()) {

            } else {
                addChildren(change.getAddedSubList());
                removeChildren(change.getRemoved());
            }
        }
    };
    private final Map<C, TreeHelper<C, ?>> childrenHelperMap = new HashMap<>();

    public TreeHelper(Callback<P, ObservableList<C>> childrenCallback) {
        this(childrenCallback, null);
    }

    public TreeHelper(Callback<P, ObservableList<C>> childrenCallback,
                      Callback<C, TreeHelper<C, ?>> childrenHelperCallback)
    {
        this.childrenCallback = childrenCallback;
        this.childrenHelperCallback = childrenHelperCallback;

    }

    public TreeItem<P> getRoot() {
        return root;
    }

    public void setRoot(TreeItem<P> root) {
        if (this.root != null) {
            ObservableList<C> children = childrenCallback.call(this.root.getValue());
            children.removeListener(childrenListener);
            removeChildren(children);
        }

        this.root = root;

        if (this.root != null) {
            ObservableList<C> children = childrenCallback.call(this.root.getValue());
            children.addListener(childrenListener);
            addChildren(children);
        }
    }

    private void addChildren(List<? extends C> addedChildren) {
        for (C child : addedChildren) {
            TreeItem<C> childItem = new TreeItem<>(child);
            if (childrenHelperCallback != null) {
                TreeHelper<C, ?> childrenHelper = childrenHelperCallback.call(child);
                childrenHelper.setRoot(childItem);
                childrenHelperMap.put(child, childrenHelper);
            }

            @SuppressWarnings("unchecked")
            TreeItem<P> childItemAsP = (TreeItem<P>) childItem;
            root.getChildren().add(childItemAsP);
        }
    }

    private void removeChildren(List<? extends C> removedChildren) {
        for (C child : removedChildren) {
            for (Iterator<TreeItem<P>> childrenItemIterator = root.getChildren().iterator();
                    childrenItemIterator.hasNext();)
            {
                TreeItem<P> childItem = childrenItemIterator.next();
                if (childItem.getValue().equals(child)) {
                    childrenItemIterator.remove();
                }
            }

            TreeHelper<C, ?> childrenHelper = childrenHelperMap.remove(child);
            if (childrenHelper != null) {
                childrenHelper.setRoot(null);
            }
        }
    }

}
