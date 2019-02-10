package com.raccon.backend.web;

import com.raccon.backend.calc.EvaluatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("unused")
@Validated
@RestController
@CrossOrigin(maxAge = 3600, methods = {RequestMethod.GET, RequestMethod.POST})
public class ApiController {

    static private int EXPRESSION_LENGTH_THRESHOLD = 10000;

    @Autowired
    private EvaluatorService evaluator;

    @GetMapping("/health-check")
    @ResponseBody
    public Response<Double> healthCheck() {
        double result = evaluator.healthCheck();
        String message = "2 + 2 = " + result;
        return new Response<>(Response.Status.HEALTH_CHECK_ETO_HOROSHECHNO, result, message);
    }

    // TODO: It's just a hotfix. Remove this when the attack stops.
    private boolean isTooLong(String expression) {
        return expression.length() > EXPRESSION_LENGTH_THRESHOLD;
    }

    private void logExpression(String expression) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss ");
        Date date = new Date();
        System.out.print(dateFormat.format(date));
        System.out.print("expression: ");
        System.out.println(expression);
    }

    @GetMapping("/evaluate")
    @ResponseBody
    public Response<Double> evaluate(@RequestParam("expression") String expression) {
        logExpression(expression);
        if (isTooLong(expression)) {
            return new Response<>(Response.Status.FAIL, 0.0, "Expression is too long!");
        }

        return new Response<>(Response.Status.OK, evaluator.evaluate(expression));
    }

    @PostMapping("/evaluate")
    public Response<Double> evaluate(@RequestBody Request request) {
        logExpression(request.getExpression());
        if (isTooLong(request.getExpression())) {
            return new Response<>(Response.Status.FAIL, 0.0, "Expression is too long!");
        }
        return new Response<>(Response.Status.OK, evaluator.evaluate(request.getExpression()));
    }

}
