package WizardTD;

import processing.core.PApplet;

public class Sidebar {
    private int width;
    private int height;
    // Add attributes for the buttons, their states, etc.

    // Attributes
    private boolean towerPlacementMode = false;

    // Button dimensions and position
    private int buttonWidth = 80;
    private int buttonHeight = 30;
    private int buttonX;
    private int buttonY;


    public Sidebar(int width, int height) {
        this.width = width;
        this.height = height;
        // Initialize the buttons and their states
        this.buttonX = App.WIDTH - this.width + 20;  // Positioning the button
        this.buttonY = App.TOPBAR + 10;   // Some padding from the top bar
    }

    public void updateButtonStates(/* parameters related to game state */) {
        // Update the state of the buttons based on the game state
        // For example, if the user can't interact with a tower, disable the corresponding button
    }

    // Methods for tower placement mode
    public void toggleTowerPlacementMode() {
        towerPlacementMode = !towerPlacementMode;
    }

    public boolean isInTowerPlacementMode() {
        return towerPlacementMode;
    }

    public boolean isTowerButtonClicked(int mouseX, int mouseY) {
        return mouseX >= buttonX && mouseX <= buttonX + buttonWidth &&
            mouseY >= buttonY && mouseY <= buttonY + buttonHeight;
    }

    public void render(PApplet app) {
        app.fill(app.color(0x83, 0x74, 0x4A));
        app.rect(app.width - this.width, App.TOPBAR, this.width, this.height - App.TOPBAR);
    
        // Change button's fill color based on tower placement mode
        if (towerPlacementMode) {
            app.fill(app.color(0xfb, 0xfb, 0x0d));  // Yellow color for active mode
        } else {
            app.fill(app.color(0x83, 0x74, 0x4A));  // Default color
        }
    
        app.rect(buttonX, buttonY, buttonWidth, buttonHeight);
        
        // Black border/highlight around the button
        app.stroke(0);  // Black stroke
        app.strokeWeight(2);  // 2 pixels wide
        app.noFill();
        app.rect(buttonX, buttonY, buttonWidth, buttonHeight);
        app.noStroke();  // Reset stroke
        
        // Button text
        app.fill(0);  // Black text
        app.text("Place Tower", buttonX + 10, buttonY + 20);
    }
    
}
