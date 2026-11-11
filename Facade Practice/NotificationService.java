package com.ecommerce;

/**
 * Subsystem Class 4: NotificationService
 * Responsible for sending transactional communications (email, SMS, push) to customers.
 */
public class NotificationService {

    /**
     * Step 1: Generate and transmit an order receipt/confirmation to the customer.
     *
     * @param customerId The customer receiving the notification.
     * @param amount The total charged amount to display in the receipt.
     * @return true if the notification was sent successfully, false otherwise.
     */
    public boolean sendReceipt(String customerId, double amount) {
        if (customerId == null || customerId.trim().isEmpty()) {
            System.out.println("[NotificationService] Step 1: Receipt could not be sent because customer ID is invalid.");
            return false;
        }

        if (amount <= 0) {
            System.out.println("[NotificationService] Step 1: Receipt could not be sent because amount is invalid.");
            return false;
        }

        // Step 1.1: Render digital receipt template
        System.out.println("Compiling receipt for customer: " + customerId);

        // Step 1.2: Dispatch email/SMS notification
        System.out.println("Receipt of $" + amount + " dispatched to customer: " + customerId);
        return true;
    }
}
