package org.ter.container;

import jakarta.servlet.ServletException;
import org.ter.connector.Request;
import org.ter.connector.Response;

import java.io.IOException;

public interface Valve {
    void invoke(Request request, Response response)
            throws IOException, ServletException;
}
