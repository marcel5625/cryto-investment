package com.marcel.crypto.investment.load;

public interface LoadPrice {

    long getTimestamp();

    String getSymbol();

    double getPrice();
}
