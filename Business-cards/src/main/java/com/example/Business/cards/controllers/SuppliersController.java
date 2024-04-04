package com.example.Business.cards.controllers;

import com.example.Business.cards.services.RequestsService;
import com.example.Business.cards.services.SuppliersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/suppliers")
public class SuppliersController {

    private final SuppliersService suppliersService;

    @Autowired
    public SuppliersController(SuppliersService suppliersService) {
        this.suppliersService = suppliersService;
    }

    @GetMapping("/showSuppliers")
    public String showSuppliers(Model model) {
        model.addAttribute("suppliers",suppliersService.findAllConsumablesAndSuppliers());

        return "suppliers/showSuppliers";
    }
}
