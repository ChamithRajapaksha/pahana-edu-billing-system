package com.pahana.edu.dao;

import com.pahana.edu.model.Item;
import java.util.List;

public interface ItemDAO {
    void addItem(Item item);
    void updateItem(Item item);
    void deleteItem(int itemId);
    Item getItemById(int itemId);
    List<Item> getAllItems();
}
