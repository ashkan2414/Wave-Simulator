import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class WaveCollection {

	public ArrayList<Wave> waves;
	private BufferedImage waveImage;
	private BufferedImage intensityImage;

	public WaveCollection() {

		waves = new ArrayList<Wave>();
	}

	public void update() {

		waveImage = new BufferedImage(Data.wg.width, Data.wg.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) waveImage.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, Data.wg.width, Data.wg.height);

		intensityImage = new BufferedImage(Data.wg.width, Data.wg.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D ig = (Graphics2D) intensityImage.getGraphics();
		ig.setColor(new Color(127, 0, 0));
		ig.fillRect(0, 0, Data.wg.width, Data.wg.height);

		for (int j = 0; j < waves.size(); j++) {

			waves.get(j).Update(g, ig);

			if (waves.get(j).getPixelRadius() - (waves.get(j).getWaveLength()) > Data.wg.width * 1.4f) {
				waves.remove(j);
			}
		}

		Data.wg.images.add(waveImage);
		Data.wg.intensityImages.add(intensityImage);
	}

}
