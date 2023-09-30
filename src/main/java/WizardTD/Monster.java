package WizardTD;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.List;
import java.util.ArrayList;
import WizardTD.subtiles.*;
import WizardTD.PathFinder.Node;


public class Monster {

    private PImage image;
    private App game;
    private float x, y, targetX, targetY; 
    private Board board;
    private String direction = "";  // Can be "up", "down", "left", "right"
    List<String> pathToWizard;
    int pathIndex = 0;
    private PathFinder pathFinder = new PathFinder();
    private float speed;  // speed of the monster in pixels per frame
    private float leftoverMove = 0.0f;  
    private int spawnDelay; 


    private float hp;  // Max hit points (initial health)
    private float currentHp;  // Current health
    private float armour;  // Percentage multiplier to damage received
    private float manaGainedOnKill;  // Mana gained when this monster is killed
    private String type;  // The sprite image to use for the monster
    private PImage deathImages[];  // Images for death animation
    private int deathFrameCount = 0;  // Frame counter for death animation
    private float manaMultiplier = 1.0f; 


    public Monster(Board board, PApplet app, float speed, int spawnDelay, float hp, float armour, float manaGainedOnKill, String type, App game) {
        this.board = board;
        this.game = game;
        this.image = app.loadImage("src/main/resources/WizardTD/" + type + ".png");
        this.hp = hp;
        this.currentHp = hp;
        this.armour = armour;
        this.manaGainedOnKill = manaGainedOnKill;
        this.type = type;
        this.speed = speed;
        this.spawnDelay = spawnDelay;
        List<int[]> spawnPoints = getSpawnPoints();
        int[] chosenSpawn = spawnPoints.get((int) (Math.random() * spawnPoints.size()));
        this.x = (float) chosenSpawn[0];
        this.y = (float) chosenSpawn[1];
        this.speed = speed;
        this.spawnDelay = spawnDelay; 

        // Load death animation images
        deathImages = new PImage[4];
        for (int i = 0; i < 4; i++) {
        deathImages[i] = app.loadImage("src/main/resources/WizardTD/" + type + (i+2) + ".png");
        }
    }
    private List<int[]> getSpawnPoints() {
        List<int[]> spawnPoints = new ArrayList<>();
        Tile[][] tiles = board.getTiles();
    
        // Check the top row
        for (int j = 0; j < tiles[0].length; j++) {
            if (tiles[0][j] instanceof PathTile) {
                spawnPoints.add(new int[]{j, -1});  // Spawn above the board
            }
        }
    
        // Check the bottom row
        for (int j = 0; j < tiles[tiles.length - 1].length; j++) {
            if (tiles[tiles.length - 1][j] instanceof PathTile) {
                spawnPoints.add(new int[]{j, tiles.length});  // Spawn below the board
            }
        }
    
        // Check the leftmost column
        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i][0] instanceof PathTile) {
                spawnPoints.add(new int[]{-1, i});  // Spawn to the left of the board
            }
        }
    
        // Check the rightmost column
        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i][tiles[i].length - 1] instanceof PathTile) {
                spawnPoints.add(new int[]{tiles[i].length, i});  // Spawn to the right of the board
            }
        }
        return spawnPoints;
    }

    public void moveWithSpeed() {
        if (spawnDelay > 0) {
            // If there's a spawn delay, decrement it and do not move the monster
            spawnDelay--;
            return;
        }
        float totalMove = speed + leftoverMove;
        int intMove = (int) totalMove;  // integer part of the movement
        leftoverMove = totalMove - intMove;  // store the fractional part for next frame
        
        for (int i = 0; i < intMove; i++) {
            move();  // call the existing move method
        }
    }
    
    public void move() {
        Tile[][] tiles = board.getTiles();
        
        if (x < 0 || x >= tiles[0].length || y < 0 || y >= tiles.length) {
            moveOutside(tiles);
        } else {
            moveInside(tiles);
        }
    }
    
    private void moveOutside(Tile[][] tiles) {
        if (y == -1) direction = "down";
        else if (y == tiles.length) direction = "up";
        else if (x == -1) direction = "right";
        else if (x == tiles[0].length) direction = "left";
        
        moveInDirection();
    }
    
    private void moveInDirection() {
        switch (direction) {
            case "up":
                y -= 1.0f / 32;
                break;
            case "down":
                y += 1.0f / 32;
                break;
            case "left":
                x -= 1.0f / 32;
                break;
            case "right":
                x += 1.0f / 32;
                break;
        }
    }

    private void resetMonsterPosition() {
        List<int[]> spawnPoints = getSpawnPoints();
        int[] chosenSpawn = spawnPoints.get((int) (Math.random() * spawnPoints.size()));
        this.x = (float) chosenSpawn[0];
        this.y = (float) chosenSpawn[1];
    }
    
    
     /**
     * moveInside method
     *          
     * This method implements the A* pathfinding algorithm,
     *
     * For details on the A* algorithm, refer to the credits in the PathFinder class.
     */
     private void moveInside(Tile[][] tiles) {
         // If the monster is close to its target or doesn't have a target, determine the next tile
         if (Math.abs(x - targetX) < 0.01 && Math.abs(y - targetY) < 0.01 || targetX == 0 && targetY == 0) {
             Node start = pathFinder.new Node((int)x, (int)y);  // Create a start node based on the monster's current position
             Node goal = null;
             
             // Loop through the tiles to find the WizardHouseTile and set the goal node to its position
             for (int i = 0; i < tiles.length; i++) {
                 for (int j = 0; j < tiles[0].length; j++) {
                     if (tiles[i][j] instanceof WizardHouseTile) {
                         goal = pathFinder.new Node(j, i);  // Update the goal node based on the WizardHouseTile's position
                     }
                 }
             }
    
            // Use the PathFinder's findPath method to get the path to the wizard house
            List<Node> path = pathFinder.findPath(start, goal, tiles);
    
            // If the path is valid and has more than one node, set the next tile in the path as the monster's target
            if (path != null && path.size() > 1) {
                Node nextTile = path.get(1);
                targetX = nextTile.x;
                targetY = nextTile.y;
            } else {
                // If the monster is already at the goal or there's no path, set the target as the current position
                targetX = x;
                targetY = y;
            }
        }
    
        // Move towards the target in pixel increments
        float deltaX = targetX - x;
        float deltaY = targetY - y;
        float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        if (distance > 0) {
            float moveX = (speed / 32.0f) * (deltaX / distance);
            float moveY = (speed / 32.0f) * (deltaY / distance);
            
            // Check if the move would overshoot the target
            float new_distance_after_move = (float) Math.sqrt((deltaX - moveX) * (deltaX - moveX) + (deltaY - moveY) * (deltaY - moveY));
            if (new_distance_after_move > distance) {
                // If it would overshoot, just move to the target
                x = targetX;
                y = targetY;
                leftoverMove += (new_distance_after_move - distance) * 32.0f / speed;  // Store the leftover movement for next frame
            } else {
                x += moveX;
                y += moveY;
            }
        }
     }

     public float getDirectionAngle() {
        switch (direction) {
            case "up": return 270;
            case "down": return 90;
            case "left": return 180;
            case "right": return 0;
            default: return 0;
        }
    }

     public void reduceHealth(float damage) {
        float actualDamage = damage * armour;  // Calculate actual damage after considering armour
        currentHp -= actualDamage;  // Reduce the monster's health by the actual damage
        if (currentHp <= 0 && deathFrameCount == 0) {  // Ensure the death sequence is only initiated once
            onDeath();
        }
    }
    
    public void onDeath() {
        // Start the death animation
        deathFrameCount = 1;
        speed = 0;  // Stop the monster's movement
        game.addMana(manaGainedOnKill * manaMultiplier);  // Updated to consider multiplier
    }

    public void update() {
        if (deathFrameCount > 0 && deathFrameCount < 20) {
            deathFrameCount++;
        }
        if (board.isWizardHouseTile((int) this.x, (int) this.y)) {
            // Monster has reached the WizardHouseTile
            game.deductMana((int) this.currentHp);  // Decrement wizard's mana by monster's current HP
            resetMonsterPosition();  // Reset monster's position to a spawn point
        }        
    }

    public float getCurrentHp() {
        return this.currentHp;
    }

    public void setManaMultiplier(float multiplier) {
        this.manaMultiplier = multiplier;
    }


    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getSpeed() {
        return this.speed;
    }

    public PImage getImage() {
        return this.image;
    }
    

    
    public void render(PApplet app) {
        app.noTint();
        if (x != -1.0f && y != -1.0f) { // Only render if the monster hasn't disappeared
            int drawX = (int) (x * App.CELLSIZE + App.CELLSIZE / 2 - image.width / 2);
            int drawY = (int) (y * App.CELLSIZE + App.CELLSIZE / 2 - image.height / 2 + App.TOPBAR);

            // Display HP bar
            float hpPercentage = PApplet.constrain(currentHp / hp, 0, 1);  // Clamp between 0 and 1
            app.fill(255, 0, 0);  // Red color for missing HP
            app.rect(drawX, drawY - 10, image.width, 5);
            app.fill(0, 255, 0);  // Green color for current HP
            app.rect(drawX, drawY - 10, image.width * hpPercentage, 5);

            if (deathFrameCount == 0) {
                // If not in death animation, display the normal monster image
                app.image(image, drawX, drawY);
            } else if (deathFrameCount < 20) {  
                // If in death animation, display the death images based on the frame count
                int deathImageIndex = (deathFrameCount / 4) % 4; // This will give values: 0, 0, 0, 0, 1, 1, 1, 1, etc.
                app.image(deathImages[deathImageIndex], drawX, drawY);
            } else {
                // After the death animation is over, remove the monster from the game
                x = -1.0f;
                y = -1.0f;
            }
        }      
    }


    
}