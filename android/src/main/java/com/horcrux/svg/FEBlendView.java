package com.horcrux.svg;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Path;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

@SuppressLint("ViewConstructor")
class FEBlendView extends FilterPrimitiveView {
    String mIn1;
    String mIn2;

		RNSVGBlendModeTypes mMode;

    enum RNSVGBlendModeTypes {
				SVG_FEBLEND_MODE_UNKNOWN,
				SVG_FEBLEND_MODE_NORMAL,
				SVG_FEBLEND_MODE_MULTIPLY,
				SVG_FEBLEND_MODE_SCREEN,
				SVG_FEBLEND_MODE_DARKEN,
				SVG_FEBLEND_MODE_LIGHTEN
		}

		public FEBlendView(ReactContext reactContext) {
			super(reactContext);
		}

		@ReactProp(name="in1")
		public void setIn1(String in1) {
			mIn1 = in1;
			invalidate();
		}

		@ReactProp(name="in2")
		public void setIn2(String in2) {
			mIn2 = in2;
			invalidate();
		}

		@ReactProp(name="mode")
		public void setMode(int mode) {
			switch (mode) {
				default:
				case 0:
					mMode = RNSVGBlendModeTypes.SVG_FEBLEND_MODE_UNKNOWN;
					break;
				case 1:
					mMode = RNSVGBlendModeTypes.SVG_FEBLEND_MODE_NORMAL;
					break;
				case 2:
					mMode = RNSVGBlendModeTypes.SVG_FEBLEND_MODE_MULTIPLY;
					break;
				case 3:
					mMode = RNSVGBlendModeTypes.SVG_FEBLEND_MODE_SCREEN;
					break;
				case 4:
					mMode = RNSVGBlendModeTypes.SVG_FEBLEND_MODE_DARKEN;
					break;
				case 5:
					mMode = RNSVGBlendModeTypes.SVG_FEBLEND_MODE_LIGHTEN;
					break;
			}

			invalidate();
		}

		@Override
		public Bitmap applyFilter(Map<String, Bitmap> results, Bitmap previousFilterResult, Path path) {
			// TODO: [FILTERS_TODO] filter is not implemented yet
			return previousFilterResult;
		}
}
