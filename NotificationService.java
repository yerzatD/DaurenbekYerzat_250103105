package com.ecommerce;

public class NotificationService {

    public void sendReceipt(String customerId, double amount) {

        System.out.println("[NotificationService] Step 1: Compiling receipt for customer: " + customerId);

        System.out.println("[NotificationService] Step 2: Receipt of $" +
                amount + " dispatched to customer: " + customerId);
    }
}