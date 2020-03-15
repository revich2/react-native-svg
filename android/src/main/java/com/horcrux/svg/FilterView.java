package com.horcrux.svg;

import android.annotation.SuppressLint;

import com.facebook.react.bridge.Dynamic;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

@SuppressLint("ViewConstructor")
class FilterView extends GroupView {
    SVGLength mX;
    SVGLength mY;

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

    @Override
    void saveDefinition() {
        if (mName != null) {
            SvgView svg = getSvgView();
            svg.defineFilter(this, mName);
        }
    }
}
