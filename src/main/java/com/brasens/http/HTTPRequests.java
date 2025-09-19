package com.brasens.http;

import com.brasens.http.objects.HttpStatusCode;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HTTPRequests {

    private static final String USER_AGENT = "Mozilla/5.0";
    private static final HttpClient httpClient = HttpClients.createDefault();

    public static HTTPResponse GET(String url, String token) throws IOException {
        HttpGet request = new HttpGet(url);
        request.addHeader("Authorization", "Bearer " + token);
        request.addHeader("User-Agent", USER_AGENT);

        return executeRequest(request);
    }

    public static HTTPResponse GET(String url, String token, String content) throws IOException {
        HttpGet request = new HttpGet(url);
        request.addHeader("Authorization", "Bearer " + token);
        request.addHeader("User-Agent", USER_AGENT);
        request.addHeader("Content-Type", "application/json");

        return executeRequest(request);
    }

    public static HTTPResponse GET(String url) throws IOException {
        HttpGet request = new HttpGet(url);
        request.addHeader("User-Agent", USER_AGENT);

        return executeRequest(request);
    }

    public static HTTPResponse POST(String url, String token, String content) throws IOException {
        HttpPost request = new HttpPost(url);
        request.addHeader("Authorization", "Bearer " + token);
        request.addHeader("User-Agent", USER_AGENT);
        request.addHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(content));

        return executeRequest(request);
    }

    public static HTTPResponse POST(String url, String token, String content, String name_content) throws IOException {
        HttpPost request = new HttpPost(url);
        request.addHeader("Authorization", "Bearer " + token);
        request.addHeader("User-Agent", USER_AGENT);
        request.setHeader("Name-Content", name_content);
        request.addHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(content));

        return executeRequest(request);
    }

    public static HTTPResponse POST(String url, String content) throws IOException {
        HttpPost request = new HttpPost(url);
        request.addHeader("User-Agent", USER_AGENT);
        request.addHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(content));

        return executeRequest(request);
    }

    public static HTTPResponse PUT(String url, String token, String content) throws IOException {
        HttpPut request = new HttpPut(url);
        request.addHeader("Authorization", "Bearer " + token);
        request.addHeader("User-Agent", USER_AGENT);
        request.addHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(content));

        return executeRequest(request);
    }

    public static HTTPResponse PUT(String url, String content) throws IOException {
        HttpPut request = new HttpPut(url);
        request.addHeader("User-Agent", USER_AGENT);
        request.addHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(content));

        return executeRequest(request);
    }

    public static HTTPResponse PATCH(String url, String token, String content) throws IOException {
        HttpPatch request = new HttpPatch(url);
        request.addHeader("Authorization", "Bearer " + token);
        request.addHeader("User-Agent", USER_AGENT);
        request.addHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(content));

        return executeRequest(request);
    }

    public static HTTPResponse PATCH(String url, String content) throws IOException {
        HttpPatch request = new HttpPatch(url);
        request.addHeader("User-Agent", USER_AGENT);
        request.addHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(content));

        return executeRequest(request);
    }

    public static HTTPResponse DELETE(String url, String token) throws IOException {
        HttpDelete request = new HttpDelete(url);
        request.addHeader("Authorization", "Bearer " + token);
        request.addHeader("User-Agent", USER_AGENT);

        return executeRequest(request);
    }

    public static HTTPResponse DELETE(String url, String token, String content) throws IOException {
        HttpDelete request = new HttpDelete(url);
        request.addHeader("Authorization", "Bearer " + token);
        request.addHeader("User-Agent", USER_AGENT);
        request.addHeader("Content-Type", "application/json");
        request.setHeader("Name-Content", content);

        return executeRequest(request);
    }

    public static HTTPResponse DELETE(String url) throws IOException {
        HttpDelete request = new HttpDelete(url);
        request.addHeader("User-Agent", USER_AGENT);

        return executeRequest(request);
    }

    private static HTTPResponse executeRequest(HttpRequestBase request) throws IOException {
        try (CloseableHttpResponse response = (CloseableHttpResponse) httpClient.execute(request)) {
            int statusCode = response.getStatusLine().getStatusCode();

            System.out.println(HttpStatusCode.fromCode(statusCode).getMessage());

            HttpEntity entity = response.getEntity();
            String responseString = null;
            if (entity != null)
                responseString = EntityUtils.toString(entity);

            return new HTTPResponse(HttpStatusCode.fromCode(statusCode), responseString);
        }
    }
}