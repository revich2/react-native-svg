package com.horcrux.svg;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Path;

import com.facebook.react.bridge.ReactContext;

import java.util.Map;

@SuppressLint("ViewConstructor")
class FEMergeView extends FilterPrimitiveView {
		boolean mHasSourceGraphicAsLastOutput;

		public FEMergeView(ReactContext reactContext) {
				super(reactContext);
		}

		public boolean hasSourceGraphicAsLastOutput() {
			return mHasSourceGraphicAsLastOutput;
		}

		@Override
		public Bitmap applyFilter(Map<String, Bitmap> results, Bitmap previousFilterResult, Path path) {
			// TODO: [FILTERS_TODO] filter is not implemented yet
			return previousFilterResult;
		}
}
