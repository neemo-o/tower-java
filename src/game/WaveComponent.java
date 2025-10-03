package game;

import java.util.List;

public interface WaveComponent {
    void scheduleSpawns(List<SpawnEvent> outEvents, long waveStartTimeMs);
}


