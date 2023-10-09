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

package com.eischet.ews.api.core;

import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;
import com.eischet.ews.api.core.service.folder.*;
import com.eischet.ews.api.core.service.item.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class EwsUtilitiesTest {

  @Test
  public void testGetBuildVersion() {
    assertEquals("Build version must be 0s", "0.0.0.0", EwsUtilities.getBuildVersion());
  }

  @Test
  public void testGetItemTypeFromXmlElementName() {
    assertEquals(Task.class, EwsUtilities.getItemTypeFromXmlElementName("Task"));
    assertEquals(EmailMessage.class, EwsUtilities.getItemTypeFromXmlElementName("Message"));
    assertEquals(PostItem.class, EwsUtilities.getItemTypeFromXmlElementName("PostItem"));
    assertEquals(SearchFolder.class, EwsUtilities.getItemTypeFromXmlElementName("SearchFolder"));
    assertEquals(Conversation.class, EwsUtilities.getItemTypeFromXmlElementName("Conversation"));
    assertEquals(Folder.class, EwsUtilities.getItemTypeFromXmlElementName("Folder"));
    assertEquals(CalendarFolder.class, EwsUtilities.getItemTypeFromXmlElementName("CalendarFolder"));
    assertEquals(MeetingMessage.class, EwsUtilities.getItemTypeFromXmlElementName("MeetingMessage"));
    assertEquals(Contact.class, EwsUtilities.getItemTypeFromXmlElementName("Contact"));
    assertEquals(Item.class, EwsUtilities.getItemTypeFromXmlElementName("Item"));
    assertEquals(Appointment.class, EwsUtilities.getItemTypeFromXmlElementName("CalendarItem"));
    assertEquals(ContactsFolder.class, EwsUtilities.getItemTypeFromXmlElementName("ContactsFolder"));
    assertEquals(MeetingRequest.class, EwsUtilities.getItemTypeFromXmlElementName("MeetingRequest"));
    assertEquals(TasksFolder.class, EwsUtilities.getItemTypeFromXmlElementName("TasksFolder"));
    assertEquals(MeetingCancellation.class, EwsUtilities.getItemTypeFromXmlElementName("MeetingCancellation"));
    assertEquals(MeetingResponse.class, EwsUtilities.getItemTypeFromXmlElementName("MeetingResponse"));
    assertEquals(ContactGroup.class, EwsUtilities.getItemTypeFromXmlElementName("DistributionList"));
  }

  @Test
  public void testEwsAssert() {
    EwsUtilities.ewsAssert(true, null, null);

    try {
      EwsUtilities.ewsAssert(false, "a", "b");
    } catch (final RuntimeException ex) {
      assertEquals("[a] b", ex.getMessage());
    }
  }

  @Test
  public void testParseBigInt() throws ExchangeXmlException {
    assertEquals(BigInteger.TEN, EwsUtilities.parse(BigInteger.class, BigInteger.TEN.toString()));
  }

  @Test
  public void testParseBigDec() throws ExchangeXmlException {
    assertEquals(BigDecimal.TEN, EwsUtilities.parse(BigDecimal.class, BigDecimal.TEN.toString()));
  }

  @Test
  public void testParseString() throws ExchangeXmlException {
    final String input = "lorem ipsum dolor sit amet";
    assertEquals(input, EwsUtilities.parse(input.getClass(), input));
  }

  @Test
  public void testParseDouble() throws ExchangeXmlException {
    Double input = Double.MAX_VALUE;
    assertEquals(input, EwsUtilities.parse(input.getClass(), input.toString()));

    input = 0.0;
    assertEquals(input, EwsUtilities.parse(input.getClass(), input.toString()));

    input = Double.MIN_VALUE;
    assertEquals(input, EwsUtilities.parse(input.getClass(), input.toString()));
  }

  @Test
  public void testParseInteger() throws ExchangeXmlException {
    Integer input = Integer.MAX_VALUE;
    assertEquals(input, EwsUtilities.parse(input.getClass(), input.toString()));

    input = 0;
    assertEquals(input, EwsUtilities.parse(input.getClass(), input.toString()));

    input = Integer.MIN_VALUE;
    assertEquals(input, EwsUtilities.parse(input.getClass(), input.toString()));
  }

  @Test
  public void testParseBoolean() throws ExchangeXmlException {
    Boolean input = Boolean.TRUE;
    assertEquals(input, EwsUtilities.parse(input.getClass(), input.toString()));

    input = Boolean.FALSE;
    assertEquals(input, EwsUtilities.parse(input.getClass(), input.toString()));
  }

  @Test
  public void testParseLong() throws ExchangeXmlException {
    Long input = Long.MAX_VALUE;
    assertEquals(input, EwsUtilities.parse(input.getClass(), input.toString()));

    input = 0L;
    assertEquals(input, EwsUtilities.parse(input.getClass(), input.toString()));

    input = Long.MIN_VALUE;
    assertEquals(input, EwsUtilities.parse(input.getClass(), input.toString()));
  }

  @Test
  public void testParseFloat() throws ExchangeXmlException {
    Float input = Float.MAX_VALUE;
    assertEquals(input, EwsUtilities.parse(input.getClass(), input.toString()));

    input = 0f;
    assertEquals(input, EwsUtilities.parse(input.getClass(), input.toString()));

    input = Float.MIN_VALUE;
    assertEquals(input, EwsUtilities.parse(input.getClass(), input.toString()));
  }

  @Test
  public void testParseShort() throws ExchangeXmlException {
    Short input = Short.MAX_VALUE;
    assertEquals(input, EwsUtilities.parse(input.getClass(), input.toString()));

    input = 0;
    assertEquals(input, EwsUtilities.parse(input.getClass(), input.toString()));

    input = Short.MIN_VALUE;
    assertEquals(input, EwsUtilities.parse(input.getClass(), input.toString()));
  }

  @Test
  public void testParseByte() throws ExchangeXmlException {
    Byte input = Byte.MAX_VALUE;
    assertEquals(input, EwsUtilities.parse(input.getClass(), input.toString()));

    input = 0;
    assertEquals(input, EwsUtilities.parse(input.getClass(), input.toString()));

    input = Byte.MIN_VALUE;
    assertEquals(input, EwsUtilities.parse(input.getClass(), input.toString()));
  }

  @Test
  public void testParseDate() throws ExchangeXmlException {
    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    String input = sdf.format(new Date());
    assertEquals(input, EwsUtilities.parse(input.getClass(), input));
  }

  @Test
  public void testParseNullValue() throws ExchangeXmlException {
    final String input = null;
    assertEquals(input, EwsUtilities.parse(String.class, input));
  }

}
