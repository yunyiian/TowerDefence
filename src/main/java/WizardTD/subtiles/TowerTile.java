package WizardTD.subtiles;

import WizardTD.*;
import processing.core.PApplet;
import processing.core.PImage;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class TowerTile extends Tile {
    private PImage image;
    private int rangeUpgradeLevel = 0;
    private int speedUpgradeLevel = 0;
    private int damageUpgradeLevel = 0;
    private PApplet app;
    private int towerAppearanceState = 0; // 0 for initial, 1 for orange, 2 for red
    private float towerRange;
    private float towerFiringSpeed;
    private float towerDamage;
    private float timeSinceLastShot = 0;  // To control the firing rate

    private int x;  // x coordinate
    private int y;  // y coordinate
    private List<Fireball> fireballs = new ArrayList<>();

    public TowerTile(PApplet app, int x, int y, float towerRange, float towerFiringSpeed, float towerDamage) {
        this.app = app;
        this.x = x;
        this.y = y;
        image = app.loadImage("src/main/resources/WizardTD/tower0.png");
    
        this.towerRange = towerRange;
        this.towerFiringSpeed = towerFiringSpeed;
        this.towerDamage = towerDamage;
    }
    


    public Monster getClosestMonsterInRange(List<Monster> monsters) {
        Monster closestMonster = null;
        float minDistance = Float.MAX_VALUE;
    
        for (Monster monster : monsters) {
            float distance = PApplet.dist(x * App.CELLSIZE, y * App.CELLSIZE, monster.getX() * App.CELLSIZE, monster.getY() * App.CELLSIZE);
            if (distance < towerRange && distance < minDistance) {
                closestMonster = monster;
                minDistance = distance;
            }
        }
        return closestMonster;
    }

    public void shootMonster(Monster monster) {
        if (timeSinceLastShot >= 1.0 / towerFiringSpeed) {
            fireballs.add(new Fireball(x * App.CELLSIZE, y * App.CELLSIZE, monster, app));
            timeSinceLastShot = 0;
        }
    }

    public void updateAndRenderFireballs() {
        Iterator<Fireball> iterator = fireballs.iterator();
        while (iterator.hasNext()) {
            Fireball fireball = iterator.next();
            fireball.update();
    
            if (fireball.hasHitTarget()) {
                fireball.getTarget().reduceHealth(towerDamage);
                iterator.remove();
            } else {
                fireball.render();
            }
        }
    }

    public void incrementTimeSinceLastShot(double increment) {
        this.timeSinceLastShot += increment;
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
        if (rangeUpgradeLevel >= 2 && speedUpgradeLevel >= 2 && damageUpgradeLevel >= 2) {
            image = app.loadImage("src/main/resources/WizardTD/tower2.png");
            towerAppearanceState = 2;
        } else if (rangeUpgradeLevel >= 1 && speedUpgradeLevel >= 1 && damageUpgradeLevel >= 1) {
            image = app.loadImage("src/main/resources/WizardTD/tower1.png");
            towerAppearanceState = 1;
        } else {
            image = app.loadImage("src/main/resources/WizardTD/tower0.png");
            towerAppearanceState = 0;
        }
    }
    

    @Override
    public void render(int x, int y, PApplet app) {
        app.image(image, x, y);
        
        float textSize = App.CELLSIZE / 4;  // Reduce the size of the text by half
        app.textSize(textSize);
        
        // Render range upgrade level
        if (rangeUpgradeLevel > towerAppearanceState) {
            app.fill(app.color(255, 0, 255));  // Magenta color
            for (int i = 0; i < rangeUpgradeLevel - towerAppearanceState; i++) {
                app.text("O", x + (i * textSize), y + textSize);
            }
        }
    
        // Render speed upgrade level
        if (speedUpgradeLevel > 0) {
            app.stroke(173, 216, 230);  // Lighter blue color
            for (int i = 0; i < speedUpgradeLevel - towerAppearanceState; i++) {
                app.strokeWeight(1);  // Keeping a constant stroke weight
                float radiusReduction = i * 4; // Adjust this value to change the gap between rings
                app.noFill();
                app.ellipse((float)x + App.CELLSIZE / 2.0f, (float)y + App.CELLSIZE / 2.0f, App.CELLSIZE / 2.0f - radiusReduction, App.CELLSIZE / 2.0f - radiusReduction);
            }
            app.noStroke();
        }

        
        // Render damage upgrade level
        if (damageUpgradeLevel > towerAppearanceState) {
            app.fill(app.color(255, 0, 255));  // Magenta color
            for (int i = 0; i < damageUpgradeLevel - towerAppearanceState; i++) {
                app.text("X", x + (i * textSize), y + App.CELLSIZE - textSize / 2);
            }
        }
        
        app.textSize(App.CELLSIZE);  // Reset text size
    }
    
    
}
