package com.horcrux.svg;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Path;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

@SuppressLint("ViewConstructor")
class FEColorMatrixView extends FilterPrimitiveView {
		String mIn1;

		RNSVGColorMatrixTypes mType;

		enum RNSVGColorMatrixTypes {
				SVG_FECOLORMATRIX_TYPE_UNKNOWN,
				SVG_FECOLORMATRIX_TYPE_MATRIX,
				SVG_FECOLORMATRIX_TYPE_SATURATE,
				SVG_FECOLORMATRIX_TYPE_HUEROTATE,
				SVG_FECOLORMATRIX_TYPE_LUMINANCETOALPHA
		}

		public FEColorMatrixView(ReactContext reactContext) {
				super(reactContext);
		}

		@ReactProp(name="in1")
		public void setIn1(String in1) {
			mIn1 = in1;
			invalidate();
		}

		@ReactProp(name="type")
		public void setType(int type) {
				switch (type) {
					default:
					case 0:
						mType = RNSVGColorMatrixTypes.SVG_FECOLORMATRIX_TYPE_UNKNOWN;
						break;
					case 1:
						mType = RNSVGColorMatrixTypes.SVG_FECOLORMATRIX_TYPE_MATRIX;
						break;
					case 2:
						mType = RNSVGColorMatrixTypes.SVG_FECOLORMATRIX_TYPE_SATURATE;
						break;
					case 3:
						mType = RNSVGColorMatrixTypes.SVG_FECOLORMATRIX_TYPE_HUEROTATE;
						break;
					case 4:
						mType = RNSVGColorMatrixTypes.SVG_FECOLORMATRIX_TYPE_LUMINANCETOALPHA;
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
