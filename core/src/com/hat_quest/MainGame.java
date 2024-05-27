package com.hat_quest;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// Main game class that initializes and manages the game screens
public class MainGame extends Game {
    private SpriteBatch batch; // SpriteBatch used for drawing 2D textures

    @Override
    public void create() {
        batch = new SpriteBatch(); // Initialize the SpriteBatch
        setScreen(new StartMenuScreen(this)); // Set the initial screen to the Start Menu
    }

    @Override
    public void render() {
        super.render(); // Delegate the rendering to the current screen
    }

    @Override
    public void dispose() {
        // Dispose of the SpriteBatch when the game is closed
        if (batch != null) {
            batch.dispose();
        }
    }

    // Getter for the SpriteBatch
    public SpriteBatch getBatch() {
        return batch;
    }
}
