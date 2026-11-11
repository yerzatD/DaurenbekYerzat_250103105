package com.ecommerce;

public class PaymentGateway {

    public boolean charge(String customerId, double amount) {

        System.out.println("[PaymentGateway] Step 1: Initiating transaction of $" + amount +
                " for customer: " + customerId);

        System.out.println("[PaymentGateway] Step 2: Payment captured successfully for customer: " + customerId);

        return true;
    }
}