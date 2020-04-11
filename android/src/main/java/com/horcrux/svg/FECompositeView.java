package com.horcrux.svg;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Path;

import com.facebook.react.bridge.Dynamic;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

@SuppressLint("ViewConstructor")
class FECompositeView extends FilterPrimitiveView {
		String mIn1;
		String mIn2;

		RNSVGCompositeOperators mOperator;

		SVGLength mK1;
		SVGLength mK2;
		SVGLength mK3;
		SVGLength mK4;

		enum RNSVGCompositeOperators {
				SVG_FECOMPOSITE_OPERATOR_UNKNOWN,
				SVG_FECOMPOSITE_OPERATOR_OVER,
				SVG_FECOMPOSITE_OPERATOR_IN,
				SVG_FECOMPOSITE_OPERATOR_OUT,
				SVG_FECOMPOSITE_OPERATOR_ATOP,
				SVG_FECOMPOSITE_OPERATOR_XOR,
				SVG_FECOMPOSITE_OPERATOR_ARITHMETIC
		}

		public FECompositeView(ReactContext reactContext) {
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

		@ReactProp(name="k1")
		public void setK1(Dynamic k1) {
				mK1 = SVGLength.from(k1);
				invalidate();
		}

		@ReactProp(name="k2")
		public void setK2(Dynamic k2) {
				mK2 = SVGLength.from(k2);
				invalidate();
		}

		@ReactProp(name="k3")
		public void setK3(Dynamic k3) {
				mK3 = SVGLength.from(k3);
				invalidate();
		}

		@ReactProp(name="k4")
		public void setK4(Dynamic k4) {
				mK4 = SVGLength.from(k4);
				invalidate();
		}

		@ReactProp(name="operator")
		public void setMode(int operator) {
			switch (operator) {
				default:
				case 0:
					mOperator = RNSVGCompositeOperators.SVG_FECOMPOSITE_OPERATOR_UNKNOWN;
					break;
				case 1:
					mOperator = RNSVGCompositeOperators.SVG_FECOMPOSITE_OPERATOR_OVER;
					break;
				case 2:
					mOperator = RNSVGCompositeOperators.SVG_FECOMPOSITE_OPERATOR_IN;
					break;
				case 3:
					mOperator = RNSVGCompositeOperators.SVG_FECOMPOSITE_OPERATOR_OUT;
					break;
				case 4:
					mOperator = RNSVGCompositeOperators.SVG_FECOMPOSITE_OPERATOR_ATOP;
					break;
				case 5:
					mOperator = RNSVGCompositeOperators.SVG_FECOMPOSITE_OPERATOR_XOR;
					break;
				case 6:
					mOperator = RNSVGCompositeOperators.SVG_FECOMPOSITE_OPERATOR_ARITHMETIC;
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
