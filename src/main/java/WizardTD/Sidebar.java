package WizardTD;

import processing.core.PApplet;

public class Sidebar {
    private int width;
    private int height;
    // Add attributes for the buttons, their states, etc.

    public Sidebar(int width, int height) {
        this.width = width;
        this.height = height;
        // Initialize the buttons and their states
    }

    public void updateButtonStates(/* parameters related to game state */) {
        // Update the state of the buttons based on the game state
        // For example, if the user can't interact with a tower, disable the corresponding button
    }

    public void render(PApplet app) {
        app.fill(app.color(0x83, 0x74, 0x4A));
        // The x-coordinate is the total width minus the sidebar width
        // The y-coordinate starts from the height of the top bar
        app.rect(app.width - this.width, App.TOPBAR, this.width, this.height - App.TOPBAR);

        // Render the buttons and their states
        // ...
    }
}
