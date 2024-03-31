package com.example.Business.cards.controllers;

import com.example.Business.cards.services.RequestsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/designs")
public class DesignsController {

    private final RequestsService requestsService;

    @Autowired
    public DesignsController(RequestsService requestsService) {
        this.requestsService = requestsService;
    }

    @GetMapping("/showDesigns")
    public String showDesigns(Model model) {
        model.addAttribute("designs",requestsService.findAllDesigns());

        return "designs/showDesigns";
    }

    @DeleteMapping("/deleteDesign")
    public String deleteDesign(@RequestParam(name = "id") int id){

//        requestsService.deleteDesignById(id);

        return "redirect:/designs/showDesigns";
    }
}
