package WizardTD.subtiles;

import WizardTD.subtiles.TowerTile;
import WizardTD.Monster;
import WizardTD.Board;
import WizardTD.App;
import processing.core.PApplet;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

public class TowerTileTest {

    PApplet app;
    TowerTile tower;
    Board board;
    Monster monster;

    @BeforeEach
    public void setUp() {
        app = new PApplet();  // Initialize the PApplet
        board = new Board();  // Initialize the board
        
        // Load layout for the board before creating monsters
        board.loadLayout("path_to_layout_file.txt", new App()); // Adjust the path based on your project structure.
        
        tower = new TowerTile(app, 5, 5, 100.0f, 1.0f, 50.0f); // Placing the tower at (5,5) with some arbitrary stats
        monster = new Monster(board, app, 2.0f, 4, 100, 1.0f, 10, "monsterType", new App()); // Creating a monster close to the tower
    }

    @Test
    public void testTowerInitialization() {
        assertEquals(5, tower.getX());
        assertEquals(5, tower.getY());
        assertNotNull(tower.getImage());
    }

    @Test
    public void testClosestMonsterInRange() {
        List<Monster> monsters = new ArrayList<>();
        monsters.add(monster);

        Monster closestMonster = tower.getClosestMonsterInRange(monsters);
        assertEquals(monster, closestMonster);
    }

    @Test
    public void testShootingMonster() {
        List<Monster> monsters = new ArrayList<>();
        monsters.add(monster);

        tower.shootMonster(monster);
        assertFalse(tower.getFireballs().isEmpty());
    }

    @Test
    public void testUpgradeRange() {
        int initialRange = (int) tower.getTowerRange();
        tower.upgradeRange();
        assertTrue(tower.getTowerRange() > initialRange);
    }

    @Test
    public void testUpgradeSpeed() {
        float initialSpeed = tower.getTowerFiringSpeed();
        tower.upgradeSpeed();
        assertTrue(tower.getTowerFiringSpeed() > initialSpeed);
    }

    @Test
    public void testUpgradeDamage() {
        float initialDamage = tower.getTowerDamage();
        tower.upgradeDamage();
        assertTrue(tower.getTowerDamage() > initialDamage);
    }

    @Test
    public void testGetCost() {
        int cost = tower.getCost();
        assertTrue(cost >= 100); // Base cost should be at least 100
    }

    @Test
    public void testGetUpgradeCost() {
        int upgradeCost = tower.getUpgradeCost(1);
        assertEquals(30, upgradeCost);  // For level 1, the upgrade cost should be 30
    }

    // Add more tests as necessary for different behaviors of the TowerTile.

}
