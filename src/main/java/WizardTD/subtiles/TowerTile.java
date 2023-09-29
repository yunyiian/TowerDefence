package WizardTD.subtiles;

import WizardTD.Tile;
import WizardTD.App;
import processing.core.PApplet;
import processing.core.PImage;

public class TowerTile extends Tile {
    private PImage image;
    private int rangeUpgradeLevel = 0;
    private int speedUpgradeLevel = 0;
    private int damageUpgradeLevel = 0;
    private PApplet app;

    public TowerTile(PApplet app) {
        this.app = app;
        image = app.loadImage("src/main/resources/WizardTD/tower0.png");
    }

    public void upgradeRange() {
        rangeUpgradeLevel++;
        System.out.println("Upgraded Range to Level: " + rangeUpgradeLevel);  // Debugging line
        updateTowerImage();
    }
    
    public void upgradeSpeed() {
        speedUpgradeLevel++;
        System.out.println("Upgraded Speed to Level: " + speedUpgradeLevel);  // Debugging line
        updateTowerImage();
    }
    
    public void upgradeDamage() {
        damageUpgradeLevel++;
        System.out.println("Upgraded Damage to Level: " + damageUpgradeLevel);  // Debugging line
        updateTowerImage();
    }
    

    private void updateTowerImage() {
        if (rangeUpgradeLevel + speedUpgradeLevel + damageUpgradeLevel >= 6) {
            image = app.loadImage("src/main/resources/WizardTD/tower2.png");
        } else if (rangeUpgradeLevel + speedUpgradeLevel + damageUpgradeLevel >= 3) {
            image = app.loadImage("src/main/resources/WizardTD/tower1.png");
        } else {
            image = app.loadImage("src/main/resources/WizardTD/tower0.png");
        }
    }    

    @Override
    public void render(int x, int y, PApplet app) {
        app.image(image, x, y);
        
        // Render range upgrade level
        if (rangeUpgradeLevel > 1) {
            app.fill(app.color(255, 0, 255));  // Magenta color
            app.text("O", x + App.CELLSIZE / 2, y + 10);
        }
        
        // Render speed upgrade level
        if (speedUpgradeLevel > 1) {
            app.stroke(0, 0, 255);  // Blue color
            app.strokeWeight(speedUpgradeLevel);
            app.noFill();
            app.ellipse((float)x + App.CELLSIZE / 2.0f, (float)y + App.CELLSIZE / 2.0f, App.CELLSIZE / 2.0f, App.CELLSIZE / 2.0f);
            app.noStroke();
        }
        
        // Render damage upgrade level
        if (damageUpgradeLevel > 1) {
            app.fill(app.color(255, 0, 255));  // Magenta color
            app.text("X", x + App.CELLSIZE / 2, y + App.CELLSIZE - 10);
        }
    }
}
