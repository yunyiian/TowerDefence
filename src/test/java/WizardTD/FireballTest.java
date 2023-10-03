package WizardTD;

import WizardTD.Fireball;
import WizardTD.Monster;
import WizardTD.Board;
import processing.core.PApplet;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FireballTest {

    PApplet app;
    Monster target;
    Fireball fireball;
    Board board;

    @BeforeEach
    public void setUp() {
        app = new PApplet();  // Initialize the PApplet
        board = new Board();  // Initialize the board
        
        // Load layout for the board before creating monsters
        board.loadLayout("path_to_layout_file.txt", new App()); // You might need to adjust the path based on your project structure.
        
        // You might need to adjust this if the Monster constructor or its dependencies change.
        target = new Monster(board, app, 2.0f, 0, 100, 1.0f, 10, "monsterType", new App());
        
        fireball = new Fireball(0, 0, target, app);  // Starting fireball at (0, 0)
    }

    @Test
    public void testFireballInitialization() {
        assertEquals(0, fireball.x);
        assertEquals(0, fireball.y);
        assertNotNull(fireball.fireballImage);
    }

    @Test
    public void testInitialTrajectory() {
        assertTrue(fireball.dx != 0 || fireball.dy != 0);  // Fireball should be moving
    }

    @Test
    public void testUpdate() {
        fireball.update(false);
        assertTrue(fireball.x != 0 || fireball.y != 0);  // Fireball should have moved

        float prevX = fireball.x;
        float prevY = fireball.y;
        fireball.update(true);  // Update with double speed
        assertTrue(Math.abs(fireball.x - prevX) > Math.abs(fireball.dx) || Math.abs(fireball.y - prevY) > Math.abs(fireball.dy));  // Fireball should have moved faster
    }

    @Test
    public void testHasHitTarget() {
        assertFalse(fireball.hasHitTarget());  // Fireball should not have hit target initially

        fireball.x = target.getX() * App.CELLSIZE;  // Manually set fireball's position to target's
        fireball.y = target.getY() * App.CELLSIZE;

        assertTrue(fireball.hasHitTarget());  // Now, fireball should have hit target
    }

    @Test
    public void testGetTarget() {
        assertEquals(target, fireball.getTarget());  // Should return the correct target
    }

    // Rendering tests might require more advanced techniques or might be better off tested manually or using visual testing tools.
}


