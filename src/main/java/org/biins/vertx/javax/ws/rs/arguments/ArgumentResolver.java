package org.biins.vertx.javax.ws.rs.arguments;

import io.vertx.ext.web.RoutingContext;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

/**
 * @author Martin Janys
 */
public interface ArgumentResolver {
    boolean canHandle(Class<?> type, Annotation[] annotations, Map<String, List<String>> httpParams, Object routingContext);
    Object handle(Class<?> type, Annotation[] annotations, Map<String, List<String>> httpParams, Object routingContext);
}
