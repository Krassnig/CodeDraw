package codedraw;

/**
 * Color provides a wide variety of colors and makes it easier to create colors.
 * Color takes all the colors from the CSS colors but switches GRAY and DARK_GRAY,
 * so that DARK_GRAY is darker than GRAY.
 * If the CodeDraw Color does not match the AWT Color or CSS colors then there is a note in the documentation.
 */
public class Color {
	/**
	 * Creates a rgba color with the alpha implicitly being 255.
	 * 0 is no color. 255 is maximum color.
	 * @param red The value can range from 0 to 255.
	 * @param green The value can range from 0 to 255.
	 * @param blue The value can range from 0 to 255.
	 */
	public Color(int red, int green, int blue) {
		this(red, green, blue, 0xFF);
	}

	/**
	 * Creates a rgba color.
	 * 0 is no color. 255 is maximum color.
	 * @param red The value can range from 0 to 255.
	 * @param green The value can range from 0 to 255.
	 * @param blue The value can range from 0 to 255.
	 * @param transparency The value can range from 0 to 255. 0 is invisible. 255 is 100% visible.
	 */
	public Color(int red, int green, int blue, int transparency) {
		checkRange(red, "red", 0, 256);
		checkRange(green, "green", 0, 256);
		checkRange(blue, "blue", 0, 256);
		checkRange(transparency, "transparency", 0, 256);

		color = new java.awt.Color(red, green, blue, transparency);
	}

	public Color(java.awt.Color color) {
		this.color = color;
	}
	
	private final java.awt.Color color;

	public int getRed() {
		return color.getRed();
	}
	
	public int getGreen() {
		return color.getGreen();
	}
	
	public int getBlue() {
		return color.getBlue();
	}

	public int getTransparency() {
		return color.getAlpha();
	}
	
	public java.awt.Color toAWTColor() {
		return color;
	}

	@Override
	public boolean equals(Object o) {
		return color.equals(o);
	}

	@Override
	public int hashCode() {
		return color.hashCode();
	}

	@Override
	public String toString() {
		if (getTransparency() == 0xFF) {
			return String.format("Color[red=%d,green=%d,blue=%d]", getRed(), getGreen(), getBlue());
		}
		else {
			return String.format("Color[red=%d,green=%d,blue=%d,transparency=%d]", getRed(), getGreen(), getBlue(), getTransparency());
		}
	}

	/**
	 * Generates a random color. The color will not be transparent.
	 * @return a random color.
	 */
	public static Color randomColor() {
		return fromRGB((int)(Math.random() * (1 << 24)));
	}

	/**
	 * Creates a grayscale color where its color component (red, green, blue) all have the same value.
	 * 0 is white. 255 is black. 128 would be gray.
	 * @param gray The value can range from 0 to 255.
	 */
	public static Color fromGrayscale(int gray) {
		checkRange(gray, "gray", 0, 256);

		return new Color(gray, gray, gray);
	}

	/**
	 * Converts a color from the HSV color space to the RGB color space.
	 * See <a href="https://en.wikipedia.org/wiki/HSL_and_HSV">Wikipedia HSL and HSV</a> for more information.
	 * @param hue The coloration on a color wheel. Can be a value between 0 and 360.
	 * @param saturation The intensity of the color. Can be a value between 0 and 100.
	 * @param brightness The brightness or luminosity of the color. Can be a value between 0 and 100.
	 * @return an RGB Color.
	 */
	public static Color fromHSV(int hue, int saturation, int brightness) {
		checkRange(hue, "hue", 0, 361);
		checkRange(saturation, "saturation", 0, 101);
		checkRange(brightness, "brightness", 0, 101);

		int v = brightness * 3;
		int c = saturation * 3 * v;

		int x = c * (60 - Math.abs(hue % 120 - 60));
		int m = (v * 300 - c) * 60;
		c *= 60;

		int r;
		int g;
		int b;

		switch (hue / 60) {
			case 6:
			case 0: r = c; g = x; b = 0; break;
			case 1: r = x; g = c; b = 0; break;
			case 2: r = 0; g = c; b = x; break;
			case 3: r = 0; g = x; b = c; break;
			case 4: r = x; g = 0; b = c; break;
			case 5: r = c; g = 0; b = x; break;
			default: throw new RuntimeException("Invalid input for hue");
		}

		return new Color(toColorByte(r + m), toColorByte(g + m), toColorByte(b + m));
	}

	private static int toColorByte(int x) {
		return x * 255 / (300 * 300 * 60);
	}

