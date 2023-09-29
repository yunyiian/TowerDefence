package WizardTD;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.event.MouseEvent;
import WizardTD.subtiles.TowerTile;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import java.io.*;
import java.util.*;

public class App extends PApplet {

    public static final int CELLSIZE = 32;
    public static final int SIDEBAR = 120;
    public static final int TOPBAR = 40;
    public static final int BOARD_WIDTH = 20;

    public static int WIDTH = CELLSIZE*BOARD_WIDTH+SIDEBAR;
    public static int HEIGHT = BOARD_WIDTH*CELLSIZE+TOPBAR;

    public static final int FPS = 60;

    public String configPath;

    public Random random = new Random();
	
	// Feel free to add any additional methods or attributes you want. Please put classes in different files.
     // The board instance.
    private Board board;
    private TopBar topBar;
    private Sidebar sidebar;
    public int initialMana;
    public int initialManaCap;
    public int manaGainedPerSecond;
    public int mana;
    private float manaUpdateTimer = 0.0f;


    private float totalGameTime = 0.0f;  // Total game elapsed time in seconds


    //Monsters
    Monster monster;
    private List<Monster> activeMonsters = new ArrayList<>();
    private List<Wave> waves = new ArrayList<>();  // List to manage waves
    private int currentWaveIndex = 0;  // Track the current wave
    private float waveTimer = 0;

    //towers
    private boolean towerPlacementMode = false;
    public float initialTowerRange;
    public float initialTowerFiringSpeed;
    public float initialTowerDamage;


    public App() {
        this.configPath = "config.json";
    }

    /**
     * Initialise the setting of the window size.
     */
	@Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player, enemies and map elements.
     */
	@Override
    public void setup() {
        frameRate(FPS);
        JSONObject config = loadJSONObject(configPath);
        initialMana = config.getInt("initial_mana");
        initialManaCap = config.getInt("initial_mana_cap");
        manaGainedPerSecond = config.getInt("initial_mana_gained_per_second");
        mana = initialMana; 


        topBar = new TopBar(WIDTH, TOPBAR, mana, initialManaCap);
        sidebar = new Sidebar(SIDEBAR, HEIGHT);

        initialTowerRange = config.getInt("initial_tower_range");
        initialTowerFiringSpeed = config.getFloat("initial_tower_firing_speed");
        initialTowerDamage = config.getInt("initial_tower_damage");
        
        // Load images during setup
		// Eg:
        // loadImage("src/main/resources/WizardTD/tower0.png");
        // loadImage("src/main/resources/WizardTD/tower1.png");
        // loadImage("src/main/resources/WizardTD/tower2.png");

        board = new Board(); // initialize the board
        board.loadLayout(config.getString("layout"), this);  

        JSONArray wavesConfig = config.getJSONArray("waves");
        for (int i = 0; i < wavesConfig.size(); i++) {
            JSONObject waveConfig = wavesConfig.getJSONObject(i);
            int duration = waveConfig.getInt("duration");
            float preWavePause = waveConfig.getFloat("pre_wave_pause");
            JSONArray monstersConfig = waveConfig.getJSONArray("monsters");
            
            for (int j = 0; j < monstersConfig.size(); j++) {
                JSONObject monsterConfig = monstersConfig.getJSONObject(j);
                Wave wave = new Wave(duration, preWavePause, monsterConfig, board, this);
                waves.add(wave);
            }
        }        
    }

    /**
     * Receive key pressed signal from the keyboard.
     */
    @Override
    public void keyPressed() {
        if (key == 'T' || key == 't') {
            sidebar.toggleTowerPlacementMode();
            towerPlacementMode = sidebar.isInTowerPlacementMode();
        } else if (key == '1') {
            sidebar.toggleRangeUpgradeMode();
        } else if (key == '2') {
            sidebar.toggleSpeedUpgradeMode();
        } else if (key == '3') {
            sidebar.toggleDamageUpgradeMode();
        }
    }    
    

    /**
     * Receive key released signal from the keyboard.
     */
	@Override
    public void keyReleased(){
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        // Check sidebar buttons
        if (sidebar.isButtonClicked(mouseX, mouseY, 0)) {
            sidebar.toggleTowerPlacementMode();
        } else if (sidebar.isButtonClicked(mouseX, mouseY, sidebar.buttonHeight + 10)) {
            sidebar.toggleRangeUpgradeMode();
        } else if (sidebar.isButtonClicked(mouseX, mouseY, 2*(sidebar.buttonHeight + 10))) {
            sidebar.toggleSpeedUpgradeMode();
        } else if (sidebar.isButtonClicked(mouseX, mouseY, 3*(sidebar.buttonHeight + 10))) {
            sidebar.toggleDamageUpgradeMode();
        }

        int tileX = mouseX / App.CELLSIZE;
        int tileY = (mouseY - App.TOPBAR) / App.CELLSIZE;
         // Check boundaries before doing anything
        if (tileX < 0 || tileX >= board.getTiles()[0].length || tileY < 0 || tileY >= board.getTiles().length) {
            return;  // Outside of board, so just return
        }

        // First, check if the clicked tile is a tower
        Tile clickedTile = board.getTiles()[tileY][tileX];
        if (clickedTile instanceof TowerTile) {
            TowerTile tower = (TowerTile) clickedTile;
            if (sidebar.isInRangeUpgradeMode()) {
                tower.upgradeRange();
            }
            if (sidebar.isInSpeedUpgradeMode()) {
                tower.upgradeSpeed();
            }
            if (sidebar.isInDamageUpgradeMode()) {
                tower.upgradeDamage();
            }
            return;  // Return early since we have already handled the tower upgrade
        }

        // Next, handle tower placement with potential upgrades
        if (sidebar.isInTowerPlacementMode()) {
            TowerTile newTower = board.placeTower(tileX, tileY, this, initialTowerRange, initialTowerFiringSpeed, initialTowerDamage);
            if (newTower != null) {  // If a tower was placed
                if (sidebar.isInRangeUpgradeMode()) {
                    newTower.upgradeRange();
                }
                if (sidebar.isInSpeedUpgradeMode()) {
                    newTower.upgradeSpeed();
                }
                if (sidebar.isInDamageUpgradeMode()) {
                    newTower.upgradeDamage();
                }
            }
        }
    }

    


