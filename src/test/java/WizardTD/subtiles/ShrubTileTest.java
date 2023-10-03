package WizardTD.subtiles;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import processing.core.PImage;

public class ShrubTileTest {

    private ShrubTile shrubTile;
    private TestApplet app;

    @BeforeEach
    public void setUp() {
        app = new TestApplet();
        shrubTile = new ShrubTile(app);
    }

    @Test
    public void testConstructor() {
        assertNotNull(app.getLastLoadedImagePath(), "Image path should not be null.");
        assertEquals("src/main/resources/WizardTD/shrub.png", app.getLastLoadedImagePath(), "Unexpected image path loaded.");
    }

    @Test
    public void testRender() {
        try {
            shrubTile.render(5, 10, app);
        } catch (Exception e) {
            fail("Render method should not throw exceptions.");
        }
    }

    private class TestApplet extends PApplet {
        private String lastLoadedImagePath;

        @Override
        public PImage loadImage(String path) {
            lastLoadedImagePath = path;
            return new PImage(); // Return an empty PImage instance.
        }

        public String getLastLoadedImagePath() {
            return lastLoadedImagePath;
        }
        
        @Override
        public void image(PImage img, float a, float b) {
            // Overridden to prevent actual rendering.
            // You can add checks here if needed.
        }
    }
}
