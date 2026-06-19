package com.seekisle.commoncore.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 时间戳工具类
 */
public class TimestampUtil {

    /**
     * 获取当前时间戳（秒级）
     *
     * @return 当前时间戳（秒级）
     */
    public static long getCurrentSeconds() {
        return Instant.now().getEpochSecond();
    }

    /**
     * 获取当前时间戳（毫秒级）
     *
     * @return 当前时间戳（毫秒级）
     */
    public static long getCurrentMillis() {
        return Instant.now().toEpochMilli();
    }

    /**
     * 获取未来x秒的时间戳（秒级）
     *
     * @param months 月
     * @return 月份偏移
     */
    public static long getMonthsLaterSeconds(long months) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        ZonedDateTime monthsLater = now.plusMonths(months);
        return monthsLater.toEpochSecond();
    }

    /**
     * 获取未来x秒的时间戳（毫秒级）
     *
     * @param seconds 秒
     * @return 秒偏移对应时间戳
     */
    public static long getSecondsLaterMillis(long seconds) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        ZonedDateTime secondsLater = now.plusSeconds(seconds);
        return secondsLater.toInstant().toEpochMilli();
    }


    /**
     * 获取未来x月的时间戳（秒级）
     * @param seconds 秒
     * @return 时间戳
     */
    public static long getSecondsLaterSeconds(long seconds) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        ZonedDateTime secondsLater = now.plusSeconds(seconds);
        return secondsLater.toEpochSecond();
    }

    /**
     * 获取未来x月的时间戳（毫秒级）
     * @param months 月
     * @return 时间戳
     */
    public static long getMonthsLaterMillis(long months) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        ZonedDateTime monthsLater = now.plusMonths(months);
        return monthsLater.toInstant().toEpochMilli();
    }


    /**
     * 获取未来x年的时间戳（秒级）
     * @param years 年
     * @return 时间戳
     */
    public static long getYearLaterSeconds(long years) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        ZonedDateTime yearLater = now.plusMonths(years);
        return yearLater.toEpochSecond();
    }

    /**
     * 获取未来x年的时间戳（毫秒级）
     * @param years 年
     * @return 时间戳
     */
    public static long getYearLaterMillis(long years) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        ZonedDateTime yearLater = now.plusMonths(years);
        return yearLater.toInstant().toEpochMilli();
    }



    /**
     * 计算两个时间戳之间的差异（毫秒）
     *
     * @param timestamp1 时间戳1
     * @param timestamp2 时间戳2
     * @return 时间戳差异（毫秒）
     */
    public static long calculateDifferenceMillis(long timestamp1, long timestamp2) {
        return ChronoUnit.MILLIS.between(
                Instant.ofEpochMilli(timestamp1),
                Instant.ofEpochMilli(timestamp2));
    }

    /**
     * 计算两个时间戳之间的差异（秒）
     *
     * @param timestamp1 时间戳1
     * @param timestamp2 时间戳2
     * @return 时间戳差异（秒）
     */
    public static long calculateDifferenceSeconds(long timestamp1, long timestamp2) {
        return ChronoUnit.SECONDS.between(
                Instant.ofEpochSecond(timestamp1),
                Instant.ofEpochSecond(timestamp2));
    }

}
