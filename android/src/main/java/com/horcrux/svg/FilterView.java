package com.horcrux.svg;

import android.annotation.SuppressLint;

import com.facebook.react.bridge.Dynamic;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

@SuppressLint("ViewConstructor")
class FilterView extends GroupView {
    SVGLength mX;
    SVGLength mY;

    SVGLength mWidth;
    SVGLength mHeight;

    Brush.BrushUnits mFilterUnits;
    Brush.BrushUnits mPrimitiveUnits;

    public FilterView(ReactContext reactContext) {
        super(reactContext);
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

    @ReactProp(name="filterUnits")
    public void setFilterUnits(int filterUnits) {
      switch (filterUnits) {
        case 0:
          mFilterUnits = Brush.BrushUnits.OBJECT_BOUNDING_BOX;
          break;
        case 1:
          mFilterUnits = Brush.BrushUnits.USER_SPACE_ON_USE;
          break;
      }
      invalidate();
    }

    @ReactProp(name="primitiveUnits")
    public void setPrimitiveUnits(int primitiveUnits) {
      switch (primitiveUnits) {
        case 0:
          mPrimitiveUnits = Brush.BrushUnits.OBJECT_BOUNDING_BOX;
          break;
        case 1:
          mPrimitiveUnits = Brush.BrushUnits.USER_SPACE_ON_USE;
          break;
      }
      invalidate();
    }

    @Override
    void saveDefinition() {
        if (mName != null) {
            SvgView svg = getSvgView();
            svg.defineFilter(this, mName);
        }
    }
}
