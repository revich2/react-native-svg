package com.horcrux.svg;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Path;

import com.facebook.react.bridge.Dynamic;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

@SuppressLint("ViewConstructor")
public class FEOffsetView extends FilterPrimitiveView {
		String mIn1;

		SVGLength mDx;
		SVGLength mDy;

		public FEOffsetView(ReactContext reactContext) {
				super(reactContext);
		}

		@ReactProp(name="in1")
		public void setIn1(String in1) {
			mIn1 = in1;
			invalidate();
		}

		@ReactProp(name="dx")
		public void setDx(Dynamic dx) {
			mDx = SVGLength.from(dx);
			invalidate();
		}

		@ReactProp(name="dy")
		public void setDy(Dynamic dy) {
			mDy = SVGLength.from(dy);
			invalidate();
		}

		@Override
		public Bitmap applyFilter(Map<String, Bitmap> results, Bitmap previousFilterResult, Path path) {
			// TODO: [FILTERS_TODO] filter is not implemented yet
			return previousFilterResult;
		}
}
