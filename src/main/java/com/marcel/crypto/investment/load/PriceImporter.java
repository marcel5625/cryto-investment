package com.marcel.crypto.investment.load;

import com.marcel.crypto.investment.config.ApplicationProperties;
import com.marcel.crypto.investment.load.csv.CsvImporter;
import com.marcel.crypto.investment.repository.DailyPriceRepository;
import com.marcel.crypto.investment.repository.MonthPriceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PriceImporter {

    private final ApplicationProperties applicationProperties;
    //ToDo extract in separate interface DataImporter, based on type import data.
    private final CsvImporter csvImporter;
    private final MonthPriceRepository monthPriceRepository;
    private final DailyPriceRepository dailyPriceRepository;

    @Transactional
    public void importPrices() {
        applicationProperties.getPricesResources().forEach(
                resource -> {
                    log.info("Start import data from file {}.", resource);
                    importPrices(resource);
                    log.info("Finish importing data from file {}.", resource);
                }
        );
    }

    private void importPrices(String resource) {
        PriceContext context = null;

        for (var price : csvImporter.getIterator(resource)) {
            if (context == null) {
                context = PriceContext.of(price);
            }
            context.apply(price);
        }

        if (context != null) {
            monthPriceRepository.save(context.toMonthPrice());
            dailyPriceRepository.saveAll(context.toDailyPrices());
        }
    }
}