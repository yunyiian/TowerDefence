package WizardTD.subtiles;

import processing.core.PApplet;

public class TowerTile extends Tile {

    public TowerTile(PApplet app) {
        image = app.loadImage("/Users/ianchang/Desktop/scaffold/src/main/resources/WizardTD/tower0.png");  // Load the default tower image.
    }

    @Override
    public void render(int x, int y, PApplet app) {
        app.image(image, x, y);
    }
}

