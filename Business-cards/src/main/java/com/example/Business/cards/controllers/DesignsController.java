package com.example.Business.cards.controllers;

import com.example.Business.cards.services.DesignsService;
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

    private final DesignsService designsService;

    @Autowired
    public DesignsController(DesignsService designsService) {
        this.designsService = designsService;
    }

    @GetMapping("/showDesigns")
    public String showDesigns(Model model) {
        model.addAttribute("designs",designsService.findAllDesigns());

        return "designs/showDesigns";
    }

}
