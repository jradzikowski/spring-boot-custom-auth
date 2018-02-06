package com.dzasek.springbootcustomauth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/","/home","/pass"})
    public String homePage() {
        return "start";
    }

}
