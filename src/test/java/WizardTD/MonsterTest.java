package WizardTD;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import WizardTD.*;
import WizardTD.PathFinder.Node;
import java.util.List;


public class MonsterTest {

    private PathFinder pathFinder;
    private Board board;
    private App mainapp;

    @Test
    public void testMonsterInitialization() {
        Monster monster = new Monster(board, null, 1.5f, 10, 100.0f, 0.8f, 15.0f, "type", mainapp);
        assertEquals(1.5f, monster.getSpeed());
        assertEquals(100.0f, monster.getCurrentHp());
        assertEquals(0.8f, monster.getArmour());
        assertEquals(15.0f, monster.getManaGainedOnKill());
        assertEquals("gremlin", monster.getType());
        assertEquals(10, monster.spawnDelay);
    }

    @Test
    public void testReduceHealth() {
        Board board = new Board();
        Monster monster = new Monster(board, null, 1.0f, 0, 100.0f, 1.0f, 10.0f, "type", null);
        monster.reduceHealth(25.0f);
        assertEquals(75.0f, monster.getCurrentHp());
    }

    @Test
    public void testMonsterSpeed() {
        Board board = new Board();
        Monster monster = new Monster(board, null, 1.5f, 0, 100.0f, 1.0f, 10.0f, "type", null);
        assertEquals(1.5f, monster.getSpeed());
    }

    @Test
    public void testHealthDoesNotGoNegative() {
        Board board = new Board();
        Monster monster = new Monster(board, null, 1.0f, 0, 100.0f, 1.0f, 10.0f, "type", null);
        monster.reduceHealth(150.0f);
        assertEquals(0.0f, monster.getCurrentHp());
    }

