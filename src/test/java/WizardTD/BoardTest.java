package WizardTD;

import static org.junit.jupiter.api.Assertions.*;

import WizardTD.App;
import WizardTD.subtiles.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BoardTest {

    Board board;
    App mainApp;

    @BeforeEach
    public void setUp() {
        mainApp = new App();  // Initialize the App
        board = new Board();  // Initialize the board
    }

    @Test
    public void testLoadLayout() {
        // Assuming there's a valid layout file named "test_layout.txt" in the project directory
        board.loadLayout("level1.txt", mainApp);
        Tile[][] tiles = board.getTiles();

        // Check if the board is not empty
        assertNotNull(tiles);
        assertTrue(tiles.length > 0);
        assertTrue(tiles[0].length > 0);
    }

    @Test
    public void testIsPathTile() {
        board.loadLayout("test_layout.txt", mainApp);
        
        // Assuming that the test_layout.txt file has a PathTile at (5,5)
        assertTrue(board.isPathTile(5, 5));
        
        // Assuming that the test_layout.txt file does NOT have a PathTile at (1,1)
        assertFalse(board.isPathTile(1, 1));
    }

    @Test
    public void testIsWizardHouseTile() {
        board.loadLayout("test_layout.txt", mainApp);
        
        // Assuming that the test_layout.txt file has a WizardHouseTile at (7,7)
        assertTrue(board.isWizardHouseTile(7, 7));
        
        // Assuming that the test_layout.txt file does NOT have a WizardHouseTile at (1,1)
        assertFalse(board.isWizardHouseTile(1, 1));
    }

    @Test
    public void testPlaceTower() {
        board.loadLayout("test_layout.txt", mainApp);
        
        // Assuming that the test_layout.txt file has a GrassTile at (3,3)
        TowerTile tower = board.placeTower(3, 3, mainApp, 100.0f, 1.0f, 50.0f);
        assertNotNull(tower);
        assertEquals(3, tower.getX());
        assertEquals(3, tower.getY());
        
        // Trying to place a tower on a non-GrassTile should return null
        TowerTile invalidTower = board.placeTower(5, 5, mainApp, 100.0f, 1.0f, 50.0f);  // Assuming (5,5) is a PathTile
        assertNull(invalidTower);
    }

    @Test
    public void testUpgradeTowerRange() {
        board.loadLayout("test_layout.txt", mainApp);
        
        TowerTile tower = board.placeTower(3, 3, mainApp, 100.0f, 1.0f, 50.0f);
        float initialRange = tower.getTowerDamage();
        
        board.upgradeTowerRange(3 * App.CELLSIZE, 3 * App.CELLSIZE + App.TOPBAR, mainApp);
        assertTrue(tower.getTowerDamage() > initialRange);
    }
}

