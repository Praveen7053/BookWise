package com.bookWise.util;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class BookUtils {

    public Timestamp getFilterTimestamp(String timeFilter) {
        if (timeFilter == null) return null;

        LocalDateTime now = LocalDateTime.now();

        switch (timeFilter) {
            case "today":
                return Timestamp.valueOf(now.toLocalDate().atStartOfDay());
            case "week":
                return Timestamp.valueOf(now.minusWeeks(1));
            case "month":
                return Timestamp.valueOf(now.minusMonths(1));
            case "year":
                return Timestamp.valueOf(now.minusYears(1));
            default:
                return null;
        }
    }


}
