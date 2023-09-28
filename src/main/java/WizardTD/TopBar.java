package WizardTD;

import processing.core.PApplet;

public class TopBar {
    private int width;
    private int height;
    private int mana;
    private int manaCap;

    private int waveNumber = -1;
    private float timeRemaining = 0.0f;

    public TopBar(int width, int height, int initialMana, int manaCap) {
        this.width = width;
        this.height = height;
        this.mana = initialMana;
        this.manaCap = manaCap;
    }

    public void setWaveTimer(int waveNumber, float timeRemaining) {
        this.waveNumber = waveNumber;
        this.timeRemaining = timeRemaining;
    }


    public void render(PApplet app) {
        // Set color and draw the top bar background
        app.fill(app.color(0x83, 0x74, 0x4A));
        app.rect(0, 0, this.width, this.height);
        
        // Set the position and size for the mana bar
        int manaBarWidth = 320;
        int manaBarHeight = 16;
        int manaBarX = this.width - manaBarWidth - 32;  // 32 pixels (1 tile) away from the right edge
        int manaBarY = (this.height - manaBarHeight) / 2;  // Centered vertically
        
        // Draw the background for the mana bar (white with black outline)
        app.fill(255);
        app.stroke(0);
        app.rect(manaBarX, manaBarY, manaBarWidth, manaBarHeight);
        
        // Draw the actual mana bar with the specified color
        app.fill(app.color(0x03, 0xD6, 0xD6));
        app.noStroke();
        float manaPercentage = (float) mana / manaCap;
        app.rect(manaBarX, manaBarY, manaPercentage * manaBarWidth, manaBarHeight);
        
        // Display "MANA :" text on the left of the bar
        app.fill(0);  // Black color for text
        app.textSize(18);
        app.text("MANA :", manaBarX - 64, manaBarY + 13);  // Adjusted y position to align text vertically with the bar
        
        // Display the mana value in the center of the mana bar
        String manaText = mana + "/" + manaCap;
        float textWidth = app.textWidth(manaText);
        app.text(manaText, manaBarX + (manaBarWidth - textWidth) / 2, manaBarY + 13);
        
        // Render wave timer
        if (waveNumber > 0 && timeRemaining >= 0) {
            String waveText = "Wave " + waveNumber + " starts: " + Math.round(timeRemaining);
            app.text(waveText, 16, manaBarY + 13);
        }
    }

    public void updateMana(float amount) {
        this.mana += amount;
        if (this.mana > manaCap) {
            this.mana = manaCap;
        }
    }
}



