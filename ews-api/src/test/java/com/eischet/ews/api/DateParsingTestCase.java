package com.eischet.ews.api;

import com.eischet.ews.api.util.DateTimeUtils;
import org.junit.Test;

import java.time.LocalDateTime;

import static junit.framework.TestCase.assertNotNull;

public class DateParsingTestCase {

    @Test
    public void parseDates() {
        final String sample = "2023-08-12T14:49:28Z";
        final LocalDateTime date = DateTimeUtils.parseDateTime(sample);
        assertNotNull(date);
        System.out.println(date);
        // TODO: add more checks -> the Z is ignored right now, because my own client does not use any date fields actually (!)
    }


}
