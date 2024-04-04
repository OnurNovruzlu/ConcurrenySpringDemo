package com.example.springdemo.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class TimeUtil {

    public static String setTimezone() {
        int offsetMillis = TimeZone.getDefault().getRawOffset();

        int hours = offsetMillis / (60 * 60 * 1000);
        int minutes = (Math.abs(offsetMillis) / (1000 * 60)) % 60;
        char sign = (offsetMillis < 0) ? '-' : '+';

        return String.format("%c%02d:%02d", sign, Math.abs(hours), minutes);
    }

    public static String applyStandard(LocalDateTime data) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").format(data);
    }

}
