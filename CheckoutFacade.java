package com.ecommerce;

public class CheckoutFacade {

    private final InventoryService inventory;
    private final PaymentGateway payment;
    private final ShippingService shipping;
    private final NotificationService notification;

    public CheckoutFacade() {
        this.inventory = new InventoryService();
        this.payment = new PaymentGateway();
        this.shipping = new ShippingService();
        this.notification = new NotificationService();
    }

    public boolean placeOrder(String productId, String customerId,
                              String address, double amount) {

        System.out.println("=== [CheckoutFacade] Starting Order Placement Workflow ===");

        // Step 1: Check stock
        if (!inventory.checkStock(productId)) {
            System.out.println("[CheckoutFacade] Aborting: Item out of stock.");
            return false;
        }

        // Step 2: Reserve stock
        inventory.reserve(productId);

        // Step 3: Process payment
        if (!payment.charge(customerId, amount)) {
            System.out.println("[CheckoutFacade] Aborting: Payment declined.");
            return false;
        }

        // Step 4: Schedule delivery
        shipping.scheduleDelivery(productId, address);

        // Step 5: Send receipt
        notification.sendReceipt(customerId, amount);

        System.out.println("=== [CheckoutFacade] Order Workflow Completed Successfully ===");

        return true;
    }
}