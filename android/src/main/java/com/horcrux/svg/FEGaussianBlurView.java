package com.horcrux.svg;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

import com.facebook.react.bridge.Dynamic;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

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
            default:
                mEdgeMode = RNSVGEdgeModeValues.SVG_EDGEMODE_UNKNOWN;
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
    void saveDefinition() { }

    @Override
    public Bitmap applyFilter(Map<String, Bitmap> results, Bitmap previousFilterResult) {
        Bitmap inResult = !this.mIn1.isEmpty() ? results.get(this.mIn1) : null;
        Bitmap inputImage = inResult != null ? inResult : previousFilterResult;

        if (inputImage == null) {
          return null;
        }

        // TODO: make filter

        return previousFilterResult;
    }
}
