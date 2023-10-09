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

package com.eischet.ews.api.core.enumeration.property;

import com.eischet.ews.api.attribute.RequiredServerVersion;
import com.eischet.ews.api.core.enumeration.misc.ExchangeVersion;

/**
 * Defines well known folder names.
 */
public enum WellKnownFolderName {

    /**
     * The Calendar folder.
     */
    Calendar,

    /**
     * The Contacts folder.
     */
    Contacts,

    /**
     * The Deleted Items folder.
     */
    DeletedItems,

    /**
     * The Drafts folder.
     */
    Drafts,

    /**
     * The Inbox folder.
     */
    Inbox,

    /**
     * The Journal folder.
     */
    Journal,

    /**
     * The Notes folder.
     */
    Notes,

    /**
     * The Outbox folder.
     */
    Outbox,

    /**
     * The Sent Items folder.
     */
    SentItems,

    /**
     * The Tasks folder.
     */
    Tasks,

    /**
     * The message folder root.
     */
    MsgFolderRoot,

    /**
     * The root of the Public Folders hierarchy.
     */
    @RequiredServerVersion(version = ExchangeVersion.Exchange2007_SP1)
    PublicFoldersRoot,

    /**
     * The root of the mailbox.
     */
    Root,

    /**
     * The Junk E-mail folder.
     */
    JunkEmail,

    /**
     * The Search Folders folder, also known as the Finder folder.
     */
    SearchFolders,

    /**
     * The Voicemail folder.
     */
    VoiceMail,

    /**
     * The Dumpster 2.0 root folder.
     */
    @RequiredServerVersion(version = ExchangeVersion.Exchange2010_SP1)
    RecoverableItemsRoot,

    /**
     * The Dumpster 2.0 soft deletions folder.
     */
    @RequiredServerVersion(version = ExchangeVersion.Exchange2010_SP1)
    RecoverableItemsDeletions,

    /**
     * The Dumpster 2.0 versions folder.
     */
    @RequiredServerVersion(version = ExchangeVersion.Exchange2010_SP1)
    RecoverableItemsVersions,

    /**
     * The Dumpster 2.0 hard deletions folder.
     */
    @RequiredServerVersion(version = ExchangeVersion.Exchange2010_SP1)
    RecoverableItemsPurges,

    /**
     * The root of the archive mailbox.
     */
    @RequiredServerVersion(version = ExchangeVersion.Exchange2010_SP1)
    ArchiveRoot,

    /**
     * The message folder root in the archive mailbox.
     */
    @RequiredServerVersion(version = ExchangeVersion.Exchange2010_SP1)
    ArchiveMsgFolderRoot,

    /**
     * The Deleted Items folder in the archive mailbox.
     */
    @RequiredServerVersion(version = ExchangeVersion.Exchange2010_SP1)
    ArchiveDeletedItems,

    /**
     * The Dumpster 2.0 root folder in the archive mailbox.
     */
    @RequiredServerVersion(version = ExchangeVersion.Exchange2010_SP1)
    ArchiveRecoverableItemsRoot,

    /**
     * The Dumpster 2.0 soft deletions folder in the archive mailbox.
     */
    @RequiredServerVersion(version = ExchangeVersion.Exchange2010_SP1)
    ArchiveRecoverableItemsDeletions,

    /**
     * The Dumpster 2.0 versions folder in the archive mailbox.
     */
    @RequiredServerVersion(version = ExchangeVersion.Exchange2010_SP1)
    ArchiveRecoverableItemsVersions,

    /**
     * The Dumpster 2.0 hard deletions folder in the archive mailbox.
     */
    @RequiredServerVersion(version = ExchangeVersion.Exchange2010_SP1)
    ArchiveRecoverableItemsPurges,


    // Original pull request: https://github.com/OfficeDev/ews-java-api/pull/527/commits/70add8c4a2d910b87e1770cf732b0e079e4800b0
    SyncIssues, Conflicts, LocalFailures, ServerFailures, RecipientCache, QuickContacts, ConversationHistory, ToDoSearch



}
