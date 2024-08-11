package com.typewrite.game;

import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

/** Utility class for retrieval of resources. */
public final class Resources {

  public static final String MODULE_DIR = "/com/typewrite/game/";

  /**
   * Retrieves an input stream for reading the specified resource.
   *
   * @param resource the name of the resource.
   * @return an InputStream for reading the resource.
   * @throws NullPointerException if the resource is not found.
   */
  public static InputStream getResourceAsStream(String resource) {
    String path = resolve(resource);
    return Objects.requireNonNull(
        GameApp.class.getResourceAsStream(path), "Resource not found: " + path);
  }

  /**
   * Retrieves the URL of the specified resource.
   *
   * @param resource the name of the resource.
   * @return a URL representing the resource.
   * @throws NullPointerException if the resource is not found.
   */
  public static URL getResourceThroughUrl(String resource) {
    String path = resolve(resource);
    return Objects.requireNonNull(GameApp.class.getResource(path), "Resource not found: " + path);
  }

  /**
   * Resolves the provided resource name to an absolute path within the module directory.
   *
   * @param resource the name of the resource.
   * @return the resolved absolute path of the resource.
   * @throws NullPointerException if the resource is null.
   */
  public static String resolve(String resource) {
    Objects.requireNonNull(resource);
    return resource.startsWith("/") ? resource : MODULE_DIR + resource;
  }
}
