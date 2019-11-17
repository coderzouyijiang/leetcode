package cn.zyj.tunnel.talkroom;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.pattern.Patterns;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Slf4j
public class UserActor extends BaseActor {

    @Getter
    @ToString
    @AllArgsConstructor
    @Builder
    public static class InviteMsg implements Serializable {
        private String roomId;
        private String userId;
        private String content;
        private Long time;
    }

    @Getter
    @ToString
    @AllArgsConstructor
    @Builder
    public static class SimpleInfo implements Serializable {
        private String id;
        private String name;
        private String signature;
    }

    private final String id;
    private String name;
    private String signature;

    public UserActor(String id) {
        this.id = id;
        name = "user[" + id + "]";
        signature = "sig-" + Math.random();
    }

    private List<InviteMsg> invokeMsgs = new LinkedList<>();

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(InviteMsg.class, msg -> {
                    invokeMsgs.add(msg);
                    final ActorRef senderRef = getContext().actorFor("../user" + msg.userId);
                    final ActorRef roomRef = getContext().actorFor("/user/room" + msg.roomId);
                    final CompletionStage<Object> getSenderInfo = Patterns.ask(senderRef, "getSimpleInfo", Duration.ofMillis(100));
                    final CompletionStage<Object> getRoomInfo = Patterns.ask(roomRef, "getSimpleInfo", Duration.ofMillis(100));
                    getSenderInfo.thenAcceptBoth(getRoomInfo, (senderInfoObj, rootInfoObj) -> {
                        SimpleInfo senderInfo = (SimpleInfo) senderInfoObj;
                        RoomActor.SimpleInfo roomInfo = (RoomActor.SimpleInfo) rootInfoObj;
                        log.info(getSelf().path() + ":invite {}[{}] into {}[{}]", senderInfo.getName(), senderInfo.getId(), roomInfo.getName(), roomInfo.getId());
                        // agree
                        final RoomActor.AgreeInviteMsg agreeInviteMsg = new RoomActor.AgreeInviteMsg(id);
                        roomRef.tell(agreeInviteMsg, getSelf());
                    });
                })
                .matchEquals("getSimpleInfo", msg -> {
                    final SimpleInfo info = new SimpleInfo(id, name, signature);
                    getSender().tell(info, getSelf());
                })
                .match(MemberActor.Say.class, msg -> {
                    log.info(name + " receive say:" + msg);
                })
                .matchAny(msg -> {
                    log.info("无法处理的消息:{}", msg);
                })
                .build();
    }
}
