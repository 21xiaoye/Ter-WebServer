package org.ter.util.res;

import java.text.MessageFormat;
import java.util.*;

public class StringManager {
    private static int LOCALE_CACHE_SIZE = 10;
    private final ResourceBundle bundle;
    private final Locale locale;
    private StringManager(String packageName, Locale locale){
        String bundleName = packageName + ".LocalStrings";
        ResourceBundle resourceBundle = null;
        try {
            if(locale.getLanguage().equals(Locale.ENGLISH.getLanguage())){
                locale = Locale.ROOT;
            }
            resourceBundle = ResourceBundle.getBundle(bundleName, locale);
        }catch (MissingResourceException exception){
            // 从当前线程的上下文加载器获取资源
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            if(Objects.nonNull(contextClassLoader)){
                try {
                    resourceBundle = ResourceBundle.getBundle(bundleName, locale, contextClassLoader);
                }catch (MissingResourceException exception1){

                }
            }
        }

        bundle = resourceBundle;
        if(Objects.nonNull(bundle)){
            Locale bundleLocal = bundle.getLocale();
            if(bundleLocal.equals(Locale.ROOT)){
                this.locale = Locale.ENGLISH;
            }else{
                this.locale = bundleLocal;
            }
        }else{
            this.locale = null;
        }
    }

    public String getString(String key){
        if(Objects.isNull(key)){
            throw new IllegalArgumentException("key may not have a null value");
        }
        String str = null;
        try {
            if(Objects.nonNull(bundle)){
                str = bundle.getString(key);
            }
        }catch (MissingResourceException exception){
            str = null;
        }
        return str;
    }
    public String getString(final  String key, final  Object... args){
        String value = getString(key);
        if(Objects.isNull(value)){
            value = key;
        }
        MessageFormat messageFormat = new MessageFormat(value);
        messageFormat.setLocale(locale);
        return messageFormat.format(args, new StringBuffer(), null).toString();
    }
    public Locale getLocale() {
        return locale;
    }

    //--------------------------------------------------------------------------
    // 保证每个包、语言环境下只有一个StringManager对象
    //--------------------------------------------------------------------------

    private static final Map<String, Map<Locale, StringManager>> STRING_MAP_MAP = new HashMap<>();

    public static final StringManager getStringManager(Class<?> clazz){
        return getStringManager(clazz.getPackage().getName());
    }

    public static final StringManager getStringManager(String packageName){
        return getStringManager(packageName, Locale.getDefault());
    }

    public static final StringManager getStringManager(String packageName, Locale locale){
        Map<Locale, StringManager> map = STRING_MAP_MAP.get(packageName);
        if(Objects.isNull(map)){
            map = new LinkedHashMap<Locale, StringManager>(LOCALE_CACHE_SIZE, 0.75f, true){
                private static final long serialVersionUID = 1L;
                @Override
                protected boolean removeEldestEntry(Map.Entry<Locale, StringManager> eldest) {
                    if(size() > (LOCALE_CACHE_SIZE -1)){
                        return true;
                    }
                    return false;
                }
            };

            STRING_MAP_MAP.put(packageName, map);
        }

        StringManager stringManager = map.get(locale);
        if(Objects.isNull(stringManager)){
            stringManager = new StringManager(packageName, locale);
            map.put(locale, stringManager);
        }
        return stringManager;
    }

    public static StringManager getStringManager(String packageName, Enumeration<Locale> requestedLocales){
        while (requestedLocales.hasMoreElements()){
            Locale locale = requestedLocales.nextElement();
            StringManager result = getStringManager(packageName, locale);
            if(result.getLocale().equals(locale)){
                return result;
            }
        }
        return getStringManager(packageName);
    }
}
