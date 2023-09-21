package WizardTD;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.List;
import java.util.ArrayList;
import WizardTD.subtiles.*;


public class Monster {

    private PImage image;
    private int x, y;  // Position of the monster
    private Board board;
    private String direction = "";  // Can be "up", "down", "left", "right"

    public Monster(Board board, PApplet app) {
        System.out.println("Board object in Monster constructor: " + board); 
        this.board = board;
        this.image = app.loadImage("src/main/resources/WizardTD/gremlin.png");
        List<int[]> spawnPoints = getSpawnPoints();
        int[] chosenSpawn = spawnPoints.get((int) (Math.random() * spawnPoints.size()));
        this.x = chosenSpawn[0];
        this.y = chosenSpawn[1];
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


        // Determine initial direction based on spawn location
        if (y == -1) direction = "down";
        else if (y == tiles.length) direction = "up";
        else if (x == -1) direction = "right";
        else if (x == tiles[0].length) direction = "left";

        // If the monster is outside the board, move it in the initial direction
        if (x < 0 || x >= tiles[0].length || y < 0 || y >= tiles.length) {
            switch (direction) {
                case "up":
                    y--;
                    break;
                case "down":
                    y++;
                    break;
                case "left":
                    x--;
                    break;
                case "right":
                    x++;
                    break;
            }
            return;  // Exit the move method after initial movement
        }
        // Check if monster is on Wizard's house
        if (tiles[y][x] instanceof WizardHouseTile) {
            // Monster disappears when it reaches the Wizard's house
            x = -1;
            y = -1;
            return;
        }

    // Determine possible directions monster can move based on neighboring tiles
    List<String> possibleDirections = new ArrayList<>();
    if (y > 0 && tiles[y - 1][x] instanceof PathTile) possibleDirections.add("up");
    if (y < tiles.length - 1 && tiles[y + 1][x] instanceof PathTile) possibleDirections.add("down");
    if (x > 0 && tiles[y][x - 1] instanceof PathTile) possibleDirections.add("left");
    if (x < tiles[y].length - 1 && tiles[y][x + 1] instanceof PathTile) possibleDirections.add("right");

    // If only one possible direction, move in that direction
    if (possibleDirections.size() == 1) {
        direction = possibleDirections.get(0);
    } else {
        // If multiple possible directions, choose the one that gets the monster closer to the Wizard's house
        direction = getDirectionCloserToWizardHouse(possibleDirections, tiles);
    }

    // Move in the determined direction
    switch (direction) {
        case "up":
            y--;
            break;
        case "down":
            y++;
            break;
        case "left":
            x--;
            break;
        case "right":
            x++;
            break;
    }
}

private String getDirectionCloserToWizardHouse(List<String> possibleDirections, Tile[][] tiles) {
    int destX = -1, destY = -1;
    
    // Find the position of the Wizard's house
    for (int i = 0; i < tiles.length; i++) {
        for (int j = 0; j < tiles[i].length; j++) {
            if (tiles[i][j] instanceof WizardHouseTile) {
                destX = j;
                destY = i;
                break;
            }
        }
    }
    
    String bestDirection = null;
    int bestDistance = Integer.MAX_VALUE;
    
    for (String dir : possibleDirections) {
        int newX = x, newY = y;
        switch (dir) {
            case "up":
                newY--;
                break;
            case "down":
                newY++;
                break;
            case "left":
                newX--;
                break;
            case "right":
                newX++;
                break;
        }

        int distance = Math.abs(newX - destX) + Math.abs(newY - destY);
        if (distance < bestDistance) {
            bestDistance = distance;
            bestDirection = dir;
        }
    }
    
    return bestDirection;
    }


    public void render(PApplet app) {
        if (x != -1 && y != -1) { // Only render if monster hasn't disappeared
            int drawX = x * App.CELLSIZE + App.CELLSIZE / 2 - image.width / 2;
            int drawY = y * App.CELLSIZE + App.CELLSIZE / 2 - image.height / 2 + App.TOPBAR;
            app.image(image, drawX, drawY);
        }
    }
}