package com.raccoon.backend.api;

import org.springframework.web.bind.annotation.*;
import com.raccoon.backend.api.response.Response;
import com.raccoon.backend.api.request.Request;

public interface Api {

    @GetMapping("/health-check")
    @ResponseBody
    Response<Double> healthCheck();

    @GetMapping("/evaluate")
    @ResponseBody
    Response<Double> evaluate(@RequestParam("expression") String expression);

    @PostMapping("/evaluate")
    Response<Double> evaluate(@RequestBody Request request);
}
