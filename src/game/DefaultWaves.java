package game;

import java.util.ArrayList;
import java.util.List;

public class DefaultWaves {
    public static List<WaveConfig> create() {
        List<WaveConfig> list = new ArrayList<>();

        EnemyType tank = new EnemyType("Besouro", 30, 23f, "src/assets/beetle.png", 47, 23, EnemyCategory.GROUND, 20);
        EnemyType basic = new EnemyType("Formiga", 14, 30f, "src/assets/ant.png", 31, 15, EnemyCategory.GROUND, 15);
        EnemyType fast = new EnemyType("Abelha", 6, 50f, "src/assets/bee.png", 34, 33, EnemyCategory.AIR, 10);
        fast.setFlipHorizontal(true);
        basic.setFlipHorizontal(true);
        tank.setFlipHorizontal(true);
        

        EnemyGroup w1 = new EnemyGroup();
        for (int i = 0; i < 9; i++) {
            w1.add(new EnemySpawn(basic, 0, i * 1500));
        }
        list.add(new WaveConfig(2, 15000, w1));

        EnemyGroup w2 = new EnemyGroup();
        for (int i = 0; i < 8; i++) {
            w2.add(new EnemySpawn(fast, 0, i * 1300));
        }
        list.add(new WaveConfig(2, 15000, w2));

        EnemyGroup w3 = new EnemyGroup();
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                w3.add(new EnemySpawn(fast, 0, i * 1000));
            } else if (i % 3 == 0) {
                w3.add(new EnemySpawn(tank, 0, i * 1000));
            } else {
                w3.add(new EnemySpawn(basic, 0, i * 1500));
            }
        }
        
            list.add(new WaveConfig(2, 15000, w3));

        EnemyGroup w4 = new EnemyGroup();
        for (int i = 0; i < 7; i++) {
            w4.add(new EnemySpawn(tank, 0, i * 900));
        }
        list.add(new WaveConfig(2, 15000, w4));

        EnemyGroup w5 = new EnemyGroup();
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                w5.add(new EnemySpawn(fast, 0, i * 1500));
            } else if (i % 2 == 1) {
                w5.add(new EnemySpawn(tank, 0, i * 800));
        }
    }
    list.add(new WaveConfig(2, 15000, w5));

        EnemyGroup w6 = new EnemyGroup();
        for (int i = 0; i < 21; i++) {
            w6.add(new EnemySpawn(basic, 0, i * 500));
        }
        list.add(new WaveConfig(2, 15000, w6));

        EnemyGroup w7 = new EnemyGroup();
        for (int i = 0; i < 32; i++) {
            if (i % 2 == 0) {
                w7.add(new EnemySpawn(fast, 0, i * 3000));
            } else if (i % 3 == 0) {
                w7.add(new EnemySpawn(tank, 0, i * 2000));
            } else {
                w7.add(new EnemySpawn(basic, 0, i * 3000));
            }
        }
        
            list.add(new WaveConfig(2, 15000, w7));

             EnemyGroup w8 = new EnemyGroup();
        for (int i = 0; i < 36; i++) {
            if (i % 2 == 0) {
                w8.add(new EnemySpawn(fast, 0, i * 400));
            } else if (i % 3 == 0) {
                w8.add(new EnemySpawn(tank, 0, i * 600));
            } else {
                w8.add(new EnemySpawn(basic, 0, i * 500));
            }
        }
        
            list.add(new WaveConfig(2, 15000, w8));

        return list;
    }
}


