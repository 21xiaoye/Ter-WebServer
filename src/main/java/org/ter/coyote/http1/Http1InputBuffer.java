package org.ter.coyote.http1;

import org.ter.coyote.InputBuffer;
import org.ter.coyote.Request;
import org.ter.coyote.http1.filter.InputFilter;
import org.ter.coyote.http1.filter.OutputFilter;
import org.ter.util.net.wrapper.SocketWrapperBase;

public class Http1InputBuffer implements InputBuffer {
    protected Request request;
    protected SocketWrapperBase<?> socketWrapper;
    protected OutputFilter[] filterLibrary;
    protected OutputFilter[] activeFilters;
    private int lastFilter;
    public Http1InputBuffer(Request request){
        this.request = request;
        this.filterLibrary = new InputFilter[0];
        this.activeFilters = new InputFilter[0];
        this.lastFilter = -1;
    }
    public void init(SocketWrapperBase<?> socketWrapper){
        this.socketWrapper = socketWrapper;
    }
}
