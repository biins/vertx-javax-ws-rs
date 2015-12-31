package org.biins.vertx.javax.ws.rs.route.rx;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.Handler;
import io.vertx.rxjava.core.MultiMap;
import io.vertx.rxjava.ext.web.RoutingContext;
import org.biins.vertx.javax.ws.rs.arguments.ArgumentResolverRegistry;
import org.biins.vertx.javax.ws.rs.route.generic.JavaxWsRxHandlerSupport;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Martin Janys
 */
class JavaxWsRxHandler extends JavaxWsRxHandlerSupport implements Handler<RoutingContext> {

    private final Object ws;
    private final Method method;

    public JavaxWsRxHandler(Object ws, Method method) {
        this.ws = ws;
        this.method = method;
    }

    @Override
    public void handle(RoutingContext routingContext) {
        Class<?>[] parameterTypes = method.getParameterTypes();

        Object[] params = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            Object resolved = ArgumentResolverRegistry.resolve(parameterTypes[i],
                    method.getParameterAnnotations()[i],
                    toMap(routingContext.request().params()),
                    routingContext);
            params[i] = resolved;
        }

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
