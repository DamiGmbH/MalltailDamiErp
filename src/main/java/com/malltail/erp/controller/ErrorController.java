package com.malltail.erp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/error/authentication")
    public String handleAuthenticationError(){
        return "/error/auth-error";
    }

    @GetMapping("/error/access-denied")
    public String handleAccessDeniedError() {
        return "/error/denied-error";
    }
}
