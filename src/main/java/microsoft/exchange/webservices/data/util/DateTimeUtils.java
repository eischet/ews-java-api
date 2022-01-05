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
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class DateTimeUtils {

    private static final Logger log = Logger.getLogger(DateTimeUtils.class.getCanonicalName());
    private static final DateTimeFormatter[] DATE_TIME_FORMATS = createDateTimeFormats();
    private static final DateTimeFormatter[] DATE_FORMATS = createDateFormats();


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
     * @return The parsed {@link Date}.
     * @throws java.lang.IllegalArgumentException If string can not be parsed.
     */
    public static Date convertDateTimeStringToDate(String value) {
        return parseInternal(value, false);
    }

    /**
     * Converts a date string to local date time.
     * <p>
     * UTC timezone will be assumed if no timezone is supplied.
     *
     * @param value The string value to parse.
     * @return The parsed {@link Date}.
     * @throws java.lang.IllegalArgumentException If string can not be parsed.
     */
    public static Date convertDateStringToDate(String value) {
        return parseInternal(value, true);
    }


    private static Date parseInternal(String value, boolean dateOnly) {
        String originalValue = value;

        if (value == null || value.isEmpty()) {
            return null;
        } else {
            if (value.endsWith("z") || value.endsWith("Z")) {
                // This seems to be an edge case. Let's remove the Z to be sure.
                value = value.substring(0, value.length() - 1); // + "Z";
            }

            if (dateOnly) {
                for (final DateTimeFormatter dateFormat : DATE_FORMATS) {
                    try {
                        final LocalDate retval = dateFormat.parse(value, LocalDate::from);
                        return Date.from(retval.atStartOfDay().toInstant(ZoneOffset.UTC));
                        // joda: return format.parseDateTime(value).toDate();
                    } catch (IllegalArgumentException | DateTimeParseException e) {
                        log.log(Level.WARNING, String.format("cannot parse '%s' (as '%s') via format %s", originalValue, value, dateFormat), e);
                        // Ignore and try the next pattern.
                    }
                }
            } else {
                for (final DateTimeFormatter format : DATE_TIME_FORMATS) {
                    try {
                        final ZonedDateTime retval = format.parse(value, ZonedDateTime::from);
                        return Date.from(retval.toInstant());
                        // joda: return format.parseDateTime(value).toDate();
                    } catch (IllegalArgumentException | DateTimeParseException e) {
                        log.log(Level.WARNING, String.format("cannot parse '%s' (as '%s') via format %s", originalValue, value, format), e);
                        // Ignore and try the next pattern.
                    }
                }
            }
        }

        throw new IllegalArgumentException(
                String.format("Date String %s not in valid UTC/local format", originalValue));
    }

    private static DateTimeFormatter[] createDateTimeFormats() {
        return new DateTimeFormatter[]{
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ").withZone(ZoneOffset.UTC),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ").withZone(ZoneOffset.UTC),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSZ").withZone(ZoneOffset.UTC),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").withZone(ZoneOffset.UTC),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS").withZone(ZoneOffset.UTC),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS").withZone(ZoneOffset.UTC),
                DateTimeFormatter.ofPattern("yyyy-MM-ddZ").withZone(ZoneOffset.UTC),
                DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneOffset.UTC)
        };
    }

    private static DateTimeFormatter[] createDateFormats() {
        return new DateTimeFormatter[]{
                DateTimeFormatter.ofPattern("yyyy-MM-ddZ").withZone(ZoneOffset.UTC),
                DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneOffset.UTC)
        };
    }

}
