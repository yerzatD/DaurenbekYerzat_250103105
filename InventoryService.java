package com.ecommerce;

public class InventoryService {

    public boolean checkStock(String productId) {

        System.out.println("[InventoryService] Step 1: Checking stock levels for item: " + productId);

        return true;
    }

    public void reserve(String productId) {

        System.out.println("[InventoryService] Step 2: Stock reserved successfully for item: " + productId);
    }
}