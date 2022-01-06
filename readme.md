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

S.E.

## Changed from the original code:

* The library has been split into ews-client-apache4 and ews-api and moved to a new package namespace.
  There's an actual use case for this: it enables me to package the original AND this one into my software at the same time,
  meaning we can run tests with both clients in parallel. I'll then remove the old client once I'm sure this one works for
  my customers.
* ews-client-apache4, like the original library, depends on Apache HTTP Components 4.
  By using that dependency, you get the "classic" EWS-Java-API, but have to create the HTTP client first.
  I plan to write an alternative client soon that uses standard Java (9+) facilities to talk to Exchange.
* ews-api only depends on JAX-WS; I'm still looking into the proper (api) dependencies to use so that it pulls in less stuff.
  My goal is to have a minimal set of dependencies in the end.
* The build now uses Java 11 instead of 7/8 (we're on 17 LTS right now, so that's still old, but not ancient).


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
