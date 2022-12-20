
package com.fdmgroup.onedayproject.controller;

import com.fdmgroup.onedayproject.model.CartItem;
import com.fdmgroup.onedayproject.model.Item;
import com.fdmgroup.onedayproject.model.User;
import com.fdmgroup.onedayproject.service.IItemService;
import com.fdmgroup.onedayproject.service.IUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ItemsListController.class)
class ItemsListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUserService mockUserService;
    @MockBean
    private IItemService mockItemService;

    @Test
    void testGetItems() throws Exception {
        // Setup
        // Configure IItemService.getAllItems(...).
        final List<Item> items = List.of(new Item(0, "name", "description", 0.0, 0));
        when(mockItemService.getAllItems()).thenReturn(items);

        // Run the test
        mockMvc.perform(get("/items-list").accept(MediaType.TEXT_HTML)).andExpect(view().name("itemsList"));

    }

    @Test
    void testGetItems_IItemServiceReturnsNoItems() throws Exception {
        // Setup
        when(mockItemService.getAllItems()).thenReturn(Collections.emptyList());

        // Run the test
        mockMvc.perform(get("/items-list").accept(MediaType.TEXT_HTML)).andExpect(view().name("itemsList"));

    }

    @Test
    void testAddItemToCart() throws Exception {
        // Setup
        // Configure IUserService.getUserDetails(...).
        final User user = prepareUser();
        when(mockUserService.getUserDetails(anyString())).thenReturn(user);

        // Configure IItemService.getItemById(...).
        final Item item = prepareItem();
        when(mockItemService.getItemById(0)).thenReturn(item);

        // Configure IUserService.addItemToCart(...).
        final CartItem cartItem = prepareCartItem();
        when(mockUserService.addItemToCart(any(User.class), any(Item.class), anyInt())).thenReturn(cartItem);

        // Run the test
         mockMvc
            .perform(post("/add-item-to-cart").param("username", "username").param("itemId", "1").param("quantity", "1").with(csrf()).accept(MediaType.TEXT_HTML))
            .andExpect(view().name("success"));

    }

    private CartItem prepareCartItem() {
        final CartItem cartItem = new CartItem(0, new User(0, "username", "firstName", "lastName", "email"), new Item(0, "name", "description", 0.0, 0), 0);
        return cartItem;
    }

    private User prepareUser() {
        final User user = new User(0, "username", "firstName", "lastName", "email");
        return user;
    }

    @Test
    void testAddItemToCart_IUserServiceGetUserDetailsThrowsException() throws Exception {
        // Setup
        when(mockUserService.getUserDetails(anyString())).thenThrow(Exception.class);

        // Run the test
        mockMvc
            .perform(post("/add-item-to-cart").param("username", "username").param("itemId", "0").param("quantity", "0").with(csrf()).accept(MediaType.TEXT_HTML))
            .andExpect(view().name("error"));

    }

    @Test
    void testAddItemToCart_IUserServiceAddItemToCartThrowsException() throws Exception {
        // Setup
        // Configure IUserService.getUserDetails(...).
        final User user = prepareUser();
        when(mockUserService.getUserDetails(anyString())).thenReturn(user);

        // Configure IItemService.getItemById(...).
        final Item item = prepareItem();
        when(mockItemService.getItemById(anyInt())).thenReturn(item);

        when(mockUserService.addItemToCart(any(User.class), any(Item.class), anyInt())).thenThrow(Exception.class);

        // Run the test
        mockMvc
            .perform(post("/add-item-to-cart").param("username", "username").param("itemId", "0").param("quantity", "0").with(csrf()).accept(MediaType.TEXT_HTML))
                .andExpect(view().name("error"));


    }

    private Item prepareItem() {
        final Item item = new Item(0, "name", "description", 0.0, 0);
        return item;
    }

    @Test
    void testGetIndex() throws Exception {
        // Setup
        // Configure IUserService.getUserDetails(...).
        final User user = prepareUser();
        when(mockUserService.getUserDetails(anyString())).thenReturn(user);

        // Run the test
        mockMvc.perform(get("/").accept(MediaType.TEXT_HTML)).andExpect(view().name("index"));

    }

    @Test
    void testGetIndex_IUserServiceThrowsException() throws Exception {
        // Setup
        when(mockUserService.getUserDetails(anyString())).thenThrow(Exception.class);
        // Run the test
        mockMvc.perform(get("/").accept(MediaType.TEXT_HTML)).andExpect(view().name("error"));

    }
}
