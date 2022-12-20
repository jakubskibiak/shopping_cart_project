
package com.fdmgroup.onedayproject.service;

import com.fdmgroup.onedayproject.model.CartItem;
import com.fdmgroup.onedayproject.model.Item;
import com.fdmgroup.onedayproject.model.User;
import com.fdmgroup.onedayproject.repository.CartItemRepository;
import com.fdmgroup.onedayproject.repository.ItemRepository;
import com.fdmgroup.onedayproject.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private ItemRepository mockItemRepository;
    @Mock
    private CartItemRepository mockCartItemRepository;

    private UserServiceImpl userServiceImplUnderTest;

    @BeforeEach
    void setUp() {
        userServiceImplUnderTest = new UserServiceImpl(mockUserRepository, mockItemRepository, mockCartItemRepository);
    }

    @Test
    void testGetUserCartItems() {
        // Setup
        final User user = prepareUser();

        // Configure CartItemRepository.findAllByUser(...).
        final List<CartItem> cartItems = List.of(prepareCartItem());
        when(mockCartItemRepository.findAllByUser(prepareUser())).thenReturn(cartItems);

        // Run the test
        final List<CartItem> result = userServiceImplUnderTest.getUserCartItems(user);

        // Verify the results
    }

    private CartItem prepareCartItem() {
        return new CartItem(0, prepareUser(), prepareItem(), 0);
    }

    private User prepareUser() {
        return new User(0, "username", "firstName", "lastName", "email");
    }

    @Test
    void testGetUserCartItems_CartItemRepositoryReturnsNoItems() {
        // Setup
        final User user = prepareUser();
        when(mockCartItemRepository.findAllByUser(any(User.class))).thenReturn(Collections.emptyList());

        // Run the test
        final List<CartItem> result = userServiceImplUnderTest.getUserCartItems(user);

        // Verify the results
        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    void testGetUserDetails() throws Exception {
        // Setup
        final User expectedResult = prepareUser();

        // Configure UserRepository.findUserByUsernameIgnoreCase(...).
        final Optional<User> user = Optional.of(prepareUser());
        when(mockUserRepository.findUserByUsernameIgnoreCase(anyString())).thenReturn(user);

        // Run the test
        final User result = userServiceImplUnderTest.getUserDetails(anyString());

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetUserDetails_UserRepositoryReturnsAbsent() {
        // Setup
        when(mockUserRepository.findUserByUsernameIgnoreCase(anyString())).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> userServiceImplUnderTest.getUserDetails(anyString())).isInstanceOf(Exception.class);
    }


    @Test
    void testAddItemToCart() throws Exception {
        // Setup
        final User user = prepareUser();
        final Item itemToAdd = prepareItem();

        // Configure CartItemRepository.findByUserAndItem(...).
        final Optional<CartItem> cartItem = Optional.of(prepareCartItem());
        when(mockCartItemRepository.findByUserAndItem(eq(prepareUser()), any(Item.class))).thenReturn(cartItem);

        // Configure CartItemRepository.save(...).
        final CartItem cartItem1 = prepareCartItem();
        when(mockCartItemRepository.save(any(CartItem.class))).thenReturn(cartItem1);

        // Configure ItemRepository.save(...).
        final Item item = prepareItem();
        when(mockItemRepository.save(any(Item.class))).thenReturn(item);

        // Run the test
        final CartItem result = userServiceImplUnderTest.addItemToCart(user, itemToAdd, 1);

        // Verify the results
        verify(mockItemRepository).save(any(Item.class));
    }

    @Test
    void testAddItemToCart_CartItemRepositoryFindByUserAndItemReturnsAbsent() throws Exception {
        // Setup
        final User user = prepareUser();
        final Item itemToAdd = prepareItem();
        when(mockCartItemRepository.findByUserAndItem(eq(prepareUser()), any(Item.class))).thenReturn(Optional.empty());

        // Configure CartItemRepository.save(...).
        final CartItem cartItem = prepareCartItem();
        when(mockCartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);

        // Configure ItemRepository.save(...).
        final Item item = prepareItem();
        when(mockItemRepository.save(any(Item.class))).thenReturn(item);

        // Run the test
        final CartItem result = userServiceImplUnderTest.addItemToCart(user, itemToAdd, 1);

        // Verify the results
        verify(mockItemRepository).save(any(Item.class));
    }

    private Item prepareItem() {
        return new Item(0, "name", "description", 1.0, 1);
    }

    @Test
    void testAddItemToCart_ThrowsExceptionWithZeroQuantityMessage() {
        // Setup
        final User user = prepareUser();
        final Item itemToAdd = prepareItem();

        // Run the test
        assertThatThrownBy(() -> userServiceImplUnderTest.addItemToCart(user, itemToAdd, 0)).isInstanceOf(Exception.class);
        verify(mockItemRepository, times(0)).save(any(Item.class));
    }
    @Test
    void testAddItemToCart_ThrowsExceptionWithNotEnoughItems() {
        // Setup
        final User user = prepareUser();
        final Item itemToAdd = prepareItem();
        itemToAdd.setPieces(1);

        // Run the test
        assertThatThrownBy(() -> userServiceImplUnderTest.addItemToCart(user, itemToAdd, 2)).isInstanceOf(Exception.class);
        verify(mockItemRepository, times(0)).save(any(Item.class));
    }
}
