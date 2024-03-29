package com.fdmgroup.onedayproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.onedayproject.model.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
}
