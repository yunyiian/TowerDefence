package WizardTD;

import WizardTD.subtiles.*;
import processing.core.PApplet;

public class Board {
    private static Tile[][] tiles = new Tile[20][20]; // 20x20 board

    public void loadLayout(String filename, PApplet app) {
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
                        tiles[i][j] = new WizardHouseTile(app);
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
        if (x < 0 || x >= tiles.length || y < 0 || y >= tiles[0].length) {
            return false;
        }
        return tiles[x][y] instanceof PathTile;
    }

    public void render(PApplet app) {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                tiles[i][j].render(j * App.CELLSIZE, i * App.CELLSIZE, app);
            }
        }
    }
}

