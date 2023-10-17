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
        mainApp.setup();
        sidebar = new Sidebar(120, 680, mainApp); 
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
    @Test
    public void testIsManaPoolSpellButtonClicked() {
        assertTrue(sidebar.isManaPoolSpellButtonClicked(sidebar.buttonX + 5, sidebar.buttonY + 6 * (sidebar.buttonHeight + 10) + 5));
        assertFalse(sidebar.isManaPoolSpellButtonClicked(sidebar.buttonX - 5, sidebar.buttonY + 6 * (sidebar.buttonHeight + 10) + 5));
    }

    @Test
    public void testIsSpeedToggleClicked() {
        assertTrue(sidebar.isSpeedToggleClicked(sidebar.buttonX + 5, sidebar.buttonY + 5));
        assertFalse(sidebar.isSpeedToggleClicked(sidebar.buttonX - 5, sidebar.buttonY + 5));
    }

    // Hover Tests
    @Test
    public void testIsButtonHovered() {
        assertTrue(sidebar.isButtonHovered(sidebar.buttonX + 5, sidebar.buttonY + 2 * (sidebar.buttonHeight + 10) + 5, 2 * (sidebar.buttonHeight + 10)));
        assertFalse(sidebar.isButtonHovered(sidebar.buttonX - 5, sidebar.buttonY + 2 * (sidebar.buttonHeight + 10) + 5, 2 * (sidebar.buttonHeight + 10)));
    }

    // State Change Tests
    @Test
    public void testExclusiveModes() {
        sidebar.toggleTowerPlacementMode();
        assertTrue(sidebar.isInTowerPlacementMode());
        assertFalse(sidebar.isInRangeUpgradeMode());
        assertFalse(sidebar.isInSpeedUpgradeMode());
        assertFalse(sidebar.isInDamageUpgradeMode());

        sidebar.toggleRangeUpgradeMode();
        assertFalse(sidebar.isInTowerPlacementMode());
        assertTrue(sidebar.isInRangeUpgradeMode());
        assertFalse(sidebar.isInSpeedUpgradeMode());
        assertFalse(sidebar.isInDamageUpgradeMode());
    }

    // Mode Toggle Consistency Tests
    @Test
    public void testModeToggleConsistency() {
        sidebar.toggleTowerPlacementMode();
        assertTrue(sidebar.isInTowerPlacementMode());
        sidebar.toggleRangeUpgradeMode();
        assertFalse(sidebar.isInTowerPlacementMode());
        assertTrue(sidebar.isInRangeUpgradeMode());
    }

    // Sidebar Boundary Tests
    @Test
    public void testClickOutsideSidebarBoundary() {
        assertFalse(sidebar.isButtonClicked(sidebar.buttonX - 100, sidebar.buttonY + 5, 0));
        assertFalse(sidebar.isButtonClicked(sidebar.buttonX + sidebar.width + 100, sidebar.buttonY + 5, 0));
    }

    @Test
    public void testButtonHover() {
        assertTrue(sidebar.isButtonHovered(sidebar.buttonX + 5, sidebar.buttonY + 5, 0));
        assertFalse(sidebar.isButtonHovered(sidebar.buttonX - 10, sidebar.buttonY - 10, 0));
    }

    @Test
    public void testButtonClick() {
        assertTrue(sidebar.isButtonClicked(sidebar.buttonX + 5, sidebar.buttonY + 5, 0));
        assertFalse(sidebar.isButtonClicked(sidebar.buttonX - 10, sidebar.buttonY - 10, 0));
    }

    @Test
    public void testUpgradeModeConsistency() {
        sidebar.toggleRangeUpgradeMode();
        assertTrue(sidebar.isInRangeUpgradeMode());
        assertFalse(sidebar.isInSpeedUpgradeMode());
        assertFalse(sidebar.isInDamageUpgradeMode());

        sidebar.toggleSpeedUpgradeMode();
        assertFalse(sidebar.isInRangeUpgradeMode());
        assertTrue(sidebar.isInSpeedUpgradeMode());
        assertFalse(sidebar.isInDamageUpgradeMode());
    }
    @Test
    public void testSidebarBoundaries() {
        assertEquals(sidebar.width, 100);
        assertEquals(sidebar.height, 600);
        assertEquals(sidebar.buttonX, App.WIDTH - sidebar.width + 10);
        assertEquals(sidebar.buttonY, App.TOPBAR + 10);
    }

    @Test
    public void testTowerPlacementCost() {
        // Test the calculation of the tower placement cost with varying numbers of upgrades.
        assertEquals(100, mainApp.calculateTowerPlacementCost(0)); // Assuming 100 is the base cost
        assertEquals(120, mainApp.calculateTowerPlacementCost(1));
        assertEquals(140, mainApp.calculateTowerPlacementCost(2));
    }

    @Test
    public void testTowerUpgradeCost() {
        assertEquals(20, mainApp.calculateUpgradeCost(0));
        assertEquals(30, mainApp.calculateUpgradeCost(1));
        assertEquals(40, mainApp.calculateUpgradeCost(2));
    }
    @Test
    public void testTowerPlacementMode() {
        assertFalse(sidebar.isInTowerPlacementMode());
        sidebar.toggleTowerPlacementMode();
        assertTrue(sidebar.isInTowerPlacementMode());
    }

    @Test
    public void testRangeUpgradeMode() {
        assertFalse(sidebar.isInRangeUpgradeMode());
        sidebar.toggleRangeUpgradeMode();
        assertTrue(sidebar.isInRangeUpgradeMode());
    }

    @Test
    public void testSpeedUpgradeMode() {
        assertFalse(sidebar.isInSpeedUpgradeMode());
        sidebar.toggleSpeedUpgradeMode();
        assertTrue(sidebar.isInSpeedUpgradeMode());
    }

    @Test
    public void testDamageUpgradeMode() {
        assertFalse(sidebar.isInDamageUpgradeMode());
        sidebar.toggleDamageUpgradeMode();
        assertTrue(sidebar.isInDamageUpgradeMode());
    }

    @Test
    public void testSpeedToggle() {
        assertFalse(sidebar.isDoubleSpeedMode());
        sidebar.toggleSpeedMode();
        assertTrue(sidebar.isDoubleSpeedMode());
        
        assertTrue(sidebar.isSpeedToggleClicked(sidebar.buttonX + 5, sidebar.buttonY + 5));
        assertFalse(sidebar.isSpeedToggleClicked(sidebar.buttonX - 5, sidebar.buttonY + 5));
    }

    @Test
    public void testPauseMode() {
        assertFalse(sidebar.isPauseActive());
        sidebar.togglePauseMode();
        assertTrue(sidebar.isPauseActive());

        assertTrue(sidebar.isPauseButtonClicked(sidebar.buttonX + 5, sidebar.buttonY + sidebar.buttonHeight + 15));
        assertFalse(sidebar.isPauseButtonClicked(sidebar.buttonX - 5, sidebar.buttonY + sidebar.buttonHeight + 15));
    }

    @Test
    public void testManaPoolSpellButtonClick() {
        assertTrue(sidebar.isManaPoolSpellButtonClicked(sidebar.buttonX + 5, sidebar.buttonY + 6 * (sidebar.buttonHeight + 10) + 5));
        assertFalse(sidebar.isManaPoolSpellButtonClicked(sidebar.buttonX - 5, sidebar.buttonY + 6 * (sidebar.buttonHeight + 10) + 5));
    }

    @Test
    public void testButtonHovered() {
        assertTrue(sidebar.isButtonHovered(sidebar.buttonX + 5, sidebar.buttonY + 5, 0));
        assertFalse(sidebar.isButtonHovered(sidebar.buttonX - 5, sidebar.buttonY + 5, 0));
    }
    
    @Test
    public void testManaPoolSpellButtonHover() {
        assertTrue(sidebar.isManaPoolSpellButtonClicked(sidebar.buttonX + 5, sidebar.buttonY + 6 * (sidebar.buttonHeight + 10) + 5));
        assertFalse(sidebar.isManaPoolSpellButtonClicked(sidebar.buttonX - 5, sidebar.buttonY + 6 * (sidebar.buttonHeight + 10) + 5));
    }
    
    @Test
    public void testAnyUpgradeMode() {
        assertFalse(sidebar.isInAnyUpgradeMode());
        sidebar.toggleRangeUpgradeMode();
        assertTrue(sidebar.isInAnyUpgradeMode());
        sidebar.toggleRangeUpgradeMode();
        assertFalse(sidebar.isInAnyUpgradeMode());
    }
    
    @Test
    public void testModeReset() {
        sidebar.toggleTowerPlacementMode();
        assertTrue(sidebar.isInTowerPlacementMode());
        sidebar.toggleTowerPlacementMode();
        assertFalse(sidebar.isInTowerPlacementMode());
    }
    
    @Test
    public void testPauseButton() {
        assertFalse(sidebar.isPauseActive());
        sidebar.togglePauseMode();
        assertTrue(sidebar.isPauseActive());
    }
    
    @Test
    public void testUpgradeModesMutualExclusion() {
        sidebar.toggleRangeUpgradeMode();
        assertTrue(sidebar.isInRangeUpgradeMode());
        assertFalse(sidebar.isInSpeedUpgradeMode());
        
        sidebar.toggleSpeedUpgradeMode();
        assertFalse(sidebar.isInRangeUpgradeMode());
        assertTrue(sidebar.isInSpeedUpgradeMode());
    }
    
    @Test
    public void testInitialStates() {
        assertFalse(sidebar.isInTowerPlacementMode());
        assertFalse(sidebar.isDoubleSpeedMode());
        assertFalse(sidebar.isPauseActive());
        assertFalse(sidebar.isInAnyUpgradeMode());
    }
    
    @Test
    public void testButtonClicked() {
        assertTrue(sidebar.isButtonClicked(sidebar.buttonX + 5, sidebar.buttonY + 5, 0));
        assertFalse(sidebar.isButtonClicked(sidebar.buttonX - 5, sidebar.buttonY + 5, 0));
    }
}
