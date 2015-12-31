package org.biins.vertx.javax.ws.rs.route.generic;

import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

import static org.biins.vertx.utils.ClassUtils.getAnnotation;
import static org.biins.vertx.utils.ClassUtils.getMethods;

/**
 * @author Martin Janys
 */
public class AbstractRoutes {

    protected static final Class<javax.ws.rs.HttpMethod> HTTP_METHOD = javax.ws.rs.HttpMethod.class;
    protected static final Class<? extends Annotation>[] HTTP_METHODS = new Class[]{PUT.class, POST.class, HEAD.class, GET.class, DELETE.class, OPTIONS.class};
    protected static final Class<Path> PATH = Path.class;
    protected static final Class<PathParam> PATH_PARAM = PathParam.class;
    protected static final Class<Produces> PRODUCES = Produces.class;

    private static final Pattern RX_PATH_VARIABLE_PATTERN = Pattern.compile("\\{([^\\}]+)\\}");

    protected String basePath = "";

    protected String basePath(Class<?> cls) {
        Path path = getAnnotation(cls, PATH);
        if (path != null) {
            return  path.value();
        }
        return "";
    }

    protected HttpMethod httpMethod(Method classMethod) {
        for (Class<? extends Annotation> httpMethod : HTTP_METHODS) {
            Annotation method = getAnnotation(classMethod, httpMethod);
            if (method != null) {
                return HttpMethod.valueOf(method.annotationType().getSimpleName());
            }
        }

        return HttpMethod.GET;
    }

    protected String path(Method method) {
        Path path = getAnnotation(method, PATH);
        if (path != null) {
            return basePath + toVertxPath(path.value());
        }
        return basePath;
    }

    private String toVertxPath(String value) {
        return RX_PATH_VARIABLE_PATTERN.matcher(value).replaceAll(":$1");
    }

    protected void validateProduces(Method method) {
        Produces produces = getAnnotation(method, Produces.class);
        if (produces != null && !produces.value()[0].equals(MediaType.APPLICATION_JSON)) {
            throw new IllegalArgumentException("Produces only jsons");
        }
    }
}
