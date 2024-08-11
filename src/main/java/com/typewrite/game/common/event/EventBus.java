package com.typewrite.game.common.event;

import java.util.function.Consumer;

/** Interface representing an event bus for subscribing, unsubscribing, and publishing events. */
public interface EventBus {

  /**
   * Subscribe to an event type.
   *
   * @param eventType the event type, can be a superclass of all events to subscribe.
   * @param subscriber the subscriber which will consume the events.
   * @param <T> the event type class.
   */
  <T extends Event> void subscribe(Class<? extends T> eventType, Consumer<T> subscriber);

  /**
   * Unsubscribe from all event types.
   *
   * @param subscriber the subscriber to unsubscribe.
   * @param <T> the event type class.
   */
  <T extends Event> void unsubscribe(Consumer<T> subscriber);

  /**
   * Unsubscribe from an event type.
   *
   * @param eventType the event type, can be a superclass of all events to unsubscribe.
   * @param subscriber the subscriber to unsubscribe.
   * @param <T> the event type class.
   */
  <T extends Event> void unsubscribe(Class<? extends T> eventType, Consumer<T> subscriber);

  /**
   * Publish an event to all subscribers.
   *
   * <p>The event type is the class of <code>event</code>. The event is published to all consumers
   * which subscribed to this event type or any superclass.
   *
   * @param event the event.
   * @param <T> the event type class.
   */
  <T extends Event> void publish(T event);
}
