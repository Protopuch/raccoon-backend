package com.raccon.backend.calc;

import com.raccoon.backend.arithmetic.ExpressionEvaluator;
import org.springframework.stereotype.Service;

@Service
public class EvaluatorService {
    public Double healthCheck() {
        return ExpressionEvaluator.simpleEvaluator().evaluate("2+2");
    }
    public Double evaluate(String expression) {
        return ExpressionEvaluator.simpleEvaluator().evaluate(expression);
    }
}
