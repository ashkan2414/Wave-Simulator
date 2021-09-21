import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class OptionsPanel extends JPanel implements ActionListener, ItemListener, ChangeListener {

	private LayoutManager layout;

	private JLabel timeScaleLabel;
	private JTextField timeScaleField;
	private JComboBox<String> timeScaleUnitSelect;

	private JLabel pixelScaleLabel;
	private JTextField pixelScaleField;
	private JComboBox<String> pixelScaleUnitSelect;

	private JButton applyButton;

	private JSlider speedSlider;
	private JLabel speedLabel;
	private JSlider scaleSlider;
	private JLabel scaleLabel;
	private JSlider resolutionSlider;
	private JLabel resolutionLabel;

	private JLabel waveTypeLabel;
	private JComboBox<String> waveTypeSelect;
	private JLabel sensorTypeLabel;
	private JComboBox<String> sensorTypeSelect;

	private JButton newButton;
	private JButton deleteButton;
	private JButton clearButton;

	private float timeScale, pixelScale;
	private float scaledTimeScale, scaledPixelScale;
	private int timeScaleUnit, pixelScaleUnit;

	private DecimalFormat df;

	public final String[] waveTypes = { "Generic", "Light", "Sound" };
	public final String[] sensorTypes = { "Point", "Line" };

	public OptionsPanel() {

		timeScale = Data.timeScale;
		pixelScale = Data.pixelScale;

		layout = new GridBagLayout();
		setLayout(layout);

		Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		setBorder(border);

		timeScaleLabel = new JLabel("Time Scale", SwingConstants.CENTER);
		timeScaleField = new JTextField("1", 3);
		timeScaleField.addActionListener(this);
		timeScaleUnitSelect = new JComboBox<String>(Data.timeUnits);
		timeScaleUnitSelect.addItemListener(this);

		pixelScaleLabel = new JLabel("Pixel Scale", SwingConstants.CENTER);
		pixelScaleField = new JTextField("1", 3);
		pixelScaleField.addActionListener(this);
		pixelScaleUnitSelect = new JComboBox<String>(Data.metricUnits);
		pixelScaleUnitSelect.addItemListener(this);

		df = new DecimalFormat("###.###");
		setTextFields();

		applyButton = new JButton("Apply");
		applyButton.addActionListener(this);

		speedSlider = new JSlider();
		speedSlider.addChangeListener(this);
		speedSlider.setMinimum(0);
		speedSlider.setMaximum(100);
		speedSlider.setMajorTickSpacing(10);
		speedSlider.setPaintLabels(true);
		speedLabel = new JLabel("Speed (%)", SwingConstants.CENTER);

		scaleSlider = new JSlider();
		scaleSlider.addChangeListener(this);
		scaleSlider.setMinimum(0);
		scaleSlider.setMaximum(100);
		scaleSlider.setMajorTickSpacing(10);
		scaleSlider.setPaintLabels(true);
		scaleLabel = new JLabel("Scale (%)", SwingConstants.CENTER);

		resolutionSlider = new JSlider();
		resolutionSlider.addChangeListener(this);
		resolutionSlider.setMinimum(0);
		resolutionSlider.setMaximum(100);
		resolutionSlider.setMajorTickSpacing(10);
		resolutionSlider.setPaintLabels(true);
		resolutionLabel = new JLabel("Resolution (%)", SwingConstants.CENTER);

		Hashtable<Integer, JLabel> sliderLabelTable = new Hashtable<Integer, JLabel>();
		sliderLabelTable.put(new Integer(0), new JLabel("0", SwingConstants.CENTER));
		sliderLabelTable.put(new Integer(100), new JLabel("100", SwingConstants.CENTER));
		speedSlider.setLabelTable(sliderLabelTable);
		scaleSlider.setLabelTable(sliderLabelTable);
		resolutionSlider.setLabelTable(sliderLabelTable);

		waveTypeLabel = new JLabel("Wave Type", SwingConstants.CENTER);
		waveTypeSelect = new JComboBox<String>(waveTypes);
		waveTypeSelect.addItemListener(this);
		waveTypeSelect.setSelectedIndex(Data.selectedWaveType);
		sensorTypeLabel = new JLabel("Sensor Type", SwingConstants.CENTER);
		sensorTypeSelect = new JComboBox<String>(sensorTypes);
		sensorTypeSelect.addItemListener(this);
		sensorTypeSelect.setSelectedIndex(Data.selectedSensorType);

		newButton = new JButton("New Emitter");
		newButton.addActionListener(this);
		deleteButton = new JButton("Delete Emitter");
		deleteButton.addActionListener(this);
		clearButton = new JButton("Clear");
		clearButton.addActionListener(this);

		this.add(timeScaleLabel, new GridBagConstraints(0, 0, 1, 1, 1, 0.8f, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
		this.add(timeScaleField, new GridBagConstraints(1, 0, 1, 1, 1, 0.8f, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 0, 10, 0), 0, 0));
		this.add(timeScaleUnitSelect, new GridBagConstraints(2, 0, 1, 1, 1, 0.8f, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));

		this.add(pixelScaleLabel, new GridBagConstraints(0, 1, 1, 1, 1, 0.8f, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
		this.add(pixelScaleField, new GridBagConstraints(1, 1, 1, 1, 1, 0.8f, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 0, 10, 0), 0, 0));
		this.add(pixelScaleUnitSelect, new GridBagConstraints(2, 1, 1, 1, 1, 0.8f, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));

		this.add(applyButton, new GridBagConstraints(0, 2, 3, 1, 0, 1.2f, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));

		this.add(speedSlider, new GridBagConstraints(0, 3, 3, 1, 1, 1.3f, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 10, 0, 10), 0, 0));
		this.add(speedLabel, new GridBagConstraints(0, 4, 3, 1, 1, 0.5f, GridBagConstraints.NORTH,
				GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));
		this.add(scaleSlider, new GridBagConstraints(0, 5, 3, 1, 1, 1.3f, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 10, 0, 10), 0, 0));
		this.add(scaleLabel, new GridBagConstraints(0, 6, 3, 1, 1, 0.5f, GridBagConstraints.NORTH,
				GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));
		this.add(resolutionSlider, new GridBagConstraints(0, 7, 3, 1, 1, 1.3f, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 10, 0, 10), 0, 0));
		this.add(resolutionLabel, new GridBagConstraints(0, 8, 3, 1, 1, 0.5f, GridBagConstraints.NORTH,
				GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		this.add(waveTypeLabel, new GridBagConstraints(0, 9, 1, 1, 0, 1f, GridBagConstraints.EAST,
				GridBagConstraints.BOTH, new Insets(5, 5, 5, 0), 0, 0));
		this.add(waveTypeSelect, new GridBagConstraints(1, 9, 2, 1, 0, 1f, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
		this.add(sensorTypeLabel, new GridBagConstraints(0, 10, 1, 1, 0, 1f, GridBagConstraints.EAST,
				GridBagConstraints.BOTH, new Insets(5, 5, 5, 0), 0, 0));
		this.add(sensorTypeSelect, new GridBagConstraints(1, 10, 2, 1, 0, 1f, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));

		this.add(newButton, new GridBagConstraints(0, 11, 3, 1, 0, 1.2f, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
		this.add(deleteButton, new GridBagConstraints(0, 12, 3, 1, 0, 1.2f, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
		this.add(clearButton, new GridBagConstraints(0, 13, 3, 1, 0, 1.2f, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));

		setPixelScale();
		setTimeScale();
		setResolution();
	}

	public void update() {
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider) e.getSource();

		if (source == speedSlider) {
			setTimeScale();
		} else if (source == scaleSlider) {
			setPixelScale();
		} else if (source == resolutionSlider) {
			setResolution();
		}

	}

	private void setTimeScale() {
		Data.timeScale = timeScale * (speedSlider.getValue() / 100f);
	}

	private void setPixelScale() {
		if (scaleSlider.getValue() == 0) {
			Data.pixelScale = pixelScale / 0.000001f;
		} else {
			Data.pixelScale = pixelScale / (scaleSlider.getValue() / 100f);
		}
	}

	private void setResolution() {
		Data.WaveResolution = resolutionSlider.getValue();
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		JComboBox<?> dropList = (JComboBox<?>) e.getSource();

		if (dropList == waveTypeSelect) {

			Data.selectedWaveType = waveTypeSelect.getSelectedIndex();

			for (int i = 0; i < Data.emitters.size(); i++) {

				Data.waveCollections.clear();
				Data.emitters.get(i).setFrequency((float) Data.waveTypes.get(Data.selectedWaveType).defaultFrequency);
				Data.emitters.get(i).setWaveLength((float) Data.waveTypes.get(Data.selectedWaveType).defaultWaveLength);
			}

			timeScale = Data.waveTypes.get(Data.selectedWaveType).defaultTimeScale;
			pixelScale = Data.waveTypes.get(Data.selectedWaveType).defaultPixelScale;
			setTimeScale();
			setPixelScale();
			setTextFields();
		} else if (dropList == sensorTypeSelect) {
			Data.sensor.startPosition = new Vector2(Data.sensor.defaultStartPosition);
			Data.sensor.endPosition = new Vector2(Data.sensor.defaultEndPosition);
			Data.selectedSensorType = dropList.getSelectedIndex();
		}

	}

	private void setTextFields() {
		timeScaleUnit = Data.GetUnit(timeScale);
		scaledTimeScale = (float) (timeScale / Math.pow(10, Data.metricScales[timeScaleUnit]));
		timeScaleField.setText(df.format(scaledTimeScale));
		timeScaleUnitSelect.setSelectedIndex(timeScaleUnit);

		pixelScaleUnit = Data.GetUnit(pixelScale);
		scaledPixelScale = (float) (pixelScale / Math.pow(10, Data.metricScales[pixelScaleUnit]));
		pixelScaleField.setText(df.format(scaledPixelScale));
		pixelScaleUnitSelect.setSelectedIndex(pixelScaleUnit);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton buttonPressed = (JButton) e.getSource();

		if (buttonPressed == applyButton) {
			timeScale = (float) (Float.parseFloat(timeScaleField.getText())
					* Math.pow(10, Data.metricScales[timeScaleUnitSelect.getSelectedIndex()]));
			pixelScale = (float) (Float.parseFloat(pixelScaleField.getText())
					* Math.pow(10, Data.metricScales[pixelScaleUnitSelect.getSelectedIndex()]));
			setTimeScale();
			setPixelScale();
		} else if (buttonPressed == newButton) {
			Data.emitters.add(new Emitter(new Vector2(Data.wg.width / 2, Data.wg.height / 2),
					(float) Data.waveTypes.get(Data.selectedWaveType).defaultFrequency,
					(float) Data.waveTypes.get(Data.selectedWaveType).defaultWaveLength));
		} else if (buttonPressed == deleteButton) {
			if (Data.selectedEmitter >= 0) {
				Data.emitters.remove(Data.selectedEmitter);
				Data.selectedEmitter = -1;
			}
		} else if (buttonPressed == clearButton) {
			Data.emitters.clear();
			Data.waveCollections.clear();
			Data.selectedEmitter = -1;
		}

	}

}
