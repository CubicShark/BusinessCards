package com.example.Business.cards.controllers;

import com.example.Business.cards.services.RequestsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/suppliers")
public class SuppliersController {

    private final RequestsService requestsService;

    @Autowired
    public SuppliersController(RequestsService requestsService) {
        this.requestsService = requestsService;
    }

    @GetMapping("/showSuppliers")
    public String showSuppliers(Model model) {
        model.addAttribute("suppliers",requestsService.findAllConsumablesAndSuppliers());

        return "suppliers/showSuppliers";
    }
}
