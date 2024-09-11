package com.woori.studylogin.Util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Component
public class DateTimeUtil {
    public static String timeConverter(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();


        long minutes = ChronoUnit.MINUTES.between(dateTime, now);
        if (minutes < 60) {
            return minutes + "분 전";
        }

        long hours = ChronoUnit.HOURS.between(dateTime, now);
        if (hours < 24) {
            return hours + "시간 전";
        }

        long days = ChronoUnit.DAYS.between(dateTime, now);
        if (days < 4) {  // 30일을 한 달로 간주
            return days + "일 전";
        }

        long months = ChronoUnit.MONTHS.between(dateTime, now);
        if (months < 12) {
            return months + "개월 전";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");
        return dateTime.format(formatter);
    }
}
