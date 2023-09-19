package WizardTD;

public class Monster {
    private String type;
    private int hp;
    private double speed;
    private double armour;
    private int manaGainedOnKill;
    private int x, y;  // Position of the monster on the board
    private PImage monsterImage;
    private PImage[] deathAnimation = new PImage[4];

    // Constructor to initialize the monster attributes
    public Monster(String type, int hp, double speed, double armour, int manaGainedOnKill) {
        this.type = type;
        this.hp = hp;
        this.speed = speed;
        this.armour = armour;
        this.manaGainedOnKill = manaGainedOnKill;
        this.x = 0;  // Starting position, you might want to adjust this
        this.y = 0;
        this.app = app;
        
        this.monsterImage = app.loadImage("WizardTD/gremlin.png");
        for (int i = 0; i < 4; i++) {
            this.deathAnimation[i] = app.loadImage("WizardTD/gremlin" + (i + 1) + ".png");
        }
    }

    public void move(Board board) {
        // Logic to move the monster along the X tiles
        // For simplicity, let's say the monster moves to the right
        if (board.isPathTile(this.x + 1, this.y)) {
            this.x++;
        }
        // You can add more logic to handle movement in other directions
    }


    // Getter and Setter methods for the attributes
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getArmour() {
        return armour;
    }

    public void setArmour(double armour) {
        this.armour = armour;
    }

    public int getManaGainedOnKill() {
        return manaGainedOnKill;
    }

    public void setManaGainedOnKill(int manaGainedOnKill) {
        this.manaGainedOnKill = manaGainedOnKill;
    }

    // Method to reduce the monster's health when it's attacked
    public void takeDamage(int damage) {
        this.hp -= damage * (1 - this.armour);
        if (this.hp < 0) {
            this.hp = 0;
        }
    }

    // Method to check if the monster is dead
    public boolean isDead() {
        return this.hp <= 0;
    }
    public void render(PApplet app) {
        if (isDead()) {
            // Render death animation
            app.image(deathAnimation[0], this.x * App.CELLSIZE, this.y * App.CELLSIZE + App.TOPBAR);
        } else {
            app.image(monsterImage, this.x * App.CELLSIZE, this.y * App.CELLSIZE + App.TOPBAR);
        }
    }

    public boolean hasReachedEnd() {
        // Return true if the monster has reached the end of its path, otherwise false.
        // For now, I'll return false as a placeholder.
        return false;
    }
    

}