	/**
	 * Creates a rgba color with the alpha implicitly being 255. The 8 most significant bits are ignored.
	 * The following 24 bits represent the red, green and blue amount of the color (8 bits each).
	 *
	 * For example Palette.fromRGB(0xFF00FF) would produce a pink color because both red and blue are set to 255.
	 * @param rgb The value can range from 0 to 16777216 (0xFFFFFF in hexadecimal)
	 */
	public static Color fromRGB(int rgb) {
		checkRange(rgb, "rgb", 0, (1 << 24) + 1);

		return new Color((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
	}

	/**
	 * Creates a rgba color.
	 * The 8 most significant bits represent red.
	 * The following 8 bits represent green.
	 * The next 8 bits represent blue .
	 * The 8 least significant bits represent the alpha value.
	 *
	 * For example Palette.fromRGBA(0xFF00FF80) would produce a pink color that is 50% transparent because both red and blue are set to 255 and the alpha value is 128.
	 * @param rgba any valid integer value.
	 */
	public static Color fromRGBA(int rgba) {
		return new Color((rgba >>> 24) & 0xFF, (rgba >> 16) & 0xFF, (rgba >> 8) & 0xFF, rgba & 0xFF);
	}

	/**
	 * Creates a new Color based of the baseColor but with a different transparency value.
	 * @param baseColor Any color. It's transparency value will be ignored when creating the new color.
	 * @param transparency The value can range from 0 to 255
	 */
	public static Color fromBaseColor(Color baseColor, int transparency) {
		if (baseColor == null) throw createParameterNullException("baseColor");
		checkRange(transparency, "transparency", 0, 256);

		return new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), transparency);
	}

