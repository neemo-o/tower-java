package entities.wave;

import java.awt.*;
import java.util.*;

import entities.enemy.Enemy;

public class WaveController {
    private final java.util.List<WaveConfig> waves = new ArrayList<>();
    private final java.util.List<Enemy> activeEnemies = new ArrayList<>();
    private final PriorityQueue<SpawnEvent> scheduledSpawns = new PriorityQueue<>();


    private final Point[] spawnPoints = new Point[] {
            new Point(40, 560), 
            new Point(40, 80)  
    };

    private Point targetPoint = new Point(400, 300);
    private int currentWaveIndex = -1;
    private long waveStartTimeMs = 0L;
    private int reachedCountThisTick = 0;
    private int killedCountThisTick = 0;
    private int rewardToGive = 0;
    private boolean autoAdvance = false;

    public java.util.List<Enemy> getActiveEnemies() { return activeEnemies; }
    public Point[] getSpawnPoints() { return spawnPoints; }
    public void setSpawnPoint(int index, Point p) { spawnPoints[index] = p; }
    public Point getTargetPoint() { return targetPoint; }
    public void setTargetPoint(Point p) { targetPoint = p; }

    public void setWaves(java.util.List<WaveConfig> configs) {
        waves.clear();
        waves.addAll(configs);
        currentWaveIndex = -1;
        scheduledSpawns.clear();
        activeEnemies.clear();
    }

    public void setAutoAdvance(boolean value) { this.autoAdvance = value; }
    public int getCurrentWaveNumber() { return currentWaveIndex >= 0 && currentWaveIndex < waves.size() ? (currentWaveIndex + 1) : 0; }
    public int getTotalWaves() { return waves.size(); }


    // gambiarra mal feita
    public void startNextWave() {
        currentWaveIndex++;
        if (currentWaveIndex >= waves.size()) return;
        WaveConfig wave = waves.get(currentWaveIndex);
        waveStartTimeMs = System.currentTimeMillis();
        scheduledSpawns.clear();
        wave.root.scheduleSpawns(asList(), waveStartTimeMs);
        Collections.sort(tmpEvents);
        scheduledSpawns.addAll(tmpEvents);
        tmpEvents.clear();
    }

    private final java.util.List<SpawnEvent> tmpEvents = new ArrayList<>();
    private java.util.List<SpawnEvent> asList() { return tmpEvents; }

    public void update(float deltaSeconds) {
        reachedCountThisTick = 0;
        killedCountThisTick = 0;
        long now = System.currentTimeMillis();
        while (!scheduledSpawns.isEmpty() && scheduledSpawns.peek().spawnTimeMs <= now) {
            SpawnEvent evt = scheduledSpawns.poll();
            Point p = spawnPoints[Math.max(0, Math.min(spawnPoints.length - 1, evt.spawnPointIndex))];
            activeEnemies.add(new Enemy(evt.type, p.x, p.y));
        }

        Iterator<Enemy> it = activeEnemies.iterator();
        while (it.hasNext()) {
            Enemy e = it.next();
            e.updateTowards(targetPoint, deltaSeconds);
            if (e.hasReachedTarget()) {
                reachedCountThisTick += e.getHealth();
                it.remove();
            } else if (e.isDead()) {
                killedCountThisTick += 1;
                rewardToGive = e.getReward();
                it.remove();
            }
        }

        // nova wave
        if (autoAdvance && scheduledSpawns.isEmpty() && activeEnemies.isEmpty() && currentWaveIndex < waves.size() - 1) {
            startNextWave();
        }
    }

    public int consumeReachedCount() {
        int v = reachedCountThisTick;
        reachedCountThisTick = 0;
        return v;
    }

    public int consumeKilledCount() {
        int v = killedCountThisTick;
        killedCountThisTick = 0;
        return v;
    }

    public int giveReward() {
        int v = rewardToGive;
        rewardToGive = 0;
        return v;
    }

    public boolean isReadyForNextWave() {
        return !waves.isEmpty() && currentWaveIndex >= 0 && currentWaveIndex < waves.size() - 1
                && scheduledSpawns.isEmpty() && activeEnemies.isEmpty();
    }

    public boolean isAllWavesFinished() {
        return currentWaveIndex >= waves.size() - 1 && scheduledSpawns.isEmpty() && activeEnemies.isEmpty();
    }
}


