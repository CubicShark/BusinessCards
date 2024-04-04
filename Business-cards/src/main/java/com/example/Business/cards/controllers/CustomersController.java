package com.example.Business.cards.controllers;

import com.example.Business.cards.services.RequestsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        model.addAttribute("customers",requestsService.findAllCustomers());

        return "customers/showCustomers";
    }

    @GetMapping("/showCustomerRequests")
    public String showCustomerRequests(@RequestParam(name = "id") int id, Model model) {
        model.addAttribute("requests",requestsService.findRequestsByCustomerId(id));

        return "customers/showCustomerRequests";
    }
}
