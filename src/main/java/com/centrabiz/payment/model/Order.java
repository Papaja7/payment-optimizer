package com.centrabiz.payment.model;

import java.math.BigDecimal;
import java.util.List;

public class Order {
    public String id;
    public BigDecimal value;
    public List<String> promotions;
}
