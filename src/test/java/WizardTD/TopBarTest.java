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
        topBar = new TopBar(app.WIDTH, app.TOPBAR, app.mana, app.initialManaCap);
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

    // Additional tests can be added as needed
}
