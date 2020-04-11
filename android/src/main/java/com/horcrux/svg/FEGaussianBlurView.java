package com.horcrux.svg;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.util.Log;

import com.facebook.react.bridge.Dynamic;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.Map;

@SuppressLint("ViewConstructor")
class FEGaussianBlurView extends FilterPrimitiveView {
    static final int gMaxKernelSize = 501;

    private static float gaussianKernelFactor() {
      return 3 / 4.f * (float) Math.sqrt(2 * Math.PI);
    }

    private int clampedToKernelSize(double value) {
      int size = Math.max(2, (int) Math.floor(value * gaussianKernelFactor() + 0.5f));

      return Math.min(size, gMaxKernelSize);
    }

    private int castToOdd(int value) {
      return value % 2 == 0 ? value + 1 : value;
    }

    private Size calculateKernelSize(double stdX, double stdY) {
      return new Size(castToOdd(clampedToKernelSize(stdX)), castToOdd(clampedToKernelSize(stdY)));
    }

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
        switch (edgeMode) {
            default:
            case 0:
                mEdgeMode = RNSVGEdgeModeValues.SVG_EDGEMODE_UNKNOWN;
                break;
            case 1:
                mEdgeMode = RNSVGEdgeModeValues.SVG_EDGEMODE_DUPLICATE;
                break;
            case 2:
                mEdgeMode = RNSVGEdgeModeValues.SVG_EDGEMODE_WRAP;
                break;
            case 3:
                mEdgeMode = RNSVGEdgeModeValues.SVG_EDGEMODE_NONE;
                break;
        }

        invalidate();
    }

    @ReactProp(name="in1")
    public void setIn1(String in1) {
        mIn1 = in1;
        invalidate();
    }

    @Override
    public Bitmap applyFilter(Map<String, Bitmap> results, Bitmap previousFilterResult, Path path) {
        Bitmap inResult = !this.mIn1.isEmpty() ? results.get(this.mIn1) : null;
        Bitmap inputImage = inResult != null ? inResult : previousFilterResult;

        if (inputImage == null) {
          return null;
        }

        Bitmap tmpBitmap = inputImage.copy(inputImage.getConfig(), true);

        // TODO: How it works?
        if (!OpenCVLoader.initDebug()) {
          Log.d("OpenCV", "OpenCV loaded successfully!");
        }

        Mat rgba = new Mat(tmpBitmap.getWidth(), tmpBitmap.getHeight(), CvType.CV_64F);
        Utils.bitmapToMat(tmpBitmap, rgba);

        double stdDeviationY = this.mStdDeviationY.value;
        double stdDeviationX = this.mStdDeviationX.value;

        Imgproc.GaussianBlur(
          rgba,
          rgba,
          calculateKernelSize(stdDeviationX * 3, stdDeviationY * 3), // 3 - This is experimental value
          stdDeviationX,
          stdDeviationY
        );

        Utils.matToBitmap(rgba, tmpBitmap);

        return tmpBitmap;
    }
}
