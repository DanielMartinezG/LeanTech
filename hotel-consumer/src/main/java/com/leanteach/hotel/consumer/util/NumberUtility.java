package com.leanteach.hotel.consumer.util;

public class NumberUtility {

  public static boolean isNotNullAndGreaterThanZero(Object number) {
    if (number instanceof Integer) {
      return isNotNullAndGreaterThanZeroInt(Integer.valueOf(number.toString()));
    } else if (number instanceof Long) {
      return isNotNullAndGreaterThanZeroLong(Long.valueOf(number.toString()));
    } else if (number instanceof Double) {
      return isNotNullAndGreaterThanZeroDouble(Double.valueOf(number.toString()));
    }
    return false;
  }

  private static boolean isNotNullAndGreaterThanZeroInt(Integer number) {
    return number > 0;
  }

  private static boolean isNotNullAndGreaterThanZeroLong(Long number) {
    return number > 0L;
  }

  private static boolean isNotNullAndGreaterThanZeroDouble(Double value) {
    return value > 0D;
  }
}
