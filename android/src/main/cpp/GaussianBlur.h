//
// Created by Ivliev Andrey on 14/04/2020.
//

#ifndef UNTITLED_GAUSSIANBLUR_H
#define UNTITLED_GAUSSIANBLUR_H


namespace RNSVGFilters {
    enum EdgeModeType {
        EDGEMODE_UNKNOWN = 0,
        EDGEMODE_DUPLICATE = 1,
        EDGEMODE_WRAP = 2,
        EDGEMODE_NONE = 3
    };

    class GaussianBlur {
    public:
        GaussianBlur(float x, float y, EdgeModeType edge);

        float stdDeviationX() const { return m_stdX; }

        void setStdDeviationX(float);

        float stdDeviationY() const { return m_stdY; }

        void setStdDeviationY(float);

        EdgeModeType edgeMode() const { return m_edgeMode; }

        void setEdgeMode(EdgeModeType);

        void applyFilter(uint8_t *ioBuffer, uint8_t *tmpPixelArray, uint paintSizeWidth,
                                  uint paintSizeHeight);


    private:
        float m_stdX;
        float m_stdY;
        EdgeModeType m_edgeMode;
    };
}
#endif //UNTITLED_GAUSSIANBLUR_H
