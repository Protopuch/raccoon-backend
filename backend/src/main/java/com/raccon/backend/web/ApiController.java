package com.raccon.backend.web;

import com.raccon.backend.calc.EvaluatorService;
import com.weddini.throttling.Throttling;
import com.weddini.throttling.ThrottlingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
@Validated
@RestController
@CrossOrigin(maxAge = 3600, methods = {RequestMethod.GET, RequestMethod.POST})
public class ApiController {

    private int EXPRESSION_LENGTH_THRESHOLD = 2000;

    @Autowired
    private EvaluatorService evaluator;

    @Throttling(type = ThrottlingType.RemoteAddr, limit = 5, timeUnit = TimeUnit.SECONDS)
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

    @Throttling(type = ThrottlingType.RemoteAddr, limit = 5, timeUnit = TimeUnit.SECONDS)
    @GetMapping("/evaluate")
    @ResponseBody
    public Response<Double> evaluate(@RequestParam("expression") String expression) {
        logExpression(expression);
        if (isTooLong(expression)) {
            return new Response<>(Response.Status.FAIL, 0.0, "Expression is too long!");
        }

        return new Response<>(Response.Status.OK, evaluator.evaluate(expression));
    }

    @Throttling(type = ThrottlingType.RemoteAddr, limit = 5, timeUnit = TimeUnit.SECONDS)
    @PostMapping("/evaluate")
    public Response<Double> evaluate(@RequestBody Request request) {
        logExpression(request.getExpression());
        if (isTooLong(request.getExpression())) {
            return new Response<>(Response.Status.FAIL, 0.0, "Expression is too long!");
        }
        return new Response<>(Response.Status.OK, evaluator.evaluate(request.getExpression()));
    }

}
