package org.maxcheung.camelboot2.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("simple")
public class SimpleController {
    @Value("${spring.application.name}")
    String appName;
 

    
    @GetMapping("/greeting")
    public String greeting() {
        return "greeting";
    }
}