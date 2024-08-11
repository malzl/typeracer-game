package com.typewrite.game.common.event;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;

/**
 * Simple event bus implementation.
 *
 * <p>Subscribe and publish events. Events are published in channels distinguished by event type.
 * Channels can be grouped using an event type hierarchy.
 *
 * <p>You can use the default event bus instance {@link #getInstance}, which is a singleton, or you
 * can create one or multiple instances of {@link DefaultEventBus}.
 */
@SuppressWarnings({"rawtypes"})
public final class DefaultEventBus implements EventBus {

  /** Private constructor for DefaultEventBus. */
  private DefaultEventBus() {}

  private final Map<Class<?>, Set<Consumer>> subscribers = new ConcurrentHashMap<>();

  /**
   * Subscribes a consumer to a specific event type.
   *
   * @param eventType the event type, can be a superclass of all events to subscribe.
   * @param subscriber the subscriber which will consume the events.
   * @param <E> the event type class.
   */
  @Override
  public <E extends Event> void subscribe(Class<? extends E> eventType, Consumer<E> subscriber) {
    Objects.requireNonNull(eventType);
    Objects.requireNonNull(subscriber);

    Set<Consumer> eventSubscribers = getOrCreateSubscribers(eventType);
    eventSubscribers.add(subscriber);
  }

  private <E> Set<Consumer> getOrCreateSubscribers(Class<E> eventType) {
    return subscribers.computeIfAbsent(eventType, k -> new CopyOnWriteArraySet<>());
  }

  /**
   * Unsubscribes a consumer from all event types.
   *
   * @param subscriber the subscriber to unsubscribe.
   * @param <E> the event type class.
   */
  @Override
  public <E extends Event> void unsubscribe(Consumer<E> subscriber) {
    Objects.requireNonNull(subscriber);
    subscribers.values().forEach(eventSubscribers -> eventSubscribers.remove(subscriber));
  }

  /**
   * Unsubscribes a consumer from a specific event type.
   *
   * @param eventType the event type, can be a superclass of all events to unsubscribe.
   * @param subscriber the subscriber to unsubscribe.
   * @param <E> the event type class.
   */
  @Override
  public <E extends Event> void unsubscribe(Class<? extends E> eventType, Consumer<E> subscriber) {
    Objects.requireNonNull(eventType);
    Objects.requireNonNull(subscriber);

    subscribers.keySet().stream()
        .filter(eventType::isAssignableFrom)
        .map(subscribers::get)
        .forEach(eventSubscribers -> eventSubscribers.remove(subscriber));
  }

  /**
   * Unsubscribes all consumers from a specific event type.
   *
   * @param eventType the event type, can be a superclass of all events to unsubscribe.
   * @param <E> the event type class.
   */
  public <E extends Event> void unsubscribeAll(Class<? extends E> eventType) {
    Objects.requireNonNull(eventType);
    subscribers.keySet().stream()
        .filter(eventType::isAssignableFrom)
        .map(subscribers::get)
        .forEach(Set::clear);
  }

  /**
   * Publishes an event to all subscribers.
   *
   * <p>The event type is the class of {@code event}. The event is published to all consumers which
   * subscribed to this event type or any superclass.
   *
   * @param event the event.
   * @param <E> the event type class.
   */
  @Override
  @SuppressWarnings("unchecked")
  public <E extends Event> void publish(E event) {
    Objects.requireNonNull(event);

    Class<?> eventType = event.getClass();
    subscribers.keySet().stream()
        .filter(type -> type.isAssignableFrom(eventType))
        .flatMap(type -> subscribers.get(type).stream())
        .forEach(subscriber -> publish(event, subscriber));
  }

  private <E extends Event> void publish(E event, Consumer<E> subscriber) {
    try {
      subscriber.accept(event);
    } catch (Exception e) {
      Thread.currentThread()
          .getUncaughtExceptionHandler()
          .uncaughtException(Thread.currentThread(), e);
    }
  }

  private static class InstanceHolder {
    private static final DefaultEventBus INSTANCE = new DefaultEventBus();
  }

  /**
   * Gets the singleton instance of DefaultEventBus.
   *
   * @return the singleton instance of DefaultEventBus.
   */
  public static synchronized DefaultEventBus getInstance() {
    return InstanceHolder.INSTANCE;
  }

  public void reset() {
    subscribers.clear();
  }
}
