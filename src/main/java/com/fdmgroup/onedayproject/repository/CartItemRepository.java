package com.fdmgroup.onedayproject.repository;

import java.util.List;
import java.util.Optional;

import com.fdmgroup.onedayproject.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.onedayproject.model.CartItem;
import com.fdmgroup.onedayproject.model.User;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    List<CartItem> findAllByUser(User user);

    Optional<CartItem> findByUserAndItem(User user, Item item);

}
