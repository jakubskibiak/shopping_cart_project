package com.fdmgroup.onedayproject.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fdmgroup.onedayproject.model.Item;
import com.fdmgroup.onedayproject.repository.ItemRepository;

@Service
public class ItemServiceImpl implements IItemService {
    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public Item getItemById(Integer itemId) {
        return itemRepository.getById(itemId);
    }
}
