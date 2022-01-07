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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class DateTimeUtilsTest {

    // Tests for DateTimeUtils.convertDateTimeStringToDate()

    @Test
    public void testDateTimeEmpty() {
        assertNull(DateTimeUtils.parseDateTime(null));
        assertNull(DateTimeUtils.parseDateTime(""));
    }

    @Test
    public void testDateTimeZulu() {
        String dateString = "2015-01-08T10:11:12Z";
        LocalDateTime parsed = DateTimeUtils.parseDateTime(dateString);
        assertEquals(2015, parsed.getYear());
        assertEquals(1, parsed.getMonthValue());
        assertEquals(8, parsed.getDayOfMonth());
        assertEquals(10, parsed.getHour());
        assertEquals(11, parsed.getMinute());
        assertEquals(12, parsed.getSecond());
    }

    @Test
    public void testDateTimeZuluLowerZ() {
        String dateString = "2015-01-08T10:11:12z";
        LocalDateTime parsed = DateTimeUtils.parseDateTime(dateString);
        assertEquals(2015, parsed.getYear());
        assertEquals(1, parsed.getMonthValue());
        assertEquals(8, parsed.getDayOfMonth());
        assertEquals(10, parsed.getHour());
        assertEquals(11, parsed.getMinute());
        assertEquals(12, parsed.getSecond());
    }

    @Test
    public void testDateTimeZuluWithPrecision() {
        String dateString = "2015-01-08T10:11:12.123Z";
        LocalDateTime parsed = DateTimeUtils.parseDateTime(dateString);
        assertEquals(2015, parsed.getYear());
        assertEquals(1, parsed.getMonthValue());
        assertEquals(8, parsed.getDayOfMonth());
        assertEquals(10, parsed.getHour());
        assertEquals(11, parsed.getMinute());
        assertEquals(12, parsed.getSecond());
    }

    @Test
    public void testDateTimeZuluWithMilliseconds() {
        String dateString = "9999-12-30T23:59:59.9999999Z";
        LocalDateTime parsed = DateTimeUtils.parseDateTime(dateString);
        assertEquals(9999, parsed.getYear());
        assertEquals(12, parsed.getMonthValue());
        assertEquals(30, parsed.getDayOfMonth());
        assertEquals(23, parsed.getHour());
        assertEquals(59, parsed.getMinute());
        assertEquals(59, parsed.getSecond());
    }

    @Test
    public void testDateTimeWithTimeZone() {
        String dateString = "2015-01-08T10:11:12+0200";
        LocalDateTime parsed = DateTimeUtils.parseDateTime(dateString);
        assertEquals(2015, parsed.getYear());
        assertEquals(1, parsed.getMonthValue());
        assertEquals(8, parsed.getDayOfMonth());
        assertEquals(8, parsed.getHour());
        assertEquals(11, parsed.getMinute());
        assertEquals(12, parsed.getSecond());
    }

    @Test
    public void testDateTimeWithTimeZoneWithColon() {
        String dateString = "2015-01-08T10:11:12-02:00";
        LocalDateTime parsed = DateTimeUtils.parseDateTime(dateString);
        assertEquals(2015, parsed.getYear());
        assertEquals(1, parsed.getMonthValue());
        assertEquals(8, parsed.getDayOfMonth());
        assertEquals(12, parsed.getHour());
        assertEquals(11, parsed.getMinute());
        assertEquals(12, parsed.getSecond());
    }

    @Test
    public void testDateTime() {
        String dateString = "2015-01-08T10:11:12";
        LocalDateTime parsed = DateTimeUtils.parseDateTime(dateString);
        assertEquals(2015, parsed.getYear());
        assertEquals(1, parsed.getMonthValue());
        assertEquals(8, parsed.getDayOfMonth());
        assertEquals(10, parsed.getHour());
        assertEquals(11, parsed.getMinute());
        assertEquals(12, parsed.getSecond());
    }

    @Test
    public void testDateZulu() {
        String dateString = "2015-01-08Z";
        LocalDate parsed = DateTimeUtils.parseDateOnly(dateString);
        assertEquals(2015, parsed.getYear());
        assertEquals(1, parsed.getMonthValue());
        assertEquals(8, parsed.getDayOfMonth());
    }

    @Test
    public void testDateOnly() {
        String dateString = "2015-01-08";
        LocalDate parsed = DateTimeUtils.parseDateOnly(dateString);
        assertEquals(2015, parsed.getYear());
        assertEquals(1, parsed.getMonthValue());
        assertEquals(8, parsed.getDayOfMonth());
    }


    // Tests for DateTimeUtils.convertDateStringToDate()

    @Test
    public void testDateOnlyEmpty() {
        assertNull(DateTimeUtils.parseDateOnly(null));
        assertNull(DateTimeUtils.parseDateOnly(""));
    }

    @Test
    public void testDateOnlyZulu() {
        String dateString = "2015-01-08Z";
        LocalDate parsed = DateTimeUtils.parseDateOnly(dateString);
        assertEquals(2015, parsed.getYear());
        assertEquals(1, parsed.getMonthValue());
        assertEquals(8, parsed.getDayOfMonth());
    }

    @Test
    public void testDateOnlyZuluWithLowerZ() {
        String dateString = "2015-01-08z";
        LocalDate parsed = DateTimeUtils.parseDateOnly(dateString);
        assertEquals(2015, parsed.getYear());
        assertEquals(1, parsed.getMonthValue());
        assertEquals(8, parsed.getDayOfMonth());
        //assertEquals(0, calendar.get(Calendar.HOUR_OF_DAY));
        //assertEquals(0, calendar.get(Calendar.MINUTE));
        //assertEquals(0, calendar.get(Calendar.SECOND));
    }

    @Test
    public void testDateOnlyWithTimeZone() {
        String dateString = "2015-01-08+0200";
        LocalDateTime parsed = DateTimeUtils.parseDateOnly(dateString).atStartOfDay();
        //Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        //calendar.setTime(parsed);
        assertEquals(2015, parsed.getYear());
        assertEquals(1, parsed.getMonthValue());
        // TODO: fix this test! assertEquals(7, parsed.getDayOfMonth());
        // too: assertEquals(22, parsed.getHour());
        assertEquals(0, parsed.getMinute());
        assertEquals(0, parsed.getSecond());
    }

    @Test
    public void testDateOnlyWithTimeZoneWithColon() {
        String dateString = "2015-01-08-02:00";
        LocalDate parsed = DateTimeUtils.parseDateOnly(dateString);
        assertEquals(2015, parsed.getYear());
        assertEquals(1, parsed.getMonthValue());
        assertEquals(8, parsed.getDayOfMonth());

        // I'm still wondering if that's the right way to do it:
        // TODO: fix this test!
        // final LocalDateTime parsed2 = parsed.atStartOfDay().atZone(ZoneOffset.UTC).toLocalDateTime();
        // assertEquals(2, parsed2.getHour());
        // assertEquals(0, parsed2.getMinute());
        // assertEquals(0, parsed2.getSecond());
    }

    @Test
    public void testDateOnlyWithoutTimeZone() {
        String dateString = "2015-01-08";
        LocalDate parsed = DateTimeUtils.parseDateOnly(dateString);
        assertEquals(2015, parsed.getYear());
        assertEquals(1, parsed.getMonthValue());
        assertEquals(8, parsed.getDayOfMonth());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertDateStringToDateBadFormat() {
        DateTimeUtils.parseDateOnly("Monday, May, 1988");
    }


}
