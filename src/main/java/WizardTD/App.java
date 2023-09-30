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

    public float manaPoolSpellInitialCost;
    public float manaPoolSpellCostIncreasePerUse;
    public float manaPoolSpellCapMultiplier;
    public float manaPoolSpellManaGainedMultiplier;
    public float currentManaPoolSpellCost;  // To keep track of the current cost after multiple uses
    private float manaMultiplier = 1.0f;  // New attribute to store the mana multiplier



    private float totalGameTime = 0.0f;  // Total game elapsed time in seconds
    private long lastFPressTime = 0;



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

        manaPoolSpellInitialCost = config.getFloat("mana_pool_spell_initial_cost");
        manaPoolSpellCostIncreasePerUse = config.getFloat("mana_pool_spell_cost_increase_per_use");
        manaPoolSpellCapMultiplier = config.getFloat("mana_pool_spell_cap_multiplier");
        manaPoolSpellManaGainedMultiplier = config.getFloat("mana_pool_spell_mana_gained_multiplier");
        currentManaPoolSpellCost = manaPoolSpellInitialCost;


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
        if (key == 'F' || key == 'f') {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastFPressTime < 300) {  // Double press within 300ms
                sidebar.toggleSpeedMode();
            }
            lastFPressTime = currentTime;
        }
        if (key == 'T' || key == 't') {
            sidebar.toggleTowerPlacementMode();
            towerPlacementMode = sidebar.isInTowerPlacementMode();
        } else if (key == '1') {
            sidebar.toggleRangeUpgradeMode();
        } else if (key == '2') {
            sidebar.toggleSpeedUpgradeMode();
        } else if (key == '3') {
            sidebar.toggleDamageUpgradeMode();
        } else if (key == 'M' || key == 'm') {  
            activateManaPoolSpell();
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
    
        // Check the "2x Speed" button first
        if (sidebar.isSpeedToggleClicked(mouseX, mouseY)) {
            sidebar.toggleSpeedMode();
            return;  // Return early to avoid handling other buttons
        }
    
        if (sidebar.isManaPoolSpellButtonClicked(mouseX, mouseY)) {
            activateManaPoolSpell();
        } else if (sidebar.isButtonClicked(mouseX, mouseY, sidebar.buttonHeight + 10)) {
            // This is the "Place Tower" button
            sidebar.toggleTowerPlacementMode();
        } else if (sidebar.isButtonClicked(mouseX, mouseY, 2*(sidebar.buttonHeight + 10))) {
            // This is the "Upgrade Range" button
            sidebar.toggleRangeUpgradeMode();
        } else if (sidebar.isButtonClicked(mouseX, mouseY, 3*(sidebar.buttonHeight + 10))) {
            // This is the "Upgrade Speed" button
            sidebar.toggleSpeedUpgradeMode();
        } else if (sidebar.isButtonClicked(mouseX, mouseY, 4*(sidebar.buttonHeight + 10))) {
            // This is the "Upgrade Damage" button
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
                int upgradeCost = calculateUpgradeCost(tower.getRangeUpgradeLevel());
                if(canAfford(upgradeCost) && tower.upgradeRange()) {
                    deductMana(upgradeCost);
                }
            }
            if (sidebar.isInSpeedUpgradeMode()) {
                int upgradeCost = calculateUpgradeCost(tower.getSpeedUpgradeLevel());
                if(canAfford(upgradeCost) && tower.upgradeSpeed()) {
                    deductMana(upgradeCost);
                }
            }
            if (sidebar.isInDamageUpgradeMode()) {
                int upgradeCost = calculateUpgradeCost(tower.getDamageUpgradeLevel());
                if(canAfford(upgradeCost) && tower.upgradeDamage()) {
                    deductMana(upgradeCost);
                }
            }
    
            return;  // Return early since we have already handled the tower upgrade
        }
    


        // Next, handle tower placement with potential upgrades
        if (sidebar.isInTowerPlacementMode()) {
            int numUpgradesSelected = 0;
            if (sidebar.isInRangeUpgradeMode()) numUpgradesSelected++;
            if (sidebar.isInSpeedUpgradeMode()) numUpgradesSelected++;
            if (sidebar.isInDamageUpgradeMode()) numUpgradesSelected++;

            int towerPlacementCost = calculateTowerPlacementCost(numUpgradesSelected);
            if (mana >= towerPlacementCost) {
                TowerTile newTower = board.placeTower(tileX, tileY, this, initialTowerRange, initialTowerFiringSpeed, initialTowerDamage);
                if (newTower != null) {  // If a tower was placed
                    mana -= towerPlacementCost;  // Deduct mana
                    topBar.setMana(mana);
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
        int updates = sidebar.isDoubleSpeedMode() ? 2 : 1;
    
        for (int u = 0; u < updates; u++) {
            // Handle waves
            if (currentWaveIndex < waves.size()) {
                Wave currentWave = waves.get(currentWaveIndex);
                if (waveTimer < currentWave.getPreWavePause()) {
                    // We're in the pause phase of the wave, don't update monsters
                } else {
                    currentWave.update();
                }
                waveTimer += 1.0 / FPS;  // Increment timer by frame duration
                if (waveTimer > (currentWave.getPreWavePause() + currentWave.getDuration())) {
                    waveTimer = 0;  // Reset timer
                    currentWaveIndex++;  // Move to the next wave
                }
            }
    
            // Update all active monsters
            for (Monster monster : activeMonsters) {
                monster.moveWithSpeed();
                monster.update();
            }        
    
            // Make towers shoot at monsters
            Tile[][] tiles = board.getTiles();
            for (int i = 0; i < tiles.length; i++) {
                for (int j = 0; j < tiles[0].length; j++) {
                    if (tiles[i][j] instanceof TowerTile) {
                        TowerTile tower = (TowerTile) tiles[i][j];
                        tower.incrementTimeSinceLastShot(1.0 / FPS);
                        Monster target = tower.getClosestMonsterInRange(activeMonsters);
                        if (target != null) {
                            tower.shootMonster(target);
                        }
                        tower.updateAndRenderFireballs();  // Update the fireballs, but consider rendering outside the loop
                    }
                }
            }
    
            // Remove dead monsters
            Iterator<Monster> monsterIterator = activeMonsters.iterator();
            while (monsterIterator.hasNext()) {
                Monster monster = monsterIterator.next();
                if (monster.getX() == -1.0f && monster.getY() == -1.0f) {
                    monsterIterator.remove();
                }
            }
    
            totalGameTime += 1.0 / FPS;
            manaUpdateTimer += 1.0 / FPS;
            if (manaUpdateTimer >= 1.0f) {
                mana += manaGainedPerSecond * manaMultiplier;  // Updated to consider multiplier
                manaUpdateTimer -= 1.0f; // reset the timer for the next second
            }
            topBar.setMana(Math.round(mana));     
            updateWaveTimer(); 
        }
    
        // Renderings (should be done once per frame)
        background(255);
        board.render(this);
        for (Monster monster : activeMonsters) {
            monster.render(this);
        }  
        sidebar.render(this);
        topBar.render(this);
        board.renderWizardHouse(this);
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

    public void addMana(float amount) {
        this.mana += amount;
    }

    public void deductMana(int cost) {
        mana -= cost;
    }
    

    private void activateManaPoolSpell() {
        if (mana >= currentManaPoolSpellCost) {
            mana -= currentManaPoolSpellCost;
            initialManaCap *= manaPoolSpellCapMultiplier;
            topBar.setManaCap(initialManaCap);
            manaMultiplier += manaPoolSpellManaGainedMultiplier;  // Increase the mana multiplier
            currentManaPoolSpellCost += manaPoolSpellCostIncreasePerUse;
        }
    }

    public float getCurrentManaMultiplier() {
        return manaMultiplier;
    }

    // Calculate the cost to place a tower with the given number of upgrades
    public int calculateTowerPlacementCost(int numUpgrades) {
        return 100 + numUpgrades * 20;  // 100 is the base cost of placing a tower, each upgrade costs 20 more
    }

    // Calculate the upgrade cost for a tower based on the current upgrade level
    public int calculateUpgradeCost(int currentUpgradeLevel) {
        return 20 + currentUpgradeLevel * 10;  // 20 is the base cost for the first upgrade, subsequent upgrades cost 10 more
    }

    public boolean canAfford(int cost) {
        return mana >= cost;
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
