package com.marcel.crypto.investment.load;

import com.marcel.crypto.investment.model.DailyPrice;
import com.marcel.crypto.investment.model.MonthPrice;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static com.marcel.crypto.investment.helper.PriceDataHelper.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PriceContextTest {

    @Test
    void of() {
        var context = PriceContext.of(PRICE_0);

        assertThat(context.toMonthPrice())
                .extracting(m -> m.getNormalize().doubleValue())
                .isEqualTo(0.0);
    }

    @Test
    void apply() {
        var context = PriceContext.of(PRICE_1);
        context.apply(PRICE_2);
        context.apply(PRICE_3);
        context.apply(PRICE_4);
        context.apply(PRICE_5);

        var monthPrice = context.toMonthPrice();

        assertMonthPrice(monthPrice);
        assertDailyPrices(context.toDailyPrices());
    }

    static void assertMonthPrice(MonthPrice monthPrice) {
        assertThat(monthPrice.getId()).isNotNull();

        assertThat(monthPrice.getMonth()).isNotNull();
        assertThat(monthPrice)
                .extracting(
                        MonthPrice::getSymbol,
                        m -> m.getMin().doubleValue(),
                        m -> m.getMax().doubleValue(),
                        m -> m.getOldest().doubleValue(),
                        m -> m.getNewest().doubleValue(),
                        m -> m.getNormalize().doubleValue(),
                        MonthPrice::getYear,
                        MonthPrice::getMonth)
                .contains(
                        "MDL",
                        7.1,
                        20.8,
                        20.1,
                        7.1,
                        1.929577464788732394366197183098592,
                        2022,
                        1
                );
    }

    static void assertDailyPrices(List<DailyPrice> dailyPrices) {
        assertThat(dailyPrices).isNotNull();
        assertThat(dailyPrices.size()).isEqualTo(2);
        assertDailyPrices(dailyPrices.get(0), "MDL", LocalDate.of(2022, Month.JANUARY, 26), 0.0);
        assertDailyPrices(dailyPrices.get(1), "MDL", LocalDate.of(2022, Month.JANUARY, 1), 0.11229946524064172);
    }

    static void assertDailyPrices(DailyPrice dailyPrice, String symbol, LocalDate date, double normalize) {
        assertThat(dailyPrice).isNotNull();
        assertThat(dailyPrice.getId()).isNotNull();
        assertThat(dailyPrice)
                .extracting(DailyPrice::getSymbol,
                        DailyPrice::getDate,
                        d -> d.getNormalize().doubleValue())
                .contains(symbol, date, normalize);
    }
}