
package net.development.mitw.utils.holograms.touch;

public enum TouchAction {
	RIGHT_CLICK, LEFT_CLICK, UNKNOWN;

	TouchAction() {
	}

	public static TouchAction fromUseAction(Object useAction) {
		if (useAction == null) {
			return UNKNOWN;

		}

		final int i = ((TouchAction) useAction).ordinal();

		switch (i) {

		case 0:

			return RIGHT_CLICK;

		case 1:

			return LEFT_CLICK;

		}

		return UNKNOWN;

	}

}
