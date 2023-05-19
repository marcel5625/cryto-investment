package com.marcel.crypto.investment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Price implements Serializable {

    @Id
    private String id;
    private LocalDate date;
    private String symbol;
    private double price;
}
