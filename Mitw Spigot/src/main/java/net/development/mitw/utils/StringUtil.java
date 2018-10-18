package net.development.mitw.utils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;

import lombok.Getter;

public final class StringUtil {

	public static final String SPLIT_PATTERN = Pattern.compile("\\s").pattern();

	private static final List<String> VOWELS = Arrays.asList("a", "e", "u", "i", "o");
	private static final int INDEX_NOT_FOUND = -1;

	private StringUtil() {
		throw new RuntimeException("Cannot instantiate a utility class.");
	}

	public static String toNiceString(String string) {
		string = ChatColor.stripColor(string).replace('_', ' ').toLowerCase();

		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < string.toCharArray().length; i++) {
			char c = string.toCharArray()[i];
			if (i > 0) {
				final char prev = string.toCharArray()[i - 1];
				if (prev == ' ' || prev == '[' || prev == '(') {
					if (i == string.toCharArray().length - 1 || c != 'x' || !Character.isDigit(string.toCharArray()[i + 1])) {
						c = Character.toUpperCase(c);
					}
				}
			} else {
				if (c != 'x' || !Character.isDigit(string.toCharArray()[i + 1])) {
					c = Character.toUpperCase(c);
				}
			}
			sb.append(c);
		}

		return sb.toString();
	}

	public static String buildMessage(final String[] args, final int start) {
		if (start >= args.length)
			return "";
		return ChatColor.stripColor(String.join(" ", Arrays.copyOfRange(args, start, args.length)));
	}

	public static String getFirstSplit(final String s) {
		return s.split(SPLIT_PATTERN)[0];
	}

	public static String getAOrAn(final String input) {
		return ((VOWELS.contains(input.substring(0, 1).toLowerCase())) ? "an" : "a");
	}

	public static boolean isEmpty(final CharSequence cs) {
		return cs == null || cs.length() == 0;
	}

	public static String replace(final String text, final String searchString, final String replacement) {
		if (isEmpty(text) || isEmpty(searchString) || replacement == null)
			return text;
		final String searchText = text;
		int start = 0;
		int end = searchText.indexOf(searchString, start);
		if (end == INDEX_NOT_FOUND)
			return text;
		final int replLength = searchString.length();
		int increase = replacement.length() - replLength;
		increase = increase < 0 ? 0 : increase;
		increase *= 16;
		final StringBuilder buf = new StringBuilder(text.length() + increase);
		while (end != INDEX_NOT_FOUND) {
			buf.append(text, start, end).append(replacement);
			start = end + replLength;
			end = searchText.indexOf(searchString, start);
		}
		buf.append(text, start, text.length());
		return buf.toString();
	}

	public static String replace(final String text, final String searchString, final Object replacement) {
		return replace(text, searchString, replacement.toString());
	}

	public static String replace(String text, final RV... replaceValues) {
		for (final RV replaceValue : replaceValues) {
			text = ChatColor.translateAlternateColorCodes('&', StringUtil.replace(text, replaceValue.getTarget(), replaceValue.getReplacement()));
		}
		return text;
	}

	public static Entry split(final String text) {
		final Entry entry = new Entry();
		if (text.length() <= 16) {
			entry.left = text;
			entry.right = "";
		} else {
			String prefix = text.substring(0, 16), suffix = "";
			if (prefix.endsWith("\u00a7")) {
				prefix = prefix.substring(0, prefix.length() - 1);
				suffix = "\u00a7" + suffix;
			}
			suffix = StringUtils.left(ChatColor.getLastColors(prefix) + suffix + text.substring(16), 16);
			entry.left = prefix;
			entry.right = suffix;
		}
		return entry;
	}

	public static class Entry {

		@Getter
		private String left = "", right = "";

	}

	public static String cc(final String textToTranslate) {
		final char altColorChar = '&';
		final char[] b = textToTranslate.toCharArray();

		for (int i = 0; i < b.length - 1; ++i) {
			if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) {
				b[i] = 167;
				b[i + 1] = Character.toLowerCase(b[i + 1]);
			}
		}

		return new String(b);
	}

}
