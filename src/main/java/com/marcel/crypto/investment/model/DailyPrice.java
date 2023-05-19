package com.marcel.crypto.investment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DailyPrice implements Serializable {

    @Id
    private String id;
    private String symbol;
    private LocalDate date;
    private BigDecimal normalize;
}