package WizardTD;

import java.util.List;
import java.util.ArrayList;
import processing.data.JSONObject;
import processing.core.PApplet;

public class Wave {
    private int duration;
    private float preWavePause;
    private List<Monster> monsters = new ArrayList<>();

    public Wave(int duration, float preWavePause, JSONObject monsterConfig, Board board, PApplet app) {
        this.duration = duration;
        this.preWavePause = preWavePause;

        int quantity = monsterConfig.getInt("quantity");
        float speed = monsterConfig.getFloat("speed");
        for (int i = 0; i < quantity; i++) {
            monsters.add(new Monster(board, app, speed));
        }
    }

    public void update() {
        // Logic to update monsters in this wave
        for (Monster monster : monsters) {
            monster.moveWithSpeed();
        }
    }

    public void render(PApplet app) {
        // Logic to render monsters in this wave
        for (Monster monster : monsters) {
            monster.render(app);
        }
    }

    public int getDuration() {
        return duration;
    }

    public float getPreWavePause() {
        return preWavePause;
    }
}
