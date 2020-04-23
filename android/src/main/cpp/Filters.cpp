//
// Created by Ivliev Andrey on 14/04/2020.
//

#include <jni.h>
#include <string>
#include "GaussianBlur.h"

extern "C"
JNIEXPORT jshortArray JNICALL
Java_com_horcrux_svg_FiltersEngine_nativeGaussianBlur(JNIEnv *env, jobject thiz, jshortArray src_pixels,
                                                 jint width, jint height,
                                                 jdouble std_x, jdouble std_y, jint edge_mode) {

  jsize length = env->GetArrayLength(src_pixels);

  uint8_t* srcPixels = new uint8_t[length];
  uint8_t* dstPixels = new uint8_t[length];

  jshort* pSrcPixels = env->GetShortArrayElements(src_pixels, NULL);
  std::copy(pSrcPixels, pSrcPixels + length, srcPixels);

  env->ReleaseShortArrayElements(src_pixels, pSrcPixels, 0);

  GaussianBlur* filter = new GaussianBlur(std_x, std_y, static_cast<EdgeModeType>(edge_mode));
  filter->platformApplyGeneric(srcPixels, dstPixels, width, height);

  jshortArray result = env->NewShortArray(length);
  jshort* pResult = new jshort[length];
  std::copy(dstPixels, dstPixels + length, pResult);

  env->SetShortArrayRegion(result, 0, length, pResult);

  return result;
}
