package codedraw;

import java.awt.*;

/**
 * Palette provides a wide variety of colors and makes it easier to create colors.
 * Alternatively you can just use awt {@link java.awt.Color}.
 */
public final class Palette {
	private Palette() { }

	/**
	 * Generates a random color. The color will not be transparent.
	 * @return a random color.
	 */
	public static Color random() {
		return fromRGB((int)(Math.random() * (1 << 24)));
	}

	/**
	 * Creates a grayscale color where its color component (red, green, blue) all have the same value.
	 * 0 is white. 255 is black. 128 would be gray.
	 * @param gray The value can range from 0 to 255.
	 */
	public static Color fromGrayscale(int gray) {
		return fromRGB(gray, gray, gray);
	}

	/**
	 * Creates an rgba color with the alpha implicitly being 255. The 8 most significant bits are ignored.
	 * The following 24 bits represent the red, green and blue amount of the color (8 bits each).
	 *
	 * For example Palette.fromRGB(0xFF00FF) would produce a pink color because both red and blue are set to 255.
	 * @param rgb The value can range from 0 to 16777216 (0xFFFFFF in hexadecimal)
	 */
	public static Color fromRGB(int rgb) {
		return fromRGB((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
	}

	/**
	 * Creates an rgba color with the alpha implicitly being 255.
	 * 0 is no color. 255 is maximum color.
	 * @param red The value can range from 0 to 255.
	 * @param green The value can range from 0 to 255.
	 * @param blue The value can range from 0 to 255.
	 */
	public static Color fromRGB(int red, int green, int blue) {
		return fromRGBA(red, green, blue, 0xFF);
	}

	/**
	 * Creates an rgba color.
	 * The 8 most significant bits represent red.
	 * The following 8 bits represent green.
	 * The next 8 bits represent blue .
	 * The 8 least significant bits represent the alpha value.
	 *
	 * For example Palette.fromRGBA(0xFF00FF80) would produce a pink color that is 50% transparent because both red and blue are set to 255 and the alpha value is 128.
	 * @param rgba any valid integer value.
	 */
	public static Color fromRGBA(int rgba) {
		return fromRGBA((rgba >>> 24) & 0xFF, (rgba >> 16) & 0xFF, (rgba >> 8) & 0xFF, rgba & 0xFF);
	}

	/**
	 * Creates a new Color based of the baseColor but with a different alpha value.
	 * @param baseColor Any color. It's alpha value will be ignored when creating the new color.
	 * @param alpha The value can range from 0 to 255
	 */
	public static Color fromBaseColor(Color baseColor, int alpha) {
		return fromRGBA(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), alpha);
	}

	/**
	 * Creates an rgba color.
	 * 0 is no color. 255 is maximum color.
	 * @param red The value can range from 0 to 255.
	 * @param green The value can range from 0 to 255.
	 * @param blue The value can range from 0 to 255.
	 * @param alpha The value can range from 0 to 255. 0 is invisible. 255 is 100% visible.
	 */
	public static Color fromRGBA(int red, int green, int blue, int alpha) {
		return new Color(red, green, blue, alpha);
	}

	public static final Color BLACK = new Color(0x000000);
	public static final Color NAVY = new Color(0x000080);
	public static final Color DARK_BLUE = new Color(0x00008B);
	public static final Color MEDIUM_BLUE = new Color(0x0000CD);
	public static final Color BLUE = new Color(0x0000FF);
	public static final Color DARK_GREEN = new Color(0x006400);
	public static final Color GREEN = new Color(0x008000);
	public static final Color TEAL = new Color(0x008080);
	public static final Color DARK_CYAN = new Color(0x008B8B);
	public static final Color DEEP_SKY_BLUE = new Color(0x00BFFF);
	public static final Color DARK_TURQUOISE = new Color(0x00CED1);
	public static final Color MEDIUM_SPRING_GREEN = new Color(0x00FA9A);
	public static final Color LIME = new Color(0x00FF00);
	public static final Color SPRING_GREEN = new Color(0x00FF7F);
	public static final Color AQUA = new Color(0x00FFFF);
	public static final Color CYAN = new Color(0x00FFFF);
	public static final Color MIDNIGHT_BLUE = new Color(0x191970);
	public static final Color DODGER_BLUE = new Color(0x1E90FF);
	public static final Color LIGHT_SEA_GREEN = new Color(0x20B2AA);
	public static final Color FOREST_GREEN = new Color(0x228B22);
	public static final Color SEA_GREEN = new Color(0x2E8B57);
	public static final Color DARK_SLATE_GRAY = new Color(0x2F4F4F);
	public static final Color DARK_SLATE_GREY = new Color(0x2F4F4F);
	public static final Color LIME_GREEN = new Color(0x32CD32);
	public static final Color MEDIUM_SEA_GREEN = new Color(0x3CB371);
	public static final Color TURQUOISE = new Color(0x40E0D0);
	public static final Color ROYAL_BLUE = new Color(0x4169E1);
	public static final Color STEEL_BLUE = new Color(0x4682B4);
	public static final Color DARK_SLATE_BLUE = new Color(0x483D8B);
	public static final Color MEDIUM_TURQUOISE = new Color(0x48D1CC);
	public static final Color INDIGO = new Color(0x4B0082);
	public static final Color DARK_OLIVE_GREEN = new Color(0x556B2F);
	public static final Color CADET_BLUE = new Color(0x5F9EA0);
	public static final Color CORNFLOWER_BLUE = new Color(0x6495ED);
	public static final Color REBECCA_PURPLE = new Color(0x663399);
	public static final Color MEDIUM_AQUA_MARINE = new Color(0x66CDAA);
	public static final Color DIM_GRAY = new Color(0x696969);
	public static final Color DIM_GREY = new Color(0x696969);
	public static final Color SLATE_BLUE = new Color(0x6A5ACD);
	public static final Color OLIVE_DRAB = new Color(0x6B8E23);
	public static final Color SLATE_GRAY = new Color(0x708090);
	public static final Color SLATE_GREY = new Color(0x708090);
	public static final Color LIGHT_SLATE_GRAY = new Color(0x778899);
	public static final Color LIGHT_SLATE_GREY = new Color(0x778899);
	public static final Color MEDIUM_SLATE_BLUE = new Color(0x7B68EE);
	public static final Color LAWN_GREEN = new Color(0x7CFC00);
	public static final Color CHARTREUSE = new Color(0x7FFF00);
	public static final Color AQUAMARINE = new Color(0x7FFFD4);
	public static final Color MAROON = new Color(0x800000);
	public static final Color PURPLE = new Color(0x800080);
	public static final Color OLIVE = new Color(0x808000);
	public static final Color GRAY = new Color(0x808080);
	public static final Color GREY = new Color(0x808080);
	public static final Color SKY_BLUE = new Color(0x87CEEB);
	public static final Color LIGHT_SKY_BLUE = new Color(0x87CEFA);
	public static final Color BLUE_VIOLET = new Color(0x8A2BE2);
	public static final Color DARK_RED = new Color(0x8B0000);
	public static final Color DARK_MAGENTA = new Color(0x8B008B);
	public static final Color SADDLE_BROWN = new Color(0x8B4513);
	public static final Color DARK_SEA_GREEN = new Color(0x8FBC8F);
	public static final Color LIGHT_GREEN = new Color(0x90EE90);
	public static final Color MEDIUM_PURPLE = new Color(0x9370DB);
	public static final Color DARK_VIOLET = new Color(0x9400D3);
	public static final Color PALE_GREEN = new Color(0x98FB98);
	public static final Color DARK_ORCHID = new Color(0x9932CC);
	public static final Color YELLOW_GREEN = new Color(0x9ACD32);
	public static final Color SIENNA = new Color(0xA0522D);
	public static final Color BROWN = new Color(0xA52A2A);
	public static final Color DARK_GRAY = new Color(0xA9A9A9);
	public static final Color DARK_GREY = new Color(0xA9A9A9);
	public static final Color LIGHT_BLUE = new Color(0xADD8E6);
	public static final Color GREEN_YELLOW = new Color(0xADFF2F);
	public static final Color PALE_TURQUOISE = new Color(0xAFEEEE);
	public static final Color LIGHT_STEEL_BLUE = new Color(0xB0C4DE);
	public static final Color POWDER_BLUE = new Color(0xB0E0E6);
	public static final Color FIRE_BRICK = new Color(0xB22222);
	public static final Color DARK_GOLDEN_ROD = new Color(0xB8860B);
	public static final Color MEDIUM_ORCHID = new Color(0xBA55D3);
	public static final Color ROSY_BROWN = new Color(0xBC8F8F);
	public static final Color DARK_KHAKI = new Color(0xBDB76B);
	public static final Color SILVER = new Color(0xC0C0C0);
	public static final Color MEDIUM_VIOLET_RED = new Color(0xC71585);
	public static final Color INDIAN_RED = new Color(0xCD5C5C);
	public static final Color PERU = new Color(0xCD853F);
	public static final Color CHOCOLATE = new Color(0xD2691E);
	public static final Color TAN = new Color(0xD2B48C);
	public static final Color LIGHT_GRAY = new Color(0xD3D3D3);
	public static final Color LIGHT_GREY = new Color(0xD3D3D3);
	public static final Color THISTLE = new Color(0xD8BFD8);
	public static final Color ORCHID = new Color(0xDA70D6);
	public static final Color GOLDEN_ROD = new Color(0xDAA520);
	public static final Color PALE_VIOLET_RED = new Color(0xDB7093);
	public static final Color CRIMSON = new Color(0xDC143C);
	public static final Color GAINSBORO = new Color(0xDCDCDC);
	public static final Color PLUM = new Color(0xDDA0DD);
	public static final Color BURLY_WOOD = new Color(0xDEB887);
	public static final Color LIGHT_CYAN = new Color(0xE0FFFF);
	public static final Color LAVENDER = new Color(0xE6E6FA);
	public static final Color DARK_SALMON = new Color(0xE9967A);
	public static final Color VIOLET = new Color(0xEE82EE);
	public static final Color PALE_GOLDEN_ROD = new Color(0xEEE8AA);
	public static final Color LIGHT_CORAL = new Color(0xF08080);
	public static final Color KHAKI = new Color(0xF0E68C);
	public static final Color ALICE_BLUE = new Color(0xF0F8FF);
	public static final Color HONEY_DEW = new Color(0xF0FFF0);
	public static final Color AZURE = new Color(0xF0FFFF);
	public static final Color SANDY_BROWN = new Color(0xF4A460);
	public static final Color WHEAT = new Color(0xF5DEB3);
	public static final Color BEIGE = new Color(0xF5F5DC);
	public static final Color WHITE_SMOKE = new Color(0xF5F5F5);
	public static final Color MINT_CREAM = new Color(0xF5FFFA);
	public static final Color GHOST_WHITE = new Color(0xF8F8FF);
	public static final Color SALMON = new Color(0xFA8072);
	public static final Color ANTIQUE_WHITE = new Color(0xFAEBD7);
	public static final Color LINEN = new Color(0xFAF0E6);
	public static final Color LIGHT_GOLDEN_ROD_YELLOW = new Color(0xFAFAD2);
	public static final Color OLD_LACE = new Color(0xFDF5E6);
	public static final Color RED = new Color(0xFF0000);
	public static final Color FUCHSIA = new Color(0xFF00FF);
	public static final Color MAGENTA = new Color(0xFF00FF);
	public static final Color DEEP_PINK = new Color(0xFF1493);
	public static final Color ORANGE_RED = new Color(0xFF4500);
	public static final Color TOMATO = new Color(0xFF6347);
	public static final Color HOT_PINK = new Color(0xFF69B4);
	public static final Color CORAL = new Color(0xFF7F50);
	public static final Color DARK_ORANGE = new Color(0xFF8C00);
	public static final Color LIGHT_SALMON = new Color(0xFFA07A);
	public static final Color ORANGE = new Color(0xFFA500);
	public static final Color LIGHT_PINK = new Color(0xFFB6C1);
	public static final Color PINK = new Color(0xFFC0CB);
	public static final Color GOLD = new Color(0xFFD700);
	public static final Color PEACH_PUFF = new Color(0xFFDAB9);
	public static final Color NAVAJO_WHITE = new Color(0xFFDEAD);
	public static final Color MOCCASIN = new Color(0xFFE4B5);
	public static final Color BISQUE = new Color(0xFFE4C4);
	public static final Color MISTY_ROSE = new Color(0xFFE4E1);
	public static final Color BLANCHED_ALMOND = new Color(0xFFEBCD);
	public static final Color PAPAYA_WHIP = new Color(0xFFEFD5);
	public static final Color LAVENDER_BLUSH = new Color(0xFFF0F5);
	public static final Color SEA_SHELL = new Color(0xFFF5EE);
	public static final Color CORNSILK = new Color(0xFFF8DC);
	public static final Color LEMON_CHIFFON = new Color(0xFFFACD);
	public static final Color FLORAL_WHITE = new Color(0xFFFAF0);
	public static final Color SNOW = new Color(0xFFFAFA);
	public static final Color YELLOW = new Color(0xFFFF00);
	public static final Color LIGHT_YELLOW = new Color(0xFFFFE0);
	public static final Color IVORY = new Color(0xFFFFF0);
	public static final Color WHITE = new Color(0xFFFFFF);
}