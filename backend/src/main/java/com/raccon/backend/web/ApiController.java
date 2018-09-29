package com.raccon.backend.web;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@CrossOrigin(maxAge = 3600, methods = {RequestMethod.GET, RequestMethod.POST})
public class ApiController {

    @GetMapping("/health-check")
    public String getHello() {
        return "Health check success!";
    }

}
