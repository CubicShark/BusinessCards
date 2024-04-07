package com.example.Business.cards.controllers;

import com.example.Business.cards.models.Consumable;
import com.example.Business.cards.models.Supplier;
import com.example.Business.cards.models.Worker;
import com.example.Business.cards.services.ConsumablesService;
import com.example.Business.cards.services.RequestsService;
import com.example.Business.cards.services.SuppliersService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/suppliers")
public class SuppliersController {

    private final SuppliersService suppliersService;
    private final ConsumablesService consumablesService;

    @Autowired
    public SuppliersController(SuppliersService suppliersService, ConsumablesService consumablesService) {
        this.suppliersService = suppliersService;
        this.consumablesService = consumablesService;
    }

    @GetMapping("/showSuppliers")
    public String showSuppliers(Model model) {
        model.addAttribute("suppliers",suppliersService.findAllConsumablesAndSuppliers());

        return "suppliers/showSuppliers";
    }

    @GetMapping("/newSupplier")
    public String newSupplier(Model model){
        model.addAttribute("supplier", new Supplier());
        model.addAttribute("consumableTypes", consumablesService.findConsumableTypes());
        return "suppliers/newSupplier";
    }

    @PostMapping("/newSupplier")
    public String newSupplier(@RequestParam(name = "consumableType") String type,
                              @RequestParam(name = "amount") int amount,
                              @ModelAttribute("supplier") @Valid Supplier supplier,
                              BindingResult bindingResult,
                              Model model){


        if(bindingResult.hasErrors()) {
            model.addAttribute("consumableTypes", consumablesService.findConsumableTypes());
            return "suppliers/newSupplier";
        }

        if(amount < 1 || amount > 10000){
            model.addAttribute("consumableTypes", consumablesService.findConsumableTypes());
            model.addAttribute("confmessage", "Количество должно быть от 1 до 10 000");
            return "suppliers/newSupplier";
        }

        Consumable consumable = new Consumable();
        consumable.setAmount(amount);
        consumable.setType(type);

        suppliersService.save(supplier);
        consumablesService.save(consumable,suppliersService.findIdByOrgName(supplier.getOrganizationName()));

        return "redirect:/suppliers/showSuppliers";
    }
}
