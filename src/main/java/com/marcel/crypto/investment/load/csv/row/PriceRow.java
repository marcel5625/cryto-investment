package com.marcel.crypto.investment.load.csv.row;

import com.marcel.crypto.investment.load.LoadPrice;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class PriceRow implements LoadPrice {

    @CsvBindByPosition(position = 0)
    private long timestamp;

    @CsvBindByPosition(position = 1)
    private String symbol;

    @CsvBindByPosition(position = 2)
    private double price;
}