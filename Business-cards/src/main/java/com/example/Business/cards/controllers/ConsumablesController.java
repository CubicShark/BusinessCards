package com.example.Business.cards.controllers;

import com.example.Business.cards.models.Consumable;
import com.example.Business.cards.models.Worker;
import com.example.Business.cards.services.ConsumablesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/consumables")
public class ConsumablesController {

    private final ConsumablesService consumablesService;

    @Autowired
    public ConsumablesController(ConsumablesService consumablesService) {
        this.consumablesService = consumablesService;
    }

    @GetMapping("/updateConsumables")
    public String updateConsumables(@RequestParam(name = "id") int id,
                               Model model){
        model.addAttribute("consumable", new Consumable());
        model.addAttribute("id", id);

        return "consumables/updateConsumables";
    }

    @PostMapping("/updateConsumables")
    public String updateConsumables(@RequestParam(name = "id") int id,
                                    @ModelAttribute("consumable") @Valid Consumable consumable,
                                    BindingResult bindingResult,
                                    Model model){


        if(bindingResult.hasErrors()) {
            model.addAttribute("id", id);
            return "consumables/updateConsumables";
        }

        consumablesService.update(consumable,id);

        return "redirect:/suppliers/showSuppliers";
    }

}
