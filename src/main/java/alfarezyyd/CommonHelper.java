package alfarezyyd;

import alfarezyyd.constant.UnitConstant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static alfarezyyd.constant.UnitConstant.POUND_TO_KILOGRAM;

public class CommonHelper {

  public static float convertFromCurrencyIntoInteger(String currencyString) {

    char currencyChar = 0;
    // Regular expression to match 'M' or 'K' anywhere in the string
    Pattern pattern = Pattern.compile("[MK]");
    Matcher matcher = pattern.matcher(currencyString);
    if (matcher.find()) {
      currencyChar = matcher.group().charAt(0);
    }
    try {
      final float currencyValue = Float.parseFloat(currencyString.substring(1, currencyString.length() - 1));
      return switch (currencyChar) {
        case 'M' -> currencyValue * 1_000_000_000;
        case 'K' -> currencyValue * 1_000_000;
        default -> Float.parseFloat(currencyString);
      };
    } catch (NumberFormatException e) {
      return 0;
    }

  }


  public static float convertUnitIntoInteger(String unitString) {
    if(unitString.isBlank() || unitString.isEmpty()){
      return 0;
    }
    char unitChar = 0;
    // Regular expression to match 'M' or 'K' anywhere in the string
    Pattern pattern = Pattern.compile("[MK]");
    Matcher matcher = pattern.matcher(unitString);
    if (matcher.find()) {
      unitChar = matcher.group().charAt(0);
    } else {
      return Float.parseFloat(unitString);
    }
    final float currencyValue = Float.parseFloat(unitString.substring(0, unitString.length() - 1));
    return switch (unitChar) {
      case 'M' -> currencyValue * 1_000_000_000;
      case 'K' -> currencyValue * 1_000_000;
      default -> Integer.parseInt(unitString);
    };
  }

  public static float convertFeetIntoCentimeter(String heightColumn) {
    if (!heightColumn.contains("cm")) {
      return feetInchIntoCentimeters(heightColumn);
    } else
      return Integer.parseInt(heightColumn.substring(0, heightColumn.indexOf("cm")));
  }

  public static float feetInchIntoCentimeters(String feetInch) {
    // Memisahkan kaki dan inci dari string input
    String[] parts = feetInch.split("'");
    float feet = Float.parseFloat(parts[0].trim());
    float inches = Float.parseFloat(parts[1].trim().replace("\"", ""));

    // Mengonversi kaki dan inci ke sentimeter
    float centimetersFromFeet = feet * UnitConstant.FEET_TO_CENTIMETERS;
    float centimetersFromInches = inches * UnitConstant.INCH_TO_CENTIMETERS;

    // Menjumlahkan hasil konversi
    return centimetersFromFeet + centimetersFromInches;
  }

  public static float convertPoundIntoKilogram(String poundString) {
    if (!poundString.contains("kg")) {
      // Konversi pound ke kilogram
      float kilograms = Integer.parseInt(poundString.substring(0, poundString.indexOf("l"))) * UnitConstant.POUND_TO_KILOGRAM;

      // Ekstrak bagian kilogram dan gram
      int kg = (int) kilograms;
      int grams = (int) ((kilograms - kg) * 1000);
      return kg + grams;
    } else {
      return Float.parseFloat( poundString.substring(0, poundString.indexOf("kg")));
    }
  }
}


