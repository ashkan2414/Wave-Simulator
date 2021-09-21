import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class WaveGraphics {

	public int width, height;
	public ArrayList<BufferedImage> images;
	public ArrayList<BufferedImage> intensityImages;
	public ArrayList<Float> intensityMap;

	public WaveGraphics() {
		images = new ArrayList<BufferedImage>();
		intensityImages = new ArrayList<BufferedImage>();
	}

	public WaveGraphics(int w, int h) {
		images = new ArrayList<BufferedImage>();
		intensityImages = new ArrayList<BufferedImage>();
		width = w;
		height = h;
	}
}
