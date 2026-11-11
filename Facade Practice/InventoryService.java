package com.ecommerce;

/**
 * Subsystem Class 1: InventoryService
 * Responsible for verifying and allocating product inventory.
 */
public class InventoryService {

    /**
     * Step 1: Check whether sufficient stock is available in the warehouse.
     *
     * @param productId The unique identifier of the item.
     * @return true if item is in stock and available for order, false otherwise.
     */
    public boolean checkStock(String productId) {
        if (productId == null || productId.trim().isEmpty()) {
            System.out.println("Invalid product ID.");
            return false;
        }

        // Step 1.1: Query warehouse database/inventory cache
        System.out.println("Checking stock levels for item: " + productId);

        // Step 1.2: Return stock status (mocked as true for this demonstration)
        return true;
    }

    /**
     * Step 2: Reserve the requested stock so subsequent concurrent orders do not oversell.
     *
     * @param productId The unique identifier of the item.
     * @return true if the stock was successfully reserved, false otherwise.
     */
    public boolean reserve(String productId) {
        if (productId == null || productId.trim().isEmpty()) {
            System.out.println("Cannot reserve stock for an empty product ID.");
            return false;
        }

        // Step 2.1: Lock and decrement available stock count
        System.out.println("Stock reserved successfully for item: " + productId);
        return true;
    }
}
