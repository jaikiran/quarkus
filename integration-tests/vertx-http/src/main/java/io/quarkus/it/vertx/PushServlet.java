package io.quarkus.it.vertx;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.PushBuilder;

/**
 *
 */
@WebServlet("/http2push")
public class PushServlet extends HttpServlet {

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final PushBuilder pushBuilder = req.newPushBuilder();
        if (pushBuilder == null) {
            throw new IllegalStateException("HTTP2 Push isn't supported");
        }
        pushBuilder.path("http2test-style.css").push();
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.getWriter().println("Hello World!!!");
    }
}
