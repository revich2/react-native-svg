package com.horcrux.svg;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.nio.ByteBuffer;

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

  private native short[] nativeGaussianBlur(short[] srcPixels, int width, int height, double stdX, double stdY, int edgeMode);

  private short[] getUint8Pixels(Bitmap srcBtm) {
    int size = srcBtm.getRowBytes() * srcBtm.getHeight();
    ByteBuffer srcBuffer = ByteBuffer.allocate(size);
    srcBtm.copyPixelsToBuffer(srcBuffer);

    byte[] pixels = srcBuffer.array();
    short[] uint8_pixels = new short[size];

    for (int i = 0; i < size; i++) {
      uint8_pixels[i] = (short) (pixels[i] & 0xff);
    }

    return uint8_pixels;
  }

  private Bitmap getBitmapFrom(short[] pixels, int width, int height) {
    Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        int redPosition = 4 * (x + width * y);

        int red = pixels[redPosition];
        int green = pixels[redPosition + 1];
        int blue = pixels[redPosition + 2];
        int alpha = pixels[redPosition + 3];

        result.setPixel(x, y, Color.argb(alpha, blue, green, red));
      }
    }

    return result;
  }

  public Bitmap gaussianBlur(Bitmap srcBtm, double stdX, double stdY, int edgeMode) {
    int width = srcBtm.getWidth();
    int height = srcBtm.getHeight();

    short[] uint8_pixels = this.getUint8Pixels(srcBtm);

    short[] blurred_pixels = this.nativeGaussianBlur(uint8_pixels, width, height, stdX, stdY, edgeMode);

    return this.getBitmapFrom(blurred_pixels, width, height);
  }
}
