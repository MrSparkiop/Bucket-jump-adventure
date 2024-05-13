package com.hat_quest;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ScoreBoard {
    private int score;
    private BitmapFont font;

    public ScoreBoard() {
        font = new BitmapFont();  // Create a font to display the points
        score = 0;


    }

    public void addScore(int amount) {
        score += amount;  // Add points to total score
    }

    public void resetScore() {
        score = 0;
    }

    public void draw(SpriteBatch batch) {
        font.draw(batch, "Score: " + score, 20, 460);  // Draw the text in the upper left corner
    }

    public void dispose() {
        font.dispose();
    }


}
