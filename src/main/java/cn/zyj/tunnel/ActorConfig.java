package cn.zyj.tunnel;

import akka.actor.ActorSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActorConfig {

    @Bean
    public ActorSystem actorSystem() {
        final ActorSystem system = ActorSystem.create("tunnel");
        return system;
    }
}
