package com.example.bq.booktest;

import androidx.annotation.NonNull;

import java.util.Calendar;

public class TimeStamp {

    private static final long MIN = 60;
    private static final long HOUR = 3600;
    private static final long DAY = 24 * HOUR;
    private static final long WEEK = 7 * DAY;
    private static final long MONTH = 4 * WEEK;
    private static final long YEAR = 12 * MONTH;

    private static String toTime(@NonNull long timeStampMillis) {
        // The time in milliseconds between the timestamp and now
        long elapsedTimeMillis = (Calendar.getInstance().getTimeInMillis() - timeStampMillis);
        // The time in seconds between the timestamp and now
        long elapsedSeconds = (elapsedTimeMillis / 1000);
        if (elapsedSeconds < 0) {
            return "ERR: -" + elapsedSeconds;
        } else if (elapsedSeconds < MIN) {
            return "Just now";
        } else if (elapsedSeconds < HOUR) {
            return (elapsedSeconds / MIN) + " min ago";
        } else if (elapsedSeconds < DAY) {
            return (elapsedSeconds / HOUR) + "h ago";
        } else if (elapsedSeconds < WEEK) {
            return (elapsedSeconds / DAY) + "d ago";
        } else if (elapsedSeconds < MONTH) {
            return (elapsedSeconds / WEEK) + "w ago";
        } else if (elapsedSeconds < YEAR) {
            return (elapsedSeconds / MONTH) + " months ago";
        } else {
            return (elapsedSeconds / YEAR) + "y ago";
        }
    }

    public static String toTime(String timeStampMillis) {
        if (timeStampMillis == null || timeStampMillis.equals("")) {
            return "No Time";
        }
        return toTime(Long.parseLong(timeStampMillis));
    }
}
