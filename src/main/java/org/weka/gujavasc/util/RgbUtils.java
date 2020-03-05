package org.weka.gujavasc.util;

import org.bytedeco.opencv.opencv_core.CvScalar;

public class RgbUtils {

  private static final int BLUE = 0;
  private static final int GREEN = 1;
  private static final int RED = 2;

  public static boolean isLaranjaCamisaBart(final CvScalar pixelRGB) {

    return pixelRGB.val(RgbUtils.BLUE) >= 11
        && pixelRGB.val(RgbUtils.BLUE) <= 22
        && pixelRGB.val(RgbUtils.GREEN) >= 85
        && pixelRGB.val(RgbUtils.GREEN) <= 105
        && pixelRGB.val(RgbUtils.RED) >= 240
        && pixelRGB.val(RgbUtils.RED) <= 255;
  }

  public static boolean isAzulCalcaoBart(final CvScalar pixelRGB) {
    return pixelRGB.val(RgbUtils.BLUE) >= 125
        && pixelRGB.val(RgbUtils.BLUE) <= 170
        && pixelRGB.val(RgbUtils.GREEN) >= 0
        && pixelRGB.val(RgbUtils.GREEN) <= 12
        && pixelRGB.val(RgbUtils.RED) >= 0
        && pixelRGB.val(RgbUtils.RED) <= 20;
  }

  public static boolean isAzulSapatoBart(final CvScalar pixelRGB) {
    return pixelRGB.val(RgbUtils.BLUE) >= 125
        && pixelRGB.val(RgbUtils.BLUE) <= 140
        && pixelRGB.val(RgbUtils.GREEN) >= 3
        && pixelRGB.val(RgbUtils.GREEN) <= 12
        && pixelRGB.val(RgbUtils.RED) >= 0
        && pixelRGB.val(RgbUtils.RED) <= 20;
  }

  public static boolean isAzulCalcaHomer(final CvScalar pixelRGB) {
    return pixelRGB.val(RgbUtils.BLUE) >= 150
        && pixelRGB.val(RgbUtils.BLUE) <= 180
        && pixelRGB.val(RgbUtils.GREEN) >= 98
        && pixelRGB.val(RgbUtils.GREEN) <= 120
        && pixelRGB.val(RgbUtils.RED) >= 0
        && pixelRGB.val(RgbUtils.RED) <= 90;
  }

  public static boolean isMarromBocaHomer(final CvScalar pixelRGB) {
    return pixelRGB.val(RgbUtils.BLUE) >= 95
        && pixelRGB.val(RgbUtils.BLUE) <= 140
        && pixelRGB.val(RgbUtils.GREEN) >= 160
        && pixelRGB.val(RgbUtils.GREEN) <= 185
        && pixelRGB.val(RgbUtils.RED) >= 175
        && pixelRGB.val(RgbUtils.RED) <= 200;
  }

  public static boolean isPretoSapatoHomer(final CvScalar pixelRGB) {
    return pixelRGB.val(RgbUtils.BLUE) >= 25
        && pixelRGB.val(RgbUtils.BLUE) <= 45
        && pixelRGB.val(RgbUtils.GREEN) >= 25
        && pixelRGB.val(RgbUtils.GREEN) <= 45
        && pixelRGB.val(RgbUtils.RED) >= 25
        && pixelRGB.val(RgbUtils.RED) <= 45;
  }
}
