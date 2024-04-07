package com.example.Business.cards.controllers;

import com.example.Business.cards.models.Request;
import com.example.Business.cards.models.Worker;
import com.example.Business.cards.services.RequestsService;
import com.example.Business.cards.services.WorkersService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/newWorker")
    public String newWorker(Model model){
        model.addAttribute("worker", new Worker());

        return "workers/newWorker";
    }

    @PostMapping("/newWorker")
    public String newWorker(@ModelAttribute("worker") @Valid Worker worker,
                             BindingResult bindingResult,
                             Model model){


        if(bindingResult.hasErrors()) {

            return "workers/newWorker";
        }

        workersService.save(worker);

        return "redirect:/workers/showWorkers";
    }

    @GetMapping("/updateWorker")
    public String updateWorker(@RequestParam(name = "id") int id,
                               Model model){
        model.addAttribute("worker", new Worker());
        model.addAttribute("id", id);
        System.out.println(id);
        return "workers/updateWorker";
    }

    @PostMapping("/updateWorker")
    public String updateWorker(@RequestParam(name = "id") int id,
                               @ModelAttribute("worker") @Valid Worker worker,
                               BindingResult bindingResult,
                               Model model){


        if(bindingResult.hasErrors()) {
            model.addAttribute("id", id);
            return "workers/updateWorker";
        }
        System.out.println(id);
        workersService.update(worker,id);

        return "redirect:/workers/showWorkers";
    }


}
