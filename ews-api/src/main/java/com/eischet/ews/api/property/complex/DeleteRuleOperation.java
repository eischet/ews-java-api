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

package com.eischet.ews.api.property.complex;

import com.eischet.ews.api.core.EwsServiceXmlWriter;
import com.eischet.ews.api.core.EwsUtilities;
import com.eischet.ews.api.core.XmlElementNames;
import com.eischet.ews.api.core.enumeration.misc.XmlNamespace;
import com.eischet.ews.api.core.exception.service.local.ExchangeValidationException;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;

/**
 * Represents an operation to delete an existing rule.
 */
public final class DeleteRuleOperation extends RuleOperation {
    /**
     * Id of the inbox rule to delete.
     */
    private String ruleId;

    /**
     * Initializes a new instance of the
     * <see cref="DeleteRuleOperation"/> class.
     */
    public DeleteRuleOperation() {
        super();
    }

    /**
     * Initializes a new instance of the
     * <see cref="DeleteRuleOperation"/> class.
     *
     * @param ruleId The Id of the inbox rule to delete.
     */
    public DeleteRuleOperation(String ruleId) {
        super();
        this.ruleId = ruleId;
    }

    /**
     * Gets or sets the Id of the rule to delete.
     */
    public String getRuleId() {
        return this.ruleId;
    }

    public void setRuleId(String value) {
        if (this.canSetFieldValue(this.ruleId, value)) {
            this.ruleId = value;
            this.changed();
        }
    }

    /**
     * Writes elements to XML.
     *
     * @param writer the writer
     */
    @Override
    public void writeElementsToXml(EwsServiceXmlWriter writer)
            throws ExchangeXmlException {
        writer.writeElementValue(XmlNamespace.Types,
                XmlElementNames.RuleId, this.getRuleId());
    }

    /**
     * Validates this instance.
     */
    @Override
    protected void internalValidate() throws ExchangeValidationException {
        EwsUtilities.validateParam(this.ruleId, "RuleId");
    }

    /**
     * Gets the Xml element name of the DeleteRuleOperation object.
     */
    @Override
    public String getXmlElementName() {
        return XmlElementNames.DeleteRuleOperation;

    }
}
