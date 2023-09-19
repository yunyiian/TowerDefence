package WizardTD;

import processing.core.PApplet;

public class TopBar {
    private int width;
    private int height;
    private int mana;

    public TopBar(int width, int height, int initialMana) {
        this.width = width;
        this.height = height;
        this.mana = initialMana;
    }

    public void render(PApplet app) {
        app.fill(245, 245, 220);  // Beige color for the top bar
        app.rect(0, 0, this.width, this.height);
        
        // Display the mana bar
        app.fill(0, 0, 255);  // Blue color for the mana bar
        float manaPercentage = (float) mana / App.INITIAL_MANA_CAP;
        app.rect(10, 10, manaPercentage * (this.width - 20), this.height - 20);
        
        // Display the mana value
        app.fill(0);  // Black color for text
        app.text(mana + "/" + App.INITIAL_MANA_CAP, this.width - 100, 20);  // Adjust position as needed
    }
}


