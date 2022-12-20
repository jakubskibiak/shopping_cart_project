package com.fdmgroup.onedayproject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fdmgroup.onedayproject.model.Item;
import com.fdmgroup.onedayproject.model.User;
import com.fdmgroup.onedayproject.service.IItemService;
import com.fdmgroup.onedayproject.service.IUserService;

@Controller
public class ItemsListController {

    @Value("${onedayproject.username.default:magda_palica}")
    private String defaultUser;
    private final IUserService userService;
    private final IItemService itemService;

    public ItemsListController(IUserService userService, IItemService itemService) {
        this.userService = userService;
        this.itemService = itemService;
    }

    @GetMapping("/items-list")
    public String getItems(ModelMap model) {

        List<Item> allItems = itemService.getAllItems();
        model.addAttribute("items", allItems);

        return "itemsList";
    }

    @PostMapping("/add-item-to-cart")
    public String addItemToCart(ModelMap model, String username /* to retrieve from authentication context */, Integer itemId, Integer quantity) throws Exception {
        User userDetails = userService.getUserDetails(defaultUser);
        Item item = itemService.getItemById(itemId);
        userService.addItemToCart(userDetails, item, quantity);
        model.addAttribute("addedItem", item);
        model.addAttribute("quantity", quantity);
        return "success";
    }

    @GetMapping("/")
    public String getIndex(ModelMap model) throws Exception {

        User userDetails = userService.getUserDetails(defaultUser);
        model.addAttribute("currentUser", userDetails);

        return "index";
    }

    @ExceptionHandler({ Exception.class })
    public ModelAndView handleError(Exception ex) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("message", ex.getMessage());
        mav.setViewName("error");
        return mav;
    }
}
