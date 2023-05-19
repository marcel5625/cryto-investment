package com.marcel.crypto.investment.load.csv;

import com.marcel.crypto.investment.load.LoadPrice;
import com.marcel.crypto.investment.load.csv.row.PriceRow;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.IterableCSVToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;

import static com.opencsv.CSVParser.DEFAULT_QUOTE_CHARACTER;
import static com.opencsv.CSVParser.DEFAULT_SEPARATOR;

@Component
@Slf4j
public class CsvImporter {

    public Iterable<? extends LoadPrice> getIterator(String resource) {
        try {
            return new IterableCSVToBeanBuilder<PriceRow>()
                    .withReader(buildReader(resource))
                    .withMapper(buildMappingStrategy())
                    .build();
        } catch (FileNotFoundException e) {
            log.error("File {} not found.", resource);
            throw new RuntimeException("File " + resource + " not found.");
        }
    }

    private CSVReader buildReader(String resource) throws FileNotFoundException {
        var classLoader = getClass().getClassLoader();
        var _resource = classLoader.getResource(resource);

        return new CSVReader(
                new FileReader(_resource == null ? resource : _resource.getFile()),
                DEFAULT_SEPARATOR,
                DEFAULT_QUOTE_CHARACTER,
                1);
    }

    private static ColumnPositionMappingStrategy<PriceRow> buildMappingStrategy() {
        var strategy = new ColumnPositionMappingStrategy<PriceRow>();
        strategy.setType(PriceRow.class);
        return strategy;
    }
}