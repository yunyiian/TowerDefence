package WizardTD;

import WizardTD.subtiles.*;
import processing.core.PApplet;
import java.util.List;
import java.util.ArrayList;
import processing.core.PImage;

public class Board {
    private static Tile[][] tiles = new Tile[20][20]; // 20x20 board
    private List<Monster> monsters = new ArrayList<>(); 

    public void loadLayout(String filename, App app) {  // Change PApplet to App
        String[] lines = app.loadStrings(filename);
        
        // First, initialize all the tiles
        for (int i = 0; i < lines.length; i++) {
            for (int j = 0; j < lines[i].length(); j++) {
                char c = lines[i].charAt(j);
                switch (c) {
                    case 'S':
                        tiles[i][j] = new ShrubTile(app);
                        break;
                    case ' ':
                        tiles[i][j] = new GrassTile(app);
                        break;
                    case 'X':
                        tiles[i][j] = new PathTile(j, i, app, this);
                        break;
                    case 'W':
                        tiles[i][j] = new GrassTile(app);
                        tiles[i][j] = new WizardHouseTile(app, j, i, this); 
                        break;
                    // Add more cases if needed.
                }
            }
        }

        // Now, after all tiles are initialized, decide the image for PathTiles
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j] instanceof PathTile) {
                    ((PathTile) tiles[i][j]).decidePathImage(app);
                }
            }
        }
    }

    public boolean isPathTile(int x, int y) {
        if (x < 0 || x >= tiles[0].length || y < 0 || y >= tiles.length) {
            return true;  // Treat the boundary as X
        }
        Tile tile = tiles[y][x];
        return tile instanceof PathTile || tile instanceof WizardHouseTile;
    }

    public void render(PApplet app) {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (!(tiles[i][j] instanceof WizardHouseTile)) {  // Render all tiles except WizardHouseTile first
                    tiles[i][j].render(j * App.CELLSIZE, i * App.CELLSIZE + App.TOPBAR, app);
                }
            }
        }
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j] instanceof WizardHouseTile) {  // Now render only WizardHouseTile
                    tiles[i][j].render(j * App.CELLSIZE, i * App.CELLSIZE + App.TOPBAR, app);
                }
            }
        }
        // Render monsters
        for (Monster monster : monsters) {
            monster.render(app);
        }
    }
    
    public Tile getNextPathTile(int x, int y) {
        // Logic to get the next X tile for a monster
        // For simplicity, let's say the next tile is to the right
        if (isPathTile(x + 1, y)) {
            return tiles[y][x + 1];
        }
        // You can add more logic to handle other directions
        return null;
    }

    public void addMonster(Monster monster) {
        this.monsters.add(monster);
    }

    public void updateMonsters() {
        for (Monster monster : monsters) {
            monster.move(this);
        }
    }
    
    
}
