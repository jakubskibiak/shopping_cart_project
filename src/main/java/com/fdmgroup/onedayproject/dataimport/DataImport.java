package com.fdmgroup.onedayproject.dataimport;

import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.fdmgroup.onedayproject.model.Item;
import com.fdmgroup.onedayproject.model.User;
import com.fdmgroup.onedayproject.repository.ItemRepository;
import com.fdmgroup.onedayproject.repository.UserRepository;

@Component
public class DataImport implements ApplicationRunner {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public DataImport(UserRepository userRepository, ItemRepository itemRepository) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        User userOfMagda = new User(null, "magda_palica", "Magda", "Palica", "magda@palica.pl");
        userRepository.saveAll(List.of(userOfMagda));

        Item item1 = new Item(null, "item_1", "desc1", 11.0, 1);
        Item item2 = new Item(null, "item_2", "desc2", 12.0, 2);
        Item item3 = new Item(null, "item_3", "desc3", 13.0, 3);
        Item item4 = new Item(null, "item_4", "desc4", 14.0, 4);
        Item item5 = new Item(null, "item_5", "desc5", 15.0, 5);

        itemRepository.saveAll(List.of(item1, item2, item3, item4, item5));
    }

}
