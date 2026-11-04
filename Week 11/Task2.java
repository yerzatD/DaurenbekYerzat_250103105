```java
import java.util.HashMap;
import java.util.Map;

// ============================================
// LEGACY SYSTEM (ADAPTEE)
// ============================================

class LegacyStockFeed {

    public String fetchRawTickerPacket(String ticker) {

        // Simulates network latency
        try {
            Thread.sleep(250);
        } catch (InterruptedException ignored) {
        }

        if ("AAPL".equalsIgnoreCase(ticker)) {
            return "TICKER=AAPL;BID=182.50;ASK=183.50;STATUS=OPEN;TIMESTAMP=17300000";
        } else if ("GOOGL".equalsIgnoreCase(ticker)) {
            return "TICKER=GOOGL;BID=164.20;ASK=164.80;STATUS=OPEN;TIMESTAMP=17300000";
        }

        return "TICKER=" + ticker + ";BID=0.00;ASK=0.00;STATUS=UNKNOWN;TIMESTAMP=0";
    }
}

// ============================================
// TARGET INTERFACE
// ============================================

interface StockProvider {

    double getMarketPrice(String ticker);

    String getStatus(String ticker);
}

// ============================================
// ADAPTER PATTERN
// ============================================

class StockFeedAdapter implements StockProvider {

    private final LegacyStockFeed legacyFeed;

    public StockFeedAdapter(LegacyStockFeed legacyFeed) {

        if (legacyFeed == null) {
            throw new IllegalArgumentException("Legacy feed cannot be null");
        }

        this.legacyFeed = legacyFeed;
    }

    private Map<String, String> parsePacket(String raw) {

        Map<String, String> map = new HashMap<>();

        String[] tokens = raw.split(";");

        for (String token : tokens) {

            String[] kv = token.split("=");

            if (kv.length == 2) {
                map.put(kv[0].trim(), kv[1].trim());
            }
        }

        return map;
    }

    @Override
    public double getMarketPrice(String ticker) {

        String raw = legacyFeed.fetchRawTickerPacket(ticker);

        Map<String, String> parsed = parsePacket(raw);

        double bid = Double.parseDouble(
            parsed.getOrDefault("BID", "0.0")
        );

        double ask = Double.parseDouble(
            parsed.getOrDefault("ASK", "0.0")
        );

        return (bid + ask) / 2.0;
    }

    @Override
    public String getStatus(String ticker) {

        String raw = legacyFeed.fetchRawTickerPacket(ticker);

        return parsePacket(raw).getOrDefault("STATUS", "UNAVAILABLE");
    }
}

// ============================================
// PROXY PATTERN
// ============================================

class CachedStockProxy implements StockProvider {

    private static class CacheEntry {

        final double price;
        final long timestamp;

        CacheEntry(double price, long timestamp) {
            this.price = price;
            this.timestamp = timestamp;
        }
    }

    private final StockProvider realProvider;

    private final long ttlMillis;

    private final Map<String, CacheEntry> priceCache = new HashMap<>();

    public CachedStockProxy(StockProvider realProvider, long ttlMillis) {

        if (realProvider == null) {
            throw new IllegalArgumentException("Real provider cannot be null");
        }

        this.realProvider = realProvider;
        this.ttlMillis = ttlMillis;
    }

    @Override
    public double getMarketPrice(String ticker) {

        long now = System.currentTimeMillis();

        if (priceCache.containsKey(ticker)) {

            CacheEntry entry = priceCache.get(ticker);

            if ((now - entry.timestamp) < ttlMillis) {

                System.out.printf(
                    "[CACHE HIT] Returning cached price for %s: $%.2f (Age: %d ms)%n",
                    ticker,
                    entry.price,
                    (now - entry.timestamp)
                );

                return entry.price;
            }
        }

        System.out.printf(
            "[NETWORK FETCH] Cache miss or expired for %s. Calling upstream adapter...%n",
            ticker
        );

        double freshPrice = realProvider.getMarketPrice(ticker);

        priceCache.put(ticker, new CacheEntry(freshPrice, now));

        return freshPrice;
    }

    @Override
    public String getStatus(String ticker) {

        return realProvider.getStatus(ticker);
    }
}

// ============================================
// MAIN
// ============================================

public class Task2Runner {

    public static void main(String[] args) throws InterruptedException {

        LegacyStockFeed legacyFeed = new LegacyStockFeed();

        StockProvider adapter = new StockFeedAdapter(legacyFeed);

        StockProvider proxy = new CachedStockProxy(adapter, 1500);

        System.out.println(">>> 1. Initial Request (Cold Cache) <<<");

        System.out.printf(
            "AAPL Price: $%.2f%n",
            proxy.getMarketPrice("AAPL")
        );

        System.out.println("\n>>> 2. Immediate Repeat Request (Hot Cache) <<<");

        System.out.printf(
            "AAPL Price: $%.2f%n",
            proxy.getMarketPrice("AAPL")
        );

        System.out.println("\n>>> 3. Sleeping 1600ms to invalidate cache... <<<");

        Thread.sleep(1600);

        System.out.println(">>> 4. Subsequent Request (Expired Cache) <<<");

        System.out.printf(
            "AAPL Price: $%.2f%n",
            proxy.getMarketPrice("AAPL")
        );
    }
}
```
