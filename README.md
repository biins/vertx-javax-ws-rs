# vertx-javax-ws-rs


## Example
```
@Component
public class ServerVerticle extends AbstractVerticle {
    @Autowired
    private ApiController apiController;

    public void start() {
        Router router = Router.router(vertx);

        new Routes().apply(router, apiController);

        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    }
}
```
