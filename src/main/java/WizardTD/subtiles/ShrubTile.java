package WizardTD.subtiles;

import processing.core.PApplet;

public class ShrubTile extends Tile {

    public ShrubTile(PApplet app) {
        image = app.loadImage("/Users/ianchang/Desktop/scaffold/src/main/resources/WizardTD/shrub.png");
    }

    @Override
    public void render(int x, int y, PApplet app) {
        app.image(image, x, y);
    }
}
