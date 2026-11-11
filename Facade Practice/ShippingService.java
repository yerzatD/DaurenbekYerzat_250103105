package com.ecommerce;

/**
 * Subsystem Class 3: ShippingService
 * Responsible for logistics, carrier dispatch, and scheduling order delivery.
 */
public class ShippingService {

    /**
     * Step 1: Schedule the delivery assignment with a logistics carrier.
     *
     * @param productId The item to be shipped.
     * @param address The shipping destination address.
     * @return true if shipping was scheduled successfully, false otherwise.
     */
    public boolean scheduleDelivery(String productId, String address) {
        if (productId == null || productId.trim().isEmpty()) {
            System.out.println("[ShippingService] Step 1: Delivery could not be scheduled without a valid product ID.");
            return false;
        }

        if (address == null || address.trim().isEmpty()) {
            System.out.println("[ShippingService] Step 1: Delivery could not be scheduled without a valid address.");
            return false;
        }

        // Step 1.1: Validate shipping address format
        System.out.println("[ShippingService] Step 1: Validating delivery destination: " + address);

        // Step 1.2: Register package with logistics courier and generate tracking number
        System.out.println("[ShippingService] Step 2: Delivery scheduled for item " + productId + " to address: " + address);
        return true;
    }
}
