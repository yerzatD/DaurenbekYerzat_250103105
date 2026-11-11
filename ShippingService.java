package com.ecommerce;

public class ShippingService {

    public void scheduleDelivery(String productId, String address) {

        System.out.println("[ShippingService] Step 1: Validating delivery destination: " + address);

        System.out.println("[ShippingService] Step 2: Delivery scheduled for item " +
                productId + " to address: " + address);
    }
}