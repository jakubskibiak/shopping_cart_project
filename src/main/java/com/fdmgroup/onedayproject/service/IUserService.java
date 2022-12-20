package com.fdmgroup.onedayproject.service;

import java.util.List;

import com.fdmgroup.onedayproject.model.CartItem;
import com.fdmgroup.onedayproject.model.Item;
import com.fdmgroup.onedayproject.model.User;

public interface IUserService {

    List<CartItem> getUserCartItems(User user);

    User getUserDetails(String userName) throws Exception;

    CartItem addItemToCart(User user, Item item, int quantity) throws Exception;
}
