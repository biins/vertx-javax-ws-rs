package org.biins.vertx.javax.ws.rs.arguments;

import io.vertx.ext.web.RoutingContext;


import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

/**
 * @author Martin Janys
 */
public class RoutingContextArgumentResolver implements ArgumentResolver {

    @Override
    public boolean canHandle(Class<?> type, Annotation[] annotations, Map<String, List<String>> httpParams, Object routingContext) {
        return RoutingContext.class.isAssignableFrom(type);
    }

    @Override
    public Object handle(Class<?> type, Annotation[] annotations, Map<String, List<String>> httpParams, Object routingContext) {
        return routingContext;
    }

}
