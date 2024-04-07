package com.example.Business.cards.controllers;

import com.example.Business.cards.models.Design;
import com.example.Business.cards.models.Worker;
import com.example.Business.cards.services.DesignsService;
import com.example.Business.cards.services.RequestsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/newDesign")
    public String newDesign(Model model){
        model.addAttribute("design", new Design());

        return "designs/newDesign";
    }

    @PostMapping("/newDesign")
    public String newDesign(@ModelAttribute("design") @Valid Design design,
                            BindingResult bindingResult,
                            Model model){


        if(bindingResult.hasErrors()) {

            return "designs/newDesign";
        }

        designsService.save(design);

        return "redirect:/designs/showDesigns";
    }

    @GetMapping("/updateDesign")
    public String updateDesign(@RequestParam(name = "id") int id,
                               Model model){
        model.addAttribute("design", new Design());
        model.addAttribute("id", id);

        return "designs/updateDesign";
    }

    @PostMapping("/updateDesign")
    public String updateDesign(@RequestParam(name = "id") int id,
                               @ModelAttribute("design") @Valid Design design,
                               BindingResult bindingResult,
                               Model model){


        if(bindingResult.hasErrors()) {
            model.addAttribute("id", id);
            return "designs/updateDesign";
        }

        designsService.update(design,id);

        return "redirect:/designs/showDesigns";
    }

}
