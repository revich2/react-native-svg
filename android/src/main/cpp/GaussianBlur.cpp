//
// Created by Ivliev Andrey on 14/04/2020.
//

#include <cstdint>
#include <algorithm>
#include "GaussianBlur.h"

#include "math.h"

static inline float gaussianKernelFactor() {
    return 3 / 4.f * sqrtf(2 * M_PI);
}

static const int gMaxKernelSize = 500;

namespace RNSVGFilters {
    static int clampedToKernelSize(float value) {
      // Limit the kernel size to 500. A bigger radius won't make a big difference for the result image but
      // inflates the absolute paint rect too much. This is compatible with Firefox' behavior.
      unsigned size = std::max<unsigned>(2, static_cast<unsigned>(floorf(
        value * gaussianKernelFactor() + 0.5f)));
      return std::min(size, static_cast<unsigned>(gMaxKernelSize));
    }

    inline void
    kernelPosition(int blurIteration, unsigned &radius, int &deltaLeft, int &deltaRight) {
      // Check http://www.w3.org/TR/SVG/filters.html#feGaussianBlurElement for details.
      switch (blurIteration) {
        case 0:
          if (!(radius % 2)) {
            deltaLeft = radius / 2 - 1;
            deltaRight = radius - deltaLeft;
          } else {
            deltaLeft = radius / 2;
            deltaRight = radius - deltaLeft;
          }
          break;
        case 1:
          if (!(radius % 2)) {
            deltaLeft++;
            deltaRight--;
          }
          break;
        case 2:
          if (!(radius % 2)) {
            deltaRight++;
            radius++;
          }
          break;
      }
    }

    GaussianBlur::GaussianBlur(float x, float y, EdgeModeType edge)
      : m_stdX(x), m_stdY(y), m_edgeMode(edge) {}

    void GaussianBlur::setStdDeviationX(float x) {
      m_stdX = x;
    }

    void GaussianBlur::setStdDeviationY(float y) {
      m_stdY = y;
    }

    void GaussianBlur::setEdgeMode(EdgeModeType edgeMode) {
      m_edgeMode = edgeMode;
    }

// This function only operates on Alpha channel.
    inline void boxBlurAlphaOnly(const uint8_t *srcPixelArray, uint8_t *dstPixelArray,
                                 unsigned dx, int &dxLeft, int &dxRight, int &stride,
                                 int &strideLine, int &effectWidth, int &effectHeight,
                                 const int &maxKernelSize) {
      const uint8_t *srcData = srcPixelArray;
      uint8_t *dstData = dstPixelArray;
      // Memory alignment is: RGBA, zero-index based.
      const int channel = 3;

      for (int y = 0; y < effectHeight; ++y) {
        int line = y * strideLine;
        int sum = 0;

        // Fill the kernel.
        for (int i = 0; i < maxKernelSize; ++i) {
          unsigned offset = line + i * stride;
          const uint8_t *srcPtr = srcData + offset;
          sum += srcPtr[channel];
        }

        // Blurring.
        for (int x = 0; x < effectWidth; ++x) {
          unsigned pixelByteOffset = line + x * stride + channel;
          uint8_t *dstPtr = dstData + pixelByteOffset;
          *dstPtr = static_cast<uint8_t>(sum / dx);

          // Shift kernel.
          if (x >= dxLeft) {
            unsigned leftOffset = pixelByteOffset - dxLeft * stride;
            const uint8_t *srcPtr = srcData + leftOffset;
            sum -= *srcPtr;
          }

          if (x + dxRight < effectWidth) {
            unsigned rightOffset = pixelByteOffset + dxRight * stride;
            const uint8_t *srcPtr = srcData + rightOffset;
            sum += *srcPtr;
          }
        }
      }
    }

