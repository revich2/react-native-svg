package com.horcrux.svg;

import android.annotation.SuppressLint;

import com.facebook.react.bridge.Dynamic;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

@SuppressLint("ViewConstructor")
public class FEPointLightView extends FilterPrimitiveView {
		SVGLength mZ;

		public FEPointLightView(ReactContext reactContext) {
				super(reactContext);
		}

		@ReactProp(name="z")
		public void setZ(Dynamic z) {
			mZ = SVGLength.from(z);
			invalidate();
		}
}
