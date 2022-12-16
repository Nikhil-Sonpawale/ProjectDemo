package com.code.sample.store.service;

import com.code.sample.store.model.Item;
import com.code.sample.store.repository.ItemRepository;
import com.code.sample.store.service.tools.StaticService;
import org.springframework.beans.factory.annotation.Autowired;

public class RateService {

    @Autowired
    private ItemRepository itemRepository;


    public int calculateRate(String itemId, int muliplicator) {

        Item item = itemRepository.findById(itemId);
        int rate = item.getPriceInCents() * StaticService.getMultiplicator();
        return rate;
    }

}
