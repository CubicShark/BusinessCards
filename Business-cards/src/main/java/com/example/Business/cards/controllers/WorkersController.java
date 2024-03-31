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
@RequestMapping("/workers")
public class WorkersController {

    private final RequestsService requestsService;

    @Autowired
    public WorkersController(RequestsService requestsService) {
        this.requestsService = requestsService;
    }

    @GetMapping("/showWorkers")
    public String showWorkers(Model model) {
        model.addAttribute("workers",requestsService.findAllWorkers());

        return "workers/showWorkers";
    }

    @DeleteMapping("/delete")
    public String deleteWorker(@RequestParam(name = "id") int id){

        requestsService.deleteWorkerById(id);

        return "redirect:/workers/showWorkers";
    }
}
