package com.typewrite.game.common.event;

import java.util.UUID;

/** Abstract class representing a generic event. */
public abstract class Event {

  protected final UUID id = UUID.randomUUID();

  /** Constructor for Event. Generates a unique identifier for the event. */
  protected Event() {}

  /**
   * Gets the unique identifier of the event.
   *
   * @return the unique identifier of the event.
   */
  public UUID getId() {
    return id;
  }

  /**
   * Compares this event to the specified object. The result is {@code true} if and only if the
   * argument is not {@code null} and is an {@code Event} object that has the same ID as this
   * object.
   *
   * @param o the object to compare this {@code Event} against.
   * @return {@code true} if the given object represents an {@code Event} equivalent to this event,
   *     {@code false} otherwise.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Event event)) {
      return false;
    }
    return id.equals(event.id);
  }

  /**
   * Returns a hash code value for the event. This method is supported for the benefit of hash
   * tables such as those provided by {@link java.util.HashMap}.
   *
   * @return a hash code value for this event.
   */
  @Override
  public int hashCode() {
    return id.hashCode();
  }

  /**
   * Returns a string representation of the event. The string representation consists of the event's
   * class name followed by the event's ID.
   *
   * @return a string representation of the event.
   */
  @Override
  public String toString() {
    return "Event{" + "id=" + id + '}';
  }
}
