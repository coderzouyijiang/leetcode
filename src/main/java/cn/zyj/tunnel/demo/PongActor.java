package cn.zyj.tunnel.demo;

import akka.actor.AbstractActor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PongActor extends AbstractActor {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("ping", it -> {
                    log.info("receive " + it);
                    getSender().tell("pong", getSelf());
                })
                .match(String.class, it -> {
                    if (it.startsWith("ask:")) {
                        final String content = it.substring(4);
                        log.info("receive ask:" + content);
                        getSender().tell("[" + content + "]", getSelf());
                    } else {
                        unhandled(it);
                    }
                })
                .matchAny(it -> log.info("unknow msg:{}", it))
                .build();
    }
}
