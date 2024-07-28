package org.ter.coyote;

import org.ter.coyote.http11.Constants;

import java.util.Objects;

public class HttpParse {
    /**
     * HTTP标头值是否包含指定属性值，例如 “Container"可能包含” keep-alive, Upgrade“多个值，
     * 从给定的标头值当中判断是否有指定属性值存在
     *
     * @param headerValue HTTP 请求标头值
     * @param fieldValue 需要查找的属性值
     * @return true存在， false不存在
     */
    public static boolean containsFieldValue(String headerValue, String fieldValue){
        if(Objects.isNull(headerValue) || Objects.isNull(fieldValue)){
            return false;
        }
        String[] split = headerValue.split(Constants.COMMA);
        for (String value: split) {
            if(value.equals(fieldValue)){
                return true;
            }
        }
        return false;
    }
}
