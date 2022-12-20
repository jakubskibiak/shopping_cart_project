package com.fdmgroup.onedayproject.service;

import java.util.List;

import com.fdmgroup.onedayproject.model.CartItem;
import com.fdmgroup.onedayproject.model.Item;
import com.fdmgroup.onedayproject.model.User;

public interface IItemService {
    List<Item> getAllItems();

    Item getItemById(Integer itemId);
}
