# webflux study

## client

client 有两个接口，一个 `getTest` 和 `postTest`。

### getTest

method: get

|  |                        |
| ------ | ------------------------- |
| method | get                       |
| 参数   | key: 任意字符串           |
| header | sign: MD5(参数.key)       |
| 返回值 | checkResult: 签名是否正确 |

### postTest

|  |                        |
| ------ | ------------------------- |
| method | post                       |
| 参数   | 任意字符串           |
| header | sign: MD5(参数)      |
| 返回值 | checkResult: 签名是否正确 |

## gateway

`gateway` 作为网关，不仅可以做路由分发，还可以对请求进行拦截和篡改，本项目实现了一个生成`sign`请求头的`demo`。

### 配置文件

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: test
          uri: http://127.0.0.1:8080
          predicates:
            - Path=/**
```

### 拦截器

```java
public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    if(exchange.getRequest().getMethod() == HttpMethod.GET) {
        return handleGet(exchange, chain);
    }
    if(exchange.getRequest().getMethod() == HttpMethod.POST) {
        return handlePost(exchange, chain);
    }
    return chain.filter(exchange);
}
```

拦截`get`请求

```java
private Mono<Void> handleGet(ServerWebExchange exchange, GatewayFilterChain chain) {
    String key = exchange.getRequest().getQueryParams().getFirst("key");
    assert key != null;
    String mySign = DigestUtils.md5DigestAsHex(key.getBytes(StandardCharsets.UTF_8));
    exchange = exchange.mutate().request(exchange.getRequest().mutate().header("sign", mySign).build()).build();
    return chain.filter(exchange);
}
```

拦截`post`请求

```java
private Mono<Void> handlePost(ServerWebExchange exchange, GatewayFilterChain chain) {
    AtomicReference<String> body = new AtomicReference<>();
     return exchange.getRequest().getBody().map(item -> {
         String data = item.toString(StandardCharsets.UTF_8);
         body.set(data);
         return data;
     }).then(Mono.defer(() -> {
         DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
         DataBuffer dataBuffer = dataBufferFactory.wrap(body.get().getBytes(StandardCharsets.UTF_8));
         ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(
                 exchange.getRequest()) {
             @Override
             public Flux<DataBuffer> getBody() {
                 return Flux.just(dataBuffer);
             }
         };
         String mySign = DigestUtils.md5DigestAsHex(body.get().getBytes(StandardCharsets.UTF_8));
         ServerWebExchange exchange1 = exchange.mutate().request(mutatedRequest).build();
         ServerWebExchange exchange2 = exchange1.mutate().request(exchange1.getRequest().mutate().header("sign", mySign).build()).build();
         return chain.filter(exchange2);
     })).then();
}
```