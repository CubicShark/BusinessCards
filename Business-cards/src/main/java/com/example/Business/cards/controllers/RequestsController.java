package com.example.Business.cards.controllers;

import com.example.Business.cards.models.Request;
import com.example.Business.cards.services.RequestsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/requests")
public class RequestsController {

    private final RequestsService requestsService;

    @Autowired
    public RequestsController(RequestsService requestsService) {
        this.requestsService = requestsService;
    }

    @GetMapping("/show")
    public String show(Model model){
        model.addAttribute("requests",requestsService.findRequestsId());
        return "requests/show";
    }

    @GetMapping("/index")
    public String book(@RequestParam(name = "id") int id, Model model) {
        model.addAttribute("request",requestsService.findAllByRequestId(id));

        return "requests/index";
    }

    @GetMapping("/newRequest")
    public String newRequest(Model model){
        model.addAttribute("request", new Request());
        model.addAttribute("designs", requestsService.findAllDesigns());
        model.addAttribute("workers", requestsService.findAllWorkers());
        return "requests/newRequest";
    }

    @PostMapping("/newRequest")
    public String newRequest(@ModelAttribute("request") Request request,
                             @RequestParam(name = "font") String font,
                             @RequestParam(name = "workerName") String workerName,
                             @RequestParam(name = "customerName") String customerName,
                             @RequestParam(name = "customerPhoneNumber") String customerPhoneNumber,
                             BindingResult bindingResult,
                             Model model){

        if(bindingResult.hasErrors()) {
            model.addAttribute("request", new Request());
            model.addAttribute("designs", requestsService.findAllDesigns());
            model.addAttribute("workers", requestsService.findAllWorkers());
            return "requests/newRequest";
        }


        requestsService.save(font, workerName, customerName, customerPhoneNumber, request);

        return "redirect:/requests/show";
    }
}
