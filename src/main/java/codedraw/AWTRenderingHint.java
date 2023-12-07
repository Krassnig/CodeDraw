package codedraw;

import java.awt.*;

interface AWTRenderingHint {
	RenderingHints.Key key();

	Object value();

	default void applyTo(Graphics2D graphics) {
		graphics.setRenderingHint(key(), value());
	}

	enum AlphaInterpolation implements AWTRenderingHint {
		DEFAULT(RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT),
		SPEED(RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED),
		QUALITY(RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

		AlphaInterpolation(Object value) {
			this.value = value;
		}

		private final Object value;

		public Object value() {
			return value;
		}

		public RenderingHints.Key key() {
			return RenderingHints.KEY_ALPHA_INTERPOLATION;
		}
	}

	enum AntiAliasing implements AWTRenderingHint {
		DEFAULT(RenderingHints.VALUE_ANTIALIAS_DEFAULT),
		OFF(RenderingHints.VALUE_ANTIALIAS_OFF),
		ON(RenderingHints.VALUE_ANTIALIAS_ON);

		AntiAliasing(Object value) {
			this.value = value;
		}

		private final Object value;

		public Object value() {
			return value;
		}

		public RenderingHints.Key key() {
			return RenderingHints.KEY_ANTIALIASING;
		}
	}

	enum ColorRendering implements AWTRenderingHint {
		DEFAULT(RenderingHints.VALUE_COLOR_RENDER_DEFAULT),
		SPEED(RenderingHints.VALUE_COLOR_RENDER_SPEED),
		QUALITY(RenderingHints.VALUE_COLOR_RENDER_QUALITY);

		ColorRendering(Object value) {
			this.value = value;
		}

		private final Object value;

		public Object value() {
			return value;
		}

		public RenderingHints.Key key() {
			return RenderingHints.KEY_COLOR_RENDERING;
		}
	}

	enum Dithering implements AWTRenderingHint {
		DEFAULT(RenderingHints.VALUE_DITHER_DEFAULT),
		DISABLE(RenderingHints.VALUE_DITHER_DISABLE),
		ENABLE(RenderingHints.VALUE_DITHER_ENABLE);

		Dithering(Object value) {
			this.value = value;
		}

		private final Object value;

		public Object value() {
			return value;
		}

		public RenderingHints.Key key() {
			return RenderingHints.KEY_DITHERING;
		}
	}
	enum FractionalMetrics implements AWTRenderingHint {
		DEFAULT(RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT),
		OFF(RenderingHints.VALUE_FRACTIONALMETRICS_OFF),
		ON(RenderingHints.VALUE_FRACTIONALMETRICS_ON);

		FractionalMetrics(Object value) {
			this.value = value;
		}

		private final Object value;

		public Object value() {
			return value;
		}

		public RenderingHints.Key key() {
			return RenderingHints.KEY_FRACTIONALMETRICS;
		}
	}

	enum Interpolation implements AWTRenderingHint {
		NEAREST_NEIGHBOR(RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR),
		BILINEAR(RenderingHints.VALUE_INTERPOLATION_BILINEAR),
		BICUBIC(RenderingHints.VALUE_INTERPOLATION_BICUBIC);

		Interpolation(Object value) {
			this.value = value;
		}

		private final Object value;

		public Object value() {
			return value;
		}

		public RenderingHints.Key key() {
			return RenderingHints.KEY_INTERPOLATION;
		}

		public static AWTRenderingHint.Interpolation from(codedraw.Interpolation interpolation) {
			switch (interpolation) {
				case NEAREST_NEIGHBOR: return NEAREST_NEIGHBOR;
				case BILINEAR: return BILINEAR;
				case BICUBIC: return BICUBIC;
				default: throw new RuntimeException("Invalid or unknown interpolation");
			}
		}
	}

	enum Rendering implements AWTRenderingHint {
		DEFAULT(RenderingHints.VALUE_RENDER_DEFAULT),
		SPEED(RenderingHints.VALUE_RENDER_SPEED),
		QUALITY(RenderingHints.VALUE_RENDER_QUALITY);

		Rendering(Object value) {
			this.value = value;
		}

		private final Object value;

		public Object value() {
			return value;
		}

		public RenderingHints.Key key() {
			return RenderingHints.KEY_RENDERING;
		}
	}

	enum ResolutionVariant implements AWTRenderingHint {
		DEFAULT(RenderingHints.VALUE_RESOLUTION_VARIANT_DEFAULT),
		BASE(RenderingHints.VALUE_RESOLUTION_VARIANT_BASE),
		DPI_FIT(RenderingHints.VALUE_RESOLUTION_VARIANT_DPI_FIT),
		SIZE_FIT(RenderingHints.VALUE_RESOLUTION_VARIANT_SIZE_FIT);

		ResolutionVariant(Object value) {
			this.value = value;
		}

		private final Object value;

		public Object value() {
			return value;
		}

		public RenderingHints.Key key() {
			return RenderingHints.KEY_RESOLUTION_VARIANT;
		}
	}

	enum StrokeControl implements AWTRenderingHint {
		DEFAULT(RenderingHints.VALUE_STROKE_DEFAULT),
		PURE(RenderingHints.VALUE_STROKE_PURE),
		NORMALIZE(RenderingHints.VALUE_STROKE_NORMALIZE);

		StrokeControl(Object value) {
			this.value = value;
		}

		private final Object value;

		public Object value() {
			return value;
		}

		public RenderingHints.Key key() {
			return RenderingHints.KEY_STROKE_CONTROL;
		}
	}

	enum TextAntiAliasing implements AWTRenderingHint {
		DEFAULT(RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT),
		OFF(RenderingHints.VALUE_TEXT_ANTIALIAS_OFF),
		ON(RenderingHints.VALUE_TEXT_ANTIALIAS_ON),
		GAPS(RenderingHints.VALUE_TEXT_ANTIALIAS_GASP),
		LCD_HBGR(RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR),
		LCD_HRGB(RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB),
		LCD_VBGR(RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VBGR),
		LCD_VRGB(RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VRGB);

		TextAntiAliasing(Object value) {
			this.value = value;
		}

		private final Object value;

		public Object value() {
			return value;
		}

		public RenderingHints.Key key() {
			return RenderingHints.KEY_TEXT_ANTIALIASING;
		}
	}
}
