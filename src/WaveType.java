import java.awt.Color;

public class WaveType {

	public float minFrequency, maxFrequency;
	public float minWaveLength, maxWaveLength;
	public float defaultFrequency, defaultWaveLength;
	public float defaultTimeScale, defaultPixelScale;
	public boolean constantSpeed;
	public float speed;
	public Color waveColor;

	public WaveType(float minF, float maxF, float minWL, float maxWL, float dF, float dWL, float dts, float dps) {
		minFrequency = minF;
		maxFrequency = maxF;
		minWaveLength = minWL;
		maxWaveLength = maxWL;
		defaultFrequency = dF;
		defaultWaveLength = dWL;
		defaultTimeScale = dts;
		defaultPixelScale = dps;

		constantSpeed = false;
		speed = 0;
		waveColor = Color.red;
	}

	public void SetConstantSpeed(float speed) {
		constantSpeed = true;
		this.speed = speed;
	}

}
