//
// Created by Ivliev Andrey on 14/04/2020.
//

#include <jni.h>
#include <string>
#include "GaussianBlur.h"

extern "C"
JNIEXPORT jstring JNICALL
Java_com_horcrux_svg_FEGaussianBlurView_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";

    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_horcrux_svg_FEGaussianBlurView_makeBlur(JNIEnv *env, jobject thiz, jbyteArray src_pixels,
                                                 jint width, jint height,
                                                 jdouble std_x, jdouble std_y, jint edge_mode) {
  jboolean isCopy = true;
  uint8_t* src = reinterpret_cast<uint8_t *>(*env->GetByteArrayElements(src_pixels, &isCopy));

  uint8_t* dst;

  GaussianBlur* filter = new GaussianBlur(std_x, std_y, static_cast<EdgeModeType>(edge_mode));
  filter->platformApplyGeneric(src, dst, width, height);

  return reinterpret_cast<jbyteArray>(dst);
}
