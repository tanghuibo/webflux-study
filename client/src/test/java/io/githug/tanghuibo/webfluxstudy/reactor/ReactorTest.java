package io.githug.tanghuibo.webfluxstudy.reactor;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.function.Function;

/**
 * @author tanghuibo
 * @date 2020/8/9上午1:02
 */
public class ReactorTest {
    Logger log = LoggerFactory.getLogger(ReactorTest.class);

    @Test
    public void test() {


        Flux<String> map = Flux.just("1231").map(item -> {
            System.out.println("item: " + item);
            return item;
        });
        Mono<Void> mono = map.then();

        mono.subscribe();

    }
}
