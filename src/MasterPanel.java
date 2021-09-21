import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

public class MasterPanel extends JPanel implements Runnable {

	private LayoutManager layout;
	private SimulatorPanel simulator;
	private EmitterEditorPanel emitterEditor;
	private OptionsPanel options;
	private SensorPanel sensor;

	private Thread thread;

	public MasterPanel() {

		new Data();

		layout = new GridBagLayout();
		this.setLayout(layout);

		Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		setBorder(border);

		sensor = Data.sensor;
		simulator = new SimulatorPanel();
		emitterEditor = new EmitterEditorPanel();
		options = new OptionsPanel();

		add(sensor, new GridBagConstraints(0, 0, 1, 1, 1, 0.15, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(simulator, new GridBagConstraints(0, 1, 1, 1, 1, 0.7, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(emitterEditor, new GridBagConstraints(0, 2, 2, 1, 0, 0.15, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(options, new GridBagConstraints(1, 0, 1, 2, 0.1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		thread = new Thread(this);
	}

	public void start() {
		thread.run();
	}

	@Override
	public void run() {

		while (true) {
			simulator.update();
			emitterEditor.update();
			options.update();
		}
	}
}
