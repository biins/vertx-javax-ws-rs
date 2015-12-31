package org.biins.vertx.javax.ws.rs.arguments;

import org.biins.vertx.utils.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Martin Janys
 */
public class ResponseArgumentResolver implements ArgumentResolver {

    private static final String RESPONSE_METHOD = "response";
    private static final String HTTP_RESPONSE_CLASS = "io.vertx.core.http.HttpServerResponse";
    private static final String RX_HTTP_RESPONSE_CLASS = "io.vertx.rxjava.core.http.HttpServerResponse";
    private static final Set<Class<?>> RESPONSE_CLASSES = new HashSet<>();

    static {
        if (ClassUtils.isPresent(HTTP_RESPONSE_CLASS)) {
            RESPONSE_CLASSES.add(ClassUtils.getClass(HTTP_RESPONSE_CLASS));
        }
        if (ClassUtils.isPresent(RX_HTTP_RESPONSE_CLASS)) {
            RESPONSE_CLASSES.add(ClassUtils.getClass(RX_HTTP_RESPONSE_CLASS));
        }
    }

    @Override
    public boolean canHandle(Class<?> type, Annotation[] annotations, Map<String, List<String>> httpParams, Object routingContext) {
        return RESPONSE_CLASSES.contains(type);
    }

    @Override
    public Object handle(Class<?> type, Annotation[] annotations, Map<String, List<String>> httpParams, Object routingContext) {
        try {
            return routingContext.getClass().getMethod(RESPONSE_METHOD).invoke(routingContext);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
