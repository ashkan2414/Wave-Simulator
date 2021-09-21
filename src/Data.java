import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Data {

	public static float deltaTime = 0.015f;
	public static WaveGraphics wg = new WaveGraphics();
	public static int WaveResolution = 0;

	public static ArrayList<Emitter> emitters = new ArrayList<Emitter>();
	public static int selectedEmitter = -1;
	public static ArrayList<WaveCollection> waveCollections = new ArrayList<WaveCollection>();
	public static SensorPanel sensor = new SensorPanel();
	public static int selectedSensorType = 0;

	public static final float c = (float) 3e8;
	public static final float soundSpeed = 341;
	public static float timeScale = (float) 3e-15;
	public static float pixelScale = (float) 4e-9;
	public static int selectedWaveType = 1;

	public static final WaveType generic = new WaveType(1f, 11f, 1f, 11f, 5f, 5f, 0.6f, 0.1f);
	public static final WaveType sound = new WaveType(100f, 1100f, 0.31f, 3.41f, 500f, 0.682f, 0.005f, 0.02f);
	public static final WaveType light = new WaveType((float) 3e14, (float) 1e15, (float) 3e-7, (float) 1e-6,
			(float) 5.8e14, (float) 5.17e-7, (float) 3e-15, (float) 4e-9);
	public static final ArrayList<WaveType> waveTypes = new ArrayList<WaveType>();

	public static final String[] metricPrefixes = { "a", "f", "p", "n", "u", "m", "", "k", "M", "G", "T", "P", "E" };
	public static final String[] timeUnits = { "as", "fs", "ps", "ns", "us", "ms", "s", "ks", "Ms", "Gs", "Ts", "Ps",
			"Es" };
	public static final String[] metricUnits = { "am", "fm", "pm", "nm", "um", "mm", "m", "km", "Mm", "Gm", "Tm", "Pm",
			"Em" };
	public static final int[] metricScales = { -18, -15, -12, -9, -6, -3, 0, 3, 6, 9, 12, 15, 18 };

	public static final int WAVE_TYPE_LIGHT = 1;

	public static final Color genericWaveColor = new Color(255, 0, 0);
	public static final Color soundWaveColor = new Color(150, 200, 255);

	public Data() {

		waveTypes.add(generic);
		waveTypes.get(0).waveColor = genericWaveColor;

		waveTypes.add(light);
		waveTypes.get(WAVE_TYPE_LIGHT).SetConstantSpeed(c);

		waveTypes.add(sound);
		waveTypes.get(2).SetConstantSpeed(soundSpeed);
		waveTypes.get(2).waveColor = soundWaveColor;

	}

	public static int GetUnit(float value) {

		float scaledValue = value;
		int counter = 0;

		if (scaledValue >= 1000) {

			while (scaledValue >= 1000) {
				scaledValue /= 1000;
				counter++;
			}

			return counter + 6;

		} else if (scaledValue < 1) {

			while (scaledValue < 1) {
				scaledValue *= 1000;
				counter++;
			}

			return 6 - counter;

		}

		return 6;
	}

	public static BufferedImage ResizeImage(BufferedImage original, int targetWidth, int targetHeight) {
		if (original != null) {
			BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
			Image resultingImage = original.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
			resizedImage.getGraphics().drawImage(resultingImage, 0, 0, null);
			return resizedImage;
		}
		return null;
	}

	public static void DrawImage(Graphics g, BufferedImage image, int centerX, int centerY) {

		if (image != null) {
			int cornerX = centerX - image.getWidth() / 2;
			int cornerY = centerY - image.getHeight() / 2;
			g.drawImage(image, cornerX, cornerY, null);
		}
	}

	public static BufferedImage RemoveWhitePixels(BufferedImage image) {
		BufferedImage processedImage = new BufferedImage(image.getWidth(), image.getHeight(),
				BufferedImage.TYPE_INT_ARGB);

		Color originalColor, finalColor;

		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				originalColor = new Color(image.getRGB(x, y));

				if (originalColor.getRed() > 225 && originalColor.getBlue() > 225 && originalColor.getGreen() > 225) {
					finalColor = new Color(originalColor.getRed(), originalColor.getGreen(), originalColor.getBlue(),
							0);
				} else {
					finalColor = originalColor;
				}

				processedImage.setRGB(x, y, finalColor.getRGB());
			}
		}

		return processedImage;
	}

	public static float mapValue(float fromMin, float fromMax, float toMin, float toMax, float value) {

		return (toMax - toMin) * (value - fromMin) / (fromMax - fromMin) + toMin;
	}

	public static void fillCircle(Graphics g, float centerX, float centerY, float radius) {
		g.fillOval((int) (centerX - radius), (int) (centerY - radius), (int) (2f * radius), (int) (2f * radius));
	}

	public static void drawCircle(Graphics g, float centerX, float centerY, float radius) {
		g.drawOval((int) (centerX - radius), (int) (centerY - radius), (int) (2f * radius), (int) (2f * radius));
	}

	public static Shape createRingShape(float centerX, float centerY, float outerRadius, float thickness) {
		Ellipse2D outer = new Ellipse2D.Double(centerX - outerRadius, centerY - outerRadius, outerRadius + outerRadius,
				outerRadius + outerRadius);
		Ellipse2D inner = new Ellipse2D.Double(centerX - outerRadius + thickness, centerY - outerRadius + thickness,
				outerRadius + outerRadius - thickness - thickness, outerRadius + outerRadius - thickness - thickness);
		Area area = new Area(outer);
		area.subtract(new Area(inner));
		return area;
	}

}
