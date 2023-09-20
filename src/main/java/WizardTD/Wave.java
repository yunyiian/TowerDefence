package WizardTD;

import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.util.ArrayList;

public class Wave {
    private PApplet app;
    private ArrayList<Monster> monsters;

    public Wave(JSONObject waveConfig, PApplet app) {
        this.app = app;
        this.monsters = new ArrayList<>();

        JSONArray monstersConfig = waveConfig.getJSONArray("monsters");
        for (int i = 0; i < monstersConfig.size(); i++) {
            JSONObject monsterConfig = monstersConfig.getJSONObject(i);
            this.monsters.add(new Monster(monsterConfig, app));
        }
    }

    public void update() {
        for (Monster monster : monsters) {
            //monster.update();
        }
    }

    public void render() {
        for (Monster monster : monsters) {
            monster.render(app);
        }
    }
}

