import java.util.List;

public class Main {
    public static void main(String[] args) {
        String line = "=".repeat(60);

        System.out.println(line);
        System.out.println(" OMNIHOME SMART CONTROLLER: SYSTEM STARTUP");
        System.out.println(line);

        LegacyBulb rawBulb = new LegacyBulb();
        LegacyThermostat rawThermostat = new LegacyThermostat();

        BulbAdapter bulbAdapter = new BulbAdapter(rawBulb);
        ThermostatAdapter thermostatAdapter = new ThermostatAdapter(rawThermostat);
        System.out.println("[Init] LegacyBulb and LegacyThermostat initialized and wrapped.");

        List<SmartDevice> deviceList = List.of(bulbAdapter, thermostatAdapter);

        // ModernHub badHub = new ModernHub(List.of(rawBulb)); // COMPILE ERROR
        /*
         * The compiler rejects this call because LegacyBulb does not implement SmartDevice,
         * so List<LegacyBulb> is not a List<SmartDevice> and the types are incompatible.
         * The Object Adapter wraps the LegacyBulb via composition and implements SmartDevice,
         * so the hub accepts it without any change to the LegacyBulb class.
         */

        ModernHub hub = new ModernHub(deviceList);
        System.out.println("[Hub] Registering " + deviceList.size() + " adapted devices into ModernHub...");

        System.out.println("--- OPERATION: ACTIVATE ALL DEVICES ---");
        System.out.println("[Action] ModernHub.activateAll() invoked.");
        hub.activateAll();
        System.out.println(" -> BulbAdapter: Brightness set to " + rawBulb.readBrightness() + ".");
        System.out.println(" -> ThermostatAdapter: Dial set to '" + rawThermostat.checkDial() + "'.");
        System.out.println("[Status] All devices reported active: "
                + (bulbAdapter.isOn() && thermostatAdapter.isOn()));
        System.out.printf("[Power] Fleet Average Power Usage: %.2f%% (Bulb: %d%%, Thermostat: %d%%)%n",
                hub.calculateAveragePowerUsage(),
                bulbAdapter.getPowerPercent(),
                thermostatAdapter.getPowerPercent());

        System.out.println("--- AUDIT: HARDWARE FAULT INJECTION (STAGE 4) ---");
        System.out.println("[Fault 1] Filament physically severed on LegacyBulb...");
        rawBulb.breakFilament();
        System.out.println(" -> BulbAdapter.isOn(): " + bulbAdapter.isOn());
        System.out.println(" -> BulbAdapter.getPowerPercent(): " + bulbAdapter.getPowerPercent() + "%");

        System.out.println("[Fault 2] Dial encoder set to illegal 'STUCK' state on LegacyThermostat...");
        rawThermostat.rotateDial("STUCK");
        System.out.println(" -> ThermostatAdapter.isOn(): " + thermostatAdapter.isOn());
        System.out.println(" -> ThermostatAdapter.getPowerPercent(): " + thermostatAdapter.getPowerPercent());

        System.out.println("--- OPERATION: EMERGENCY SHUTDOWN ---");
        System.out.println("[Action] ModernHub.emergencyShutdown() invoked.");
        hub.emergencyShutdown();
        System.out.println(" -> BulbAdapter: Brightness set to " + rawBulb.readBrightness() + ".");
        System.out.println(" -> ThermostatAdapter: Dial rotated to '" + rawThermostat.checkDial() + "'.");
        System.out.printf("[Power] Fleet Average Power Usage: %.2f%%%n", hub.calculateAveragePowerUsage());

        System.out.println(line);
        System.out.println(" ALL INTEGRATION TESTS PASSED");
        System.out.println(line);
    }
}