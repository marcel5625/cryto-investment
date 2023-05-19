package com.marcel.crypto.investment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MonthPrice implements Serializable {

    @Id
    private String id;
    private String symbol;
    private BigDecimal min;
    private BigDecimal max;
    private BigDecimal oldest;
    private BigDecimal newest;
    private BigDecimal normalize;
    private int month;
    private int year;
}