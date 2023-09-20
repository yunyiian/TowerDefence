package WizardTD;

import processing.data.JSONArray;
import processing.data.JSONObject;

import java.util.ArrayList;
import java.util.List;
import WizardTD.Wave;
public class WaveManager {

    private List<Wave> waves;
    private int currentWaveIndex;

    public WaveManager(JSONObject config) {
        this.waves = new ArrayList<>();
        JSONArray waveConfigs = config.getJSONArray("waves");
        for (int i = 0; i < waveConfigs.size(); i++) {
            JSONObject waveConfig = waveConfigs.getJSONObject(i);
            Wave wave = new Wave(waveConfig, yourPAppletInstance);
            waves.add(wave);
        }
        this.currentWaveIndex = 0;
    }

    public Wave getCurrentWave() {
        if (currentWaveIndex < waves.size()) {
            return waves.get(currentWaveIndex);
        }
        return null;
    }

    public void moveToNextWave() {
        currentWaveIndex++;
    }

    public List<Monster> update() {
        // Update the state of the wave and return a list of new monsters to be spawned.
        // For now, I'll return an empty list as a placeholder.
        return new ArrayList<>();
    }
    // Additional methods to manage waves can be added here.
}