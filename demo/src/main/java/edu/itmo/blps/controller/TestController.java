package edu.itmo.blps.controller;

import edu.itmo.blps.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/test")
@RestController
public class TestController {

    @Autowired
    KafkaProducerService producerService;
    @PostMapping
    public String test(){
        System.out.println("Producing");
        producerService.sendMessage("asd");
        return "ok";
    }
}
