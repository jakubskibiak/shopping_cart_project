
package com.fdmgroup.onedayproject.controller;

import com.fdmgroup.onedayproject.model.CartItem;
import com.fdmgroup.onedayproject.model.Item;
import com.fdmgroup.onedayproject.model.User;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ShoppingCartController.class)
class ShoppingCartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUserService mockUserService;

    @Test
    void testGetUserShoppingCart() throws Exception {
        // Setup
        // Configure IUserService.getUserDetails(...).
        final User user = prepareUser();
        when(mockUserService.getUserDetails(anyString())).thenReturn(user);

        // Configure IUserService.getUserCartItems(...).
        final List<CartItem> cartItems = List.of(prepareCardItem());
        when(mockUserService.getUserCartItems(any(User.class))).thenReturn(cartItems);

        // Run the test
        mockMvc.perform(get("/shopping-cart").param("username", "username").accept(MediaType.TEXT_HTML)).andExpect(view().name("shoppingCart"));
    }

    private CartItem prepareCardItem() {
        return new CartItem(0, prepareUser(), prepareItem(), 0);
    }

    private Item prepareItem() {
        return new Item(0, "name", "description", 0.0, 0);
    }

    private User prepareUser() {
        final User user = new User(0, "username", "firstName", "lastName", "email");
        return user;
    }

    @Test
    void testGetUserShoppingCart_IUserServiceGetUserDetailsThrowsException() throws Exception {
        // Setup
        when(mockUserService.getUserDetails(anyString())).thenThrow(Exception.class);

        // Run the test
        mockMvc.perform(get("/shopping-cart").param("username", "username").accept(MediaType.TEXT_HTML)).andExpect(view().name("error"));
    }

    @Test
    void testGetUserShoppingCart_IUserServiceGetUserCartItemsReturnsNoItems() throws Exception {
        // Setup
        // Configure IUserService.getUserDetails(...).
        final User user = prepareUser();
        when(mockUserService.getUserDetails(anyString())).thenReturn(user);

        when(mockUserService.getUserCartItems(any(User.class))).thenReturn(Collections.emptyList());

        // Run the test
        mockMvc.perform(get("/shopping-cart").param("username", "username").accept(MediaType.TEXT_HTML)).andExpect(view().name("shoppingCart"));
    }
}
