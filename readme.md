# This is an unofficial fork of Microsoft's EWS-Java-Api

## Why:

Microsoft has stopped working on the EWS-Java-API, as announced July 19th 2018. There's a new "Graph" API to replace it.
But you have to meet some very specific criteria to be able to use these new APIs: 
see https://docs.microsoft.com/en-us/graph/hybrid-rest-support for what's available right now.

Here's the end of support statement: https://developer.microsoft.com/en-us/graph/blogs/upcoming-changes-to-exchange-web-services-ews-api-for-office-365/

My problem is, my software needs to read and manipulate Exchange mails *today*, and so I'm kind of stuck with EWS for now.

Triggered by last year's "Log4Shell" issues, I've started hunting down old and superfluous dependencies in my software, 
and the EWS API pulls in quite a few old packages... that's why I'm putting in some effort to modernize the old code now.

Issues and contributions are welcome.

No packages are provided right now, but I'm thinking about it. 
If you want to use the code at the moment, simply run `mvn install` locally.

Thanks to Microsoft for releasing this code under the MIT license!

*S.E.*

## Changed from the original code:

Split into a number of packages:

* `ews-api` contains most of the original code, but no HTTP client, which needs to be created before using the ExchangeService.
* `ews-client-apache4` contains the original, Apache HTTP Components 4.x based client only.
* `ews-client-java` contains a new client based on Java's built-in HTTP client.

Since XML (javax.xml) has been removed from Java, I've added a dependency on `jakarta.xml.bind:jakarta.xml.bind-api:3.0.1`
with a runtime dependency on `com.sun.xml.bind:jaxb-impl:3.0.1`. If you prefer a different implementation, you should be
able to override this.

The package names have been changed from `microsoft.*` to `com.eischet.ews.*`. There's an actual use case for this,
because it allows me to include the old and the new package in my software at the same time, allowing my users to test
the new code more easily.

The build now uses Java 11.
