import java.awt.EventQueue;

import javax.swing.JFrame;

public class WaveMain {

	private MasterPanel panel;
	private JFrame frame;

	public static void main(String[] args) {
		new WaveMain();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// WaveMain window = new WaveMain();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public WaveMain() {
		frame = new JFrame("Wave Simulator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 680);
		frame.setResizable(false);
		panel = new MasterPanel();
		frame.setContentPane(panel);
		frame.setVisible(true);
		panel.start();
	}
}
