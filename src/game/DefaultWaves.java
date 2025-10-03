package game;

import java.util.ArrayList;
import java.util.List;

public class DefaultWaves {
    public static List<WaveConfig> create() {
        List<WaveConfig> list = new ArrayList<>();

        EnemyType basic = new EnemyType("Formiga", 10, 30f, "src/assets/ant.png", 31, 15);
        EnemyType fast = new EnemyType("Abelha", 6, 50f, "src/assets/bee.png", 34, 33);
        fast.setFlipHorizontal(true);
        basic.setFlipHorizontal(true);

        EnemyGroup w1 = new EnemyGroup();
        for (int i = 0; i < 6; i++) {
            w1.add(new EnemySpawn(basic, i % 2, i * 4000));
        }
        list.add(new WaveConfig(2, 15000, w1));

        return list;
    }
}


