package entities.wave;

import entities.enemy.EnemyType;

public class SpawnEvent implements Comparable<SpawnEvent> {
    public final long spawnTimeMs;
    public final EnemyType type;
    public final int spawnPointIndex; // 0 ou 1

    public SpawnEvent(long spawnTimeMs, EnemyType type, int spawnPointIndex) {
        this.spawnTimeMs = spawnTimeMs;
        this.type = type;
        this.spawnPointIndex = spawnPointIndex;
    }

    @Override
    public int compareTo(SpawnEvent o) {
        return Long.compare(this.spawnTimeMs, o.spawnTimeMs);
    }
}