    @Override
    public void mouseReleased(MouseEvent e) {

    }

    public void mouseDragged(MouseEvent e) {
    }
    
    /**
     * Draw all elements in the game by current frame.
     */
    @Override
    public void draw() {
        background(255); // Clear the background.


        // Render the board
        board.render(this);

        // Handle waves
        if (currentWaveIndex < waves.size()) {
            Wave currentWave = waves.get(currentWaveIndex);
            
            if (waveTimer < currentWave.getPreWavePause()) {
                // We're in the pause phase of the wave, don't update monsters
            } else {
                currentWave.update();
            }
            
            waveTimer += 1.0 / FPS;  // Increment timer by frame duration

            // Check if the entire wave duration (pause + active) is over
            if (waveTimer > (currentWave.getPreWavePause() + currentWave.getDuration())) {
                waveTimer = 0;  // Reset timer
                currentWaveIndex++;  // Move to the next wave
            }
        }

        // Update and render all active monsters
        for (Monster monster : activeMonsters) {
            monster.moveWithSpeed();
            monster.update();
            monster.render(this);
        }        

        // Make towers shoot at monsters
        Tile[][] tiles = board.getTiles();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (tiles[i][j] instanceof TowerTile) {
                    TowerTile tower = (TowerTile) tiles[i][j];
                    tower.incrementTimeSinceLastShot(1.0 / FPS);  // Add this line
                    Monster target = tower.getClosestMonsterInRange(activeMonsters);
                    if (target != null) {
                        tower.shootMonster(target);
                    }
                    tower.updateAndRenderFireballs();  // Add this line to update and render the fireballs
                }
            }
        }

        Iterator<Monster> monsterIterator = activeMonsters.iterator();
        while (monsterIterator.hasNext()) {
            Monster monster = monsterIterator.next();
            if (monster.getX() == -1.0f && monster.getY() == -1.0f) {
                monsterIterator.remove();
            }
        }


        totalGameTime += 1.0 / FPS;
        
        // Render the sidebar
        sidebar.render(this);

        // Render the top bar
        topBar.render(this);
        manaUpdateTimer += 1.0 / FPS;
        if (manaUpdateTimer >= 1.0f) {
            mana += manaGainedPerSecond;
            manaUpdateTimer -= 1.0f; // reset the timer for the next second
        }
        
        topBar.setMana(Math.round(mana));     


        board.renderWizardHouse(this);

        updateWaveTimer(); 
    }

    public void addActiveMonster(Monster monster) {
        activeMonsters.add(monster);
    }

    private void updateWaveTimer() {
        float elapsedTime = totalGameTime;
        float timeForNextWave = 0.0f;
        
        for (int i = 0; i < waves.size(); i++) {
            float waveTotalTime = waves.get(i).getPreWavePause() + (i == waves.size() - 1 ? 0 : waves.get(i).getDuration());
            
            if (i == 0) {
                // For the first wave
                timeForNextWave = waves.get(i).getPreWavePause();
            } else {
                // For subsequent waves
                timeForNextWave = waves.get(i).getPreWavePause() + waves.get(i - 1).getDuration();
            }
            
            if (elapsedTime < timeForNextWave) {
                topBar.setWaveTimer(i + 1, timeForNextWave - elapsedTime);
                return;
            } else {
                elapsedTime -= timeForNextWave;
            }
        }
    
        // All waves completed
        topBar.setWaveTimer(-1, 0);
    }


    public static void main(String[] args) {
        PApplet.main("WizardTD.App");
    }

    /**
     * Source: https://stackoverflow.com/questions/37758061/rotate-a-buffered-image-in-java
     * @param pimg The image to be rotated
     * @param angle between 0 and 360 degrees
     * @return the new rotated image
     */
    public PImage rotateImageByDegrees(PImage pimg, double angle) {
        BufferedImage img = (BufferedImage) pimg.getNative();
        double rads = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int w = img.getWidth();
        int h = img.getHeight();
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);

        PImage result = this.createImage(newWidth, newHeight, ARGB);
        //BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        BufferedImage rotated = (BufferedImage) result.getNative();
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - w) / 2, (newHeight - h) / 2);

        int x = w / 2;
        int y = h / 2;

        at.rotate(rads, x, y);
        g2d.setTransform(at);
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();
        for (int i = 0; i < newWidth; i++) {
            for (int j = 0; j < newHeight; j++) {
                result.set(i, j, rotated.getRGB(i, j));
            }
        }

        return result;
    }
}
