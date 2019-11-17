package cn.zyj.tunnel.demo;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.pattern.Patterns;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.CompletionStage;

@Slf4j
public class PingActor extends AbstractActor {

    private final ActorRef pongActorRef;

    public PingActor(String pongActorPath) {
        this.pongActorRef = getContext().actorFor(pongActorPath);
    }

    @Override
    public Receive createReceive() {
//        final ActorRef pongActorRef = getContext().actorFor("../pongActor");
        return receiveBuilder()
                .matchEquals("ask", it -> {
                    final CompletionStage<Object> ask = Patterns.ask(pongActorRef, "ask:" + Math.random(), Duration.ofMillis(10));
                    ask.thenAccept(resp -> log.info("receive ask resp:" + resp));

                })
                .matchEquals("ping", it -> {
                    log.info("receive " + it);
                    pongActorRef.tell("ping", getSelf());
                })
                .matchAny(it -> log.info("receive {}", it))
                .build();
    }
}
