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
    public void testMonsterSpawnInterval() {
        Wave wave = new Wave(60, 10.0f, monsterConfig, board, app);
        
        for (int i = 0; i < 19; i++) {
            wave.update();
        }
        
        // After 19 frames, no monster should be spawned yet
        assertTrue(wave.getMonsters().isEmpty());

        wave.update();  // 20th frame

        // After 20 frames, 1 monster should have been spawned
        assertEquals(1, wave.getMonsters().size());
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
        
        // Call the render method and ensure no exceptions are thrown
        assertDoesNotThrow(() -> wave.render(app));
    }

    @Test
    public void testWaveCompletion() {
        Wave wave = new Wave(60, 10.0f, monsterConfig, board, app);
        
        // Simulate the duration of the wave plus some extra frames
        for (int i = 0; i < 70; i++) {
            wave.update();
        }
        
        // Assert that no more monsters are spawned after the wave's duration
        assertEquals(3, wave.getMonsters().size());
    }

    @Test
    public void testMonsterConfiguration() {
        Wave wave = new Wave(60, 10.0f, monsterConfig, board, app);
        wave.update();  // Spawn a monster
        
        Monster monster = wave.getMonsters().get(0);
        
        // Assert that monster properties match the configuration
        assertEquals(100.0f, monster.getCurrentHp());
        assertEquals(10.0f, monster.getArmour());
        assertEquals(5.0f, monster.getManaGainedOnKill());
        assertEquals("gremlin", monster.getType());
    }

    @Test
    public void testUpdate_NoMonsterSpawned() {
        Wave wave = new Wave(60, 10.0f, monsterConfig, board, app);
        
        // Simulate 10 frames of update, which is less than the spawn interval
        for (int i = 0; i < 10; i++) {
            wave.update();
        }
        
        // After 10 frames, no monster should have been spawned
        assertTrue(wave.getMonsters().isEmpty());
    }
    
    @Test
    public void testUpdate_NotAllMonstersSpawned() {
        Wave wave = new Wave(60, 10.0f, monsterConfig, board, app);
        
        // Simulate exactly the duration of the wave
        for (int i = 0; i < 60; i++) {
            wave.update();
        }
        
        // After 60 frames, not all 3 monsters might have been spawned
        assertTrue(wave.getMonsters().size() <= 3);
    }
    
    @Test
    public void testGetterMethods() {
        Wave wave = new Wave(70, 15.0f, monsterConfig, board, app);
        
        assertEquals(70, wave.getDuration());
        assertEquals(15.0f, wave.getPreWavePause());
        assertNotNull(wave.getMonsters());  // Ensure the monsters list is not null

    }
}