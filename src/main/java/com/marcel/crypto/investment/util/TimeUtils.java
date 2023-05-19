package com.marcel.crypto.investment.util;

import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.time.LocalDate;

import static java.util.TimeZone.getTimeZone;

@UtilityClass
public class TimeUtils {

    public static LocalDate toLocalDate(long timestamp) {
        return Instant.ofEpochMilli(timestamp)
                .atZone(getTimeZone("GMT").toZoneId())
                .toLocalDate();
    }
}