package codedraw.drawing;

/**
 * Interpolation defines how drawn images are upscaled and downscaled.
 * For more details see <a href="https://en.wikipedia.org/wiki/Bicubic_interpolation">Wikipedia Bicubic Interpolation</a>
 * and <a href="https://en.wikipedia.org/wiki/Image_scaling">Wikipedia Image Scaling</a>.
 */
public enum Interpolation {
	/**
	 * When increasing or decreasing the size of an image the nearest neighbor is picked to color a pixel.
	 * Sharpness of edges is preserved but images can look very jagged.
	 */
	NEAREST_NEIGHBOR,
	/**
	 * Bilinear interpolation interpolates pixel values via linear equations.
	 * Bilinear reduces contrast and makes images appear less sharp.
	 */
	BILINEAR,
	/**
	 * Bicubic interpolation interpolates pixel using a cubic equation.
	 * Bicubic interpolation usually looks pretty good and smooth, but is slow.
	 */
	BICUBIC
}
