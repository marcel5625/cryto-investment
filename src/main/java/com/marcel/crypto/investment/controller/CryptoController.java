package com.marcel.crypto.investment.controller;

import com.marcel.crypto.investment.model.DailyPrice;
import com.marcel.crypto.investment.model.MonthPrice;
import com.marcel.crypto.investment.repository.DailyPriceRepository;
import com.marcel.crypto.investment.repository.MonthPriceRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/v1/cryptos")
@RequiredArgsConstructor
public final class CryptoController {

    private final MonthPriceRepository monthPriceRepository;
    private final DailyPriceRepository dailyPriceRepository;

    @Operation(summary = "Get list of all the cryptos sorted by normalized range desc for a specific month")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of cryptos",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MonthPrice.class))})
    })
    @GetMapping("/normalize/{year}/{month}")
    public List<MonthPrice> getNormalizeCryptos(@PathVariable int year,
                                                @PathVariable int month) {
        return monthPriceRepository.findByYearAndMonth(year,
                month,
                Sort.by(DESC, "normalize"));
    }

    @Operation(summary = "Get monthly crypto information - min, max, oldest, newest")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Monthly Crypto information was found.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MonthPrice.class))}),
            @ApiResponse(responseCode = "404", description = "Monthly Cryto information not found",
                    content = @Content)})
    @GetMapping("/{crypto}/{year}/{month}")
    public Optional<MonthPrice> getMonthlyCryptoInformation(
            @PathVariable String crypto,
            @PathVariable int year,
            @PathVariable int month) {
        return monthPriceRepository.findBySymbolAndYearAndMonth(crypto, year, month);
    }

    @Operation(summary = "Get daily crypto information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Daily crypto information was found.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DailyPrice.class))}),
            @ApiResponse(responseCode = "404", description = "Daily crypto information not found",
                    content = @Content)})
    @GetMapping("/normalize/highest/{date}")
    public Optional<DailyPrice> getDailyNormalizeCrypto(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return dailyPriceRepository.findByDate(date, PageRequest.of(0, 1, DESC, "normalize")).get().findFirst();
    }
}