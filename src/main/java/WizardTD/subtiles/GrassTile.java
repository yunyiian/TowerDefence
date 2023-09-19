package WizardTD.subtiles;

import processing.core.PApplet;

public class GrassTile extends Tile {

    public GrassTile(PApplet app) {
        image = app.loadImage("src/main/resources/WizardTD/grass.png");
    }

    @Override
    public void render(int x, int y, PApplet app) {
        app.image(image, x, y);
    }
}






