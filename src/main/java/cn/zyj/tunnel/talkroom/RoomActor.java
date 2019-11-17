package cn.zyj.tunnel.talkroom;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.pattern.Patterns;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import scala.collection.Iterator;
import scala.collection.immutable.Iterable;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Slf4j
public class RoomActor extends BaseActor {

    @Getter
    @ToString
    @AllArgsConstructor
    @Builder
    public static class SimpleInfo implements Serializable {
        private String id;
        private String name;
    }

    @Getter
    @ToString
    @AllArgsConstructor
    @Builder
    public static class Say implements Serializable {
        private String userId;
        private String content;
    }

    @Getter
    @ToString
    @AllArgsConstructor
    @Builder
    public static class AgreeInviteMsg implements Serializable {
        private String userId;
    }

    public RoomActor(String id, String name) {
        this.id = id;
        this.name = name;
    }

    private String id;
    private String name;

    /*
    private Map<String, ActorRef> members;
    */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("getSimpleInfo", msg -> {
                    final SimpleInfo info = new SimpleInfo(id, name);
                    getSender().tell(info, getSelf());
                })
                .match(AgreeInviteMsg.class, msg -> {
                    /*
                    if (members.containsKey(msg.getUserId())) return;
                    final ActorRef memberRef = getContext().actorOf(Props.create(MemberActor.class, msg.getUserId()));
                    members.put(msg.getUserId(), memberRef);
                    */
                    final ActorRef actorRef = getContext().actorOf(Props.create(MemberActor.class, msg.getUserId()), "member" + msg.getUserId());
                    log.info("create actorRef:" + actorRef);
                })
                .match(Say.class, msg -> {
                    final MemberActor.Say memberSay = new MemberActor.Say(msg.getUserId(), msg.getContent(), System.currentTimeMillis());
                    final Iterator<ActorRef> children = context().children().iterator();
                    while (children.hasNext()) {
                        children.next().tell(memberSay, getSelf());
                    }
                })
                .matchAny(msg -> {
                    log.info("无法处理的消息:{}", msg);
                })
                .build();
    }
}
