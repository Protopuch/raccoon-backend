package com.raccon.backend.web;

import com.raccon.backend.calc.EvaluatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings("unused")
@Validated
@RestController
@CrossOrigin(maxAge = 3600, methods = {RequestMethod.GET, RequestMethod.POST})
public class ApiController {
    @Autowired
    private EvaluatorService evaluator;

    @GetMapping("/health-check")
    @ResponseBody
    public Response<Double> healthCheck() {
        double result = evaluator.healthCheck();
        String message = "2 + 2 = " + result;
        return new Response<>(Response.Status.OK, result, message);
    }

    @GetMapping("/evaluate")
    @ResponseBody
    public Response<Double> evaluate(@RequestParam("expression") String expression) {
        return new Response<>(Response.Status.OK, evaluator.evaluate(expression));
    }

    @PostMapping("/evaluate")
    public Response<Double> evaluate(@RequestBody Request request) {
        return new Response<>(Response.Status.OK, evaluator.evaluate(request.getExpression()));
    }

}
