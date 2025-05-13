package com.centrabiz.payment.logic;

import com.centrabiz.payment.model.Order;
import com.centrabiz.payment.model.PaymentMethod;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Optimizer {

    private static final String PUNKTY = "PUNKTY";
    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    private record Option(String method1, BigDecimal amount1, String method2, BigDecimal amount2, BigDecimal totalCost) {}

    public Map<String, BigDecimal> optimize(List<Order> orders, List<PaymentMethod> methods) {
        Map<String, PaymentMethod> methodMap = new HashMap<>();
        Map<String, BigDecimal> methodLimits = new HashMap<>();
        Map<String, BigDecimal> usedAmounts = new HashMap<>();

        // Inicjalizacja metod
        for (PaymentMethod method : methods) {
            methodMap.put(method.id, method);
            methodLimits.put(method.id, method.limit);
            usedAmounts.put(method.id, BigDecimal.ZERO);
        }

        for (Order order : orders) {
            BigDecimal value = order.value;
            List<Option> options = new ArrayList<>();

            // OPCJA 1: 100% kartą z rabatem
            for (PaymentMethod method : methods) {
                if (!method.id.equals(PUNKTY) && value.compareTo(methodLimits.get(method.id)) <= 0) {
                    boolean promo = order.promotions != null && order.promotions.contains(method.id);
                    BigDecimal discount = promo ? BigDecimal.valueOf(method.discount) : BigDecimal.ZERO;
                    BigDecimal total = value.subtract(value.multiply(discount).divide(ONE_HUNDRED, 2, RoundingMode.HALF_UP));
                    options.add(new Option(method.id, total, null, BigDecimal.ZERO, total));
                }
            }

            // OPCJA 2: 100% punktami
            PaymentMethod punkty = methodMap.get(PUNKTY);
            if (punkty != null && value.compareTo(methodLimits.get(PUNKTY)) <= 0) {
                BigDecimal discount = BigDecimal.valueOf(punkty.discount);
                BigDecimal total = value.subtract(value.multiply(discount).divide(ONE_HUNDRED, 2, RoundingMode.HALF_UP));
                options.add(new Option(PUNKTY, total, null, BigDecimal.ZERO, total));
            }

            // OPCJA 3: ≥10% punktami + reszta kartą
            BigDecimal minPoints = value.multiply(new BigDecimal("0.10")).setScale(2, RoundingMode.HALF_UP);
            if (punkty != null && minPoints.compareTo(methodLimits.get(PUNKTY)) <= 0) {
                BigDecimal rest = value.subtract(minPoints);
                for (PaymentMethod method : methods) {
                    if (!method.id.equals(PUNKTY) && rest.compareTo(methodLimits.get(method.id)) <= 0) {
                        BigDecimal discounted = value.subtract(value.multiply(new BigDecimal("10")).divide(ONE_HUNDRED, 2, RoundingMode.HALF_UP));
                        BigDecimal proportion = minPoints.divide(value, 5, RoundingMode.HALF_UP);
                        BigDecimal pointsAmount = discounted.multiply(proportion).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal restAmount = discounted.subtract(pointsAmount);
                        options.add(new Option(PUNKTY, pointsAmount, method.id, restAmount, discounted));
                    }
                }
            }

            
            Option best = options.stream().min(Comparator.comparing(o -> o.totalCost)).orElseThrow();

            
            usedAmounts.put(best.method1, usedAmounts.get(best.method1).add(best.amount1));
            methodLimits.put(best.method1, methodLimits.get(best.method1).subtract(best.amount1));
            if (best.method2 != null) {
                usedAmounts.put(best.method2, usedAmounts.get(best.method2).add(best.amount2));
                methodLimits.put(best.method2, methodLimits.get(best.method2).subtract(best.amount2));
            }
        }

        return usedAmounts;
    }
}
