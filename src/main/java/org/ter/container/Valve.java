package org.ter.container;

import org.ter.connector.Request;
import org.ter.connector.Response;

import javax.servlet.ServletException;
import java.io.IOException;

public interface Valve {
    void invoke(Request request, Response response)
            throws IOException, ServletException;
    Valve getNext();
    void setNext(Valve valve);
}
