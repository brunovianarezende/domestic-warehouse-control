package org.vaadin.ext.bruno.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ItemService {

    private final List<Item> items = new ArrayList<>();

    // I should have really started with the springboot support... I'll try to change the project structure later on
    private ItemService() {
    }

    private static final ItemService itemService = new ItemService();

    public static ItemService getInstance() {
        return itemService;
    }

    public int getCount() {
        return items.size();
    }

    public List<Item> getItems() {
        return items;
    }

    public String addItem(Item item) {
        UUID newId = UUID.randomUUID();
        item.setId(newId.toString());
        items.add(item);
        return item.getId();
    }

    public Optional<Item> getItem(String itemId) {
        return items.stream()
                .filter(i -> i.getId().equals(itemId))
                .findAny();
    }

    public void saveItem(Item item) {
        int theIndex = -1;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId().equals(item.getId())) {
                theIndex = i;
                break;
            }
        }
        if (theIndex != -1) {
            items.set(theIndex, item);
        }
    }
}
