package WizardTD;

import processing.core.PApplet;
import processing.data.JSONObject;
import processing.data.JSONArray;
import java.util.ArrayList;
import java.util.List;

public class Wave {
    private float preWavePause;
    private float duration;
    private List<Monster> monsters;

    public Wave(JSONObject waveConfig, PApplet app) {
        this.preWavePause = waveConfig.getFloat("pre_wave_pause");
        this.duration = waveConfig.getFloat("duration");
        this.monsters = new ArrayList<>();

        JSONArray monstersConfig = waveConfig.getJSONArray("monsters");
        for (int i = 0; i < monstersConfig.size(); i++) {
            JSONObject monsterConfig = monstersConfig.getJSONObject(i);
            int quantity = monsterConfig.getInt("quantity");
            for (int j = 0; j < quantity; j++) {
                this.monsters.add(new Monster(monsterConfig, app));
            }
        }
    }

    public float getPreWavePause() {
        return preWavePause;
    }

    public float getDuration() {
        return duration;
    }

    public List<Monster> getMonsters() {
        return monsters;
    }

    // Additional methods to handle wave logic can be added here.
}
