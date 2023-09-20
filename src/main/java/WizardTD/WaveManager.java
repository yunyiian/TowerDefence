package WizardTD;

import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class WaveManager {
    private PApplet app;
    private JSONObject waveConfig;
    private Wave[] waves;

    public WaveManager(JSONObject waveConfig, PApplet app) {
        this.waveConfig = waveConfig;
        this.app = app;

        JSONArray wavesConfig = waveConfig.getJSONArray("waves");
        this.waves = new Wave[wavesConfig.size()];

        for (int i = 0; i < wavesConfig.size(); i++) {
            JSONObject singleWaveConfig = wavesConfig.getJSONObject(i);
            this.waves[i] = new Wave(singleWaveConfig, app);
        }
    }

    public void update() {
        for (Wave wave : waves) {
            wave.update();
        }
    }

    public void render() {
        for (Wave wave : waves) {
            wave.render();
        }
    }
}
