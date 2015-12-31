package org.biins.vertx.javax.ws.rs.arguments;

import javax.ws.rs.PathParam;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Martin Janys
 */
public class PathParamArgumentResolver implements ArgumentResolver {

    private PathParam getPathParamAnnotation(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (PathParam.class.isAssignableFrom(annotation.getClass()))
                return (PathParam) annotation;
        }
        return null;
    }

    @Override
    public boolean canHandle(Class<?> type, Annotation[] annotations, Map<String, List<String>> httpParams, Object routingContext) {
        return getPathParamAnnotation(annotations) != null;
    }

    @Override
    public Object handle(Class<?> type, Annotation[] annotations, Map<String, List<String>> httpParams, Object routingContext) {
        PathParam pathParam = getPathParamAnnotation(annotations);
        List<String> values = httpParams.get(pathParam.value());
        if (Collection.class.isAssignableFrom(type)) {
            return values;
        }
        else {
            if (values != null && values.size() > 0) {
                return values.get(0);
            }
        }
        return null;
    }
}
