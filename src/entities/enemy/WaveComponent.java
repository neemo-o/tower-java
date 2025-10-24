package entities.enemy;

import java.util.List;

import entities.wave.SpawnEvent;
// Interface para componentes de ondas de inimigos, nada demais.
public interface WaveComponent {
    void scheduleSpawns(List<SpawnEvent> outEvents, long waveStartTimeMs);
}


