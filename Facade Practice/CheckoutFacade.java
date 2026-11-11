package com.ecommerce;

/**
 * The Facade Class: CheckoutFacade
 * Coordinates the underlying subsystems (Inventory, Payment, Shipping, Notification)
 * behind a unified, single-method interface for callers.
 */
public class CheckoutFacade {

    // Subsystem instances maintained by the Facade
    private final InventoryService inventory;
    private final PaymentGateway payment;
    private final ShippingService shipping;
    private final NotificationService notification;

    /**
     * Step 1: Initialize all internal subsystem dependencies.
     */
    public CheckoutFacade() {
        this.inventory = new InventoryService();
        this.payment = new PaymentGateway();
        this.shipping = new ShippingService();
        this.notification = new NotificationService();
    }

    /**
     * Step 2: Unified checkout orchestrator.
     * Coordinates the full multi-service workflow in the proper sequence.
     *
     * @param productId Identifier of the purchased item.
     * @param customerId Identifier of the purchasing user.
     * @param address Physical delivery destination.
     * @param amount Total transaction charge.
     * @return true if the entire checkout workflow succeeded, false otherwise.
     */
    public boolean placeOrder(String productId, String customerId, String address, double amount) {
        if (productId == null || productId.trim().isEmpty()) {
            System.out.println("[CheckoutFacade] Aborting: Invalid product ID.");
            return false;
        }

        if (customerId == null || customerId.trim().isEmpty()) {
            System.out.println("[CheckoutFacade] Aborting: Invalid customer ID.");
            return false;
        }

        if (address == null || address.trim().isEmpty()) {
            System.out.println("[CheckoutFacade] Aborting: Invalid shipment address.");
            return false;
        }

        if (amount <= 0) {
            System.out.println("[CheckoutFacade] Aborting: Invalid order amount.");
            return false;
        }

        System.out.println("=== [CheckoutFacade] Starting Order Placement Workflow ===");

        // Step 2.1: Verify warehouse inventory availability
        if (!inventory.checkStock(productId)) {
            System.out.println("[CheckoutFacade] Aborting: Item out of stock.");
            return false;
        }

        // Step 2.2: Reserve the item in stock
        if (!inventory.reserve(productId)) {
            System.out.println("[CheckoutFacade] Aborting: Unable to reserve product.");
            return false;
        }

        // Step 2.3: Process billing and charge customer
        if (!payment.charge(customerId, amount)) {
            System.out.println("[CheckoutFacade] Aborting: Payment declined.");
            return false;
        }

        // Step 2.4: Schedule logistics carrier dispatch
        if (!shipping.scheduleDelivery(productId, address)) {
            System.out.println("[CheckoutFacade] Aborting: Shipping scheduling failed.");
            return false;
        }

        // Step 2.5: Dispatch digital receipt confirmation
        if (!notification.sendReceipt(customerId, amount)) {
            System.out.println("[CheckoutFacade] Aborting: Receipt notification failed.");
            return false;
        }

        System.out.println("=== [CheckoutFacade] Order Workflow Completed Successfully ===");
        return true;
    }
}
