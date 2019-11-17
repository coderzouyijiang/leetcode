package cn.zyj.tunnel;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import cn.zyj.tunnel.demo.PingActor;
import cn.zyj.tunnel.demo.PongActor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PongActorTest {

    @Autowired
    private ActorSystem system;

    @Test
    public void test() {
        final ActorRef pongActorRef = system.actorOf(Props.create(PongActor.class), "pongActor");
        final ActorRef pingActorRef = system.actorOf(Props.create(PingActor.class, pongActorRef.path().toString()), "pingActor");
        pingActorRef.tell("ping", ActorRef.noSender());
        pingActorRef.tell("abc", ActorRef.noSender());
        pingActorRef.tell("", ActorRef.noSender());
        pingActorRef.tell("ask", ActorRef.noSender());
        pingActorRef.tell("ask", ActorRef.noSender());
        pongActorRef.tell("hello", ActorRef.noSender());
    }
}
