package com.esgrupo10.SATM.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class SatmController {

    @GetMapping("/")
    public String viewHP() {
        return "index";
    }

    @GetMapping("/sucesso")
    public String sucesso() {
        return "sucesso";
    }

    @GetMapping("/login")
    public String login() { return "login"; }

    @GetMapping("/logout")
    public String logout() { return "logout"; }
}
