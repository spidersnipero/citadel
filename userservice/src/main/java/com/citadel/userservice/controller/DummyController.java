package com.citadel.userservice.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DummyController {
    @GetMapping("/admin/tester")
    public String tester(HttpServletRequest request){

        return "passed "+request.getSession().getId();
    }

}
