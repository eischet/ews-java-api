# This is an unofficial fork of Microsoft's EWS-Java-API

## Why:

Microsoft has stopped working on the EWS-Java-API, as announced July 19th 2018. There's a new "Graph" API to replace it.

But you have to meet some very specific criteria to be able to use these new APIs: 
see https://docs.microsoft.com/en-us/graph/hybrid-rest-support for what's available right now.

Here's the end of support statement: https://developer.microsoft.com/en-us/graph/blogs/upcoming-changes-to-exchange-web-services-ews-api-for-office-365/

However, to use the new Graph API, you need to be in a "hybrid" setup, or in Exchange Online only.
Unfortunately, it's too early for my own users to use that new API, and I don't know if everybody will really go "hybrid" in the future.
My software needs to read and manipulate on-premise Exchange mails *today*, and so I'm kind of stuck with EWS for now.

Triggered by last year's "Log4Shell" issues, I've started hunting down old and superfluous dependencies in my software, 
and the EWS API pulls in quite a few old packages... that's why I'm putting in some effort to modernize the old code now.

Issues and contributions are welcome.

No packages are provided right now, but I'm thinking about it. 
If you want to use the code at the moment, simply run `mvn install` locally.

Thanks to Microsoft for releasing this code under the MIT license!

*S.E.*

## Version 2.1-SNAPSHOT:

The original code was split into a number of packages:

* `ews-api` contains most of the original code, but no HTTP client, which needs to be created before using the ExchangeService.
* `ews-client-apache4` contains the original, Apache HTTP Components 4.x based client only.
* `ews-client-apache5` contains a new client based on Apache HTTP Components 5.x.
* `ews-client-java` contains a new client based on Java's built-in HTTP client (which does not actually work right now because of NTLM issues)

Since XML (javax.xml) has been removed from Java, I've added a dependency on `jakarta.xml.bind:jakarta.xml.bind-api:3.0.1`
with a runtime dependency on `com.sun.xml.bind:jaxb-impl:3.0.1`. If you prefer a different implementation, you should be
able to override this.

The package names have been changed from `microsoft.*` to `com.eischet.ews.*`. There's an actual use case for this,
because it allows me to include the old and the new package in my software at the same time, allowing my users to test
the new code more easily.

The build now uses Java 11.


## Version 2.2-SNAPSHOT, 2023-10-09:

* Removed some JavaDoc comments that only stated the obvious or where even plainly wrong (copy/pasted presumably).
* Reduced the number of "throws Exception" to 5264, down from 8241 (!). That's still terrible, but it's a start.
  Added a new "ExchangeException" base class and modified the Exception hierarchy to use it.
  The near-term goal is to have two main exceptions, one for usage/XML errors and one for communication errors.
  Ideally, validation of messages sent to the server should only happen when requests are actually sent, and any
  e.g. Version checks that right now happen in the Item constructor (and cascade down to all derived classes)
  should not be there at all.


## What to use

If your Exchange system does have the Graph API, i.e. you're in a hybrid or cloud-only setup, you might as well use that.
This is the wrong project in this case, and you'll need a different client, e.g. https://github.com/microsoftgraph/msgraph-sdk-java

If you don't mind using the old Apache HTTP Components 4.x, you can use the original Microsoft package instead of this.
They do have *known security issues*, though, that the Apache Team has fixed in version 5.x.

Otherwise, use the `ews-client-apache5` package. 

This should get you started, from the original documentation:
https://github.com/OfficeDev/ews-java-api/wiki/Getting-Started-Guide#using-the-library

Creating the ExchangeService is a bit different, because you need to supply a client:

    final ApacheHttpClient client = new ApacheHttpClient();
    // configure the client...
    ExchangeService service = new ExchangeService(client, ExchangeVersion.Exchange2010_SP2);



## Future Plans

My main goal is to keep this project alive for at least a few years, but not to add significant new features myself.
I'm not going to spend significant amounts of time on this project, though.
Pull requests are welcome, though, if there's something you need to have.

There are some areas which could be improved, and I'll work on these as time permits:

* The old API throws Exception in a *lot* of places, and I hope to clean that up a bit, while keeping the general code as-is.
* Many of the JavaDocs trigger errors and/or look autogenerated, stating the obvious (e.g. int foo -- "the foo"), and should be cleaned up.
  My current favorite is the StringList::toString JavaDoc, which takes quite a lot of words to explain how generic toString works,
  but returns something entirely different.
* There's quite a lot of unused code, e.g. all methods of IAsyncResult, and some unused type parameters, that could be removed/improved.

