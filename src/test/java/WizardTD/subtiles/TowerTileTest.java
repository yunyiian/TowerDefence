package WizardTD.subtiles;

import WizardTD.subtiles.TowerTile;
import WizardTD.Monster;
import WizardTD.Board;
import WizardTD.App;
import processing.core.PImage;
import processing.core.PApplet;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

public class TowerTileTest {

    private PApplet app;
    private TowerTile tower;
    private Board board;
    private Monster monster;
    private App mainApp; 

    private TowerTile towerTile;
    private List<Monster> monsters;

    @BeforeEach
    public void setUp() {
        app = new PApplet();  // Initialize the PApplet
        board = new Board();  // Initialize the board
        
        // Load layout for the board before creating monsters
        board.loadLayout("path_to_layout_file.txt", new App()); // Adjust the path based on your project structure.
        
        tower = new TowerTile(app, 5, 5, 100.0f, 1.0f, 50.0f); // Placing the tower at (5,5) with some arbitrary stats
        monster = new Monster(board, app, 2.0f, 4, 100, 1.0f, 10, "gremlin", new App()); // Creating a monster close to the tower
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

        // BoardTest

    @Test
    public void testPlaceTowerBoundary() {
        // Test placing a tower at an out-of-bound location
        TowerTile invalidTower = board.placeTower(-1, -1, mainApp, 100.0f, 1.0f, 50.0f);
        assertNull(invalidTower);
    }

    @Test
    public void testInvalidUpgradeTower() {
        // Test upgrading a non-existing tower
        board.upgradeTowerRange(-1, -1, mainApp);
        // Assert any expected state or behavior after an invalid upgrade attempt.
    }

    // TowerTileTest

    @Test
    public void testDespawnFireballs() {
        tower.shootMonster(monster);
        assertFalse(tower.getFireballs().isEmpty());

        tower.despawnFireballsTargeting(monster);
        assertTrue(tower.getFireballs().isEmpty());
    }

    @Test
    public void testNotShootingOutOfReachMonster() {
        // Assuming that the monster is out of range
        Monster outOfReachMonster = new Monster(board, app, 1000.0f, 4, 100, 1.0f, 10, "gremlin", new App());
        
        List<Monster> monsters = new ArrayList<>();
        monsters.add(outOfReachMonster);

        tower.shootMonster(outOfReachMonster);
        assertTrue(tower.getFireballs().isEmpty());
    }

    @Test
    public void testNotTargetingOutOfReachMonster() {
        // Assuming that the monster is out of range
        Monster outOfReachMonster = new Monster(board, app, 1000.0f, 4, 100, 1.0f, 10, "gremlin", new App());
        
        List<Monster> monsters = new ArrayList<>();
        monsters.add(outOfReachMonster);

        Monster targetedMonster = tower.getClosestMonsterInRange(monsters);
        assertNull(targetedMonster);
    }
    @Test
    public void testInitialFireballsList() {
        assertTrue(tower.getFireballs().isEmpty());
    }
    
    
    @Test
    public void testIsMouseHovering() {
        assertTrue(tower.isMouseHovering(tower.getX() * App.CELLSIZE, tower.getY() * App.CELLSIZE));
        assertFalse(tower.isMouseHovering(0, 0));  // Assuming this point is not on the tower
    }
    @Test
    public void testShootMonster() {
        TowerTile tower = new TowerTile(app, 5, 5, 100, 1, 50);
        Monster monster = new Monster(board, app, 1, 1, 100, 1, 10, "gremlin", mainApp);
        
        // Simulate the monster being close enough to the tower
        monster.setX(5.5f);
        monster.setY(5.5f);
        
        tower.shootMonster(monster);
        assertEquals(1, tower.getFireballs().size());
    }
    
    @Test
    public void testShootDeadMonster() {
        TowerTile tower = new TowerTile(app, 5, 5, 100, 1, 50);
        Monster monster = new Monster(board, app, 1, 1, 100, 1, 10, "gremlin", mainApp);
        
        // Simulate the monster being close enough and already dead
        monster.setX(5.5f);
        monster.setY(5.5f);
        monster.reduceHealth(200);  // Killing the monster
        
        tower.shootMonster(monster);
        assertEquals(0, tower.getFireballs().size());
    }

    @Test
    public void testReduceHealth() {
        Monster monster = new Monster(board, app, 1, 1, 100, 0.5f, 10, "gremlin", mainApp);  // 50% armor
        monster.reduceHealth(50);  // Apply 50 damage, but with armor it should be 25
        assertEquals(75, monster.getCurrentHp(), 0.01);
    }

    @Test
    public void testUpgradeTowerNotEnoughMoney() {
        TowerTile tower = new TowerTile(app, 5, 5, 100, 1, 50);
        // Simulate not having enough money in the App instance
        assertFalse(tower.upgradeRange());
        assertFalse(tower.upgradeSpeed());
        assertFalse(tower.upgradeDamage());
    }

    // Add more tests as necessary for different behaviors of the TowerTile.
    @Test
    public void testUpdateFireballs() {
        TowerTile tower = new TowerTile(app, 5, 5, 100, 1, 50);
        Monster monster = new Monster(board, app, 1, 1, 100, 1, 10, "gremlin", mainApp);
        
        tower.shootMonster(monster);
        tower.updateFireballs(false);  // Update without doubleSpeed
        assertEquals(1, tower.getFireballs().size());  // Fireball should still be present
    }

    @Test
    public void testIncrementTimeSinceLastShot() {
        TowerTile tower = new TowerTile(app, 5, 5, 100, 1, 50);
        tower.incrementTimeSinceLastShot(0.5);
        // Unfortunately, we can't assert the timeSinceLastShot directly as it's a private field.
        // So we'll indirectly verify its behavior by shooting the monster after incrementing.
        Monster monster = new Monster(board, app, 1, 1, 100, 1, 10, "gremlin", mainApp);
        tower.shootMonster(monster);
        assertEquals(1, tower.getFireballs().size());
    }

    @Test
    public void testUpdateTowerImage() {
        TowerTile tower = new TowerTile(app, 5, 5, 100, 1, 50);
        
        // Initial state
        assertEquals(0, tower.towerAppearanceState); 

        tower.upgradeRange();
        tower.upgradeSpeed();
        tower.upgradeDamage();
        
        // Assert the updated image of the tower after upgrades.
        assertEquals(1, tower.towerAppearanceState);
    }

    @Test
    public void testUpgradeMethods() {
        App mainAppMock = new App() {
            @Override
            public int calculateUpgradeCost(int currentLevel) {
                return 20;
            }

            @Override
            public boolean canAfford(int cost) {
                return cost <= 20;  // Mock method to always return true for cost <= 20
            }
        };
        
        TowerTile tower = new TowerTile(mainAppMock, 5, 5, 100, 1, 50);
        
        assertTrue(tower.upgradeRange());
        assertTrue(tower.upgradeSpeed());
        assertTrue(tower.upgradeDamage());
    }

    @Test
    public void testTowerAppearanceState() {
        TowerTile tower = new TowerTile(app, 5, 5, 100, 1, 50);
        
        tower.upgradeRange();
        tower.upgradeSpeed();
        tower.upgradeDamage();

        assertEquals(1, tower.towerAppearanceState);

        tower.upgradeRange();
        tower.upgradeSpeed();
        tower.upgradeDamage();

        assertEquals(2, tower.towerAppearanceState);
    }

    @Test
    public void testDespawnFireballsTargeting() {
        TowerTile tower = new TowerTile(app, 5, 5, 100, 1, 50);
        Monster monster1 = new Monster(board, app, 1, 1, 100, 1, 10, "gremlin", mainApp);
        Monster monster2 = new Monster(board, app, 1, 2, 100, 1, 10, "gremlin", mainApp);
        
        tower.shootMonster(monster1);
        tower.shootMonster(monster2);
        assertEquals(2, tower.getFireballs().size());

        tower.despawnFireballsTargeting(monster1);
        assertEquals(1, tower.getFireballs().size());
    }

    // Add more tests for other methods and behaviors as necessary.
    @Test
    public void testFireballRemovalWhenHit() {
        TowerTile tower = new TowerTile(app, 5, 5, 100, 1, 50);
        Monster monster = new Monster(board, app, 1, 1, 100, 1, 10, "gremlin", mainApp);
        
        tower.shootMonster(monster);
        tower.updateFireballs(false);
        
        // Assuming that the fireball instantly hits the target (you might need to tweak this depending on your game's mechanics).
        assertTrue(tower.getFireballs().isEmpty());
    }

    @Test
    public void testMultipleUpgradesInSequence() {
        TowerTile tower = new TowerTile(app, 5, 5, 100, 1, 50);
        
        float initialRange = tower.getTowerRange();
        float initialSpeed = tower.getTowerFiringSpeed();
        float initialDamage = tower.getTowerDamage();
        
        tower.upgradeRange();
        tower.upgradeSpeed();
        tower.upgradeDamage();
        
        assertTrue(tower.getTowerRange() > initialRange);
        assertTrue(tower.getTowerFiringSpeed() > initialSpeed);
        assertTrue(tower.getTowerDamage() > initialDamage);
        
        tower.upgradeRange();
        tower.upgradeSpeed();
        tower.upgradeDamage();
        
        assertTrue(tower.getTowerRange() > initialRange + tower.getTowerRange());
        assertTrue(tower.getTowerFiringSpeed() > initialSpeed + tower.getTowerFiringSpeed());
        assertTrue(tower.getTowerDamage() > initialDamage + tower.getTowerDamage());
    }

    @Test
    public void testFiringSpeedIncrement() {
        TowerTile tower = new TowerTile(app, 5, 5, 100, 1, 50);
        Monster monster = new Monster(board, app, 1, 1, 100, 1, 10, "gremlin", mainApp);
        
        tower.shootMonster(monster);
        assertEquals(1, tower.getFireballs().size());
        
        tower.upgradeSpeed();
        
        tower.shootMonster(monster);
        assertEquals(2, tower.getFireballs().size());  // Should be able to shoot more frequently after speed upgrade
    }

    @Test
    public void testDamageIncrease() {
        TowerTile tower = new TowerTile(app, 5, 5, 100, 1, 50);
        Monster monster = new Monster(board, app, 1, 1, 100, 1, 10, "gremlin", mainApp);
        
        float initialMonsterHp = monster.getCurrentHp();
        
        tower.shootMonster(monster);
        tower.updateFireballs(false);
        
        float damageAfterFirstShot = initialMonsterHp - monster.getCurrentHp();
        
        tower.upgradeDamage();
        tower.shootMonster(monster);
        tower.updateFireballs(false);
        
        float damageAfterSecondShot = monster.getCurrentHp();
        
        assertTrue(damageAfterSecondShot < damageAfterFirstShot);  // Damage after upgrade should be higher
    }
    @Test
    public void testImageUpdateOnUpgrade() {
        TowerTile tower = new TowerTile(app, 5, 5, 100, 1, 50);
        
        // Assuming that we start with tower0.png
        assertTrue(tower.getImage().toString().contains("tower0.png"));
        
        tower.upgradeRange();
        tower.upgradeSpeed();
        tower.upgradeDamage();
        
        // Assuming the image changes to tower1.png after one upgrade in each category
        assertTrue(tower.getImage().toString().contains("tower1.png"));
        
        tower.upgradeRange();
        tower.upgradeSpeed();
        tower.upgradeDamage();
        
        // Assuming the image changes to tower2.png after two upgrades in each category
        assertTrue(tower.getImage().toString().contains("tower2.png"));
    }

    @Test
    public void testCostCalculation() {
        TowerTile tower = new TowerTile(app, 5, 5, 100, 1, 50);
        assertEquals(100, tower.getCost());  // Base cost
        
        tower.upgradeRange();
        assertEquals(120, tower.getCost());  // Base + one upgrade
        
        tower.upgradeSpeed();
        tower.upgradeDamage();
        assertEquals(160, tower.getCost());  // Base + three upgrades
    }

    @Test
    public void testHoveringDetection() {
        TowerTile tower = new TowerTile(app, 5, 5, 100, 1, 50);
        
        assertTrue(tower.isMouseHovering(5 * App.CELLSIZE, 5 * App.CELLSIZE));  // Mouse hovering on the tower
        assertFalse(tower.isMouseHovering(0, 0));  // Mouse not hovering on the tower
    }

    @Test
    public void testFireballDespawn() {
        TowerTile tower = new TowerTile(app, 5, 5, 100, 1, 50);
        Monster monster1 = new Monster(board, app, 1, 1, 100, 1, 10, "gremlin", mainApp);
        Monster monster2 = new Monster(board, app, 2, 2, 100, 1, 10, "gremlin", mainApp);
        
        tower.shootMonster(monster1);
        tower.shootMonster(monster2);
        
        assertEquals(2, tower.getFireballs().size());
        
        tower.despawnFireballsTargeting(monster1);
        assertEquals(1, tower.getFireballs().size());
    }

    @Test
    public void testNoFireballCreationBeforeThreshold() {
        TowerTile tower = new TowerTile(app, 5, 5, 100, 2, 50);  // Firing speed of 2 per second
        Monster monster = new Monster(board, app, 6, 6, 100, 1, 10, "gremlin", mainApp);  // Within tower's range
        
        tower.incrementTimeSinceLastShot(0.4);  // Less than half a second
        tower.shootMonster(monster);
        assertTrue(tower.getFireballs().isEmpty());
      }  

    @Test
    public void testTowerAppearanceStateChange() {
        TowerTile tower = new TowerTile(app, 5, 5, 100, 1, 50);
        
        assertEquals(0, tower.towerAppearanceState);  // Initial state
        
        tower.upgradeRange();
        tower.upgradeSpeed();
        tower.upgradeDamage();
        
        assertEquals(1, tower.towerAppearanceState);  // After first set of upgrades
        
        tower.upgradeRange();
        tower.upgradeSpeed();
        tower.upgradeDamage();
        
        assertEquals(2, tower.towerAppearanceState);  // After second set of upgrades
    }
    @Test
    public void testUpgradeBeyondMaximum() {
        TowerTile tower = new TowerTile(app, 5, 5, 100, 1, 50);
        
        for (int i = 0; i < 10; i++) {  // Arbitrary number beyond any reasonable cap
            tower.upgradeRange();
            tower.upgradeSpeed();
            tower.upgradeDamage();
        }
        
        // Assuming 3 is the max level for any upgrade, so it shouldn't go beyond that
        assertTrue(tower.getRangeUpgradeLevel() <= 3);
        assertTrue(tower.getSpeedUpgradeLevel() <= 3);
        assertTrue(tower.getDamageUpgradeLevel() <= 3);
    }
    @Test
    public void testTowerBaseState() {
        TowerTile tower = new TowerTile(app, 5, 5, 100, 1, 50);
        
        assertEquals(100, tower.getTowerRange());
        assertEquals(1, tower.getTowerFiringSpeed());
        assertEquals(50, tower.getTowerDamage());
    }

    @Test
    public void testNegativeUpgrades() {
        TowerTile tower = new TowerTile(app, 5, 5, 100, 1, 50);
        
        // Assume there's an internal mechanism preventing negative upgrades
        // but let's forcefully set negative upgrade levels for testing.
        tower.rangeUpgradeLevel = -1;
        tower.speedUpgradeLevel = -1;
        tower.damageUpgradeLevel = -1;
        
        tower.upgradeRange();
        tower.upgradeSpeed();
        tower.upgradeDamage();
        
        assertTrue(tower.getRangeUpgradeLevel() >= 0);
        assertTrue(tower.getSpeedUpgradeLevel() >= 0);
        assertTrue(tower.getDamageUpgradeLevel() >= 0);
    }

    @Test
    public void testTowerStateAfterMonsterDeath() {
        TowerTile tower = new TowerTile(app, 5, 5, 100, 1, 50);
        Monster monster = new Monster(board, app, 6, 6, 100, 1, 10, "gremlin", mainApp);  // Within tower's range
        
        tower.shootMonster(monster);
        monster.reduceHealth(200);  // Kill the monster
        
        // Ensure that any fireballs targeting the dead monster are despawned
        tower.despawnFireballsTargeting(monster);
        assertTrue(tower.getFireballs().isEmpty());
    }

    @Test
    public void testMultipleFireballs() {
        TowerTile tower = new TowerTile(app, 5, 5, 100, 2, 50);  // Firing speed of 2 per second
        Monster monster1 = new Monster(board, app, 6, 6, 100, 1, 10, "gremlin", mainApp);
        Monster monster2 = new Monster(board, app, 7, 7, 100, 1, 10, "gremlin", mainApp);
        
        tower.incrementTimeSinceLastShot(0.6);  // More than half a second
        tower.shootMonster(monster1);
        tower.shootMonster(monster2);
        
        assertEquals(2, tower.getFireballs().size());
    }
    
}
