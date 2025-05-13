package com.centrabiz.payment;

import com.centrabiz.payment.logic.Optimizer;
import com.centrabiz.payment.model.Order;
import com.centrabiz.payment.model.PaymentMethod;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class OptimizerTest {

    @Test
    public void testSimpleOptimization() {
        List<Order> orders = List.of(
            new Order() {{
                id = "ORDER1";
                value = new BigDecimal("100.00");
                promotions = List.of("CARD_A");
            }},
            new Order() {{
                id = "ORDER2";
                value = new BigDecimal("200.00");
                promotions = List.of("CARD_B");
            }},
            new Order() {{
                id = "ORDER3";
                value = new BigDecimal("50.00");
                promotions = null;
            }}
        );

        List<PaymentMethod> methods = List.of(
            new PaymentMethod() {{
                id = "CARD_A";
                discount = 10;
                limit = new BigDecimal("150.00");
            }},
            new PaymentMethod() {{
                id = "CARD_B";
                discount = 5;
                limit = new BigDecimal("200.00");
            }},
            new PaymentMethod() {{
                id = "PUNKTY";
                discount = 15;
                limit = new BigDecimal("100.00");
            }}
        );

        Optimizer optimizer = new Optimizer();
        Map<String, BigDecimal> result = optimizer.optimize(orders, methods);

        assertEquals(3, result.size());
        assertTrue(result.get("CARD_A").compareTo(BigDecimal.ZERO) > 0);
        assertTrue(result.get("CARD_B").compareTo(BigDecimal.ZERO) > 0);
        assertTrue(result.get("PUNKTY").compareTo(BigDecimal.ZERO) > 0);
    }
}
