package WizardTD;

import processing.core.PApplet;
import WizardTD.subtiles.TowerTile;

public class Sidebar {
    private int width;
    private int height;

    private boolean towerPlacementMode = false;
    private boolean rangeUpgradeMode = false;
    private boolean speedUpgradeMode = false;
    private boolean damageUpgradeMode = false;
    private boolean speedToggleActive = false;


    public int buttonWidth = 80;
    public int buttonHeight = 30;
    private int buttonX;
    private int buttonY;

    public Sidebar(int width, int height) {
        this.width = width;
        this.height = height;
        this.buttonX = App.WIDTH - this.width + 20;
        this.buttonY = App.TOPBAR + 10;
    }

    public void toggleTowerPlacementMode() {
        towerPlacementMode = !towerPlacementMode;
    }

    public boolean isInTowerPlacementMode() {
        return towerPlacementMode;
    }

    public void toggleRangeUpgradeMode() {
        rangeUpgradeMode = !rangeUpgradeMode;
        System.out.println("Range upgrade mode toggled: " + rangeUpgradeMode);  // Debugging line
    }

    public boolean isInRangeUpgradeMode() {
        return rangeUpgradeMode;
    }

    public void toggleSpeedUpgradeMode() {
        speedUpgradeMode = !speedUpgradeMode;
        System.out.println("Speed upgrade mode toggled: " + speedUpgradeMode);  // Debugging line
    }

    public boolean isInSpeedUpgradeMode() {
        return speedUpgradeMode;
    }
    public void toggleDamageUpgradeMode() {
        damageUpgradeMode = !damageUpgradeMode;
        System.out.println("Damage upgrade mode toggled: " + damageUpgradeMode);  // Debugging line
    }
    

    public boolean isInDamageUpgradeMode() {
        return damageUpgradeMode;
    }

    public boolean isInAnyUpgradeMode() {
        return isInRangeUpgradeMode() || isInSpeedUpgradeMode() || isInDamageUpgradeMode();
    }

    public boolean isManaPoolSpellButtonClicked(int mouseX, int mouseY) {
        return isButtonClicked(mouseX, mouseY, 4 * (buttonHeight + 10));
    }

    public boolean isSpeedToggleClicked(int mouseX, int mouseY) {
        return isButtonClicked(mouseX, mouseY, 0);
    }

    public void toggleSpeedMode() {
        speedToggleActive = !speedToggleActive;
    }

    public boolean isDoubleSpeedMode() {
        return speedToggleActive;
    }

    private void renderUpgradeCosts(PApplet app, TowerTile selectedTower) {
        int startX = app.width - this.width + 10;  // adjust to the left side of the sidebar
        int startY = app.height - 100; // starting position at bottom left
    
        app.fill(app.color(0x83, 0x74, 0x4A));
        app.rect(startX, startY, 150, 80); // background for the costs
    
        app.fill(255); // Text color
        app.textSize(14); 
        app.text("Upgrade Cost", startX + 10, startY + 20);
    
        int totalCost = 0;
        if (isInRangeUpgradeMode()) {
            int cost = selectedTower.getNextRangeUpgradeCost();
            app.text("Range: " + cost, startX + 20, startY + 40);
            totalCost += cost;
        }
        if (isInSpeedUpgradeMode()) {
            int cost = selectedTower.getNextSpeedUpgradeCost();
            app.text("Speed: " + cost, startX + 20, startY + 60);
            totalCost += cost;
        }
        if (isInDamageUpgradeMode()) {
            int cost = selectedTower.getNextDamageUpgradeCost();
            app.text("Damage: " + cost, startX + 20, startY + 80);
            totalCost += cost;
        }
    
        app.text("Total: " + totalCost, startX + 20, startY + 100);
    }
    
    
    

    // Check which button was clicked
    public boolean isButtonClicked(int mouseX, int mouseY, int yOffset) {
        return mouseX >= buttonX && mouseX <= buttonX + buttonWidth &&
               mouseY >= buttonY + yOffset && mouseY <= buttonY + yOffset + buttonHeight;
    }

    // Update render method:
    public void render(PApplet app, TowerTile selectedTower) {
        app.noTint();
        app.fill(app.color(0x83, 0x74, 0x4A));
        app.rect(app.width - this.width, App.TOPBAR, this.width, this.height - App.TOPBAR);

        renderButton(app, "2x Speed", 0, speedToggleActive); 
        renderButton(app, "Place Tower", buttonHeight + 10, towerPlacementMode);
        renderButton(app, "Upgrade Range", 2*(buttonHeight + 10), rangeUpgradeMode);
        renderButton(app, "Upgrade Speed", 3*(buttonHeight + 10), speedUpgradeMode);
        renderButton(app, "Upgrade Damage", 4*(buttonHeight + 10), damageUpgradeMode);
        renderButton(app, "Mana Pool Spell", 5 * (buttonHeight + 10), false);  

        if (selectedTower != null && isInAnyUpgradeMode()) {
            renderUpgradeCosts(app, selectedTower);
        }
    }

    private void renderButton(PApplet app, String text, int yOffset, boolean active) {
        if (active) {
            app.fill(app.color(0xfb, 0xfb, 0x0d)); 
        } else {
            app.fill(app.color(0x83, 0x74, 0x4A)); 
        }
        app.rect(buttonX, buttonY + yOffset, buttonWidth, buttonHeight);

        app.stroke(0);
        app.strokeWeight(2);
        app.noFill();
        app.rect(buttonX, buttonY + yOffset, buttonWidth, buttonHeight);
        app.noStroke();

        app.fill(0);
        app.textSize(App.CELLSIZE);  // Ensure text size is consistent
        app.text(text, buttonX + 10, buttonY + 20 + yOffset);
    }
}
