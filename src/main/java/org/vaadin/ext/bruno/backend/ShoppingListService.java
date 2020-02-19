package org.vaadin.ext.bruno.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ShoppingListService {

    private final List<ShoppingList> shoppingLists = new ArrayList<>();

    private ShoppingListService() {
    }

    private static final ShoppingListService instance = new ShoppingListService();

    public static ShoppingListService getInstance() {
        return instance;
    }

    public int getCount() {
        return shoppingLists.size();
    }

    public List<ShoppingList> getShoppingLists() {
        return shoppingLists;
    }

    public String addShoppingList(ShoppingList shoppingList) {
        UUID newId = UUID.randomUUID();
        shoppingList.setId(newId.toString());
        shoppingLists.add(shoppingList);
        return shoppingList.getId();
    }

    public Optional<ShoppingList> getShoppingList(String id) {
        return shoppingLists.stream()
                .filter(i -> i.getId().equals(id))
                .findAny();
    }

    public void saveShoppingList(ShoppingList shoppingList) {
        int theIndex = -1;
        for (int i = 0; i < shoppingLists.size(); i++) {
            if (shoppingLists.get(i).getId().equals(shoppingList.getId())) {
                theIndex = i;
                break;
            }
        }
        if (theIndex != -1) {
            shoppingLists.set(theIndex, shoppingList);
        }
    }

    public void deleteShoppingList(ShoppingList shoppingList) {
        int theIndex = -1;
        for (int i = 0; i < shoppingLists.size(); i++) {
            if (shoppingLists.get(i).getId().equals(shoppingList.getId())) {
                theIndex = i;
                break;
            }
        }
        if (theIndex != -1) {
            shoppingLists.remove(theIndex);
        }
    }

    public void markAsDone(ShoppingList shoppingList) {
        getShoppingList(shoppingList.getId()).ifPresent(s -> {
            s.setDone(true);
        });
    }
}
