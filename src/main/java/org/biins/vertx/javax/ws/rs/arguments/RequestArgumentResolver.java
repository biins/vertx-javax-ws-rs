package org.biins.vertx.javax.ws.rs.arguments;

import io.netty.util.CharsetUtil;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;
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
public class RequestArgumentResolver implements ArgumentResolver {

    private static final String REQUEST_METHOD = "request";
    private static final String HTTP_REQUEST_CLASS = "io.vertx.core.http.HttpServerRequest";
    private static final String RX_HTTP_REQUEST_CLASS = "io.vertx.rxjava.core.http.HttpServerRequest";
    private static final Set<Class<?>> REQUEST_CLASSES = new HashSet<>();

    static {
        if (ClassUtils.isPresent(HTTP_REQUEST_CLASS)) {
            REQUEST_CLASSES.add(ClassUtils.getClass(HTTP_REQUEST_CLASS));
        }
        if (ClassUtils.isPresent(RX_HTTP_REQUEST_CLASS)) {
            REQUEST_CLASSES.add(ClassUtils.getClass(RX_HTTP_REQUEST_CLASS));
        }
    }

    @Override
    public boolean canHandle(Class<?> type, Annotation[] annotations, Map<String, List<String>> httpParams, Object routingContext) {
        return REQUEST_CLASSES.contains(type);
    }

    @Override
    public Object handle(Class<?> type, Annotation[] annotations, Map<String, List<String>> httpParams, Object routingContext) {
        try {
            return routingContext.getClass().getMethod(REQUEST_METHOD).invoke(routingContext);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
