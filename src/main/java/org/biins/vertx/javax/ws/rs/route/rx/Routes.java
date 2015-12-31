package org.biins.vertx.javax.ws.rs.route.rx;

import io.vertx.rxjava.ext.web.Router;
import org.biins.vertx.javax.ws.rs.route.generic.AbstractRoutes;

import java.lang.reflect.Method;

import static org.biins.vertx.utils.ClassUtils.getMethods;

/**
 * @author Martin Janys
 */
public class Routes extends AbstractRoutes {

    public void apply(Router router, Object api) {
        Class<?> cls = api.getClass();

        basePath = basePath(cls);

        Method[] methods = getMethods(cls);
        for (Method method : methods) {
            validateProduces(method);
            router.route(httpMethod(method), path(method)).handler(new JavaxWsRxHandler(api, method));
        }
    }
}
