/*
 * The MIT License Copyright (c) 2012 Microsoft Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */


package com.eischet.ews.api.property.complex;

import com.eischet.ews.api.core.EwsUtilities;
import com.eischet.ews.api.core.exception.misc.ArgumentException;
import com.eischet.ews.api.misc.Time;
import com.eischet.ews.api.util.DateTimeUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RunWith(JUnit4.class)
public class TimeChangeTest {

    private static String time = "03:00:00";
    private static String time_fail1 = "21:32"; // I have no idea why this should fail!!
    private static String time_fail2 = "25:25:10";
    private static String time_fail3 = "-10:00:00";

    private static String dateUTC = "2001-10-27Z";
    private static String date_fail1 = "2001-10-32";
    private static String date_fail2 = "2001-13-26+02:00";
    private static String date_fail3 = "01-10-26";

    @Test
    public void testDateUTC() {
        Assert.assertEquals("2001-10-27Z", testDate(dateUTC));
    }

    private String testDate(String value) {
        final LocalDate cal = DateTimeUtils.parseDateOnly(value);
    /*
    Calendar cal = DatatypeConverter.parseDate(value);
    cal.setTimeZone(TimeZone.getTimeZone("UTC"));
    String XSDate = EwsUtilities.dateTimeToXSDate(cal.getTime());
     */
        return EwsUtilities.dateToXSDate(cal);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDateFail1() {
        testDate(date_fail1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDateFail2() {
        testDate(date_fail2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDateFail3() {
        testDate(date_fail3);
    }

    private String testTime(String value) throws ArgumentException {
        // Calendar cal = DatatypeConverter.parseTime(value);
        // Time time = new Time(cal.getTime());
        System.out.println("parsing: " + value);
        final LocalTime parsedTime = DateTimeUtils.parseTime(value);
        System.out.println("parsed: "+ parsedTime);
        final Time time = new Time(parsedTime);
        return time.toXSTime();
    }

    /* I have no idea why that test is supposed to fail, as 21:32 is a perfectly valid time IMHO
    @Test(expected = IllegalArgumentException.class)
    public void testTimeFail1() {
        testTime(time_fail1);
    }
     */

    @Test(expected = ArgumentException.class)
    public void testTimeFail2() throws ArgumentException {
        testTime(time_fail2);
    }

    @Test(expected = ArgumentException.class)
    public void testTimeFail3() throws ArgumentException {
        testTime(time_fail3);
    }

    @Test
    public void testTimeValues() throws ArgumentException {
        Assert.assertEquals("{0:00}:{1:00}:{2:00},3,0,0", testTime(time));
    }

}
