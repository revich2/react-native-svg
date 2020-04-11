package com.horcrux.svg;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.view.View;

import com.facebook.react.bridge.Dynamic;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.HashMap;
import java.util.Map;

@SuppressLint("ViewConstructor")
class FilterView extends GroupView {
    SVGLength mX;
    SVGLength mY;

    SVGLength mWidth;
    SVGLength mHeight;

    Brush.BrushUnits mFilterUnits;
    Brush.BrushUnits mPrimitiveUnits;

    Map<String, Bitmap> resultByName = new HashMap<>();

    boolean mHasSourceGraphicAsLastOutput;

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

    public Bitmap applyFilter(Bitmap img, Bitmap background, Path path) {
      resultByName.clear();

      Bitmap sourceAlpha = this.applySourceAlphaFilter(img);
      Bitmap backgroundAlpha = this.applySourceAlphaFilter(background);

      resultByName.put("SourceGraphic", img);
      resultByName.put("SourceAlpha", sourceAlpha);
      resultByName.put("BackgroundImage", background);
      resultByName.put("BackgroundAlpha", backgroundAlpha);

      Bitmap result = img;
			FilterPrimitiveView filterPrimitive = null;

      for (int i = 0; i < getChildCount(); i++) {
        View node = getChildAt(i);
        if (node instanceof FilterPrimitiveView) {
          filterPrimitive = (FilterPrimitiveView) node;

          result = filterPrimitive.applyFilter(this.resultByName, result, path);

          String resultName = filterPrimitive.mResult;
          if (resultName != null) {
            resultByName.put(resultName, result);
          }
        }
      }

      mHasSourceGraphicAsLastOutput = false;
      if (filterPrimitive != null && filterPrimitive instanceof FEMergeView) {
				FEMergeView m = (FEMergeView) filterPrimitive;
				mHasSourceGraphicAsLastOutput = m.hasSourceGraphicAsLastOutput();
			}

      return result;
    }

    private Bitmap applySourceAlphaFilter(Bitmap img) {
      return this.isolateAlphaChannel(img);
    }

    private Bitmap isolateAlphaChannel(Bitmap sourceImg) {
      int width = sourceImg.getWidth();
      int height = sourceImg.getHeight();

      int nPixels = width * height;
      int[] pixels = new int[nPixels];
      sourceImg.getPixels(pixels, 0, width, 0, 0, width, height);

      for (int i = 0; i < nPixels; i++) {
        int color = pixels[i];

        pixels[i] = color>>24 & 0xff;
      }

      Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
      result.setPixels(pixels, 0, width, 0, 0, width, height);

      return result;
    }

		public boolean hasSourceGraphicAsLastOutput() {
			return mHasSourceGraphicAsLastOutput;
		}

    @Override
    void saveDefinition() {
        if (mName != null) {
            SvgView svg = getSvgView();
            svg.defineFilter(this, mName);
        }
    }
}
