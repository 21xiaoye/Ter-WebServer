/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.ter.util;


import javax.swing.plaf.PanelUI;
import java.security.cert.CRL;

/**
 * Constants.
 *
 * @author Remy Maucherat
 */
public final class Constants {

    public static final int DEFAULT_CONNECTION_TIMEOUT = 60000;

    /**
     * CRLF.
     */
    public static final String CRLF = "\r\n";
    public static final byte[] _CRLF_BYTES = CRLF.getBytes();


    /**
     * CR.
     */
    public static final byte CR = (byte) '\r';


    /**
     * LF.
     */
    public static final byte LF = (byte) '\n';


    /**
     * SP.
     */
    public static final byte SP = (byte) ' ';
    public static final String SP_STR = " ";

    /**
     * EMPTY
     */
    public static final String EMPTY = "";


    /**
     * HT.
     */
    public static final byte HT = (byte) '\t';


    /**
     * COLON.
     */
    public static final byte COLON = (byte) ':';
    public static final String COLON_STR = ":";


    /**
     * SEMI_COLON.
     */
    public static final byte SEMI_COLON = (byte) ';';
    public static final String SEMI_COLON_STR = ";";

    /**
     * CARET_PLUS
     */
    public static final String CARET_PLUS = "^ +";

    /**
     * 'A'.
     */
    public static final byte A = (byte) 'A';
    /**
     * '0'
     */
    public static final byte ZERO = (byte) '0';


    /**
     * 'a'.
     */
    public static final byte a = (byte) 'a';


    /**
     * 'Z'.
     */
    public static final byte Z = (byte) 'Z';


    /**
     * '?'.
     */
    public static final byte QUESTION = (byte) '?';

    /**
     * ','
     */
    public static final String COMMA = ",";
    /**
     * Lower case offset.
     */
    public static final byte LC_OFFSET = A - a;
    public static final String ALL = "*";

    /* Various constant "strings" */

    public static final String CONNECTION = "Connection";
    public static final String KEEP_ALIVE_HEADER_VALUE_TOKEN = "keep-alive";
    public static final String CLOSE = "close";
    public static final String USER_AGENT = "User-Agent";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String CONTENT_LANGUAGE = "Content-Language";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String HOST = "Host";
    public static final String TRANSFER_ENCODING = "Transfer-Encoding";
    public static final String CHUNKED = "chunked";
    public static final String DATE = "Date";
    public static final byte[] ACK_BYTES = ("HTTP/1.1 100 " + CRLF + CRLF).getBytes();
    /**
     * Identity filters (input and output).
     */
    public static final int IDENTITY_FILTER = 0;


    /**
     * Chunked filters (input and output).
     */
    public static final int CHUNKED_FILTER = 1;


    /**
     * Void filters (input and output).
     */
    public static final int VOID_FILTER = 2;


    /**
     * GZIP filter (output).
     */
    public static final int GZIP_FILTER = 3;


    /**
     * Buffered filter (input)
     */
    public static final int BUFFERED_FILTER = 3;


    /**
     * HTTP/1.0.
     */
    public static final String HTTP_10 = "HTTP/1.0";

    /**
     * HTTP/1.1.
     */
    public static final String HTTP_11 = "HTTP/1.1";
    public static final byte[] _HTTP11_BYTES = HTTP_11.getBytes();
    public static final String HTTPS = "https";
    public static final String OPTIONS = "OPTIONS";
    public static final String GET = "GET";
}
