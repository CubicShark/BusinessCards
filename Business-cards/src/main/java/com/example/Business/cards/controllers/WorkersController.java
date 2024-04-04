package com.example.Business.cards.controllers;

import com.example.Business.cards.models.Request;
import com.example.Business.cards.services.RequestsService;
import com.example.Business.cards.services.WorkersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/workers")
public class WorkersController {

    private final RequestsService requestsService;
    private final WorkersService workersService;

    @Autowired
    public WorkersController(RequestsService requestsService, WorkersService workersService) {
        this.requestsService = requestsService;
        this.workersService = workersService;
    }

    @GetMapping("/showWorkers")
    public String showWorkers(Model model) {
        model.addAttribute("workers",workersService.findAllWorkers());

        return "workers/showWorkers";
    }

    @DeleteMapping("/delete")
    public String deleteWorker(@RequestParam(name = "id") int id){

        //TODO сделать чтобы не удаляло чела который работает сейчас

        List<Request> requests = requestsService.findRequestsByWorkerId(id);

        for (Request request : requests) {
            requestsService.fillConsumablesBasketArchiveByRequestId(request.getId());
            requestsService.deleteFromConsumablesBasketByRequestId(request.getId());
        }

        workersService.deleteWorkerById(id);

        return "redirect:/workers/showWorkers";
    }
}
