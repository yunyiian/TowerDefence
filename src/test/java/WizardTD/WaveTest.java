package WizardTD;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import processing.data.JSONObject;
import java.util.List;

public class WaveTest {
    private Board board;
    private PApplet app;
    private JSONObject monsterConfig;

    @BeforeEach
    public void setUp() {
        board = new Board();
        app = new App();
        
        monsterConfig = new JSONObject();
        monsterConfig.setFloat("speed", 1.0f);
        monsterConfig.setInt("quantity", 3);
        monsterConfig.setFloat("hp", 100.0f);
        monsterConfig.setFloat("armour", 10.0f);
        monsterConfig.setFloat("mana_gained_on_kill", 5.0f);
        monsterConfig.setString("type", "basic");
    }

    @Test
    public void testConstructor() {
        Wave wave = new Wave(60, 10.0f, monsterConfig, board, app);
        assertEquals(60, wave.getDuration());
        assertEquals(10.0f, wave.getPreWavePause());
        assertTrue(wave.getMonsters().isEmpty());
    }

    @Test
    public void testUpdate() {
        Wave wave = new Wave(60, 10.0f, monsterConfig, board, app);
        
        // Simulate 20 frames of update
        for (int i = 0; i < 20; i++) {
            wave.update();
        }
        
        // After 20 frames, 1 monster should have been spawned
        assertEquals(1, wave.getMonsters().size());
        
        // Simulate 60 more frames of update
        for (int i = 0; i < 60; i++) {
            wave.update();
        }
        
        // After 80 total frames, 3 monsters should have been spawned
        assertEquals(3, wave.getMonsters().size());
    }
    
    @Test
    public void testRender() {
        Wave wave = new Wave(60, 10.0f, monsterConfig, board, app);
        
        // Call the render method and assume no exceptions are thrown
        wave.render(app);
    }

    // ... add more tests as needed.
}
