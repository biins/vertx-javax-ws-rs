package org.biins.vertx.javax.ws.rs.route;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.ext.web.RoutingContext;
import org.biins.vertx.javax.ws.rs.arguments.ArgumentResolverRegistry;
import org.biins.vertx.javax.ws.rs.route.generic.JavaxWsRxHandlerSupport;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Martin Janys
 */
class JavaxWsRxHandler extends JavaxWsRxHandlerSupport implements Handler<RoutingContext> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final Object ws;
    private final Method method;

    public JavaxWsRxHandler(Object ws, Method method) {
        this.ws = ws;
        this.method = method;
    }

    @Override
    public void handle(RoutingContext routingContext) {
        Object[] params = createParams(method.getParameterTypes(), method.getParameterAnnotations(), toMap(routingContext.request().params()), routingContext);

        try {
            routingContext.response().end(toJson(method.invoke(ws, params)));
        } catch (IllegalAccessException | InvocationTargetException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, List<String>> toMap(MultiMap params) {
        Map<String, List<String>> map = new HashMap<>();
        for (String name : params.names()) {
            map.put(name, params.getAll(name));
        }
        return map;
    }

}
