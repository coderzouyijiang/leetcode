package cn.zyj.tunnel;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import cn.zyj.tunnel.demo.SumActor;
import cn.zyj.tunnel.talkroom.RoomActor;
import cn.zyj.tunnel.talkroom.UserActor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PreDestroy;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.LongStream;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TalkRoomTest {

    @Autowired
    private ActorSystem system;

    @Test
    public void test() throws InterruptedException {
        final ActorRef room = system.actorOf(Props.create(RoomActor.class, "1", "room"), "room1");
        ActorRef user1 = system.actorOf(Props.create(UserActor.class, "1"), "user1");
        ActorRef user2 = system.actorOf(Props.create(UserActor.class, "2"), "user2");
        final RoomActor.AgreeInviteMsg agreeInviteMsg = new RoomActor.AgreeInviteMsg("1");
        room.tell(agreeInviteMsg, ActorRef.noSender());
        final UserActor.InviteMsg inviteMsg = new UserActor.InviteMsg("1", "1", "1 invite 2", System.currentTimeMillis());
        user2.tell(inviteMsg, room);
        room.tell(new RoomActor.Say("1", "111111"), ActorRef.noSender());
        room.tell(new RoomActor.Say("2", "222222"), ActorRef.noSender());
        Thread.sleep(1000);
        system.stop(user1);
        Thread.sleep(1000);
        room.tell(new RoomActor.Say("1", "333333"), ActorRef.noSender());
        room.tell(new RoomActor.Say("1", "444444"), ActorRef.noSender());
        user1 = system.actorOf(Props.create(UserActor.class, "1"), "user1");
        room.tell(new RoomActor.Say("2", "5555"), ActorRef.noSender());
        room.tell(new RoomActor.Say("2", "6666"), ActorRef.noSender());


        Thread.currentThread().join(2000);
    }

    @Test
    public void test_SumActor() throws InterruptedException {

        final Random random = new Random();
        long[] arr = new long[1000_0000];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = random.nextInt(10000);
        }
        log.info("arr:" + Arrays.toString(arr));
        final long t1 = System.currentTimeMillis();
        final long expect = LongStream.of(arr).sum();
        final long time = System.currentTimeMillis() - t1;

        final ActorRef sumActor = system.actorOf(Props.create(SumActor.class, 10000), "sum");
        sumActor.tell(new SumActor.Params(arr, 0, arr.length), ActorRef.noSender());

        Thread.currentThread().join(3000);
        log.info("expect:{},time={}", expect, time);
    }

}
