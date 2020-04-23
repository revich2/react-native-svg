package com.horcrux.svg;

public class FiltersEngine {
  static {
    System.loadLibrary("Filters");
  }

  static private FiltersEngine instance = null;

  public static FiltersEngine getInstance() {
      FiltersEngine localInstance = FiltersEngine.instance;

      if (localInstance == null) {
          synchronized (FiltersEngine.class) {
              localInstance = FiltersEngine.instance;

              if (localInstance == null) {
                  instance = localInstance = new FiltersEngine();
              }
          }
      }

      return localInstance;
  };

  public native short[] nativeGaussianBlur(short[] srcPixels, int width, int height, double stdX, double stdY, int edgeMode);
}
