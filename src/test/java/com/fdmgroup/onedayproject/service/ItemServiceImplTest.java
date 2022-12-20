
package com.fdmgroup.onedayproject.service;

import com.fdmgroup.onedayproject.model.Item;
import com.fdmgroup.onedayproject.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository mockItemRepository;

    private ItemServiceImpl itemServiceImplUnderTest;

    @BeforeEach
    void setUp() {
        itemServiceImplUnderTest = new ItemServiceImpl(mockItemRepository);
    }

    @Test
    void testGetAllItems() {
        // Setup
        // Configure ItemRepository.findAll(...).
        final List<Item> items = prepareItemsList();
        when(mockItemRepository.findAll()).thenReturn(items);

        // Run the test
        final List<Item> result = itemServiceImplUnderTest.getAllItems();
        assertThat(result).isNotNull();

    }

    private List<Item> prepareItemsList() {
        final List<Item> items = List.of(prepareItem());
        return items;
    }

    private Item prepareItem() {
        return new Item(0, "name", "description", 0.0, 0);
    }

    @Test
    void testGetAllItems_ItemRepositoryReturnsNoItems() {
        // Setup
        when(mockItemRepository.findAll()).thenReturn(Collections.emptyList());

        // Run the test
        final List<Item> result = itemServiceImplUnderTest.getAllItems();

        // Verify the results
        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    void testGetItemById() {
        // Setup
        // Configure ItemRepository.getById(...).
        final Item item = prepareItem();
        when(mockItemRepository.getById(0)).thenReturn(item);

        // Run the test
        final Item result = itemServiceImplUnderTest.getItemById(0);

        assertThat(result).isNotNull();
    }
}
