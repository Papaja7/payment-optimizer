package com.centrabiz.payment;

import com.centrabiz.payment.logic.Optimizer;
import com.centrabiz.payment.model.Order;
import com.centrabiz.payment.model.PaymentMethod;
import com.centrabiz.payment.util.JsonParser;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class PaymentOptimizerApp {
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Użycie: java -jar app.jar <orders.json> <paymentmethods.json>");
            return;
        }

        List<Order> orders = JsonParser.readOrders(Paths.get(args[0]));
        List<PaymentMethod> methods = JsonParser.readMethods(Paths.get(args[1]));

        Optimizer optimizer = new Optimizer();
        Map<String, BigDecimal> results = optimizer.optimize(orders, methods);

        results.forEach((method, amount) ->
            System.out.printf("%s %.2f%n", method, amount)
        );
    }
}
