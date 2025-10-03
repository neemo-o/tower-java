package game;

import java.util.List;

public class EnemySpawn implements WaveComponent {
    private final EnemyType type;
    private final int spawnPointIndex; // 0 ou 1
    private final long delayFromWaveStartMs;

    public EnemySpawn(EnemyType type, int spawnPointIndex, long delayFromWaveStartMs) {
        this.type = type;
        this.spawnPointIndex = spawnPointIndex;
        this.delayFromWaveStartMs = delayFromWaveStartMs;
    }

    @Override
    public void scheduleSpawns(List<SpawnEvent> outEvents, long waveStartTimeMs) {
        outEvents.add(new SpawnEvent(waveStartTimeMs + delayFromWaveStartMs, type, spawnPointIndex));
    }
}


