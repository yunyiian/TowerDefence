package WizardTD;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.List;
import java.util.ArrayList;
import WizardTD.subtiles.*;
import WizardTD.PathFinder.Node;


public class Monster {

    private PImage image;
    
    private float x, y, targetX, targetY; 
    private Board board;
    private String direction = "";  // Can be "up", "down", "left", "right"
    List<String> pathToWizard;
    int pathIndex = 0;
    private PathFinder pathFinder = new PathFinder();
    private float speed;  // speed of the monster in pixels per frame
    private float leftoverMove = 0.0f;  

    public Monster(Board board, PApplet app, float speed) {
        this.board = board;
        this.image = app.loadImage("src/main/resources/WizardTD/gremlin.png");
        List<int[]> spawnPoints = getSpawnPoints();
        int[] chosenSpawn = spawnPoints.get((int) (Math.random() * spawnPoints.size()));
        this.x = (float) chosenSpawn[0];
        this.y = (float) chosenSpawn[1];
        this.speed = speed;
    }

    // Overloaded constructor for backward compatibility
    public Monster(Board board, PApplet app) {
        this(board, app, 1.0f); // Default speed set to 1.0f
    }

    public void moveWithSpeed() {
        float totalMove = speed + leftoverMove;
        int intMove = (int) totalMove;  // integer part of the movement
        leftoverMove = totalMove - intMove;  // store the fractional part for next frame
        
        for (int i = 0; i < intMove; i++) {
            move();  // call the existing move method
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
    
        // Move towards the target in pixel increments, as before
        float deltaX = targetX - x;
        float deltaY = targetY - y;
        float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        if (distance > 0) {
            float moveX = (speed / 32.0f) * (deltaX / distance);
            float moveY = (speed / 32.0f) * (deltaY / distance);
            x += moveX;
            y += moveY;
        }
    }
    
    

    public void render(PApplet app) {
        if (x != -1.0f && y != -1.0f) { // Only render if monster hasn't disappeared
            int drawX = (int) (x * App.CELLSIZE + App.CELLSIZE / 2 - image.width / 2);
            int drawY = (int) (y * App.CELLSIZE + App.CELLSIZE / 2 - image.height / 2 + App.TOPBAR);
            app.image(image, drawX, drawY);
        }
    }
}