package com.example.Business.cards.controllers;

import com.example.Business.cards.services.RequestsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customers")
public class CustomersController {

    private final RequestsService requestsService;

    @Autowired
    public CustomersController(RequestsService requestsService) {
        this.requestsService = requestsService;
    }

    @GetMapping("/showCustomers")
    public String showCustomers(Model model) {
        model.addAttribute("requests",requestsService.findAllRequestsIdAndTheirCustomers());

        return "customers/showCustomers";
    }
}
