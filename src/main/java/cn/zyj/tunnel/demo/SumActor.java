package cn.zyj.tunnel.demo;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.sun.xml.internal.rngom.digested.DDataPattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Slf4j
public class SumActor extends AbstractActor {

    @Getter
    @ToString
    @AllArgsConstructor
    @Builder
    public static class Params implements Serializable {
        private long[] data;
        private int begin;
        private int end;
    }

    private final int maxNum;

    private int childNum = 0;
    private long result = 0;
    private long startTime = 0;

    public SumActor(int maxNum) {
        this.maxNum = maxNum;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Params.class, params -> {
                    startTime = System.currentTimeMillis();
                    log.info("{}:收到参数:下标[{},{})", getSelf().path(), params.getBegin(), params.getEnd());
                    final int start = params.getBegin();
                    final int end = params.getEnd();
                    final int length = end - start;
                    if (length <= maxNum) {
                        for (int i = start; i < end; i++) {
                            result += params.data[i];
                        }
                        log.info("{}:计算完成,result={},sender={}", getSelf().path(), result, getSender().path());
                        getSender().tell(result, getSelf());
                        getContext().stop(getSelf());
                    } else {
                        int perNum = (int) Math.max(maxNum, Math.ceil((double) length / (double) maxNum));
                        childNum = (int) Math.ceil((double) length / (double) perNum);
                        log.info("{}:分发计算任务,childNum={}", getSelf().path(), childNum);
                        int childIndex = 0;
                        for (int i = start; i < end; i += perNum) {
                            final Params param2 = new Params(params.getData(), i, Math.min(i + perNum, end));
                            final ActorRef childRef = getContext().actorOf(Props.create(SumActor.class, maxNum), (childIndex++) + "");
                            childRef.tell(param2, getSelf());
                        }
                    }
                })
                .match(Long.class, childResult -> {
                    childNum--;
                    result += childResult;
                    if (childNum <= 0) {
                        final long endTime = System.currentTimeMillis();
                        final long time = endTime - startTime;
                        log.info("{}:所有子节点计算完成,result={},parent={},time={}", getSelf().path(), result, getContext().parent().path(), time);
                        context().parent().tell(result, getSelf());
                        getContext().stop(getSelf());
                    }
                })
                .build();
    }
}
