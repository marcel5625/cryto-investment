package com.marcel.crypto.investment.event;

import com.marcel.crypto.investment.load.PriceImporter;
import com.marcel.crypto.investment.repository.MonthPriceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public final class PriceImportEvent {

    private final MonthPriceRepository monthPriceRepository;
    private final PriceImporter priceImporter;

    @EventListener(ApplicationReadyEvent.class)
    public void importPrices() {
        if (monthPriceRepository.count() > 0) {
            log.info("The prices has already been imported.");
        } else {
            priceImporter.importPrices();
        }
    }
}