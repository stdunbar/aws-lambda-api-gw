package com.hotjoe.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("unused")
public class HttpClientLambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private static final Gson gson = new Gson();

    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {

        long startTime = System.currentTimeMillis();

        context.getLogger().log("got body\n" + gson.toJson(event));

        String contentResponse;
        HttpResponse<String> httpResponse;
        try (HttpClient httpClient = HttpClient.newBuilder().build()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .header("Accept", "application/json")
                    .uri(URI.create("https://icanhazdadjoke.com/"))
                    .build();

            httpResponse = httpClient.
                    send(request, HttpResponse.BodyHandlers.ofString());

        } catch (IOException | InterruptedException ie) {
            contentResponse = "Error calling service - " + ie.getMessage();
            context.getLogger().log(contentResponse);
            return writeResponse("{\"error\": \"" + contentResponse + "\"}", 500);
        }

        context.getLogger().log("took " + (System.currentTimeMillis() - startTime) + "ms to call API");

        APIGatewayProxyResponseEvent proxyResponseEvent = writeResponse(httpResponse.body(), 200);

        context.getLogger().log("returning " + gson.toJson(proxyResponseEvent));

        return proxyResponseEvent;
    }

    private APIGatewayProxyResponseEvent writeResponse(String jsonString, int errorCode) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("User-Agent", "https://github.com/stdunbar/aws-lambda-api-gw");

        return new APIGatewayProxyResponseEvent()
                .withHeaders(headers)
                .withBody(jsonString)
                .withIsBase64Encoded(Boolean.FALSE)
                .withStatusCode(errorCode);
    }
}
