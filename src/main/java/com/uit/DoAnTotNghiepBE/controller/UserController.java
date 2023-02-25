package com.uit.DoAnTotNghiepBE.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @GetMapping("/hello1234")
    public static String Hello() {
        System.out.println("Hello1234");
        return "hello";
    }
    @PostMapping("/hello")
    public static void Hello1() {
        System.out.println("Helloo");
    }
}
