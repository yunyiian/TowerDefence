package WizardTD;

import java.util.List;
import java.util.ArrayList;
import processing.data.JSONObject;
import processing.core.PApplet;

public class Wave {
    private int duration;
    private float preWavePause;
    private List<Monster> monsters = new ArrayList<>();

    private Board board;
    private PApplet app;
    private float speed;

    private float spawnInterval;  // Time interval (in frames) between spawning each monster
    private float currentSpawnTime = 0;  // Elapsed time (in frames) since the last spawn
    private int monstersToSpawn;  // Number of monsters left to spawn

    public Wave(int duration, float preWavePause, JSONObject monsterConfig, Board board, PApplet app) {
        this.duration = duration;
        this.preWavePause = preWavePause;
        this.board = board;
        this.app = app;
        this.speed = monsterConfig.getFloat("speed");

        int quantity = monsterConfig.getInt("quantity");
        
        // Calculate the spawn interval in frames
        this.spawnInterval = (duration * 60.0f) / quantity;  // Assuming 60 fps
        this.monstersToSpawn = quantity;
    }

    public void update() {
        currentSpawnTime += 1;  // Increment the elapsed time since the last spawn

        // If it's time to spawn a new monster and there are still monsters left to spawn
        if (currentSpawnTime >= spawnInterval && monstersToSpawn > 0) {
            int spawnDelay = Math.round(spawnInterval * monsters.size());
            monsters.add(new Monster(board, app, speed, spawnDelay));
            monstersToSpawn--;
            currentSpawnTime = 0;  // Reset the elapsed time since the last spawn
        }

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
