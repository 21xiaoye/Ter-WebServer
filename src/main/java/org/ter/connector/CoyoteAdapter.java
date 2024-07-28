package org.ter.connector;

import org.ter.coyote.Adapter;
import org.ter.coyote.CoyoteRequest;
import org.ter.coyote.CoyoteResponse;

public class CoyoteAdapter implements Adapter {
    @Override
    public void service(CoyoteRequest request, CoyoteResponse response) throws Exception {
        System.out.println("处理完成......");
    }
}
