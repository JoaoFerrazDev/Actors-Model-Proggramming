package config;

import akka.actor.OneForOneStrategy;
import akka.actor.SupervisorStrategy;
import akka.actor.SupervisorStrategyConfigurator;
import akka.japi.pf.DeciderBuilder;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

public class GuardianSupervisorStrategy implements SupervisorStrategyConfigurator
{
    @Override
    public SupervisorStrategy create()
    {
        return new OneForOneStrategy(1,
                Duration.create(30, TimeUnit.SECONDS),
                true,
                DeciderBuilder
                    .match(RuntimeException.class, e -> SupervisorStrategy.resume())
                    .matchAny(o -> SupervisorStrategy.restart())
                    .build());
    }
}