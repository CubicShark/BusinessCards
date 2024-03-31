package com.example.Business.cards.controllers;

import com.example.Business.cards.models.ConsumableType;
import com.example.Business.cards.models.Customer;
import com.example.Business.cards.models.Request;
import com.example.Business.cards.services.RequestsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        model.addAttribute("requests",requestsService.findNotEndedRequestsId());
        return "requests/show";
    }

    @GetMapping("/index")
    public String index(@RequestParam(name = "id") int id, Model model) {
        model.addAttribute("request",requestsService.findAllByRequestId(id));

        return "requests/index";
    }

    @GetMapping("/newRequest")
    public String newRequest(Model model){
        model.addAttribute("request", new Request());
        //model.addAttribute("customer", new Customer());     TODO в модели ловить ошибку и в хиденне передавать данные нужные
        System.out.println(LocalDate.now());
        model.addAttribute("designs", requestsService.findAllDesigns());
        model.addAttribute("workers", requestsService.findAllWorkers());
        return "requests/newRequest";
    }

    @PostMapping("/newRequest")
    public String newRequest(@RequestParam(name = "font") String font,
                             @RequestParam(name = "workerName") String workerName,
                             @RequestParam(name = "customerName") String customerName,
                             @RequestParam(name = "customerPhoneNumber") String customerPhoneNumber,
                             @ModelAttribute("request") @Valid Request request,
                             BindingResult bindingResult,
                             Model model){

        if(bindingResult.hasErrors()) {
            model.addAttribute("designs", requestsService.findAllDesigns());
            model.addAttribute("workers", requestsService.findAllWorkers());
            return "requests/newRequest";
        }

        requestsService.saveRequest(font, workerName, customerName, customerPhoneNumber, request);

        return "redirect:/requests/show";
    }

    @GetMapping("/endUp")
    public String endUp(@RequestParam(name = "id") int id, Model model) {
        requestsService.endUpRequest(id);
        model.addAttribute("requests",requestsService.findNotEndedRequestsId());
        return "requests/show";
    }

    // TODO OtCHETNOST

    @GetMapping("/reporting")
    public String Reporting(Model model) {
        model.addAttribute("request",new Request());
        return "requests/reporting";
    }

    @PostMapping("/reporting")
    public String Reporting(RedirectAttributes redirectAttributes,
                            @ModelAttribute("request") @Valid Request request,
                            BindingResult bindingResult, Model model) {


        redirectAttributes.addAttribute("firstDate", request.getStartDate());
        redirectAttributes.addAttribute("secondDate", request.getEndDate());
        return "redirect:/requests/showRequestsReporting";
    }

    @GetMapping("/showRequestsReporting")
    public String showRequestsReporting(@RequestParam(name = "firstDate")Date firstDate,
                                        @RequestParam(name = "secondDate")Date secondDate,
                                        Model model) {

        List<ConsumableType> consumables = new ArrayList<>();

        List<String> types = requestsService.findConsumableTypes();

        for (String type : types) {
            consumables.add(new ConsumableType(type, requestsService.findUsedConsumablesSum(firstDate, secondDate, type)));
        }

        model.addAttribute("firstDate",firstDate);
        model.addAttribute("secondDate",secondDate);
        model.addAttribute("doneRequestsNumber",requestsService.findDoneRequestsNumber(firstDate,secondDate));
        model.addAttribute("consumables",consumables);
        return "requests/showRequestsReporting";
    }
}
