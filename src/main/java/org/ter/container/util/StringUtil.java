package org.ter.container.util;

import java.util.Objects;

public class StringUtil {
    public static String[] splitCommaSeparated(String s){
        if(Objects.isNull(s) || s.length() == 0){
            return new String[0];
        }
        String[] split = s.split(",");
        for (int i = 0; i < split.length; i++) {
            split[i] = split[i].trim();
        }
        return split;
    }
}