    inline void boxBlur(const uint8_t *srcPixelArray, uint8_t *dstPixelArray,
                        unsigned dx, int dxLeft, int dxRight, int stride, int strideLine,
                        int effectWidth, int effectHeight, bool alphaImage, EdgeModeType edgeMode) {
      const int maxKernelSize = std::min(dxRight, effectWidth);

      if (alphaImage)
        return boxBlurAlphaOnly(srcPixelArray, dstPixelArray, dx, dxLeft, dxRight, stride,
                                strideLine, effectWidth, effectHeight, maxKernelSize);

      const uint8_t *srcData = srcPixelArray;
      uint8_t *dstData = dstPixelArray;

      // Concerning the array width/length: it is Element size + Margin + Border. The number of pixels will be
      // P = width * height * channels.
      for (int y = 0; y < effectHeight; ++y) {
        int line = y * strideLine;
        int sumR = 0, sumG = 0, sumB = 0, sumA = 0;

        if (edgeMode == EDGEMODE_NONE) {
          // Fill the kernel.
          for (int i = 0; i < maxKernelSize; ++i) {
            unsigned offset = line + i * stride;
            const uint8_t *srcPtr = srcData + offset;
            sumR += *srcPtr++;
            sumG += *srcPtr++;
            sumB += *srcPtr++;
            sumA += *srcPtr;
          }

          // Blurring.
          for (int x = 0; x < effectWidth; ++x) {
            unsigned pixelByteOffset = line + x * stride;
            uint8_t *dstPtr = dstData + pixelByteOffset;

            *dstPtr++ = static_cast<uint8_t>(sumR / dx);
            *dstPtr++ = static_cast<uint8_t>(sumG / dx);
            *dstPtr++ = static_cast<uint8_t>(sumB / dx);
            *dstPtr = static_cast<uint8_t>(sumA / dx);

            // Shift kernel.
            if (x >= dxLeft) {
              unsigned leftOffset = pixelByteOffset - dxLeft * stride;
              const uint8_t *srcPtr = srcData + leftOffset;
              sumR -= srcPtr[0];
              sumG -= srcPtr[1];
              sumB -= srcPtr[2];
              sumA -= srcPtr[3];
            }

            if (x + dxRight < effectWidth) {
              unsigned rightOffset = pixelByteOffset + dxRight * stride;
              const uint8_t *srcPtr = srcData + rightOffset;
              sumR += srcPtr[0];
              sumG += srcPtr[1];
              sumB += srcPtr[2];
              sumA += srcPtr[3];
            }
          }

        } else {
          // FIXME: Add support for 'wrap' here.
          // Get edge values for edgeMode 'duplicate'.
          const uint8_t *edgeValueLeft = srcData + line;
          const uint8_t *edgeValueRight = srcData + (line + (effectWidth - 1) * stride);

          // Fill the kernel.
          for (int i = dxLeft * -1; i < dxRight; ++i) {
            // Is this right for negative values of 'i'?
            unsigned offset = line + i * stride;
            const uint8_t *srcPtr = srcData + offset;

            if (i < 0) {
              sumR += edgeValueLeft[0];
              sumG += edgeValueLeft[1];
              sumB += edgeValueLeft[2];
              sumA += edgeValueLeft[3];
            } else if (i >= effectWidth) {
              sumR += edgeValueRight[0];
              sumG += edgeValueRight[1];
              sumB += edgeValueRight[2];
              sumA += edgeValueRight[3];
            } else {
              sumR += *srcPtr++;
              sumG += *srcPtr++;
              sumB += *srcPtr++;
              sumA += *srcPtr;
            }
          }

          // Blurring.
          for (int x = 0; x < effectWidth; ++x) {
            unsigned pixelByteOffset = line + x * stride;
            uint8_t *dstPtr = dstData + pixelByteOffset;

            *dstPtr++ = static_cast<uint8_t>(sumR / dx);
            *dstPtr++ = static_cast<uint8_t>(sumG / dx);
            *dstPtr++ = static_cast<uint8_t>(sumB / dx);
            *dstPtr = static_cast<uint8_t>(sumA / dx);

            // Shift kernel.
            if (x < dxLeft) {
              sumR -= edgeValueLeft[0];
              sumG -= edgeValueLeft[1];
              sumB -= edgeValueLeft[2];
              sumA -= edgeValueLeft[3];
            } else {
              unsigned leftOffset = pixelByteOffset - dxLeft * stride;
              const uint8_t *srcPtr = srcData + leftOffset;
              sumR -= srcPtr[0];
              sumG -= srcPtr[1];
              sumB -= srcPtr[2];
              sumA -= srcPtr[3];
            }

            if (x + dxRight >= effectWidth) {
              sumR += edgeValueRight[0];
              sumG += edgeValueRight[1];
              sumB += edgeValueRight[2];
              sumA += edgeValueRight[3];
            } else {
              unsigned rightOffset = pixelByteOffset + dxRight * stride;
              const uint8_t *srcPtr = srcData + rightOffset;
              sumR += srcPtr[0];
              sumG += srcPtr[1];
              sumB += srcPtr[2];
              sumA += srcPtr[3];
            }
          }
        }
      }
    }

    inline void standardBoxBlur(uint8_t *ioBuffer, uint8_t *tempBuffer, unsigned kernelSizeX,
                                unsigned kernelSizeY, int stride, uint paintSizeWidth,
                                uint paintSizeHeight, bool isAlphaImage, EdgeModeType edgeMode) {
      int dxLeft = 0;
      int dxRight = 0;
      int dyLeft = 0;
      int dyRight = 0;

      uint8_t *fromBuffer = ioBuffer;
      uint8_t *toBuffer = tempBuffer;

      for (int i = 0; i < 3; ++i) {
        if (kernelSizeX) {
          kernelPosition(i, kernelSizeX, dxLeft, dxRight);

          boxBlur(fromBuffer, toBuffer, kernelSizeX, dxLeft, dxRight, 4, stride, paintSizeWidth,
                  paintSizeHeight, isAlphaImage, edgeMode);
          std::swap(reinterpret_cast<std::tuple<uint8_t *> &>(fromBuffer),
                    reinterpret_cast<std::tuple<uint8_t *> &>(toBuffer));
        }

        if (kernelSizeY) {
          kernelPosition(i, kernelSizeY, dyLeft, dyRight);

          boxBlur(fromBuffer, toBuffer, kernelSizeY, dyLeft, dyRight, stride, 4, paintSizeHeight,
                  paintSizeWidth, isAlphaImage, edgeMode);
          std::swap(reinterpret_cast<std::tuple<uint8_t *> &>(fromBuffer),
                    reinterpret_cast<std::tuple<uint8_t *> &>(toBuffer));
        }
      }
    }

    void GaussianBlur::applyFilter(uint8_t *ioBuffer, uint8_t *tmpPixelArray,
                                            uint paintSizeWidth, uint paintSizeHeight) {
      int stride = 4 * paintSizeWidth;
      unsigned kernelSizeX = clampedToKernelSize(m_stdX);
      unsigned kernelSizeY = clampedToKernelSize(m_stdY);

      RNSVGFilters::standardBoxBlur(ioBuffer, tmpPixelArray, kernelSizeX, kernelSizeY, stride, paintSizeWidth,
                      paintSizeHeight, false, m_edgeMode);
    }
}
