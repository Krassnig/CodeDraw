package codedraw.drawing;

import java.awt.image.BufferedImage;

public enum BufferedImageType {
	CUSTOM(BufferedImage.TYPE_CUSTOM),
	INT_RGB(BufferedImage.TYPE_INT_RGB),
	INT_ARGB(BufferedImage.TYPE_INT_ARGB),
	INT_ARGB_PRE(BufferedImage.TYPE_INT_ARGB_PRE),
	INT_BGR(BufferedImage.TYPE_INT_BGR),
	THREE_BYTE_BGR(BufferedImage.TYPE_3BYTE_BGR),
	FOUR_BYTE_ABGR(BufferedImage.TYPE_4BYTE_ABGR),
	FOUR_BYTE_ABGR_PRE(BufferedImage.TYPE_4BYTE_ABGR_PRE),
	USHORT_565_RGB(BufferedImage.TYPE_USHORT_565_RGB),
	USHORT_555_RGB(BufferedImage.TYPE_USHORT_555_RGB),
	BYTE_GRAY(BufferedImage.TYPE_BYTE_GRAY),
	USHORT_GRAY(BufferedImage.TYPE_USHORT_GRAY),
	BYTE_BINARY(BufferedImage.TYPE_BYTE_BINARY),
	BYTE_INDEXED(BufferedImage.TYPE_BYTE_INDEXED);

	private final int type;

	BufferedImageType(int type) {
		this.type = type;
	}

	int getType() {
		return type;
	}
}
