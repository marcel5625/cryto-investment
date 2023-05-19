package com.marcel.crypto.investment.repository;

import com.marcel.crypto.investment.model.MonthPrice;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface MonthPriceRepository extends MongoRepository<MonthPrice, String> {

    Optional<MonthPrice> findBySymbolAndYearAndMonth(String crypto, int year, int month);

    List<MonthPrice> findByYearAndMonth(int year, int month, Sort sort);
}