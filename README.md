# aws-lambda-api-gw
Java AWS Lambda to show calling another service / api

This code is an [AWS Lambda](https://aws.amazon.com/lambda/) that is meant as a demonstration for
calling another API from within the Lambda.  The API that is called is from
[icanhazdadjoke.com](https://icanhazdadjoke.com/) which provides a super simple API to interact with.  This code
assumes that it will be fronted with [API Gateway](https://aws.amazon.com/api-gateway/) and returns values based
on that assumption.

## Building
The code is built using [maven](https://maven.apache.org/) and leverages Java APIs in Java 11 and above.  To build
simply run:

```shell
mvn clean package
```

The build uses Java 21 which is the most recent Java supported by the AWS Java Lambda runtime.  It should be deployed
via the AWS console.  Yes, this is "click ops" but this code isn't meant to be one of your production Lambda's
(unless your business is to generate a stream of dad jokes).

## Runtime notes
As indicated, this code is meant to have API Gateway in front of it.  It will, however, run fine from the console or
AWS CLI for testing.  But the returned object is a
[APIGatewayProxyResponseEvent](https://javadoc.io/static/com.amazonaws/aws-lambda-java-events/3.11.4/com/amazonaws/services/lambda/runtime/events/APIGatewayProxyResponseEvent.html)
so, if called without API Gateway, you'll get a different object than expected.

Note that the very first run of this code takes about 4 seconds with a 512MB Lambda.  After that the response times are
around 200-300 ms.  The called API is the vast majority of that time.
