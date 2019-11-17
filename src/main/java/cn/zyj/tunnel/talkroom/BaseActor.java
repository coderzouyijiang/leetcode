package cn.zyj.tunnel.talkroom;

import akka.actor.AbstractActor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public abstract class BaseActor extends AbstractActor {

    @Override
    public void preStart() throws Exception {
        super.preStart();
        log.info("preStart:{},{}", getSelf().path(), getSelf().hashCode());
    }

    @Override
    public void postStop() throws Exception {
        super.postStop();
        log.info("postStop:{},{}", getSelf().path(), getSelf().hashCode());
    }

    @Override
    public void preRestart(Throwable reason, Optional<Object> message) throws Exception {
        super.preRestart(reason, message);
        log.info("preRestart:{},{}", getSelf().path(), getSelf().hashCode());
    }

    @Override
    public void postRestart(Throwable reason) throws Exception {
        super.postRestart(reason);
        log.info("postRestart:{},{}", getSelf().path(), getSelf().hashCode());
    }
}
