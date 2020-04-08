package com.horcrux.svg;

import android.graphics.Bitmap;

import com.facebook.react.bridge.Dynamic;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

interface FilterPrimitive {
  Bitmap applyFilter(Map<String, Bitmap> results, Bitmap previousFilterResult);
}

public class FilterPrimitiveView extends DefinitionView implements FilterPrimitive {
    SVGLength mX;
    SVGLength mY;

    SVGLength mWidth;
    SVGLength mHeight;

    FilterPrimitiveView(ReactContext reactContext) { super(reactContext); }

    @ReactProp(name = "x")
    public void setX(Dynamic x) {
        mX = SVGLength.from(x);
        invalidate();
    }

    @ReactProp(name="y")
    public void setY(Dynamic y) {
        mY = SVGLength.from(y);
        invalidate();
    }

    @ReactProp(name = "width")
    public void setWidth(Dynamic width) {
        mWidth = SVGLength.from(width);
        invalidate();
    }

    @ReactProp(name="height")
    public void setHeight(Dynamic height) {
        mHeight = SVGLength.from(height);
        invalidate();
    }

    public Bitmap applyFilter(Map<String, Bitmap> results, Bitmap previousFilterResult) {
        return previousFilterResult;
    }
}
