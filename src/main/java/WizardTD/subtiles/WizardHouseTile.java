package WizardTD.subtiles;

import processing.core.PApplet;

public class WizardHouseTile extends Tile {

    public WizardHouseTile(PApplet app) {
        image = app.loadImage("/Users/ianchang/Desktop/scaffold/src/main/resources/WizardTD/wizard_house.png");
    }

    @Override
    public void render(int x, int y, PApplet app) {
        app.image(image, x, y);
    }
}

