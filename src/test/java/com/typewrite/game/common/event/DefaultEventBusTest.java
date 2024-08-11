package com.typewrite.game.common.event;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DefaultEventBusTest {

  private DefaultEventBus eventBus;

  @BeforeEach
  void setUp() {
    eventBus = DefaultEventBus.getInstance();
    eventBus.reset(); // Reset the state before each test
  }

  @Test
  void testSubscribeAndPublish() {
    AtomicBoolean received = new AtomicBoolean(false);
    eventBus.subscribe(TestEvent.class, event -> received.set(true));
    eventBus.publish(new TestEvent());
    assertTrue(received.get(), "Event should be received by the subscriber");
  }

  @Test
  void testUnsubscribe() {
    AtomicBoolean received = new AtomicBoolean(false);
    Consumer<TestEvent> subscriber = event -> received.set(true);
    eventBus.subscribe(TestEvent.class, subscriber);
    eventBus.unsubscribe(subscriber);
    eventBus.publish(new TestEvent());
    assertFalse(
        received.get(), "Event should not be received by the subscriber after unsubscribing");
  }

  @Test
  void testMultipleSubscribers() {
    AtomicBoolean receivedByFirst = new AtomicBoolean(false);
    AtomicBoolean receivedBySecond = new AtomicBoolean(false);

    eventBus.subscribe(TestEvent.class, event -> receivedByFirst.set(true));
    eventBus.subscribe(TestEvent.class, event -> receivedBySecond.set(true));

    eventBus.publish(new TestEvent());

    assertTrue(receivedByFirst.get(), "Event should be received by the first subscriber");
    assertTrue(receivedBySecond.get(), "Event should be received by the second subscriber");
  }

  @Test
  void testEventHierarchy() {
    AtomicBoolean baseEventReceived = new AtomicBoolean(false);
    AtomicBoolean derivedEventReceived = new AtomicBoolean(false);

    eventBus.subscribe(BaseEvent.class, event -> baseEventReceived.set(true));
    eventBus.subscribe(DerivedEvent.class, event -> derivedEventReceived.set(true));

    eventBus.publish(new DerivedEvent());

    assertTrue(baseEventReceived.get(), "Base event subscriber should receive derived event");
    assertTrue(derivedEventReceived.get(), "Derived event subscriber should receive derived event");
  }

  @Test
  void testExceptionHandlingInSubscribers() {
    AtomicBoolean received = new AtomicBoolean(false);

    eventBus.subscribe(
        TestEvent.class,
        event -> {
          throw new RuntimeException("Exception in subscriber");
        });
    eventBus.subscribe(TestEvent.class, event -> received.set(true));

    assertDoesNotThrow(
        () -> eventBus.publish(new TestEvent()), "Publishing event should not throw an exception");
    assertTrue(received.get(), "Other subscribers should still receive the event");
  }

  @Test
  void testSubscribeToMultipleEventTypes() {
    AtomicInteger eventCount = new AtomicInteger(0);

    Consumer<Event> subscriber = event -> eventCount.incrementAndGet();

    eventBus.subscribe(TestEvent.class, subscriber);
    eventBus.subscribe(AnotherEvent.class, subscriber);

    eventBus.publish(new TestEvent());
    eventBus.publish(new AnotherEvent());

    assertEquals(2, eventCount.get(), "Subscriber should receive both events");
  }

  @Test
  void testUnsubscribeSpecificEventType() {
    AtomicInteger eventCount = new AtomicInteger(0);

    Consumer<Event> subscriber = event -> eventCount.incrementAndGet();

    eventBus.subscribe(TestEvent.class, subscriber);
    eventBus.subscribe(AnotherEvent.class, subscriber);

    eventBus.unsubscribe(TestEvent.class, subscriber);

    eventBus.publish(new TestEvent());
    eventBus.publish(new AnotherEvent());

    assertEquals(1, eventCount.get(), "Subscriber should only receive AnotherEvent");
  }

  @Test
  void testPublishNullEvent() {
    assertThrows(
        NullPointerException.class,
        () -> eventBus.publish(null),
        "Publishing null event should throw NullPointerException");
  }

  @Test
  void testUnsubscribeNonSubscribedEvent() {
    AtomicBoolean received = new AtomicBoolean(false);

    Consumer<TestEvent> subscriber = event -> received.set(true);

    eventBus.unsubscribe(TestEvent.class, subscriber);
    eventBus.publish(new TestEvent());

    assertFalse(received.get(), "Unsubscribing non-subscribed event should have no effect");
  }

  @Test
  void testConcurrentAccess() throws InterruptedException {
    AtomicInteger eventCount = new AtomicInteger(0);

    Consumer<TestEvent> subscriber = event -> eventCount.incrementAndGet();

    eventBus.subscribe(TestEvent.class, subscriber);

    ExecutorService executorService = Executors.newFixedThreadPool(10);
    for (int i = 0; i < 100; i++) {
      executorService.submit(() -> eventBus.publish(new TestEvent()));
    }

    executorService.shutdown();
    executorService.awaitTermination(1, TimeUnit.MINUTES);

    assertEquals(100, eventCount.get(), "All events should be received by the subscriber");
  }

  static class BaseEvent extends Event {
    public BaseEvent() {
      super();
    }
  }

  static class DerivedEvent extends BaseEvent {
    public DerivedEvent() {
      super();
    }
  }

  static class TestEvent extends Event {
    public TestEvent() {
      super();
    }
  }

  static class AnotherEvent extends Event {
    public AnotherEvent() {
      super();
    }
  }
}