	public static final Color TRANSPARENT = fromRGBA(0x00000000);
	public static final Color BLACK = fromRGB(0x000000);
	public static final Color NAVY = fromRGB(0x000080);
	public static final Color DARK_BLUE = fromRGB(0x00008B);
	public static final Color MEDIUM_BLUE = fromRGB(0x0000CD);
	/**
	 * This color is a synonym for {@link Color#AQUA}.
	 */
	public static final Color BLUE = fromRGB(0x0000FF);
	public static final Color DARK_GREEN = fromRGB(0x006400);
	/**
	 * This color differs from {@link java.awt.Color#GREEN}.
	 * If you want AWT Green use {@link Color#LIME} instead.
	 */
	public static final Color GREEN = fromRGB(0x008000);
	public static final Color TEAL = fromRGB(0x008080);
	public static final Color DARK_CYAN = fromRGB(0x008B8B);
	public static final Color DEEP_SKY_BLUE = fromRGB(0x00BFFF);
	public static final Color DARK_TURQUOISE = fromRGB(0x00CED1);
	public static final Color MEDIUM_SPRING_GREEN = fromRGB(0x00FA9A);
	public static final Color LIME = fromRGB(0x00FF00);
	public static final Color SPRING_GREEN = fromRGB(0x00FF7F);
	/**
	 * This color is a synonym for {@link Color#BLUE}.
	 */
	public static final Color AQUA = fromRGB(0x00FFFF);
	public static final Color CYAN = fromRGB(0x00FFFF);
	public static final Color MIDNIGHT_BLUE = fromRGB(0x191970);
	public static final Color DODGER_BLUE = fromRGB(0x1E90FF);
	public static final Color LIGHT_SEA_GREEN = fromRGB(0x20B2AA);
	public static final Color FOREST_GREEN = fromRGB(0x228B22);
	public static final Color SEA_GREEN = fromRGB(0x2E8B57);
	public static final Color DARK_SLATE_GRAY = fromRGB(0x2F4F4F);
	public static final Color LIME_GREEN = fromRGB(0x32CD32);
	public static final Color MEDIUM_SEA_GREEN = fromRGB(0x3CB371);
	public static final Color TURQUOISE = fromRGB(0x40E0D0);
	public static final Color ROYAL_BLUE = fromRGB(0x4169E1);
	public static final Color STEEL_BLUE = fromRGB(0x4682B4);
	public static final Color DARK_SLATE_BLUE = fromRGB(0x483D8B);
	public static final Color MEDIUM_TURQUOISE = fromRGB(0x48D1CC);
	public static final Color INDIGO = fromRGB(0x4B0082);
	public static final Color DARK_OLIVE_GREEN = fromRGB(0x556B2F);
	public static final Color CADET_BLUE = fromRGB(0x5F9EA0);
	public static final Color CORNFLOWER_BLUE = fromRGB(0x6495ED);
	public static final Color REBECCA_PURPLE = fromRGB(0x663399);
	public static final Color MEDIUM_AQUA_MARINE = fromRGB(0x66CDAA);
	public static final Color DIM_GRAY = fromRGB(0x696969);
	public static final Color SLATE_BLUE = fromRGB(0x6A5ACD);
	public static final Color OLIVE_DRAB = fromRGB(0x6B8E23);
	public static final Color SLATE_GRAY = fromRGB(0x708090);
	public static final Color LIGHT_SLATE_GRAY = fromRGB(0x778899);
	public static final Color MEDIUM_SLATE_BLUE = fromRGB(0x7B68EE);
	public static final Color LAWN_GREEN = fromRGB(0x7CFC00);
	public static final Color CHARTREUSE = fromRGB(0x7FFF00);
	public static final Color AQUAMARINE = fromRGB(0x7FFFD4);
	public static final Color MAROON = fromRGB(0x800000);
	public static final Color PURPLE = fromRGB(0x800080);
	public static final Color OLIVE = fromRGB(0x808000);
	/**
	 * This color differs from {@link java.awt.Color#GRAY}.
	 * If you want AWT gray use {@link Color#DARK_GRAY} instead.
	 * Note that this color does not match CSS gray but instead matches CSS dark gray.
	 */
	public static final Color GRAY = fromRGB(0xA9A9A9);
	public static final Color SKY_BLUE = fromRGB(0x87CEEB);
	public static final Color LIGHT_SKY_BLUE = fromRGB(0x87CEFA);
	public static final Color BLUE_VIOLET = fromRGB(0x8A2BE2);
	public static final Color DARK_RED = fromRGB(0x8B0000);
	public static final Color DARK_MAGENTA = fromRGB(0x8B008B);
	public static final Color SADDLE_BROWN = fromRGB(0x8B4513);
	public static final Color DARK_SEA_GREEN = fromRGB(0x8FBC8F);
	public static final Color LIGHT_GREEN = fromRGB(0x90EE90);
	public static final Color MEDIUM_PURPLE = fromRGB(0x9370DB);
	public static final Color DARK_VIOLET = fromRGB(0x9400D3);
	public static final Color PALE_GREEN = fromRGB(0x98FB98);
	public static final Color DARK_ORCHID = fromRGB(0x9932CC);
	public static final Color YELLOW_GREEN = fromRGB(0x9ACD32);
	public static final Color SIENNA = fromRGB(0xA0522D);
	public static final Color BROWN = fromRGB(0xA52A2A);
	/**
	 * This color differs from {@link java.awt.Color#DARK_GRAY}
	 * Note that this color does not match CSS dark gray but instead matches CSS gray.
	 */
	public static final Color DARK_GRAY = fromRGB(0x808080);
	public static final Color LIGHT_BLUE = fromRGB(0xADD8E6);
	public static final Color GREEN_YELLOW = fromRGB(0xADFF2F);
	public static final Color PALE_TURQUOISE = fromRGB(0xAFEEEE);
	public static final Color LIGHT_STEEL_BLUE = fromRGB(0xB0C4DE);
	public static final Color POWDER_BLUE = fromRGB(0xB0E0E6);
	public static final Color FIRE_BRICK = fromRGB(0xB22222);
	public static final Color DARK_GOLDEN_ROD = fromRGB(0xB8860B);
	public static final Color MEDIUM_ORCHID = fromRGB(0xBA55D3);
	public static final Color ROSY_BROWN = fromRGB(0xBC8F8F);
	public static final Color DARK_KHAKI = fromRGB(0xBDB76B);
	public static final Color SILVER = fromRGB(0xC0C0C0);
	public static final Color MEDIUM_VIOLET_RED = fromRGB(0xC71585);
	public static final Color INDIAN_RED = fromRGB(0xCD5C5C);
	public static final Color PERU = fromRGB(0xCD853F);
	public static final Color CHOCOLATE = fromRGB(0xD2691E);
	public static final Color TAN = fromRGB(0xD2B48C);
	/**
	 * This color differs from {@link java.awt.Color#LIGHT_GRAY}.
	 * If you want AWT light gray use {@link Color#SILVER} instead.
	 */
	public static final Color LIGHT_GRAY = fromRGB(0xD3D3D3);
	public static final Color THISTLE = fromRGB(0xD8BFD8);
	public static final Color ORCHID = fromRGB(0xDA70D6);
	public static final Color GOLDEN_ROD = fromRGB(0xDAA520);
	public static final Color PALE_VIOLET_RED = fromRGB(0xDB7093);
	public static final Color CRIMSON = fromRGB(0xDC143C);
	public static final Color GAINSBORO = fromRGB(0xDCDCDC);
	public static final Color PLUM = fromRGB(0xDDA0DD);
	public static final Color BURLY_WOOD = fromRGB(0xDEB887);
	public static final Color LIGHT_CYAN = fromRGB(0xE0FFFF);
	public static final Color LAVENDER = fromRGB(0xE6E6FA);
	public static final Color DARK_SALMON = fromRGB(0xE9967A);
	public static final Color VIOLET = fromRGB(0xEE82EE);
	public static final Color PALE_GOLDEN_ROD = fromRGB(0xEEE8AA);
	public static final Color LIGHT_CORAL = fromRGB(0xF08080);
	public static final Color KHAKI = fromRGB(0xF0E68C);
	public static final Color ALICE_BLUE = fromRGB(0xF0F8FF);
	public static final Color HONEY_DEW = fromRGB(0xF0FFF0);
	public static final Color AZURE = fromRGB(0xF0FFFF);
	public static final Color SANDY_BROWN = fromRGB(0xF4A460);
	public static final Color WHEAT = fromRGB(0xF5DEB3);
	public static final Color BEIGE = fromRGB(0xF5F5DC);
	public static final Color WHITE_SMOKE = fromRGB(0xF5F5F5);
	public static final Color MINT_CREAM = fromRGB(0xF5FFFA);
	public static final Color GHOST_WHITE = fromRGB(0xF8F8FF);
	public static final Color SALMON = fromRGB(0xFA8072);
	public static final Color ANTIQUE_WHITE = fromRGB(0xFAEBD7);
	public static final Color LINEN = fromRGB(0xFAF0E6);
	public static final Color LIGHT_GOLDEN_ROD_YELLOW = fromRGB(0xFAFAD2);
	public static final Color OLD_LACE = fromRGB(0xFDF5E6);
	public static final Color RED = fromRGB(0xFF0000);
	/**
	 * This color is a synonym for {@link Color#MAGENTA}.
	 */
	public static final Color FUCHSIA = fromRGB(0xFF00FF);
	/**
	 * This color is a synonym for {@link Color#FUCHSIA}.
	 */
	public static final Color MAGENTA = fromRGB(0xFF00FF);
	public static final Color DEEP_PINK = fromRGB(0xFF1493);
	public static final Color ORANGE_RED = fromRGB(0xFF4500);
	public static final Color TOMATO = fromRGB(0xFF6347);
	public static final Color HOT_PINK = fromRGB(0xFF69B4);
	public static final Color CORAL = fromRGB(0xFF7F50);
	public static final Color DARK_ORANGE = fromRGB(0xFF8C00);
	public static final Color LIGHT_SALMON = fromRGB(0xFFA07A);
	/**
	 * This color differs from {@link java.awt.Color#ORANGE}.
	 */
	public static final Color ORANGE = fromRGB(0xFFA500);
	public static final Color LIGHT_PINK = fromRGB(0xFFB6C1);
	/**
	 * This color differs from {@link java.awt.Color#PINK}.
	 */
	public static final Color PINK = fromRGB(0xFFC0CB);
	public static final Color GOLD = fromRGB(0xFFD700);
	public static final Color PEACH_PUFF = fromRGB(0xFFDAB9);
	public static final Color NAVAJO_WHITE = fromRGB(0xFFDEAD);
	public static final Color MOCCASIN = fromRGB(0xFFE4B5);
	public static final Color BISQUE = fromRGB(0xFFE4C4);
	public static final Color MISTY_ROSE = fromRGB(0xFFE4E1);
	public static final Color BLANCHED_ALMOND = fromRGB(0xFFEBCD);
	public static final Color PAPAYA_WHIP = fromRGB(0xFFEFD5);
	public static final Color LAVENDER_BLUSH = fromRGB(0xFFF0F5);
	public static final Color SEA_SHELL = fromRGB(0xFFF5EE);
	public static final Color CORNSILK = fromRGB(0xFFF8DC);
	public static final Color LEMON_CHIFFON = fromRGB(0xFFFACD);
	public static final Color FLORAL_WHITE = fromRGB(0xFFFAF0);
	public static final Color SNOW = fromRGB(0xFFFAFA);
	public static final Color YELLOW = fromRGB(0xFFFF00);
	public static final Color LIGHT_YELLOW = fromRGB(0xFFFFE0);
	public static final Color IVORY = fromRGB(0xFFFFF0);
	public static final Color WHITE = fromRGB(0xFFFFFF);

	private static IllegalArgumentException createParameterNullException(String parameterName) {
		return new IllegalArgumentException("The parameter " + parameterName + " cannot be null.");
	}

	private static void checkRange(int parameter, String parameterName, int minimumInclusive, int maximumExclusive) {
		if (!(minimumInclusive <= parameter && parameter < maximumExclusive)) {
			throw new IllegalArgumentException("The parameter " + parameterName + " must be great or equal to " + minimumInclusive + " and smaller than " + maximumExclusive);
		}
	}
}