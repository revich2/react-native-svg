package com.horcrux.svg;

import android.graphics.Bitmap;
import android.graphics.Path;

import com.facebook.react.bridge.Dynamic;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

interface FilterPrimitive {
  Bitmap applyFilter(Map<String, Bitmap> results, Bitmap previousFilterResult, Path path);
}

public class FilterPrimitiveView extends DefinitionView implements FilterPrimitive {
    protected FiltersEngine filtersEngine;

    SVGLength mX;
    SVGLength mY;

    SVGLength mWidth;
    SVGLength mHeight;

    String mResult;

    FilterPrimitiveView(ReactContext reactContext) {
      super(reactContext);

      this.filtersEngine = FiltersEngine.getInstance();
    }

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

    @ReactProp(name="result")
    public void setResult(String result) {
        invalidate();

        mResult = result;
    }

    public Bitmap applyFilter(Map<String, Bitmap> results, Bitmap previousFilterResult, Path path) {
        return previousFilterResult;
    }
}
