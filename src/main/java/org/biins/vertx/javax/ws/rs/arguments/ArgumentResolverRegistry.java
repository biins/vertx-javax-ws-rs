package org.biins.vertx.javax.ws.rs.arguments;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Martin Janys
 */
public class ArgumentResolverRegistry {

    private static final List<? extends ArgumentResolver> REGISTRY = Arrays.asList(
            new RoutingContextArgumentResolver(),
            new RequestArgumentResolver(),
            new ResponseArgumentResolver(),
            new PathParamArgumentResolver());

    public static Object resolve(Class<?> type, Annotation[] annotations, Map<String, List<String>> httpParams, Object routingContext) {
        for (ArgumentResolver argumentResolver : REGISTRY) {
            if (argumentResolver.canHandle(type, annotations, httpParams, routingContext)) {
                return argumentResolver.handle(type, annotations, httpParams, routingContext);
            }
        }
        return null;
    }

}
