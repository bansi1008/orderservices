package com.example.Servicelayer;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

@RequestMapping("/test")
public class Test {
    @RequestMapping("/hello")
    public String hello() {
        return "Hello from Test Service!";
    }
    
}
