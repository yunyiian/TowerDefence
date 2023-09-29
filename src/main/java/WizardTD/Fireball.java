package WizardTD;

import processing.core.PApplet;
import processing.core.PImage;

public class Fireball {
    float x, y;  // Current position
    float dx, dy;  // x and y increments
    float speed = 5;  // Fireball speed
    Monster target;  // Target monster
    PImage fireballImage;
    PApplet app;


    public Fireball(float startX, float startY, Monster target, PApplet app) {
        this.x = startX;
        this.y = startY;
        this.target = target;
        this.app = app;

        // Load fireball image
        fireballImage = app.loadImage("src/main/resources/WizardTD/fireball.png");

        // Calculate angle to target
        float angle = PApplet.atan2(target.getY() * App.CELLSIZE - y, target.getX() * App.CELLSIZE - x);

        // Calculate x and y increments
        dx = speed * PApplet.cos(angle);
        dy = speed * PApplet.sin(angle);
    }

    public void update() {
        x += dx;
        y += dy;
    }

    public boolean hasHitTarget() {
        return PApplet.dist(x, y, target.getX() * App.CELLSIZE, target.getY() * App.CELLSIZE) < 5;  // Check if within hit radius
    }

    public Monster getTarget() {
        return this.target;
    }

    public void render() {
        app.image(fireballImage, x - fireballImage.width / 2, y - fireballImage.height / 2);
    }

}
