package com.marcel.crypto.investment.load.csv;

import org.junit.jupiter.api.Test;

import static java.util.stream.StreamSupport.stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class CsvImporterTest {

    @Test
    void getIterator() {
        var importer = new CsvImporter();

        var iterator = importer.getIterator("csv/prices/ETH_values.csv");

        assertThat(stream(iterator.spliterator(), false).count()).isEqualTo(95L);
    }

    @Test
    void getIterator_resourceNotFund() {
        var importer = new CsvImporter();

        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> importer.getIterator("csv/prices/not-found.csv"))
                .withMessage("File csv/prices/not-found.csv not found.");
    }
}