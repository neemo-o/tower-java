package entities.enemy;

import java.util.List;

import entities.wave.SpawnEvent;

public interface WaveComponent {
    void scheduleSpawns(List<SpawnEvent> outEvents, long waveStartTimeMs);
}


