/*
 * The MIT License
 * Copyright (c) 2012 Microsoft Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package microsoft.exchange.webservices.data.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

public final class DateTimeUtils {

    private static final Logger log = Logger.getLogger(DateTimeUtils.class.getCanonicalName());
    private static final Formatter[] DATE_TIME_FORMATS = createDateTimeFormats();



    private DateTimeUtils() {
        throw new UnsupportedOperationException();
    }


    /**
     * Converts a date time string to local date time.
     * <p>
     * Note: this method also allows dates without times, in which case the time will be 00:00:00 in the
     * supplied timezone. UTC timezone will be assumed if no timezone is supplied.
     *
     * @param value The string value to parse.
     * @return The parsed {@link LocalDateTime}.
     * @throws java.lang.IllegalArgumentException If string can not be parsed.
     */
    public static LocalDateTime convertDateTimeStringToDate(String value) {
        return parseDateTime(value);
    }

    /**
     * Converts a date string to local date time.
     * <p>
     * UTC timezone will be assumed if no timezone is supplied.
     *
     * @param value The string value to parse.
     * @return The parsed {@link LocalDate}.
     * @throws java.lang.IllegalArgumentException If string can not be parsed.
     */
    public static LocalDate convertDateStringToDate(String value) {
        return parseDateOnly(value);
    }

/*
    private static Date parseInternal(String value, boolean dateOnly) {
        String originalValue = value;
        if (value == null || value.isEmpty()) {
            return null;
        }
        // This seems to be an edge case. Let's upper-case the Z to be sure.
        if (value.endsWith("z")) {
            value = value.substring(0, value.length() - 1) + "Z";
        }

        for (final Formatter dateTimeFormat : DATE_TIME_FORMATS) {
            final Date parsed = dateTimeFormat.parseDate(value, dateOnly);
            if (parsed != null) {
                return parsed;
            }
        }


        throw new IllegalArgumentException(String.format("Date String %s not in valid UTC/local format for %s", originalValue, dateOnly ? "date" : "datetime"));
    }


 */
    private static Formatter[] createDateTimeFormats() {
        return new Formatter[]{
                Formatter.of(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                Formatter.of(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                Formatter.of(DateTimeFormatter.ISO_ZONED_DATE_TIME),

                Formatter.datetime("yyyy-MM-dd'T'HH:mm:ssZ"),
                Formatter.datetime("yyyy-MM-dd'T'HH:mm:ss.SSSZ"),
                Formatter.datetime("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSZ"),
                Formatter.datetime("yyyy-MM-dd'T'HH:mm:ss"),
                Formatter.datetime("yyyy-MM-dd'T'HH:mm:ss.SSS"),
                Formatter.datetime("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS"),
                Formatter.date("yyyy-MM-ddZ"),
                Formatter.date("yyyy-MM-dd")
        };
    }

    public static LocalDate parseDateOnly(final String readElementValue) {
        return null; // TODO: parse it
    }

    public static LocalDateTime parseDateTime(final String value) {
        return null; // TODO: actually parse it
    }

    public static LocalTime parseTime(final String value) {
        return null; // TODO: parse it
    }

    private static class Formatter {
        private final String pattern;
        private final DateTimeFormatter wrapped;
        private final boolean dateOnly;

        private Formatter(final String pattern, final boolean dateOnly) {
            this.pattern = pattern;
            this.wrapped = DateTimeFormatter.ofPattern(pattern); // .withZone(ZoneOffset.UTC);
            this.dateOnly = dateOnly;
        }

        public Formatter(final DateTimeFormatter wrapped, final boolean dateOnly) {
            this.pattern = wrapped.toString();
            this.wrapped = wrapped;
            this.dateOnly = dateOnly;
        }

        public static Formatter datetime(final String pattern) {
            return new Formatter(pattern, false);
        }

        public static Formatter date(final String pattern) {
            return new Formatter(pattern, true);
        }

        public static Formatter of(final DateTimeFormatter wrapped) {
            return new Formatter(wrapped, false);
        }
/*
        public Date parseDate(final String value, final boolean returnDateOnly) {
            if (dateOnly) {
                try {
                    final LocalDate retval = wrapped.parse(value, LocalDate::from);
                    return Date.from(retval.atStartOfDay().toInstant(ZoneOffset.UTC));
                    // joda: return format.parseDateTime(value).toDate();
                } catch (IllegalArgumentException | DateTimeParseException e) {
                    log.log(Level.WARNING, String.format("cannot parse '%s' via format %s (date only=%s)", value, pattern, returnDateOnly), e);
                }
            } else {
                try {
                    final LocalDateTime retval = wrapped.parse(value, LocalDateTime::from);
                    return Date.from(retval.toInstant(ZoneOffset.UTC));
                } catch (IllegalArgumentException | DateTimeParseException e) {
                    log.log(Level.WARNING, String.format("cannot parse '%s' via format %s (date only=%s)", value, pattern, returnDateOnly), e);
                }
                try {
                    final ZonedDateTime retval = wrapped.parse(value, ZonedDateTime::from);
                    return Date.from(retval.toInstant());
                    // joda: return format.parseDateTime(value).toDate();
                } catch (IllegalArgumentException | DateTimeParseException e) {
                    log.log(Level.WARNING, String.format("cannot parse '%s' via format %s (date only=%s)", value, pattern, returnDateOnly), e);
                }
            }
            return null;
        }
*/
        @Override
        public String toString() {
            return pattern;
        }
    }

}
