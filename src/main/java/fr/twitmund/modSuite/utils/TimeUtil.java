package fr.twitmund.modSuite.utils;

public enum TimeUtil {
    SECOND(1000L),
    MINUTE(60000L),
    HOUR(3600000L),
    DAY(86400000L),
    WEEK(604800000L),
    MONTH(2628000000L),
    YEAR(31536000000L);

    private final long time;

    TimeUtil(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public long getMillis(long amount) {
        return time * amount;
    }

    public long toMillis() {
        return time;
    }

    public long getSeconds(long amount) {
        return time * amount / 1000;
    }

    public long getMinutes(long amount) {
        return time * amount / 60000;
    }

    public long getHours(long amount) {
        return time * amount / 3600000;
    }

    public long getDays(long amount) {
        return time * amount / 86400000;
    }

    public long getWeeks(long amount) {
        return time * amount / 604800000;
    }

    public long getMonths(long amount) {
        return time * amount / 2628000000L;
    }

    public long getYears(long amount) {
        return time * amount / 31536000000L;
    }


    /** Formats a time in milliseconds into a human-readable string.
     *
     * @param time the time in milliseconds
     * @return a formatted string representing the time
     */
    public static String formatTime(long time) {
        long years = YEAR.getYears(time);
        time -= YEAR.getMillis(years);
        long months = MONTH.getMonths(time);
        time -= MONTH.getMillis(months);
        long weeks = WEEK.getWeeks(time);
        time -= WEEK.getMillis(weeks);
        long days = DAY.getDays(time);
        time -= DAY.getMillis(days);
        long hours = HOUR.getHours(time);
        time -= HOUR.getMillis(hours);
        long minutes = MINUTE.getMinutes(time);
        time -= MINUTE.getMillis(minutes);
        long seconds = SECOND.getSeconds(time);

        StringBuilder builder = new StringBuilder();
        if (years > 0) {
            builder.append(years).append(" year").append(years > 1 ? "s" : "").append(" ");
        }
        if (months > 0) {
            builder.append(months).append(" month").append(months > 1 ? "s" : "").append(" ");
        }
        if (weeks > 0) {
            builder.append(weeks).append(" week").append(weeks > 1 ? "s" : "").append(" ");
        }
        if (days > 0) {
            builder.append(days).append(" day").append(days > 1 ? "s" : "").append(" ");
        }
        if (hours > 0) {
            builder.append(hours).append(" hour").append(hours > 1 ? "s" : "").append(" ");
        }
        if (minutes > 0) {
            builder.append(minutes).append(" minute").append(minutes > 1 ? "s" : "").append(" ");
        }
        if (seconds > 0) {
            builder.append(seconds).append(" second").append(seconds > 1 ? "s" : "").append(" ");
        }

        return builder.toString().trim();
    }

    /**
     * Parses a time in seconds and returns the current time plus the specified seconds.
     *
     * @param seconds the number of seconds to add to the current time
     * @return the future time in milliseconds
     */
    public static long parseTime(long seconds) {
        return System.currentTimeMillis() + ( seconds * 1000L);
    }

}
