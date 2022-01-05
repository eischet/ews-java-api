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

import org.apache.commons.lang3.StringUtils;
// replaced with java.time: import org.joda.time.format.DateTimeFormat;
// replaced with java.time: import org.joda.time.format.DateTimeFormatter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

public final class DateTimeUtils {

  private static final DateTimeFormatter[] DATE_TIME_FORMATS = createDateTimeFormats();
  private static final DateTimeFormatter[] DATE_FORMATS = createDateFormats();


  private DateTimeUtils() {
    throw new UnsupportedOperationException();
  }


  /**
   * Converts a date time string to local date time.
   *
   * Note: this method also allows dates without times, in which case the time will be 00:00:00 in the
   * supplied timezone. UTC timezone will be assumed if no timezone is supplied.
   *
   * @param value The string value to parse.
   * @return The parsed {@link Date}.
   *
   * @throws java.lang.IllegalArgumentException If string can not be parsed.
   */
  public static Date convertDateTimeStringToDate(String value) {
    return parseInternal(value, false);
  }

  /**
   * Converts a date string to local date time.
   *
   * UTC timezone will be assumed if no timezone is supplied.
   *
   * @param value The string value to parse.
   * @return The parsed {@link Date}.
   *
   * @throws java.lang.IllegalArgumentException If string can not be parsed.
   */
  public static Date convertDateStringToDate(String value) {
    return parseInternal(value, true);
  }


  private static Date parseInternal(String value, boolean dateOnly) {
    String originalValue = value;

    if (StringUtils.isEmpty(value)) {
      return null;
    } else {
      if (value.endsWith("z")) {
        // This seems to be an edge case. Let's uppercase the Z to be sure.
        value = value.substring(0, value.length() - 1) + "Z";
      }

      final DateTimeFormatter[] formats = dateOnly ? DATE_FORMATS : DATE_TIME_FORMATS;
      for (final DateTimeFormatter format : formats) {
        try {
          final LocalDateTime retval = format.parse(value, LocalDateTime::from);
          return Date.from(retval.toInstant(ZoneOffset.UTC));
          // joda: return format.parseDateTime(value).toDate();
        } catch (IllegalArgumentException e) {
          // Ignore and try the next pattern.
        }
      }
    }

    throw new IllegalArgumentException(
        String.format("Date String %s not in valid UTC/local format", originalValue));
  }

  private static DateTimeFormatter[] createDateTimeFormats() {
    return new DateTimeFormatter[] {

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
    return new DateTimeFormatter[] {
        DateTimeFormatter.ofPattern("yyyy-MM-ddZ").withZone(ZoneOffset.UTC),
        DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneOffset.UTC)
    };
  }

}
