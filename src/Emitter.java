public class Emitter {

	private Vector2 position;
	private float frequency;
	private float waveLength;
	private float lastWave;
	private boolean waveAdded;

	public Emitter(Vector2 position, float frequency, float waveLength) {
		this.position = new Vector2(position);
		this.frequency = frequency;
		this.waveLength = waveLength;
		lastWave = 0;
		waveAdded = false;
	}

	public void update(WaveGraphics wg, float deltaTime) {

		if ((System.nanoTime() - lastWave) / 1000000000f > 1f / frequency / Data.timeScale) {
			Wave wave = new Wave(position, frequency * waveLength, waveLength);

			for (int i = 0; i < Data.waveCollections.size(); i++) {

				if (Data.waveCollections.get(i).waves.size() > 0) {

					if (Data.waveCollections.get(i).waves.get(0).getSourcePosition().x == position.x
							&& Data.waveCollections.get(i).waves.get(0).getSourcePosition().y == position.y) {

						waveAdded = true;
						Data.waveCollections.get(i).waves.add(wave);
					}

				} else {
					waveAdded = true;
					Data.waveCollections.get(i).waves.add(wave);
				}

			}

			if (waveAdded == false) {

				Data.waveCollections.add(new WaveCollection());
				Data.waveCollections.get(Data.waveCollections.size() - 1).waves.add(wave);
			}

			waveAdded = false;
			lastWave = System.nanoTime();
		}
	}

	public Vector2 getPosition() {
		return position;
	}

	public float getFrequency() {
		return frequency;
	}

	public void setFrequency(float frequency) {
		this.frequency = frequency;
	}

	public float getWaveLength() {
		return waveLength;
	}

	public void setWaveLength(float waveLength) {
		this.waveLength = waveLength;
	}

}
