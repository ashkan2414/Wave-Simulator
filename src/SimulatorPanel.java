import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

public class SimulatorPanel extends JPanel implements MouseListener, MouseMotionListener {

	private boolean holding = false;
	private MouseMovementHandler movingObject = new MouseMovementHandler();
	private BufferedImage emitterImage;
	private BufferedImage processedEmitterImage;
	private BufferedImage sensorImage;
	private BufferedImage processedSensorImage;

	public SimulatorPanel() {

		Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		setBorder(border);
		addMouseListener(this);
		addMouseMotionListener(this);

		try {
			emitterImage = ImageIO.read(getClass().getClassLoader().getResource("WavePad.png"));
			sensorImage = ImageIO.read(getClass().getClassLoader().getResource("Sensor.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		processedEmitterImage = Data.ResizeImage(emitterImage, 100, 100);
		processedSensorImage = Data.ResizeImage(sensorImage, 60, 60);
	}

	public void update() {

		repaint();
		revalidate();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		long start = System.nanoTime();

		Data.wg.width = this.getWidth();
		Data.wg.height = this.getHeight();

		g.setColor(Color.white);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		updateEmitters();
		updateWaves();
		drawWaves(g);
		drawEmitterImages(g);
		drawSensor(g);

		Data.wg.images.clear();
		Data.wg.intensityImages.clear();

		Data.deltaTime = (System.nanoTime() - start) / 1000000000f;

	}

	private void updateEmitters() {
		for (int i = 0; i < Data.emitters.size(); i++) {
			Data.emitters.get(i).update(Data.wg, Data.deltaTime);
		}
	}

	private void updateWaves() {

		for (int i = 0; i < Data.waveCollections.size(); i++) {

			Data.waveCollections.get(i).update();

			if (Data.waveCollections.get(i).waves.size() < 1) {
				Data.waveCollections.remove(i);
			}
		}
	}

	private void drawWaves(Graphics g)

	{
		if (Data.wg.images.size() > 0) {
			int re, gr, bl, counter;
			float in, intensity;
			DataBuffer buffer = Data.wg.images.get(0).getRaster().getDataBuffer();
			DataBuffer intensityBuffer = Data.wg.intensityImages.get(0).getRaster().getDataBuffer();
			Color color;

			for (int pixel = 0, x = 0, y = 0; pixel < buffer.getSize(); pixel++) {
				re = 0;
				gr = 0;
				bl = 0;
				counter = 0;
				in = 0;

				for (int i = 0; i < Data.wg.images.size(); i++) {

					buffer = Data.wg.images.get(i).getRaster().getDataBuffer();
					color = new Color(buffer.getElem(x + (Data.wg.width * y)));

					intensityBuffer = Data.wg.intensityImages.get(i).getRaster().getDataBuffer();
					intensity = new Color(intensityBuffer.getElem(x + (Data.wg.width * y))).getRed();

					if (color.getRed() < 255 || color.getGreen() < 255 || color.getBlue() < 255) {
						re += color.getRed();
						gr += color.getGreen();
						bl += color.getBlue();
						counter++;

						in += (intensity / 255f) * 2f - 1;
					}
				}

				if (counter > 0) {

					in = (in + 1f) / 2f;
					if (in > 1f)
						in = 1f;
					if (in < 0)
						in = 0;

					re = (int) (re / counter * in);
					gr = (int) (gr / counter * in);
					bl = (int) (bl / counter * in);

				} else {
					in = 0;
					re = 255;
					gr = 255;
					bl = 255;
				}

				g.setColor(new Color(re, gr, bl));
				g.drawLine(x, y, x, y);

				Data.sensor.addSensorValue(in, new Vector2(x, y));

				x++;
				if (x == Data.wg.width) {
					x = 0;
					y++;
				}

				if (y == Data.wg.height) {
					break;
				}
			}

			Data.sensor.update();

		} else {
			Data.sensor.blankUpdate();
		}
	}

	private void drawEmitterImages(Graphics g) {
		for (int i = 0; i < Data.emitters.size(); i++) {
			Data.DrawImage(g, processedEmitterImage, (int) Data.emitters.get(i).getPosition().x,
					(int) Data.emitters.get(i).getPosition().y);
		}

		if (Data.selectedEmitter >= 0) {
			g.setColor(new Color(0, 0, 0, 125));
			Data.fillCircle(g, Data.emitters.get(Data.selectedEmitter).getPosition().x,
					Data.emitters.get(Data.selectedEmitter).getPosition().y, 10);
		}
	}

	private void drawSensor(Graphics g) {
		if (Data.selectedSensorType == 0) {
			Data.DrawImage(g, processedSensorImage, (int) Data.sensor.startPosition.x,
					(int) Data.sensor.startPosition.y);
		} else if (Data.selectedSensorType == 1) {

			float sensorLength = Vector2.GetDistance(Data.sensor.startPosition, Data.sensor.endPosition);
			Vector2 sensorDirection = Vector2.Subtract(Data.sensor.startPosition, Data.sensor.endPosition);
			float sensorAngle = (float) -Math.atan2(sensorDirection.x, sensorDirection.y);
			Vector2 sensorHalfPoint = new Vector2((Data.sensor.startPosition.x + Data.sensor.endPosition.x) / 2f,
					(Data.sensor.startPosition.y + Data.sensor.endPosition.y) / 2f);

			Rectangle2D rect = new Rectangle2D.Double(-4, -sensorLength / 2, 8, sensorLength);
			AffineTransform transform = new AffineTransform();
			transform.translate(sensorHalfPoint.x, sensorHalfPoint.y);
			transform.rotate(sensorAngle);
			Shape rotatedRect = transform.createTransformedShape(rect);

			g.setColor(new Color(180, 220, 255));
			((Graphics2D) g).fill(rotatedRect);
			g.setColor(Color.black);
			((Graphics2D) g).draw(rotatedRect);

			g.setColor(new Color(60, 60, 60));
			Data.fillCircle(g, sensorHalfPoint.x, sensorHalfPoint.y, 8);
			g.setColor(new Color(0, 100, 255));
			Data.fillCircle(g, Data.sensor.startPosition.x, Data.sensor.startPosition.y, 8);
			g.setColor(new Color(255, 155, 0));
			Data.fillCircle(g, Data.sensor.endPosition.x, Data.sensor.endPosition.y, 8);

			g.setColor(Color.black);
			Data.drawCircle(g, sensorHalfPoint.x, sensorHalfPoint.y, 8);
			Data.drawCircle(g, Data.sensor.startPosition.x, Data.sensor.startPosition.y, 8);
			Data.drawCircle(g, Data.sensor.endPosition.x, Data.sensor.endPosition.y, 8);

		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		holding = false;
	}

	@Override

	public void mousePressed(MouseEvent e) {

		Vector2 point = new Vector2(e.getPoint().x, e.getPoint().y);
		float distance = 1000;
		int selectedEmitter = 0;

		if (Data.emitters.size() > 0) {

			for (int i = 0; i < Data.emitters.size(); i++) {
				if (Vector2.GetDistance(point, Data.emitters.get(i).getPosition()) < distance) {
					distance = Vector2.GetDistance(point, Data.emitters.get(i).getPosition());
					selectedEmitter = i;
					movingObject.setObject(Data.emitters.get(i).getPosition());
				}
			}

		}

		if (Vector2.GetDistance(point, Data.sensor.startPosition) < distance) {
			distance = Vector2.GetDistance(point, Data.sensor.startPosition);
			selectedEmitter = -1;
			movingObject.setObject(Data.sensor.startPosition);
		}

		if (Data.selectedSensorType == 1) {
			if (Vector2.GetDistance(point, Data.sensor.endPosition) < distance) {
				distance = Vector2.GetDistance(point, Data.sensor.endPosition);
				selectedEmitter = -1;
				movingObject.setObject(Data.sensor.endPosition);
			}

			Vector2 sensorHalfPoint = new Vector2((Data.sensor.startPosition.x + Data.sensor.endPosition.x) / 2f,
					(Data.sensor.startPosition.y + Data.sensor.endPosition.y) / 2f);
			if (Vector2.GetDistance(point, sensorHalfPoint) < distance) {
				distance = Vector2.GetDistance(point, sensorHalfPoint);
				selectedEmitter = -1;
				movingObject.setObject(sensorHalfPoint);
				movingObject.addRelativeVector(Data.sensor.startPosition);
				movingObject.addRelativeVector(Data.sensor.endPosition);
			}
		}

		if (distance < 30) {
			Data.selectedEmitter = selectedEmitter;
			holding = true;
		} else {
			Data.selectedEmitter = -1;
			holding = false;
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		holding = false;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (holding) {
			movingObject.setObjectPosition(new Vector2(e.getPoint().x, e.getPoint().y));
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
	}

}

class MouseMovementHandler {

	private Vector2 objectPosition;
	private ArrayList<RelativeVector> relativeVectors;

	public MouseMovementHandler() {
		this.objectPosition = new Vector2(0, 0);
		relativeVectors = new ArrayList<RelativeVector>();
	}

	public MouseMovementHandler(Vector2 objectPosition) {
		this.objectPosition = objectPosition;
		relativeVectors = new ArrayList<RelativeVector>();
	}

	public void addRelativeVector(Vector2 relativeVector) {
		relativeVectors.add(new RelativeVector(objectPosition, relativeVector));
	}

	public void setObject(Vector2 objectPosition) {
		this.objectPosition = objectPosition;
		clearRelativeVectors();
	}

	public void setObjectPosition(Vector2 newPosition) {
		objectPosition.x = newPosition.x;
		objectPosition.y = newPosition.y;
		updateRelativeVectors();
	}

	public void clearRelativeVectors() {
		relativeVectors.clear();
	}

	private void updateRelativeVectors() {

		for (int i = 0; i < relativeVectors.size(); i++) {
			relativeVectors.get(i).updateRelativeVector();
		}
	}

}

class RelativeVector {

	Vector2 relativeVector;
	Vector2 differenceVector;
	Vector2 parentVector;

	public RelativeVector(Vector2 parentVector, Vector2 relativeVector) {
		this.parentVector = parentVector;
		this.relativeVector = relativeVector;
		this.differenceVector = Vector2.Subtract(parentVector, relativeVector);
	}

	public void updateRelativeVector() {
		Vector2 newPosition = Vector2.Add(parentVector, differenceVector);
		relativeVector.x = newPosition.x;
		relativeVector.y = newPosition.y;
	}

}
