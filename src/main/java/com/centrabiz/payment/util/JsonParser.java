package com.centrabiz.payment.util;

import com.centrabiz.payment.model.Order;
import com.centrabiz.payment.model.PaymentMethod;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class JsonParser {
    public static List<Order> readOrders(Path path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(path.toFile(), new TypeReference<List<Order>>() {});
    }

    public static List<PaymentMethod> readMethods(Path path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(path.toFile(), new TypeReference<List<PaymentMethod>>() {});
    }
}
