/* SPDX-License-Identifier: MIT */

package com.typewrite.game.common.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotation to mark methods as event listeners.
 *
 * <p>Methods annotated with {@code @Listener} are considered to be handling events.
 */
@Target(ElementType.METHOD)
public @interface Listener {}
