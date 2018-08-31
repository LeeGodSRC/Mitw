package net.development.mitw.utils;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public final class MathUtil {

	public static String convertTicksToMinutes(int ticks) {
		long minute = (long) ticks / 1200L;
		long second = (long) ticks / 20L - (minute * 60L);

		String secondString = Math.round(second) + "";

		if (second < 10) {
			secondString = 0 + secondString;
		}
		String minuteString = Math.round(minute) + "";

		if (minute == 0) {
			minuteString = 0 + "";
		}

		return minuteString + ":" + secondString;
	}

	public static String convertToRomanNumeral(int Int) {
		LinkedHashMap<String, Integer> roman_numerals = new LinkedHashMap<String, Integer>();
		roman_numerals.put("M", Integer.valueOf(1000));
		roman_numerals.put("CM", Integer.valueOf(900));
		roman_numerals.put("D", Integer.valueOf(500));
		roman_numerals.put("CD", Integer.valueOf(400));
		roman_numerals.put("C", Integer.valueOf(100));
		roman_numerals.put("XC", Integer.valueOf(90));
		roman_numerals.put("L", Integer.valueOf(50));
		roman_numerals.put("XL", Integer.valueOf(40));
		roman_numerals.put("X", Integer.valueOf(10));
		roman_numerals.put("IX", Integer.valueOf(9));
		roman_numerals.put("V", Integer.valueOf(5));
		roman_numerals.put("IV", Integer.valueOf(4));
		roman_numerals.put("I", Integer.valueOf(1));
		String res = "";

		Entry<?, ?> entry;
		for (Iterator<?> arg2 = roman_numerals.entrySet().iterator(); arg2
				.hasNext(); Int %= ((Integer) entry.getValue()).intValue()) {
			entry = (Entry<?, ?>) arg2.next();
			int matches = Int / ((Integer) entry.getValue()).intValue();
			res = res + repeat((String) entry.getKey(), matches);
		}

		return res;
	}

	private static String repeat(String s, int n) {
		if (s == null) {
			return null;
		} else {
			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < n; ++i) {
				sb.append(s);
			}

			return sb.toString();
		}
	}

	public static int absolteValue(int i) {
		return Math.abs(i);
	}

	public static double roundToHalves(double d) {
		return Math.round(d * 2.0D) / 2.0D;
	}
}
