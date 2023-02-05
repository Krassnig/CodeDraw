package auto;

import codedraw.Image;
import org.junit.Test;

public class ImageCreationTest {
	@Test(expected = RuntimeException.class)
	public void loadingNonExistentFileShouldThrow() {
		Image image = Image.fromFile("./image_that_does_not_exist.png");
	}

	@Test(expected = RuntimeException.class)
	public void loadingREADMEShouldThrow() {
		Image image = Image.fromFile("./README.md");
	}

	@Test(expected = RuntimeException.class)
	public void malformedUrlShouldThrow() {
		Image image = Image.fromUrl("gkdfg///::");
	}

	@Test(expected = RuntimeException.class)
	public void nonExistentFileShouldThrow() {
		Image image = Image.fromUrl("https://definetely-not-a-wesite.at/file/that/does/not/exist.png");
	}

	@Test(expected = RuntimeException.class)
	public void nonExistentFileInResourceShouldThrow() {
		Image image = Image.fromResource("./i_dont_exist.png");
	}

	@Test(expected = IllegalArgumentException.class)
	public void nonBase64StringToImage() {
		Image image = Image.fromBase64String("ÜÜÜÜÜÜ");
	}

	@Test(expected = RuntimeException.class)
	public void emptyBase64String() {
		Image image = Image.fromBase64String("");
	}
}
