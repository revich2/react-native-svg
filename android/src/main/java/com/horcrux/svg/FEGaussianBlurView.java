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

        int stdDeviationY = (int) Math.floor(this.mStdDeviationY.value);
        int stdDeviationX = (int) Math.floor(this.mStdDeviationX.value);

        // TODO: What is a kernel size? It depends from image size?
        Imgproc.GaussianBlur(rgba, rgba, new Size(471, 471), stdDeviationX, stdDeviationY);

        Utils.matToBitmap(rgba, tmpBitmap);

        return tmpBitmap;
    }
}
