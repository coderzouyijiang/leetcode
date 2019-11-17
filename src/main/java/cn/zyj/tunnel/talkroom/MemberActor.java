package cn.zyj.tunnel.talkroom;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MemberActor extends BaseActor {

    @Getter
    @ToString
    @AllArgsConstructor
    @Builder
    public static class Say implements Serializable {
        private String userId;
        private String content;
        private Long time;
    }

    private final String userId;
    private String name;

    private List<Say> sayList = new ArrayList<>();

    private int sendIndex = 0;

    public MemberActor(String userId) {
        this.userId = userId;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Say.class, msg -> {
                    log.info("receive say:" + msg);
                    sayList.add(msg);
                    final ActorRef userRef = getContext().actorFor("/user/user" + userId);
                    if (!userRef.isTerminated()) {
                        for (Say say : sayList.subList(sendIndex, sayList.size())) {
                            userRef.tell(say, getSelf());
                            sendIndex++;
                        }
                    }
                })
                .build();
    }
}
