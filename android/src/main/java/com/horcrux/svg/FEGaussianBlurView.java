package com.horcrux.svg;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Path;

import com.facebook.react.bridge.Dynamic;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.nio.ByteBuffer;
import java.util.Map;

@SuppressLint("ViewConstructor")
class FEGaussianBlurView extends FilterPrimitiveView {
    enum RNSVGEdgeModeValues {
        SVG_EDGEMODE_UNKNOWN,
        SVG_EDGEMODE_DUPLICATE,
        SVG_EDGEMODE_WRAP,
        SVG_EDGEMODE_NONE,
    }

    SVGLength mStdDeviationX;
    SVGLength mStdDeviationY;
    RNSVGEdgeModeValues mEdgeMode;

    String mIn1;

    public FEGaussianBlurView(ReactContext reactContext) {
        super(reactContext);
    }

    @ReactProp(name="stdDeviationX")
    public void setStdDeviationX(Dynamic stdDeviationX) {
        mStdDeviationX = SVGLength.from(stdDeviationX);
        invalidate();
    }

    @ReactProp(name="stdDeviationY")
    public void setStdDeviationY(Dynamic stdDeviationY) {
        mStdDeviationY = SVGLength.from(stdDeviationY);
        invalidate();
    }

    @ReactProp(name="edgeMode")
    public void setEdgeMode(int edgeMode) {
        mEdgeMode = this.getEdgeMode(edgeMode);
        invalidate();
    }

    private int getEdgeMode(RNSVGEdgeModeValues edgeMode) {
      switch (edgeMode) {
        default:
        case SVG_EDGEMODE_UNKNOWN:
          return 0;
        case SVG_EDGEMODE_DUPLICATE:
          return 1;
        case SVG_EDGEMODE_WRAP:
          return 2;
        case SVG_EDGEMODE_NONE:
          return 3;
      }
    };

    private RNSVGEdgeModeValues getEdgeMode(int edgeMode) {
      switch (edgeMode) {
        default:
        case 0:
          return RNSVGEdgeModeValues.SVG_EDGEMODE_UNKNOWN;
        case 1:
          return RNSVGEdgeModeValues.SVG_EDGEMODE_DUPLICATE;
        case 2:
          return RNSVGEdgeModeValues.SVG_EDGEMODE_WRAP;
        case 3:
          return RNSVGEdgeModeValues.SVG_EDGEMODE_NONE;
      }
    }

    @ReactProp(name="in1")
    public void setIn1(String in1) {
        mIn1 = in1;
        invalidate();
    }

    private Bitmap gaussianBlur(Bitmap srcBtm, double stdX, double stdY, RNSVGEdgeModeValues edgeMode) {
      int width = srcBtm.getWidth();
      int height = srcBtm.getHeight();

      int size = srcBtm.getRowBytes() * srcBtm.getHeight();
      ByteBuffer srcBuffer = ByteBuffer.allocate(size);
      srcBtm.copyPixelsToBuffer(srcBuffer);

      byte[] pixels = srcBuffer.array();
      short[] uint8_pixels = new short[size];

      for (int i = 0; i < size; i++) {
        uint8_pixels[i] = (short) (pixels[i] & 0xff);
      }

      short[] blurred_pixels = this.filtersEngine.nativeGaussianBlur(uint8_pixels, width, height, stdX, stdY, this.getEdgeMode(edgeMode));

      Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

      for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {
          int redPosition = 4 * (x + width * y);

          int red = blurred_pixels[redPosition];
          int green = blurred_pixels[redPosition + 1];
          int blue = blurred_pixels[redPosition + 2];
          int alpha = blurred_pixels[redPosition + 3];

          result.setPixel(x, y, Color.argb(alpha, blue, green, red));
        }
      }

      return result;
    }

    @Override
    public Bitmap applyFilter(Map<String, Bitmap> results, Bitmap previousFilterResult, Path path) {
        Bitmap inResult = !this.mIn1.isEmpty() ? results.get(this.mIn1) : null;
        Bitmap inputImage = inResult != null ? inResult : previousFilterResult;

        if (inputImage == null) {
          return null;
        }

        double stdDeviationY = this.mStdDeviationY.value;
        double stdDeviationX = this.mStdDeviationX.value;

        return this.gaussianBlur(inputImage, stdDeviationX, stdDeviationY, mEdgeMode);
    }
}

