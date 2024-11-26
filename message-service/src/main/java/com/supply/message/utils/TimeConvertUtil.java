package com.supply.message.utils;

import com.google.protobuf.Timestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimeConvertUtil {

    public static Timestamp convertLocalDateTimeToTimestamp(LocalDateTime localDateTime, String zoneId) {
        ZoneId zone = ZoneId.of(zoneId);
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();
    }
}
