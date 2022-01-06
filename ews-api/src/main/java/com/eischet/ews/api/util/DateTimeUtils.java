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

package com.eischet.ews.api.util;

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


    private static Formatter[] createDateTimeFormats() {
        return new Formatter[]{
                Formatter.of(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                Formatter.of(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                Formatter.of(DateTimeFormatter.ISO_ZONED_DATE_TIME),
                Formatter.of(DateTimeFormatter.ISO_LOCAL_DATE),
                Formatter.of(DateTimeFormatter.ISO_DATE),

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

    public static LocalDate parseDateOnly(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        if (value.endsWith("Z")) {
            value = value.substring(0, value.length() - 1);
        }
        for (final Formatter dateTimeFormat : DATE_TIME_FORMATS) {
            LocalDate result = dateTimeFormat.parseLocalDate(value);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    public static LocalDateTime parseDateTime(final String value) {
        for (final Formatter dateTimeFormat : DATE_TIME_FORMATS) {
            LocalDateTime result = dateTimeFormat.parseLocalDateTime(value);
            if (result != null) {
                return result;
            }
        }
        return null;
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

        public LocalDate parseLocalDate(final String value) {
            if (dateOnly) {
                try {
                    return wrapped.parse(value, LocalDate::from);
                } catch (RuntimeException e) {
                    log.warning(() -> String.format("cannot parse value '%s' with pattern '%s' (%s) as LocalDate", value, pattern, e.getMessage()));
                    return null;
                }
            } else {
                return null;
            }
        }


        public LocalDateTime parseLocalDateTime(final String value) {
            try {
                return wrapped.parse(value, LocalDateTime::from);
            } catch (RuntimeException e) {
                log.warning(() -> String.format("cannot parse value '%s' with pattern '%s' (%s) as LocalDate", value, pattern, e.getMessage()));
                return null;
            }
        }
    }

}
