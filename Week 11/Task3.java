```java
// ============================================
// STRATEGY PATTERN
// ============================================

interface DispatchStrategy {

    boolean shouldDispatch(int severityLevel, int currentHour);

    String getStrategyName();
}

// Always sends notifications
class ImmediateStrategy implements DispatchStrategy {

    @Override
    public boolean shouldDispatch(int severityLevel, int currentHour) {
        return true;
    }

    @Override
    public String getStrategyName() {
        return "IMMEDIATE_DISPATCH";
    }
}

// Only sends during business hours,
// except emergency alerts (severity >= 5)
class BusinessHoursStrategy implements DispatchStrategy {

    @Override
    public boolean shouldDispatch(int severityLevel, int currentHour) {

        if (severityLevel >= 5) {
            return true;
        }

        return currentHour >= 9 && currentHour <= 17;
    }

    @Override
    public String getStrategyName() {
        return "BUSINESS_HOURS_ONLY (09:00 - 17:00)";
    }
}


// ============================================
// BRIDGE PATTERN - IMPLEMENTOR
// ============================================

interface MessageChannel {

    void transmit(String recipient, String message);
}

// Email implementation
class EmailChannel implements MessageChannel {

    @Override
    public void transmit(String recipient, String message) {

        System.out.printf(
            "[EMAIL PROTOCOL -> %s]%n%s%n",
            recipient,
            message
        );
    }
}

// SMS implementation
class SmsChannel implements MessageChannel {

    @Override
    public void transmit(String recipient, String message) {

        String condensed = message.replace("\n", " -- ");

        System.out.printf(
            "[SMS PROTOCOL -> %s] %s%n",
            recipient,
            condensed
        );
    }
}


// ============================================
// BRIDGE PATTERN - ABSTRACTION
// ============================================

abstract class Alert {

    protected final MessageChannel channel;

    protected final DispatchStrategy strategy;

    public Alert(MessageChannel channel, DispatchStrategy strategy) {

        if (channel == null || strategy == null) {
            throw new IllegalArgumentException(
                "Dependencies cannot be null"
            );
        }

        this.channel = channel;
        this.strategy = strategy;
    }

    public void processAlert(
        String recipient,
        String rawContent,
        int currentHour
    ) {

        int severity = getSeverityLevel();

        if (strategy.shouldDispatch(severity, currentHour)) {

            String formatted = formatAlertBody(rawContent);

            channel.transmit(recipient, formatted);

        } else {

            System.out.printf(
                "[DISPATCH SUPPRESSED] Alert blocked by policy '%s' at hour %02d:00 (Severity: %d)%n",
                strategy.getStrategyName(),
                currentHour,
                severity
            );
        }
    }

    public abstract int getSeverityLevel();

    public abstract String formatAlertBody(String raw);
}


// ============================================
// REFINED ABSTRACTION 1
// ============================================

class CriticalSystemAlert extends Alert {

    public CriticalSystemAlert(
        MessageChannel channel,
        DispatchStrategy strategy
    ) {
        super(channel, strategy);
    }

    @Override
    public int getSeverityLevel() {
        return 5;
    }

    @Override
    public String formatAlertBody(String raw) {

        return "!!! CRITICAL PRODUCTION ALERT !!!\n"
            + "Incident Report: " + raw + "\n"
            + "ACTION REQUIRED: SRE on-call immediate intervention.";
    }
}


// ============================================
// REFINED ABSTRACTION 2
// ============================================

class DailySummaryAlert extends Alert {

    public DailySummaryAlert(
        MessageChannel channel,
        DispatchStrategy strategy
    ) {
        super(channel, strategy);
    }

    @Override
    public int getSeverityLevel() {
        return 1;
    }

    @Override
    public String formatAlertBody(String raw) {

        return "Daily Digest:\n" + raw;
    }
}


// ============================================
// MAIN
// ============================================

public class Task3Runner {

    public static void main(String[] args) {

        MessageChannel email = new EmailChannel();

        MessageChannel sms = new SmsChannel();

        DispatchStrategy immediate = new ImmediateStrategy();

        DispatchStrategy businessHours = new BusinessHoursStrategy();


        // 1. Critical alert at night
        System.out.println(
            ">>> 1. Critical Alert via SMS (Night time: 02:00, Emergency Bypass) <<<"
        );

        Alert emergency = new CriticalSystemAlert(
            sms,
            businessHours
        );

        emergency.processAlert(
            "+1555019283",
            "Database primary node kernel panic",
            2
        );


        // 2. Daily summary at night
        System.out.println(
            "\n>>> 2. Daily Summary via Email at Night (Hour: 23:00 - Should Suppress) <<<"
        );

        Alert summaryNight = new DailySummaryAlert(
            email,
            businessHours
        );

        summaryNight.processAlert(
            "dev-team@company.internal",
            "99.98% uptime achieved today.",
            23
        );


        // 3. Daily summary during business hours
        System.out.println(
            "\n>>> 3. Daily Summary via Email during Workday (Hour: 11:00 - Should Deliver) <<<"
        );

        Alert summaryDay = new DailySummaryAlert(
            email,
            businessHours
        );

        summaryDay.processAlert(
            "dev-team@company.internal",
            "99.98% uptime achieved today.",
            11
        );
    }
}
```
