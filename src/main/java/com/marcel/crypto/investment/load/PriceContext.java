package com.marcel.crypto.investment.load;

import com.marcel.crypto.investment.load.csv.row.PriceRow;
import com.marcel.crypto.investment.model.DailyPrice;
import com.marcel.crypto.investment.model.MonthPrice;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static com.marcel.crypto.investment.util.TimeUtils.toLocalDate;
import static java.math.BigDecimal.ZERO;
import static java.math.MathContext.DECIMAL128;
import static java.util.UUID.randomUUID;
import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public final class PriceContext {

    private final long timestamp;
    private final String symbol;
    private BigDecimal min;
    private BigDecimal max;
    private BigDecimal oldest;
    private BigDecimal newest;
    private final Map<LocalDate, List<BigDecimal>> dailyPrices;

    public static PriceContext of(LoadPrice row) {
        return new PriceContext(
                row.getTimestamp(),
                row.getSymbol(),
                BigDecimal.valueOf(row.getPrice()),
                BigDecimal.valueOf(row.getPrice()),
                BigDecimal.valueOf(row.getPrice()),
                BigDecimal.valueOf(row.getPrice()),
                new HashMap<>()
        );
    }

    public void apply(LoadPrice price) {
        var value = BigDecimal.valueOf(price.getPrice());
        this.min = value.compareTo(min) < 0 ? value : min;
        this.max = value.compareTo(max) > 0 ? value : max;
        this.oldest = price.getTimestamp() < timestamp ? value : oldest;
        this.newest = price.getTimestamp() > timestamp ? value : newest;
        applyDaily(price.getTimestamp(), value);
    }

    public MonthPrice toMonthPrice() {
        var date = toLocalDate(timestamp);

        return MonthPrice.builder()
                .id(randomUUID().toString())
                .symbol(symbol)
                .min(min)
                .max(max)
                .oldest(oldest)
                .newest(newest)
                .normalize(normalize())
                .year(date.getYear())
                .month(date.getMonth().getValue())
                .build();
    }

    public List<DailyPrice> toDailyPrices() {
        return dailyPrices.entrySet().stream()
                .map(dailyPrice -> buildDailyPrices(dailyPrice.getKey(), dailyPrice.getValue()))
                .toList();
    }

    private DailyPrice buildDailyPrices(LocalDate date, List<BigDecimal> dailyPrices) {
        return DailyPrice.builder()
                .id(randomUUID().toString())
                .symbol(symbol)
                .date(date)
                .normalize(normalize(dailyPrices))
                .build();
    }

    private BigDecimal normalize() {
        return normalize(this.min, this.max);
    }

    private BigDecimal normalize(List<BigDecimal> prices) {
        var min = prices.stream().min(Comparator.comparing(p -> p));
        var max = prices.stream().max(Comparator.comparing(p -> p));

        return normalize(min.orElse(ZERO), max.orElse(ZERO));
    }

    private BigDecimal normalize(BigDecimal min, BigDecimal max) {
        if (min.compareTo(ZERO) == 0) {
            return min;
        }

        return max.subtract(min).divide(min, DECIMAL128);
    }

    private void applyDaily(long timestamp, BigDecimal value) {
        var date = toLocalDate(timestamp);
        if (!this.dailyPrices.containsKey(date)) {
            this.dailyPrices.put(date, new ArrayList<>());
        }
        this.dailyPrices.get(date).add(value);
    }
}