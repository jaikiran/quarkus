package io.quarkus.it.vertx;

import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.vertx.core.runtime.VertxCoreRecorder;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpVersion;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

@QuarkusTest
public class Http2PushTestCase {

    @TestHTTPResource(value = "/http2push", ssl = false)
    URL url;

    /**
     * Tests that a servlet can use a {@link javax.servlet.http.PushBuilder} to do a server push
     *
     * @throws Exception
     */
    @Test
    public void testPush() throws Exception {
        final WebClientOptions options = new WebClientOptions()
                .setProtocolVersion(HttpVersion.HTTP_2)
                .setHttp2ClearTextUpgrade(true);
        final WebClient client = WebClient.create(VertxCoreRecorder.getVertx().get(), options);
        final String targetUri = "/http2push";
        final CompletableFuture<HttpResponse<Buffer>> result = sendRequest(client, targetUri);
        final HttpResponse<Buffer> httpResponse = result.get(10, TimeUnit.SECONDS);
        Assertions.assertEquals(200, httpResponse.statusCode(), "Unexpected HTTP response code");
        Assertions.assertTrue(httpResponse.bodyAsString("UTF-8").contains("Hello World!!!"), "Unexpected response content");
    }

    private CompletableFuture<HttpResponse<Buffer>> sendRequest(final WebClient client, final String path) {
        final CompletableFuture<HttpResponse<Buffer>> result = new CompletableFuture<>();
        client.get(url.getPort(), url.getHost(), path)
                .send(ar -> {
                    if (ar.succeeded()) {
                        // Obtain response
                        HttpResponse<Buffer> response = ar.result();
                        result.complete(response);
                    } else {
                        result.completeExceptionally(ar.cause());
                    }
                });
        return result;
    }
}
