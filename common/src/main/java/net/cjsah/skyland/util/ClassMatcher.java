package net.cjsah.skyland.util;

import java.util.function.Predicate;

public class ClassMatcher {
    private final Class<?> clazz;
    private final Predicate<String> predicate;

    public ClassMatcher(Class<?> clazz) {
        this.clazz = clazz;
        this.predicate = name -> true;
    }

    public ClassMatcher(Class<?> clazz, Predicate<String> predicate) {
        this.clazz = clazz;
        this.predicate = predicate;
    }

    public boolean match(Class<?> clazz, String templateName) {
        return this.clazz.isAssignableFrom(clazz) && this.predicate.test(templateName);
    }


}
