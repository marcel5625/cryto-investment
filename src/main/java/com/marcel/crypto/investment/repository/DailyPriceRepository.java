package com.marcel.crypto.investment.repository;

import com.marcel.crypto.investment.model.DailyPrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;

public interface DailyPriceRepository extends MongoRepository<DailyPrice, String> {

    Page<DailyPrice> findByDate(LocalDate localDate, Pageable pageable);
}