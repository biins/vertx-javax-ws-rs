package org.biins.vertx.javax.ws.rs.route.generic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.biins.vertx.javax.ws.rs.arguments.ArgumentResolverRegistry;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

/**
 * @author Martin Janys
 */
abstract public class JavaxWsRxHandlerSupport {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    protected Object[] createParams(Class<?>[] parameterTypes, Annotation[][] parameterAnnotations, Map<String,List<String>> httpParams, Object routingContext) {
        Object[] params = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            Object resolved = ArgumentResolverRegistry.resolve(parameterTypes[i], parameterAnnotations[i], httpParams, routingContext);
            params[i] = resolved;
        }

        return params;
    }

    protected String toJson(Object obj) throws JsonProcessingException {
        return MAPPER.writeValueAsString(obj);
    }

}
