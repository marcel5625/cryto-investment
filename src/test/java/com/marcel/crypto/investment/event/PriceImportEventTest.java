package com.marcel.crypto.investment.event;

import com.marcel.crypto.investment.load.PriceImporter;
import com.marcel.crypto.investment.repository.MonthPriceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PriceImportEventTest {

    @Mock
    MonthPriceRepository priceRepository;
    @Mock
    PriceImporter priceImporter;

    @InjectMocks
    PriceImportEvent event;

    @Test
    void importPrices() {
        when(priceRepository.count()).thenReturn(0L);

        event.importPrices();

        verify(priceImporter).importPrices();
    }

    @Test
    void importPrices_alreadyExecuted() {
        when(priceRepository.count()).thenReturn(1L);

        event.importPrices();

        verify(priceImporter, never()).importPrices();
    }
}