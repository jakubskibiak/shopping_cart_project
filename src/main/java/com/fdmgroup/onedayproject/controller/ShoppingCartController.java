package com.fdmgroup.onedayproject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fdmgroup.onedayproject.model.CartItem;
import com.fdmgroup.onedayproject.model.User;
import com.fdmgroup.onedayproject.service.IUserService;

@Controller
public class ShoppingCartController {
    @Value("${onedayproject.username.default:magda_palica}")
    private String defaultUser;
    private final IUserService userService;

    public ShoppingCartController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/shopping-cart")
    public String getUserShoppingCart(ModelMap model, String username /* to retrieve from authentication context */) throws Exception {
        User userDetails = userService.getUserDetails(defaultUser);

        List<CartItem> userCartItems = userService.getUserCartItems(userDetails);
        model.addAttribute("userCartItems", userCartItems);
        model.addAttribute("totalAmount", userCartItems.stream().mapToDouble(el -> el.getQuantity() * el.getItem().getPrice()).sum());
        return "shoppingCart";
    }

    @ExceptionHandler({ Exception.class })
    public ModelAndView handleError(Exception ex) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("message", ex.getMessage());
        mav.setViewName("error");
        return mav;
    }
}
