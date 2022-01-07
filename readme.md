# UNOFFICAL FORK

## Why:

Microsoft has stopped working on the EWS-Java-API, as announced July 19th 2018. There's a new "Graph" API to replace it.
But you have to meet some very specific criteria to be able to use these new APIs: 
see https://docs.microsoft.com/en-us/graph/hybrid-rest-support for what's available right now.

Here's the end of support statement: https://developer.microsoft.com/en-us/graph/blogs/upcoming-changes-to-exchange-web-services-ews-api-for-office-365/

My problem is, my software needs to read and manipulate Exchange mails *today*, and so I'm kind of stuck with EWS for now.

Triggered by last year's "Log4Shell" issues, I've started hunting down old and superfluous dependencies in my software, 
and the EWS API pulls in quite a few old packages... that's why I'm putting in some effort to modernize the old code now.

Thanks to Microsoft for releasing this code under the MIT license!

Issues and contributions are welcome.

No packages are provided right now, but I'm thinking about it. 
If you want to use the code at the moment, simply run `mvn install` locally.


S.E.

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


# OLD INFO:

## Getting started resources

Please see the [Getting Started Guide](https://github.com/OfficeDev/ews-java-api/wiki/Getting-Started-Guide) on our wiki for an introduction to this library.

## Using the library
Please see [this wiki-entry](https://github.com/OfficeDev/ews-java-api/wiki/Getting-Started-Guide#using-the-library) on how to include the library in your project

### Maven / Gradle
For Documentation on how to use _ews-java-api_ with maven or gradle please refer to [this section in our wiki](https://github.com/OfficeDev/ews-java-api/wiki#maven--gradle-integration). 

### Building from source
To build a JAR from the source yourself, please see [this page](https://github.com/OfficeDev/ews-java-api/wiki/Building-EWS-JAVA-API).


This project has adopted the [Microsoft Open Source Code of Conduct](https://opensource.microsoft.com/codeofconduct/). For more information, see the [Code of Conduct FAQ](https://opensource.microsoft.com/codeofconduct/faq/) or contact [opencode@microsoft.com](mailto:opencode@microsoft.com) with any additional questions or comments.
