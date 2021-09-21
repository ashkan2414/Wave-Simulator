import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

public class SensorPanel extends JPanel {

	private final int graphPadding = 10;
	public final Vector2 defaultStartPosition = new Vector2(100, 100);
	public final Vector2 defaultEndPosition = new Vector2(100, 200);

	private ArrayList<Float> sensorValues;
	public Vector2 startPosition;
	public Vector2 endPosition;

	public SensorPanel(Vector2 startPosition, Vector2 endPosition) {
		this.startPosition = new Vector2(startPosition);
		this.endPosition = new Vector2(endPosition);
		sensorValues = new ArrayList<Float>();
		Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		setBorder(border);
	}

	public SensorPanel() {
		this.startPosition = new Vector2(defaultStartPosition);
		this.endPosition = new Vector2(defaultEndPosition);
		sensorValues = new ArrayList<Float>();
		Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		setBorder(border);
	}

	void update() {
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		g.setColor(Color.black);
		g.drawRect(0, 0, this.getWidth(), this.getHeight());

		if (Data.selectedSensorType == 0) {
			while (sensorValues.size() > this.getWidth() - 4) {
				sensorValues.remove(0);
			}

			while (sensorValues.size() < this.getWidth() - 4) {
				sensorValues.add(new Float(0));
			}

		} else if (Data.selectedSensorType == 1) {

			int sensorLength = (int) Vector2.GetDistance(startPosition, endPosition);

			while (sensorValues.size() > sensorLength - 1) {
				sensorValues.remove(sensorValues.size() - 1);
			}

			while (sensorValues.size() < sensorLength - 1) {
				sensorValues.add(new Float(0));
			}

		}

		for (int x = 0; x < sensorValues.size() - 1; x++) {
			g.drawLine((int) Data.mapValue(0, sensorValues.size(), 0, this.getWidth(), x),
					(int) ((1f - sensorValues.get(x)) * (this.getHeight() - 2f * graphPadding)) + graphPadding,
					(int) Data.mapValue(0, sensorValues.size(), 0, this.getWidth(), x + 1),
					(int) ((1f - sensorValues.get(x + 1)) * (this.getHeight() - 2f * graphPadding)) + graphPadding);
		}
	}

	public void addSensorValue(float value, Vector2 position) {

		if (Data.selectedSensorType == 0) {
			if (position.x == startPosition.x && position.y == startPosition.y) {
				sensorValues.add(value);
			}
		} else if (Data.selectedSensorType == 1) {

			float sensorDistance = Vector2.GetDistance(startPosition, endPosition);
			float posDistance = Vector2.GetDistance(startPosition, position);

			if (posDistance < sensorDistance && posDistance > 1) {

				Vector2 sensorDirection = Vector2.Subtract(startPosition, endPosition);
				sensorDirection.normalize();

				Vector2 posDirection = Vector2.Subtract(startPosition, position);
				posDirection.normalize();

				if (Math.abs(sensorDirection.x - posDirection.x) < 6
						&& Math.abs(sensorDirection.y - posDirection.y) < 6) {
					while (sensorValues.size() < sensorDistance - 1) {
						sensorValues.add(new Float(0));
					}
					sensorValues.set((int) posDistance - 1, value);
				}
			}
		}

	}

	public void blankUpdate() {
		if (Data.selectedSensorType == 0) {
			sensorValues.add(new Float(0));
		} else if (Data.selectedSensorType == 1) {
			sensorValues.clear();
		}

		update();
	}

}
