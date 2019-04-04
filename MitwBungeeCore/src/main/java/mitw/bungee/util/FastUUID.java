package mitw.bungee.util;

import java.util.UUID;

public class FastUUID {
	
	private static final boolean USE_JDK_UUID_TO_STRING;
	private static final int UUID_STRING_LENGTH = 36;
	private static final char[] HEX_DIGITS;
	private static final long[] HEX_VALUES;

	public static UUID parseUUID(CharSequence uuidSequence) {
		if (uuidSequence.length() == 36 && uuidSequence.charAt(8) == 45 && uuidSequence.charAt(13) == 45
				&& uuidSequence.charAt(18) == 45 && uuidSequence.charAt(23) == 45) {
			long mostSignificantBits = getHexValueForChar(uuidSequence.charAt(0)) << 60;
			mostSignificantBits |= getHexValueForChar(uuidSequence.charAt(1)) << 56;
			mostSignificantBits |= getHexValueForChar(uuidSequence.charAt(2)) << 52;
			mostSignificantBits |= getHexValueForChar(uuidSequence.charAt(3)) << 48;
			mostSignificantBits |= getHexValueForChar(uuidSequence.charAt(4)) << 44;
			mostSignificantBits |= getHexValueForChar(uuidSequence.charAt(5)) << 40;
			mostSignificantBits |= getHexValueForChar(uuidSequence.charAt(6)) << 36;
			mostSignificantBits |= getHexValueForChar(uuidSequence.charAt(7)) << 32;
			mostSignificantBits |= getHexValueForChar(uuidSequence.charAt(9)) << 28;
			mostSignificantBits |= getHexValueForChar(uuidSequence.charAt(10)) << 24;
			mostSignificantBits |= getHexValueForChar(uuidSequence.charAt(11)) << 20;
			mostSignificantBits |= getHexValueForChar(uuidSequence.charAt(12)) << 16;
			mostSignificantBits |= getHexValueForChar(uuidSequence.charAt(14)) << 12;
			mostSignificantBits |= getHexValueForChar(uuidSequence.charAt(15)) << 8;
			mostSignificantBits |= getHexValueForChar(uuidSequence.charAt(16)) << 4;
			mostSignificantBits |= getHexValueForChar(uuidSequence.charAt(17));
			long leastSignificantBits = getHexValueForChar(uuidSequence.charAt(19)) << 60;
			leastSignificantBits |= getHexValueForChar(uuidSequence.charAt(20)) << 56;
			leastSignificantBits |= getHexValueForChar(uuidSequence.charAt(21)) << 52;
			leastSignificantBits |= getHexValueForChar(uuidSequence.charAt(22)) << 48;
			leastSignificantBits |= getHexValueForChar(uuidSequence.charAt(24)) << 44;
			leastSignificantBits |= getHexValueForChar(uuidSequence.charAt(25)) << 40;
			leastSignificantBits |= getHexValueForChar(uuidSequence.charAt(26)) << 36;
			leastSignificantBits |= getHexValueForChar(uuidSequence.charAt(27)) << 32;
			leastSignificantBits |= getHexValueForChar(uuidSequence.charAt(28)) << 28;
			leastSignificantBits |= getHexValueForChar(uuidSequence.charAt(29)) << 24;
			leastSignificantBits |= getHexValueForChar(uuidSequence.charAt(30)) << 20;
			leastSignificantBits |= getHexValueForChar(uuidSequence.charAt(31)) << 16;
			leastSignificantBits |= getHexValueForChar(uuidSequence.charAt(32)) << 12;
			leastSignificantBits |= getHexValueForChar(uuidSequence.charAt(33)) << 8;
			leastSignificantBits |= getHexValueForChar(uuidSequence.charAt(34)) << 4;
			leastSignificantBits |= getHexValueForChar(uuidSequence.charAt(35));
			return new UUID(mostSignificantBits, leastSignificantBits);
		} else {
			throw new IllegalArgumentException("Illegal UUID string: " + uuidSequence);
		}
	}

