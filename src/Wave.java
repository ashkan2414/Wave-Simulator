import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;

public class Wave {

	private Vector2 sourcePosition;
	private float speed;
	private float waveLength;
	private float radius;
	private float pixelRadius;

	public Wave(Vector2 sourcePosition, float speed, float waveLength) {
		this.speed = speed;
		this.sourcePosition = new Vector2(sourcePosition);
		this.waveLength = waveLength;
		radius = 0;
		pixelRadius = 0;
	}

	public void Update(Graphics2D g, Graphics2D ig) {

		radius += speed * Data.timeScale * Data.deltaTime;
		pixelRadius = radius / Data.pixelScale;

		int pixelWaveLength = (int) Math.round(waveLength / Data.pixelScale);
		float intensity = 0;

		g.setColor(getWaveColor());
		Shape waveRing = Data.createRingShape(sourcePosition.x, sourcePosition.y, pixelRadius, pixelWaveLength + 5);
		g.fill(waveRing);

		int minResolution = (int) (pixelWaveLength / 8f);
		int resolution = (int) ((1f - minResolution) * Data.WaveResolution / 100f + minResolution);

		for (int x = 0; x <= pixelWaveLength; x += resolution) {
			intensity = (float) ((-Math.cos(2f * Math.PI * x / pixelWaveLength) + 1f) / 2f);

			if (pixelRadius - x >= 0) {
				ig.setColor(new Color(intensity, 0, 0));
				Shape intensityRing = Data.createRingShape(sourcePosition.x, sourcePosition.y, pixelRadius - x,
						pixelWaveLength - x + 5);
				ig.fill(intensityRing);
			}
		}
	}

	public Vector2 getSourcePosition() {
		return sourcePosition;
	}

	public float getSpeed() {
		return speed;
	}

	public float getWaveLength() {
		return waveLength;
	}

	public float getRadius() {
		return radius;
	}

	public float getPixelRadius() {
		return pixelRadius;
	}

	private Color getWaveColor() {

		if (Data.selectedWaveType == Data.WAVE_TYPE_LIGHT) {

			Color lightColor;
			float red = 0, green = 0, blue = 0;

			float nanoWaveLength = (float) (waveLength / Math.pow(10, -9));

			if (nanoWaveLength >= 380 && nanoWaveLength < 410) {

				red = 0.6f - 0.41f * (410f - nanoWaveLength) / 30f;
				green = 0;
				blue = 0.39f + 0.6f * (410f - nanoWaveLength) / 30f;

			} else if (nanoWaveLength >= 410 && nanoWaveLength < 440) {

				red = 0.19f - 0.19f * (440f - nanoWaveLength) / 30f;
				green = 0;
				blue = 1f;

			} else if (nanoWaveLength >= 440 && nanoWaveLength < 490) {

				red = 0;
				green = 1f - (490f - nanoWaveLength) / 50f;
				blue = 1f;

			} else if (nanoWaveLength >= 490 && nanoWaveLength < 510) {

				red = 0;
				green = 1f;
				blue = (510f - nanoWaveLength) / 20f;

			} else if (nanoWaveLength >= 510 && nanoWaveLength < 580) {

				red = 1f - (580f - nanoWaveLength) / 70f;
				green = 1f;
				blue = 0;

			} else if (nanoWaveLength >= 580 && nanoWaveLength < 640) {

				red = 1f;
				green = (640f - nanoWaveLength) / 60f;
				blue = 0;

			} else if (nanoWaveLength >= 640 && nanoWaveLength < 700) {

				red = 1f;
				green = 0;
				blue = 0;

			} else if (nanoWaveLength >= 700 && nanoWaveLength < 780) {

				red = 0.35f + 0.65f * (780f - nanoWaveLength) / 80f;
				green = 0;
				blue = 0;

			} else {

				red = 0;
				green = 0;
				blue = 0;
			}

			if (red > 1)
				red = 1f;
			if (green > 1)
				green = 1f;
			if (blue > 1)
				blue = 1f;
			if (red < 0)
				red = 0;
			if (green < 0)
				green = 0;
			if (blue < 0)
				blue = 0;

			lightColor = new Color((float) red, (float) green, (float) blue);

			return lightColor;

		} else {

			return Data.waveTypes.get(Data.selectedWaveType).waveColor;
		}

	}

}
