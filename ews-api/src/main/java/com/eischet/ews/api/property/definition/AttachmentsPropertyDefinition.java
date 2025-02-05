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

package com.eischet.ews.api.property.definition;

import com.eischet.ews.api.core.XmlElementNames;
import com.eischet.ews.api.core.enumeration.misc.ExchangeVersion;
import com.eischet.ews.api.core.enumeration.property.PropertyDefinitionFlags;
import com.eischet.ews.api.property.complex.AttachmentCollection;
import com.eischet.ews.api.property.complex.ICreateComplexPropertyDelegate;

import java.util.EnumSet;

/**
 * Represents base Attachments property type.
 */
public final class AttachmentsPropertyDefinition extends ComplexPropertyDefinition<AttachmentCollection> {

    private static final EnumSet<PropertyDefinitionFlags> Exchange2010SP2PropertyDefinitionFlags = EnumSet
            .of(PropertyDefinitionFlags.AutoInstantiateOnRead,
                    PropertyDefinitionFlags.CanSet,
                    PropertyDefinitionFlags.ReuseInstance,
                    PropertyDefinitionFlags.UpdateCollectionItems);

    public AttachmentsPropertyDefinition() {
        super(null, XmlElementNames.Attachments, "item:Attachments",
                EnumSet
                        .of(PropertyDefinitionFlags.AutoInstantiateOnRead),
                ExchangeVersion.Exchange2007_SP1,
                new ICreateComplexPropertyDelegate<AttachmentCollection>() {
                    public AttachmentCollection createComplexProperty() {
                        return new AttachmentCollection();
                    }
                });

    }

    /**
     * Determines whether the specified flag is set.
     *
     * @param flag    The flag.
     * @param version Requested version.
     * @return true/false if the specified flag is set,otherwise false.
     */
    @Override
    public boolean hasFlag(PropertyDefinitionFlags flag, ExchangeVersion version) {
        if (version != null
                && this.getVersion()
                .compareTo(ExchangeVersion.Exchange2010_SP2) >= 0) {
            return AttachmentsPropertyDefinition.Exchange2010SP2PropertyDefinitionFlags
                    .contains(flag);
        }
        return super.hasFlag(flag, version);
    }

}
