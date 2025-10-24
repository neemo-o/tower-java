package entities.enemy;

import java.util.ArrayList;
import java.util.List;

import entities.wave.SpawnEvent;
// utilizaçao do composite para agrupar inimigos em ondas
public class EnemyGroup implements WaveComponent {
    private final List<WaveComponent> children = new ArrayList<>();

    public EnemyGroup add(WaveComponent child) {
        children.add(child);
        return this;
    }

    @Override
    public void scheduleSpawns(List<SpawnEvent> outEvents, long waveStartTimeMs) {
        for (WaveComponent c : children) {
            c.scheduleSpawns(outEvents, waveStartTimeMs);
        }
    }
}


