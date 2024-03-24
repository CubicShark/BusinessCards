package com.example.Business.cards.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cardsMarket")
public class CardsMarketController {

    @GetMapping()
    public String Home(){
        return "cardsMarket/home";
    }


}
