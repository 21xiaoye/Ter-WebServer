package org.ter.container.pipeline;

import org.ter.connector.Request;
import org.ter.connector.Response;
import java.io.IOException;

public class StandardHostValve extends ValveBase {
    @Override
    public void invoke(Request request, Response response) throws IOException {
        System.out.println("host....");
    }
}