	public static String toString(UUID uuid) {
		if (USE_JDK_UUID_TO_STRING) {
			return uuid.toString();
		} else {
			long mostSignificantBits = uuid.getMostSignificantBits();
			long leastSignificantBits = uuid.getLeastSignificantBits();
			char[] uuidChars = new char[] { HEX_DIGITS[(int) ((mostSignificantBits & -1152921504606846976L) >>> 60)],
					HEX_DIGITS[(int) ((mostSignificantBits & 1080863910568919040L) >>> 56)],
					HEX_DIGITS[(int) ((mostSignificantBits & 67553994410557440L) >>> 52)],
					HEX_DIGITS[(int) ((mostSignificantBits & 4222124650659840L) >>> 48)],
					HEX_DIGITS[(int) ((mostSignificantBits & 263882790666240L) >>> 44)],
					HEX_DIGITS[(int) ((mostSignificantBits & 16492674416640L) >>> 40)],
					HEX_DIGITS[(int) ((mostSignificantBits & 1030792151040L) >>> 36)],
					HEX_DIGITS[(int) ((mostSignificantBits & 64424509440L) >>> 32)], '-',
					HEX_DIGITS[(int) ((mostSignificantBits & 4026531840L) >>> 28)],
					HEX_DIGITS[(int) ((mostSignificantBits & 251658240L) >>> 24)],
					HEX_DIGITS[(int) ((mostSignificantBits & 15728640L) >>> 20)],
					HEX_DIGITS[(int) ((mostSignificantBits & 983040L) >>> 16)], '-',
					HEX_DIGITS[(int) ((mostSignificantBits & 61440L) >>> 12)],
					HEX_DIGITS[(int) ((mostSignificantBits & 3840L) >>> 8)],
					HEX_DIGITS[(int) ((mostSignificantBits & 240L) >>> 4)],
					HEX_DIGITS[(int) (mostSignificantBits & 15L)], '-',
					HEX_DIGITS[(int) ((leastSignificantBits & -1152921504606846976L) >>> 60)],
					HEX_DIGITS[(int) ((leastSignificantBits & 1080863910568919040L) >>> 56)],
					HEX_DIGITS[(int) ((leastSignificantBits & 67553994410557440L) >>> 52)],
					HEX_DIGITS[(int) ((leastSignificantBits & 4222124650659840L) >>> 48)], '-',
					HEX_DIGITS[(int) ((leastSignificantBits & 263882790666240L) >>> 44)],
					HEX_DIGITS[(int) ((leastSignificantBits & 16492674416640L) >>> 40)],
					HEX_DIGITS[(int) ((leastSignificantBits & 1030792151040L) >>> 36)],
					HEX_DIGITS[(int) ((leastSignificantBits & 64424509440L) >>> 32)],
					HEX_DIGITS[(int) ((leastSignificantBits & 4026531840L) >>> 28)],
					HEX_DIGITS[(int) ((leastSignificantBits & 251658240L) >>> 24)],
					HEX_DIGITS[(int) ((leastSignificantBits & 15728640L) >>> 20)],
					HEX_DIGITS[(int) ((leastSignificantBits & 983040L) >>> 16)],
					HEX_DIGITS[(int) ((leastSignificantBits & 61440L) >>> 12)],
					HEX_DIGITS[(int) ((leastSignificantBits & 3840L) >>> 8)],
					HEX_DIGITS[(int) ((leastSignificantBits & 240L) >>> 4)],
					HEX_DIGITS[(int) (leastSignificantBits & 15L)] };
			return new String(uuidChars);
		}
	}

	static long getHexValueForChar(char c) {
		try {
			if (HEX_VALUES[c] < 0L) {
				throw new IllegalArgumentException("Illegal hexadecimal digit: " + c);
			}
		} catch (ArrayIndexOutOfBoundsException var2) {
			throw new IllegalArgumentException("Illegal hexadecimal digit: " + c);
		}

		return HEX_VALUES[c];
	}

	public static int getUuidStringLength() {
		return UUID_STRING_LENGTH;
	}

	static {
		int i = 0;

		try {
			i = Integer.parseInt(System.getProperty("java.specification.version"));
		} catch (NumberFormatException var2) {
			;
		}

		USE_JDK_UUID_TO_STRING = i >= 9;
		HEX_DIGITS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		HEX_VALUES = new long[128];

		for (i = 0; i < HEX_VALUES.length; ++i) {
			HEX_VALUES[i] = -1L;
		}

		HEX_VALUES[48] = 0L;
		HEX_VALUES[49] = 1L;
		HEX_VALUES[50] = 2L;
		HEX_VALUES[51] = 3L;
		HEX_VALUES[52] = 4L;
		HEX_VALUES[53] = 5L;
		HEX_VALUES[54] = 6L;
		HEX_VALUES[55] = 7L;
		HEX_VALUES[56] = 8L;
		HEX_VALUES[57] = 9L;
		HEX_VALUES[97] = 10L;
		HEX_VALUES[98] = 11L;
		HEX_VALUES[99] = 12L;
		HEX_VALUES[100] = 13L;
		HEX_VALUES[101] = 14L;
		HEX_VALUES[102] = 15L;
		HEX_VALUES[65] = 10L;
		HEX_VALUES[66] = 11L;
		HEX_VALUES[67] = 12L;
		HEX_VALUES[68] = 13L;
		HEX_VALUES[69] = 14L;
		HEX_VALUES[70] = 15L;
	}
}
