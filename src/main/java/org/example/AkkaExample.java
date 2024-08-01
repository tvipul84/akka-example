package org.example;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class AkkaExample {

    // Define the message protocol
    public static class Greet {
        public final String whom;

        public Greet(String whom) {
            this.whom = whom;
        }
    }

    // Define the Greeter actor
    public static class Greeter extends AbstractBehavior<Greet> {

        public static Behavior<Greet> create() {
            return Behaviors.setup(Greeter::new);
        }

        private Greeter(ActorContext<Greet> context) {
            super(context);
        }

        @Override
        public Receive<Greet> createReceive() {
            return newReceiveBuilder()
                    .onMessage(Greet.class, this::onGreet)
                    .build();
        }

        private Behavior<Greet> onGreet(Greet command) {
            getContext().getLog().info("Hello, {}!", command.whom);
            System.out.printf("Hello %s!", command.whom);
            return this;
        }
    }

    // Define the main method to run the actor system
    public static void main(String[] args) {
        final ActorSystem<Greet> actorSystem = ActorSystem.create(Greeter.create(), "greeterSystem");

        actorSystem.tell(new Greet("World"));

        try {
            // Let the actor system run for a few seconds before terminating
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            actorSystem.terminate();
        }
    }
}
