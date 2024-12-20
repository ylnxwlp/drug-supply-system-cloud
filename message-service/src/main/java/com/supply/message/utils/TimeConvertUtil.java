package com.supply.message.utils;

import com.google.protobuf.Timestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeConvertUtil {

    public static LocalDateTime convert(Timestamp timestamp) {
        Instant instant = Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("Asia/Shanghai"));
        return zonedDateTime.toLocalDateTime();
    }

    public static Timestamp convertLocalDateTimeToTimestamp(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZoneId.of("Asia/Shanghai")).toInstant();
        return Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();
    }
}