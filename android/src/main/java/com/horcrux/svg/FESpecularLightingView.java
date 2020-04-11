package com.horcrux.svg;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Path;

import com.facebook.react.bridge.Dynamic;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

@SuppressLint("ViewConstructor")
public class FESpecularLightingView extends FilterPrimitiveView {
		String mIn1;

		SVGLength mSurfaceScale;
		SVGLength mSpecularConstant;
		SVGLength mSpecularExponent;
		SVGLength mKernelUnitLengthX;
		SVGLength mKernelUnitLengthY;

//		mLightingColor

		public FESpecularLightingView(ReactContext reactContext) {
				super(reactContext);
		}

		@ReactProp(name="in1")
		public void setIn1(String in1) {
				mIn1 = in1;
				invalidate();
		}

		@ReactProp(name="surfaceScale")
		public void setSurfaceScale(Dynamic surfaceScale) {
			mSurfaceScale = SVGLength.from(surfaceScale);
			invalidate();
		}

		@ReactProp(name="specularConstant")
		public void setSpecularConstant(Dynamic specularConstant) {
			mSpecularConstant = SVGLength.from(specularConstant);
			invalidate();
		}

		@ReactProp(name="specularExponent")
		public void setSpecularExponent(Dynamic specularExponent) {
			mSpecularExponent = SVGLength.from(specularExponent);
			invalidate();
		}

		@ReactProp(name="kernelUnitLengthX")
		public void setKernelUnitLengthX(Dynamic kernelUnitLengthX) {
			mKernelUnitLengthX = SVGLength.from(kernelUnitLengthX);
			invalidate();
		}

		@ReactProp(name="kernelUnitLengthY")
		public void setKernelUnitLengthY(Dynamic kernelUnitLengthY) {
			mKernelUnitLengthY = SVGLength.from(kernelUnitLengthY);
			invalidate();
		}

		@Override
		public Bitmap applyFilter(Map<String, Bitmap> results, Bitmap previousFilterResult, Path path) {
			// TODO: [FILTERS_TODO] filter is not implemented yet
			return previousFilterResult;
		}
}
