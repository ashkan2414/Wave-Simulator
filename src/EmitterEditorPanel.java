import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class EmitterEditorPanel extends JPanel implements ChangeListener {

	private LayoutManager layout;
	private JSlider frequencySlider;
	private JLabel frequencyLabel;
	private JSlider waveLengthSlider;
	private JLabel waveLengthLabel;

	public class WaveTypeInformation {

		public int frequencyUnit;
		public float scaledMinFrequency;
		public float scaledMaxFrequency;

		public int waveLengthUnit;
		public float scaledMinWaveLength;
		public float scaledMaxWaveLength;

		public Hashtable<Integer, JLabel> frequencyLabels;
		public Hashtable<Integer, JLabel> waveLengthLabels;

		WaveTypeInformation() {
			frequencyLabels = new Hashtable<Integer, JLabel>();
			waveLengthLabels = new Hashtable<Integer, JLabel>();
		}

	}

	private ArrayList<WaveTypeInformation> waveTypeInformation;

	public EmitterEditorPanel() {

		layout = new GridBagLayout();
		setLayout(layout);
		Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		setBorder(border);

		frequencySlider = new JSlider();
		frequencySlider.addChangeListener(this);
		frequencySlider.setMajorTickSpacing(10);
		frequencySlider.setPaintTicks(true);
		frequencySlider.setPaintLabels(true);
		frequencySlider.setMaximum(100);
		frequencySlider.setMinimum(0);
		frequencyLabel = new JLabel("Frequency", SwingConstants.CENTER);
		waveLengthSlider = new JSlider();
		waveLengthSlider.addChangeListener(this);
		waveLengthSlider.setMajorTickSpacing(10);
		waveLengthSlider.setPaintTicks(true);
		waveLengthSlider.setPaintLabels(true);
		waveLengthSlider.setMaximum(100);
		waveLengthSlider.setMinimum(0);
		waveLengthLabel = new JLabel("WaveLength", SwingConstants.CENTER);

		waveTypeInformation = new ArrayList<WaveTypeInformation>();

		for (int i = 0; i < Data.waveTypes.size(); i++) {

			waveTypeInformation.add(new WaveTypeInformation());

			waveTypeInformation.get(i).frequencyUnit = Data.GetUnit(Data.waveTypes.get(i).minFrequency);
			waveTypeInformation.get(i).scaledMinFrequency = (float) (Data.waveTypes.get(i).minFrequency
					/ Math.pow(10D, Data.metricScales[waveTypeInformation.get(i).frequencyUnit]));
			waveTypeInformation.get(i).scaledMaxFrequency = (float) (Data.waveTypes.get(i).maxFrequency
					/ Math.pow(10D, Data.metricScales[waveTypeInformation.get(i).frequencyUnit]));

			waveTypeInformation.get(i).waveLengthUnit = Data.GetUnit(Data.waveTypes.get(i).minWaveLength);
			waveTypeInformation.get(i).scaledMinWaveLength = (float) (Data.waveTypes.get(i).minWaveLength
					/ Math.pow(10D, Data.metricScales[waveTypeInformation.get(i).waveLengthUnit]));
			waveTypeInformation.get(i).scaledMaxWaveLength = (float) (Data.waveTypes.get(i).maxWaveLength
					/ Math.pow(10D, Data.metricScales[waveTypeInformation.get(i).waveLengthUnit]));

			for (int x = 0; x <= 100; x += 10) {
				waveTypeInformation.get(i).frequencyLabels
						.put(new Integer(x),
								new JLabel(
										Integer.toString(
												(int) mapValue(0, 100f, waveTypeInformation.get(i).scaledMinFrequency,
														waveTypeInformation.get(i).scaledMaxFrequency, x)),
										SwingConstants.CENTER));
				waveTypeInformation.get(i).waveLengthLabels
						.put(new Integer(x),
								new JLabel(
										Integer.toString(
												(int) mapValue(0, 100f, waveTypeInformation.get(i).scaledMinWaveLength,
														waveTypeInformation.get(i).scaledMaxWaveLength, x)),
										SwingConstants.CENTER));
			}

		}

		this.add(frequencySlider, new GridBagConstraints(0, 0, 1, 1, 6, 1, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(5, 10, 0, 10), 0, 0));
		this.add(frequencyLabel, new GridBagConstraints(0, 1, 1, 1, 6, 1, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 0), 0, 0));

		this.add(waveLengthSlider, new GridBagConstraints(1, 0, 1, 1, 6, 1, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(5, 10, 0, 10), 0, 0));
		this.add(waveLengthLabel, new GridBagConstraints(1, 1, 1, 1, 6, 1, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 0), 0, 0));
	}

	public void update() {

		frequencySlider.setLabelTable(waveTypeInformation.get(Data.selectedWaveType).frequencyLabels);
		waveLengthSlider.setLabelTable(waveTypeInformation.get(Data.selectedWaveType).waveLengthLabels);

		frequencyLabel.setText("Frequency ("
				+ Data.metricPrefixes[waveTypeInformation.get(Data.selectedWaveType).frequencyUnit] + "Hz)");
		waveLengthLabel.setText("Wave Length ("
				+ Data.metricPrefixes[waveTypeInformation.get(Data.selectedWaveType).waveLengthUnit] + "m)");

		if (Data.selectedEmitter >= 0) {
			if (!this.isVisible())
				this.setVisible(true);
			frequencySlider.setValue(getEmitterFrequency());
			waveLengthSlider.setValue(getEmitterWaveLength());
		} else {
			if (this.isVisible())
				this.setVisible(false);
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {

		if (Data.selectedEmitter >= 0) {
			JSlider source = (JSlider) e.getSource();
			if (source == frequencySlider) {
				Data.emitters.get(Data.selectedEmitter)
						.setFrequency(mapValue(0, 100, Data.waveTypes.get(Data.selectedWaveType).minFrequency,
								Data.waveTypes.get(Data.selectedWaveType).maxFrequency, frequencySlider.getValue()));
				if (Data.waveTypes.get(Data.selectedWaveType).constantSpeed) {
					Data.emitters.get(Data.selectedEmitter)
							.setWaveLength(Data.waveTypes.get(Data.selectedWaveType).speed
									/ Data.emitters.get(Data.selectedEmitter).getFrequency());
					waveLengthSlider.setValue(getEmitterWaveLength());
				}
			}

			if (source == waveLengthSlider) {
				Data.emitters.get(Data.selectedEmitter)
						.setWaveLength(mapValue(0, 100, Data.waveTypes.get(Data.selectedWaveType).minWaveLength,
								Data.waveTypes.get(Data.selectedWaveType).maxWaveLength, waveLengthSlider.getValue()));
				if (Data.waveTypes.get(Data.selectedWaveType).constantSpeed) {
					Data.emitters.get(Data.selectedEmitter).setFrequency(Data.waveTypes.get(Data.selectedWaveType).speed
							/ Data.emitters.get(Data.selectedEmitter).getWaveLength());
					frequencySlider.setValue(getEmitterFrequency());
				}
			}
		}
	}

	public static float mapValue(float fromMin, float fromMax, float toMin, float toMax, float value) {

		return (toMax - toMin) * (value - fromMin) / (fromMax - fromMin) + toMin;
	}

	private int getEmitterFrequency() {

		return Math.round(mapValue(waveTypeInformation.get(Data.selectedWaveType).scaledMinFrequency,
				waveTypeInformation.get(Data.selectedWaveType).scaledMaxFrequency, 0, 100f,
				(float) (Data.emitters.get(Data.selectedEmitter).getFrequency() / Math.pow(10,
						Data.metricScales[waveTypeInformation.get(Data.selectedWaveType).frequencyUnit]))));
	}

	private int getEmitterWaveLength() {
		return Math.round(mapValue(waveTypeInformation.get(Data.selectedWaveType).scaledMinWaveLength,
				waveTypeInformation.get(Data.selectedWaveType).scaledMaxWaveLength, 0, 100f,
				(float) (Data.emitters.get(Data.selectedEmitter).getWaveLength() / Math.pow(10,
						Data.metricScales[waveTypeInformation.get(Data.selectedWaveType).waveLengthUnit]))));
	}

}