    @Test
    public void testMonsterMoveOutside() {
        Monster monster = new Monster(board, null, 1.5f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        float initialX = monster.getX();
        float initialY = monster.getY();
        monster.moveOutside(board.getTiles());
        // The expected position after moving outside will depend on the initial spawn location and direction.
        // For this example, assuming the monster was spawned at the top of the board and is moving downwards:
        assertTrue(monster.getY() > initialY);
        assertEquals("down", monster.direction);
    }

    @Test
    public void testMonsterMoveInside() {
        Monster monster = new Monster(board, null, 1.5f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        float initialX = monster.getX();
        float initialY = monster.getY();
        // For this test, we might need to set a known board state or mock the PathFinder's findPath method.
        monster.moveInside(board.getTiles());
        // Assertions will depend on the board state and the monster's path.
        // For simplicity, we'll just verify the monster moved:
        assertTrue(monster.getX() != initialX || monster.getY() != initialY);
    }

    @Test
    public void testMonsterDeath() {
        Monster monster = new Monster(board, null, 1.5f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        monster.reduceHealth(100.0f);
        assertEquals(0.0f, monster.getCurrentHp());
        // Assuming the onDeath method sets the speed to 0 and initiates the death sequence:
        assertEquals(0.0f, monster.speed);
        // Add more assertions based on the onDeath method's functionality...
    }

    @Test
    public void testMonsterMoveInDirection() {
        Monster monster = new Monster(board, null, 1.5f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        monster.direction = "up";
        float initialY = monster.getY();
        monster.moveInDirection();
        assertTrue(monster.getY() < initialY);
    }

    @Test
    public void testMonsterArmourEffectOnDamage() {
        Monster monster = new Monster(board, null, 1.0f, 0, 100.0f, 0.5f, 10.0f, "type", mainapp);
        monster.reduceHealth(50.0f);
        assertEquals(75.0f, monster.getCurrentHp());  // Expected 25 damage due to armor multiplier of 0.5
    }

    @Test
    public void testMonsterManaMultiplier() {
        Monster monster = new Monster(board, null, 1.0f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        monster.setManaMultiplier(2.0f);
        monster.reduceHealth(100.0f);  // Kill the monster
    }

    @Test
    public void testMonsterSpawnPoints() {
        Monster monster = new Monster(board, null, 1.0f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        List<int[]> spawnPoints = monster.getSpawnPoints();
        for (int[] point : spawnPoints) {
            assertTrue(board.isPathTile(point[0], point[1]));
        }
    }

    @Test
    public void testMonsterDirectionAngle() {
        Monster monster = new Monster(board, null, 1.0f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        monster.direction = "up";
        assertEquals(270, monster.getDirectionAngle());
        monster.direction = "down";
        assertEquals(90, monster.getDirectionAngle());
        monster.direction = "left";
        assertEquals(180, monster.getDirectionAngle());
        monster.direction = "right";
        assertEquals(0, monster.getDirectionAngle());
    }

    @Test
    public void testMonsterResetPosition() {
        Monster monster = new Monster(board, null, 1.0f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        float initialX = monster.getX();
        float initialY = monster.getY();
        monster.resetMonsterPosition();
        assertTrue(initialX != monster.getX() || initialY != monster.getY());  // Monster's position should change
    }

    @Test
    public void testMonsterDeathAnimation() {
        Monster monster = new Monster(board, null, 1.0f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        monster.reduceHealth(100.0f);  // Reduce health to 0 to trigger death
        monster.update();
        assertEquals(1, monster.deathFrameCount);  // Ensure death animation starts
    }

    @Test
    public void testMonsterMovementOutside() {
        Monster monster = new Monster(board, null, 1.0f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        monster.x = -1;  // Place the monster outside the board on the left
        monster.move();
        assertEquals("right", monster.direction);  // Monster should move to the right to get back onto the board
    }

    @Test
    public void testMonsterSpeedMovement() {
        Monster monster = new Monster(board, null, 2.0f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        float initialX = monster.getX();
        float initialY = monster.getY();
        monster.moveWithSpeed();
        float deltaX = monster.getX() - initialX;
        float deltaY = monster.getY() - initialY;
        assertTrue(Math.abs(deltaX) <= 2.0f && Math.abs(deltaY) <= 2.0f);  // Ensure monster moved based on its speed
    }

    @Test
    public void testMonsterNearTarget() {
        Monster monster = new Monster(board, null, 1.0f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        monster.targetX = monster.x;
        monster.targetY = monster.y;
        assertTrue(monster.isNearTarget());  // Monster is at its target
    }

    @Test
    public void testMonsterAlignedWithTile() {
        Monster monster = new Monster(board, null, 1.0f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        monster.x = 1.0f;  // Monster is aligned with a tile
        monster.y = 1.0f;  // Monster is aligned with a tile
        assertTrue(monster.isAlignedWithTile());  
    }


    @Test
    public void testFindWizardHouseTileGoal() {
        Monster monster = new Monster(board, null, 1.0f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        PathFinder.Node goal = monster.findWizardHouseTileGoal(board.getTiles());
        assertNotNull(goal);  // The goal should not be null if a WizardHouseTile exists on the board
    }

    @Test
    public void testSetManaMultiplier() {
        Monster monster = new Monster(board, null, 1.0f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        monster.setManaMultiplier(3.0f);
        assertEquals(3.0f, monster.manaMultiplier);  // Ensure the multiplier was set correctly
    }

    @Test
    public void testMoveInsideWithNoPath() {
        Monster monster = new Monster(board, null, 1.0f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        monster.currentPath = null;  // Ensure there's no path
        float initialX = monster.getX();
        float initialY = monster.getY();
        monster.moveInside(board.getTiles());
        assertEquals(initialX, monster.getX());  // X position should not change
        assertEquals(initialY, monster.getY());  // Y position should not change
    }

    @Test
    public void testMoveInsideToTarget() {
        Monster monster = new Monster(board, null, 1.0f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        float initialX = monster.getX();
        float initialY = monster.getY();
        monster.targetX = initialX + 1;
        monster.targetY = initialY + 1;
        monster.moveInside(board.getTiles());
        assertTrue(monster.getX() > initialX);  // X position should increase
        assertTrue(monster.getY() > initialY);  // Y position should increase
    }
    @Test
    public void testMonsterInitialPosition() {
        Monster monster = new Monster(board, null, 1.0f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        List<int[]> spawnPoints = monster.getSpawnPoints();
        boolean validSpawn = false;
        for (int[] point : spawnPoints) {
            if (monster.getX() == point[0] && monster.getY() == point[1]) {
                validSpawn = true;
                break;
            }
        }
        assertTrue(validSpawn);
    }

    @Test
    public void testMonsterTargetMovement() {
        Monster monster = new Monster(board, null, 1.0f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        monster.targetX = 5;
        monster.targetY = 5;
        monster.moveInside(board.getTiles());
        assertFalse(monster.getX() == 5 && monster.getY() == 5);
    }

    @Test
    public void testMonsterDirectionAngleCalculation() {
        Monster monster = new Monster(board, null, 1.0f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        monster.direction = "up";
        assertEquals(270, monster.getDirectionAngle());
    }

    @Test
    public void testMonsterDeathAnimationFrames() {
        Monster monster = new Monster(board, null, 1.0f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        monster.onDeath();
        assertEquals(1, monster.deathFrameCount);  // Ensure death animation starts

        for (int i = 0; i < 19; i++) {
            monster.update();
        }
        assertEquals(20, monster.deathFrameCount);  // Should run for 20 frames
    }
    @Test
    public void testMonsterSpeedAccumulation() {
        Monster monster = new Monster(board, null, 1.5f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        monster.moveWithSpeed();
        assertTrue(monster.leftoverMove > 0);  // Ensure leftoverMove accumulates
    }

    @Test
    public void testMonsterReachingWizardHouse() {

        Monster monster = new Monster(board, null, 1.0f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        monster.x = 5;  // Assuming this position corresponds to a WizardHouseTile
        monster.y = 5;
        monster.update();
        assertFalse(monster.getX() == 5 && monster.getY() == 5);
    }

    @Test
    public void testMonsterDirectionalMovement() {
        Monster monster = new Monster(board, null, 1.0f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        float initialX = monster.getX();
        float initialY = monster.getY();
        
        monster.direction = "up";
        monster.moveInDirection();
        assertTrue(monster.getY() < initialY);

        monster.direction = "down";
        monster.moveInDirection();
        assertTrue(monster.getY() > initialY);

        monster.direction = "left";
        monster.moveInDirection();
        assertTrue(monster.getX() < initialX);

        monster.direction = "right";
        monster.moveInDirection();
        assertTrue(monster.getX() > initialX);
    }

    @Test
    public void testMonsterManaMultiplierEffect() {
        Monster monster = new Monster(board, null, 1.0f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        monster.setManaMultiplier(2.0f);
        monster.reduceHealth(100.0f);  // Kill the monster
    }

    @Test
    public void testMonsterResetPositionToSpawnPoint() {
        Monster monster = new Monster(board, null, 1.0f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        monster.resetMonsterPosition();
        List<int[]> spawnPoints = monster.getSpawnPoints();
        boolean isAtSpawnPoint = false;
        for (int[] point : spawnPoints) {
            if (point[0] == monster.getX() && point[1] == monster.getY()) {
                isAtSpawnPoint = true;
                break;
            }
        }
        assertTrue(isAtSpawnPoint);
    }

    @Test
    public void testMonsterPathfindingUpdate() {
        Monster monster = new Monster(board, null, 1.0f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        monster.moveInside(board.getTiles());
        assertNotNull(monster.currentPath);
    }

    @Test
    public void testMonsterHealthBarPercentage() {
        Monster monster = new Monster(board, null, 1.0f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        monster.reduceHealth(25.0f);
        float hpPercentage = monster.getCurrentHp() / monster.hp;
        assertEquals(0.75f, hpPercentage);
    }

    @Test
    public void testMonsterInitialSpawnPoint() {
        Monster monster = new Monster(board, null, 1.0f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        boolean isSpawnedOnPath = board.isPathTile((int) monster.getX(), (int) monster.getY());
        assertTrue(isSpawnedOnPath);
    }

    @Test
    public void testMonsterArmourCalculation() {
        Monster monster = new Monster(board, null, 1.0f, 0, 100.0f, 0.5f, 10.0f, "type", mainapp);
        monster.reduceHealth(40.0f); // With armor, actual damage should be 20.0f
        assertEquals(80.0f, monster.getCurrentHp());
    }

    @Test
    public void testMonsterLeftoverMovement() {
        Monster monster = new Monster(board, null, 1.5f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        monster.leftoverMove = 0.6f;
        monster.moveWithSpeed();
        assertTrue(monster.leftoverMove < 0.6f); // Leftover movement should decrease
    }

    @Test
    public void testMonsterDeathSequenceFrameCount() {
        Monster monster = new Monster(board, null, 1.0f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        monster.reduceHealth(100.0f); // Kill the monster
        monster.update();
        assertTrue(monster.deathFrameCount > 0);
    }

    @Test
    public void testMonsterSpeedMovementCalculation() {
        Monster monster = new Monster(board, null, 1.5f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        float initialX = monster.getX();
        monster.moveWithSpeed();
        assertTrue(monster.getX() != initialX); // Monster should have moved
    }
    @Test
    public void testMonsterImageLoadingFailure() {
        Monster monster = new Monster(board, null, 1.0f, 0, 100.0f, 1.0f, 10.0f, "nonExistentType", mainapp);
        assertNull(monster.getImage()); // Image should be null for an invalid path
    }

    @Test
    public void testMonsterNegativeManaMultiplier() {
        Monster monster = new Monster(board, null, 1.0f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        monster.setManaMultiplier(-2.0f);
        assertEquals(1.0f, monster.manaMultiplier); // Assuming you have logic to reset negative multipliers to 1.0
    }

    @Test
    public void testMonsterPositionAlignment() {
        Monster monster = new Monster(board, null, 1.0f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        monster.x = 5.0f; // Perfectly aligned with tile
        monster.y = 5.0f; // Perfectly aligned with tile
        assertTrue(monster.isAlignedWithTile());
    }

    @Test
    public void testMonsterPositionReset() {
        Monster monster = new Monster(board, null, 1.0f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        float initialX = monster.getX();
        float initialY = monster.getY();
        monster.resetMonsterPosition();
        monster.resetMonsterPosition(); // Resetting multiple times
        assertTrue(initialX != monster.getX() || initialY != monster.getY());
    }

    @Test
    public void testMonsterMovementWithLeftoverMove() {
        Monster monster = new Monster(board, null, 1.5f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        monster.leftoverMove = 0.75f;
        monster.moveWithSpeed();
        assertTrue(monster.leftoverMove >= 0 && monster.leftoverMove < 1); // Assert leftoverMove is within expected bounds
    }

    @Test
    public void testMonsterNearTargetEpsilon() {
        Monster monster = new Monster(board, null, 1.5f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        monster.targetX = monster.x + Monster.EPSILON / 2; // Setting the target within EPSILON range
        monster.targetY = monster.y;
        assertTrue(monster.isNearTarget());
    }

    @Test
    public void testMonsterDeathSequenceWithImages() {
        Monster monster = new Monster(board, null, 1.5f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        monster.reduceHealth(100.0f); // Kill the monster
        monster.update();
        assertTrue(monster.deathFrameCount > 0 && monster.deathFrameCount < monster.deathImages.length * 4);
    }

    @Test
    public void testMonsterHPBarRenderingLogic() {
        Monster monster = new Monster(board, null, 1.5f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        float hpPercentage = monster.getCurrentHp() / monster.hp;
        assertTrue(hpPercentage >= 0 && hpPercentage <= 1);
    }
    @Test
    public void testMonsterMovementTowardWizardHouse() {
        Monster monster = new Monster(board, null, 1.5f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        monster.moveInside(board.getTiles());
        assertFalse(monster.x == monster.targetX && monster.y == monster.targetY); // Assert that monster has moved towards the target.
    }

    @Test
    public void testMonsterImageLoading() {
        Monster monster = new Monster(board, null, 1.5f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        assertNotNull(monster.getImage());
    }

    @Test
    public void testMonsterArmourBoundaries() {
        Monster monster = new Monster(board, null, 1.5f, 0, 100.0f, -0.5f, 10.0f, "type", mainapp); // Negative armour value
        monster.reduceHealth(50.0f);
        assertTrue(monster.getCurrentHp() > 50); // Due to negative armour, the health should decrease by less than 50

        monster = new Monster(board, null, 1.5f, 0, 100.0f, 2.0f, 10.0f, "type", mainapp); // Armour value greater than 1
        monster.reduceHealth(50.0f);
        assertEquals(0.0f, monster.getCurrentHp()); // Monster should be dead as the damage is amplified by the armour

        monster = new Monster(board, null, 1.5f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp); // Exact armour value of 1
        monster.reduceHealth(50.0f);
        assertEquals(50.0f, monster.getCurrentHp()); // Damage should be exactly 50
    }

    @Test
    public void testMonsterDirectionAfterResetPosition() {
        Monster monster = new Monster(board, null, 1.5f, 0, 100.0f, 1.0f, 10.0f, "type", mainapp);
        monster.x = -1.0f; // Outside the board
        monster.resetMonsterPosition();
        assertTrue(monster.direction.equals("right") || monster.direction.equals("left") || monster.direction.equals("up") || monster.direction.equals("down"));
    }
}

