package com.citadel.productservice.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DummyController {

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/products/tester")
    public String tester(HttpServletRequest request){

        return "passed "+request.getSession().getId();
    }

}
