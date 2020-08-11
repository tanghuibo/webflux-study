package io.githug.tanghuibo.webfluxstudy.gateway.filter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.*;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author tanghuibo
 * @date 2020/8/9上午1:11
 */
@Component
public class MyGlobalFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if(exchange.getRequest().getMethod() == HttpMethod.GET) {
            return handleGet(exchange, chain);
        }

        if(exchange.getRequest().getMethod() == HttpMethod.POST) {
            return handlePost(exchange, chain);
        }

        return chain.filter(exchange);

    }


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

    private Mono<Void> handleGet(ServerWebExchange exchange, GatewayFilterChain chain) {
        String key = exchange.getRequest().getQueryParams().getFirst("key");
        assert key != null;
        String mySign = DigestUtils.md5DigestAsHex(key.getBytes(StandardCharsets.UTF_8));
        exchange = exchange.mutate().request(exchange.getRequest().mutate().header("sign", mySign).build()).build();
        return chain.filter(exchange);
    }
}
