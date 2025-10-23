package entities.wave;

import entities.enemy.WaveComponent;

public class WaveConfig {
    public final int waveNumber;
    public final long durationMs;
    public final WaveComponent root;

    public WaveConfig(int waveNumber, long durationMs, WaveComponent root) {
        this.waveNumber = waveNumber;
        this.durationMs = durationMs;
        this.root = root;
    }
}


