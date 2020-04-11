package com.horcrux.svg;

import android.annotation.SuppressLint;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

@SuppressLint("ViewConstructor")
class FEMergeNodeView extends FilterPrimitiveView {
		String mIn1;

		public FEMergeNodeView(ReactContext reactContext) {
				super(reactContext);
		}

		@ReactProp(name="in1")
		public void setIn1(String in1) {
				mIn1 = in1;
				invalidate();
		}
}
