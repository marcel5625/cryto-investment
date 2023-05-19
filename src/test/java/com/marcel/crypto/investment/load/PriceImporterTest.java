package com.marcel.crypto.investment.load;

import com.marcel.crypto.investment.config.ApplicationProperties;
import com.marcel.crypto.investment.load.csv.CsvImporter;
import com.marcel.crypto.investment.model.DailyPrice;
import com.marcel.crypto.investment.model.MonthPrice;
import com.marcel.crypto.investment.repository.DailyPriceRepository;
import com.marcel.crypto.investment.repository.MonthPriceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.marcel.crypto.investment.helper.PriceDataHelper.*;
import static com.marcel.crypto.investment.load.PriceContextTest.assertDailyPrices;
import static com.marcel.crypto.investment.load.PriceContextTest.assertMonthPrice;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PriceImporterTest {

    @Mock
    ApplicationProperties applicationProperties;
    @Mock
    CsvImporter csvImporter;
    @Mock
    MonthPriceRepository monthPriceRepository;
    @Mock
    DailyPriceRepository dailyPriceRepository;

    @InjectMocks
    PriceImporter priceImporter;

    @Captor
    ArgumentCaptor<List<DailyPrice>> dailyPriceArgumentCaptor;
    @Captor
    ArgumentCaptor<MonthPrice> monthPriceArgumentCaptor;

    @Test
    void importPrices() {
        when(applicationProperties.getPricesResources()).thenReturn(List.of("resource1"));
        Iterable<? extends LoadPrice> data = List.of(PRICE_1, PRICE_2, PRICE_3, PRICE_4, PRICE_5);
        doReturn(data).when(csvImporter).getIterator("resource1");

        priceImporter.importPrices();

        verify(monthPriceRepository).save(monthPriceArgumentCaptor.capture());
        verify(dailyPriceRepository).saveAll(dailyPriceArgumentCaptor.capture());

        assertMonthPrice(monthPriceArgumentCaptor.getValue());
        assertDailyPrices(dailyPriceArgumentCaptor.getValue());
    }
}