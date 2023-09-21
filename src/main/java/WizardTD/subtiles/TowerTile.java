package WizardTD.subtiles;
import WizardTD.Tile;
import processing.core.PApplet;
import processing.core.PImage;

public class TowerTile extends Tile {
    private PImage image;

    public TowerTile(PApplet app) {
        image = app.loadImage("src/main/resources/WizardTD/tower0.png");
    }

    @Override
    public void render(int x, int y, PApplet app) {
        app.image(image, x, y);
    }
}