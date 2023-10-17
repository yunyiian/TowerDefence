package WizardTD;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class TopBarTest {

    private TopBar topBar;
    private App app;

    @BeforeEach
    public void setUp() {
        app = new App();
        app.setup();  // Initializes the App settings and sets up initial conditions
        topBar = new TopBar(App.WIDTH, App.TOPBAR, app.mana, app.initialManaCap);  // Changed app.WIDTH and app.TOPBAR to App.WIDTH and App.TOPBAR
    }    

    @Test
    public void testUpdateMana() {
        topBar.updateMana(50);
        assertEquals(150, topBar.getMana(), "Mana should be updated correctly.");

        topBar.updateMana(100);
        assertEquals(200, topBar.getMana(), "Mana should not exceed mana cap.");
    }

    @Test
    public void testSetMana() {
        topBar.setMana(50);
        assertEquals(50, topBar.getMana(), "Mana should be set correctly.");

        topBar.setMana(300);
        assertEquals(200, topBar.getMana(), "Mana should not exceed mana cap when set.");
    }

    @Test
    public void testSetManaCap() {
        topBar.setManaCap(250);
        assertEquals(250, topBar.getManaCap(), "Mana cap should be updated correctly.");

        topBar.updateMana(200);
        assertEquals(250, topBar.getMana(), "Mana should not exceed updated mana cap.");
    }
    @Test
    public void testManaOverflow() {
        topBar = new TopBar(app.WIDTH, app.TOPBAR, 180, 200);
        topBar.updateMana(50);
        assertEquals(200, topBar.getMana(), "Mana should not exceed mana cap when updated.");
    }

    @Test
    public void testManaDepletion() {
        topBar = new TopBar(app.WIDTH, app.TOPBAR, 50, 200);
        topBar.updateMana(-100);
        assertEquals(0, topBar.getMana(), "Mana should not go below zero.");
    }
    @Test
    public void testWaveTimerInitialization() {
        assertEquals(-1, topBar.waveNumber, "Initial wave number should be -1.");
        assertEquals(0.0f, topBar.timeRemaining, "Initial time remaining should be 0.0.");
    }
    
    @Test
    public void testUpdateManaWithZero() {
        int initialMana = topBar.getMana();
        topBar.updateMana(0);
        assertEquals(initialMana, topBar.getMana(), "Mana should remain unchanged when updated with 0.");
    }
    
    @Test
    public void testSetNegativeManaCap() {
        int initialManaCap = topBar.getManaCap();
        topBar.setManaCap(-100);
        assertEquals(initialManaCap, topBar.getManaCap(), "Mana cap should not be set to a negative value.");
    }
    
    @Test
    public void testSetWaveTimerWithNegativeWaveNumber() {
        topBar.setWaveTimer(-5, 60.0f);
        assertNotEquals(-5, topBar.waveNumber, "Wave number should not be set to a negative value.");
    }
    
    @Test
    public void testUpdateManaWithNegativeValueExceedingCurrentMana() {
        topBar.setMana(50);
        topBar.updateMana(-100);
        assertEquals(0, topBar.getMana(), "Mana should not go below zero.");
    }
    
    @Test
    public void testManaCapIncreaseAndCurrentMana() {
        topBar.setMana(200);
        topBar.setManaCap(250);
        topBar.updateMana(50);
        assertEquals(250, topBar.getMana(), "Mana should be equal to the new mana cap when increased.");
    }
    
    @Test
    public void testManaCapDecreaseAndCurrentMana() {
        topBar.setMana(200);
        topBar.setManaCap(150);
        assertEquals(150, topBar.getMana(), "Mana should decrease to the new mana cap when the cap is reduced.");
    }
    // Additional tests can be added as needed
}
