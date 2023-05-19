package com.marcel.crypto.investment.helper;

import com.marcel.crypto.investment.load.LoadPrice;

public class PriceDataHelper {

    public static TestPriceData PRICE_1 = new TestPriceData(1641009600000L, "MDL", 20.1);
    public static TestPriceData PRICE_2 = new TestPriceData(1641020400000L, "MDL", 19.2);
    public static TestPriceData PRICE_3 = new TestPriceData(1641031200000L, "MDL", 18.7);
    public static TestPriceData PRICE_4 = new TestPriceData(1641034800000L, "MDL", 20.8);
    public static TestPriceData PRICE_5 = new TestPriceData(1643234400000L, "MDL", 7.1);
    public static TestPriceData PRICE_0 = new TestPriceData(1641081600000L, "MDL", 0);

    public static class TestPriceData implements LoadPrice {

        private final long timestamp;
        private final String symbol;
        private final double price;

        public TestPriceData(long timestamp, String symbol, double price) {
            this.timestamp = timestamp;
            this.symbol = symbol;
            this.price = price;
        }

        @Override
        public long getTimestamp() {
            return timestamp;
        }

        @Override
        public String getSymbol() {
            return symbol;
        }

        @Override
        public double getPrice() {
            return price;
        }
    }
}
