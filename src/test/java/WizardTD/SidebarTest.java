package WizardTD;

import static org.junit.jupiter.api.Assertions.*;

import WizardTD.App;
import WizardTD.subtiles.TowerTile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SidebarTest {

    Sidebar sidebar;
    App mainApp;

    @BeforeEach
    public void setUp() {
        mainApp = new App(); // Initialize the App
        sidebar = new Sidebar(100, 600, mainApp); // Use arbitrary width and height for testing
    }

    @Test
    public void testToggleTowerPlacementMode() {
        assertFalse(sidebar.isInTowerPlacementMode());
        sidebar.toggleTowerPlacementMode();
        assertTrue(sidebar.isInTowerPlacementMode());
        sidebar.toggleTowerPlacementMode();
        assertFalse(sidebar.isInTowerPlacementMode());
    }

    @Test
    public void testToggleRangeUpgradeMode() {
        assertFalse(sidebar.isInRangeUpgradeMode());
        sidebar.toggleRangeUpgradeMode();
        assertTrue(sidebar.isInRangeUpgradeMode());
        sidebar.toggleRangeUpgradeMode();
        assertFalse(sidebar.isInRangeUpgradeMode());
    }

    @Test
    public void testToggleSpeedUpgradeMode() {
        assertFalse(sidebar.isInSpeedUpgradeMode());
        sidebar.toggleSpeedUpgradeMode();
        assertTrue(sidebar.isInSpeedUpgradeMode());
        sidebar.toggleSpeedUpgradeMode();
        assertFalse(sidebar.isInSpeedUpgradeMode());
    }

    @Test
    public void testToggleDamageUpgradeMode() {
        assertFalse(sidebar.isInDamageUpgradeMode());
        sidebar.toggleDamageUpgradeMode();
        assertTrue(sidebar.isInDamageUpgradeMode());
        sidebar.toggleDamageUpgradeMode();
        assertFalse(sidebar.isInDamageUpgradeMode());
    }

    @Test
    public void testIsInAnyUpgradeMode() {
        assertFalse(sidebar.isInAnyUpgradeMode());
        sidebar.toggleRangeUpgradeMode();
        assertTrue(sidebar.isInAnyUpgradeMode());
        sidebar.toggleSpeedUpgradeMode();
        assertTrue(sidebar.isInAnyUpgradeMode());
        sidebar.toggleDamageUpgradeMode();
        assertTrue(sidebar.isInAnyUpgradeMode());
    }

    @Test
    public void testToggleSpeedMode() {
        assertFalse(sidebar.isDoubleSpeedMode());
        sidebar.toggleSpeedMode();
        assertTrue(sidebar.isDoubleSpeedMode());
        sidebar.toggleSpeedMode();
        assertFalse(sidebar.isDoubleSpeedMode());
    }

    @Test
    public void testTogglePauseMode() {
        assertFalse(sidebar.isPauseActive());
        sidebar.togglePauseMode();
        assertTrue(sidebar.isPauseActive());
        sidebar.togglePauseMode();
        assertFalse(sidebar.isPauseActive());
    }

    // Add more tests for other methods as necessary.
}
