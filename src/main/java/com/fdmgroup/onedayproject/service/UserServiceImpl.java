package com.fdmgroup.onedayproject.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fdmgroup.onedayproject.model.CartItem;
import com.fdmgroup.onedayproject.model.Item;
import com.fdmgroup.onedayproject.model.User;
import com.fdmgroup.onedayproject.repository.CartItemRepository;
import com.fdmgroup.onedayproject.repository.ItemRepository;
import com.fdmgroup.onedayproject.repository.UserRepository;

@Service
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final CartItemRepository cartItemRepository;

    public UserServiceImpl(UserRepository userRepository, ItemRepository itemRepository, CartItemRepository cartItemRepository) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public List<CartItem> getUserCartItems(User user) {
        return cartItemRepository.findAllByUser(user);
    }

    @Override
    public User getUserDetails(String userName) throws Exception{
        return userRepository.findUserByUsernameIgnoreCase(userName).orElseThrow(() -> new Exception("There is no user " + userName));
    }

    @Override
    public CartItem addItemToCart(User user,  Item itemToAdd, int quantity) throws Exception {
        validateIfPossibleToAddItemToCart(itemToAdd, quantity);
        CartItem userCartItem = cartItemRepository.findByUserAndItem(user, itemToAdd).orElse(new CartItem(null, user, itemToAdd, 0));
        userCartItem.setQuantity(userCartItem.getQuantity() + quantity);
        itemToAdd.setPieces(itemToAdd.getPieces() - quantity);
        userCartItem = cartItemRepository.save(userCartItem);
        itemRepository.save(itemToAdd);
        return userCartItem;

    }

    private void validateIfPossibleToAddItemToCart(Item itemToAd, int quantity) throws Exception {
        if (itemToAd.getPieces() < quantity) {
            throw new Exception("Not enough items in stock");
        }
        if (quantity <= 0 ) {
            throw new Exception("quantity must be grater than 0");
        }
    }
}
